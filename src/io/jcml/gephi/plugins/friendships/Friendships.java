/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.jcml.gephi.plugins.friendships;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.datalab.api.GraphElementsController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.HierarchicalGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.Modularity;
import org.gephi.statistics.spi.Statistics;
import org.openide.util.Lookup;

/**
 *
 * @author jacomyal
 */
public class Friendships implements Statistics {
    
    private HashMap<Integer, HashMap<Integer, Float>> counts;
    private float p0Ratio; //  o --> o
    private float p1Ratio; // --> o -->
    private float p2Ratio; // --> o <--
    private float p3Ratio; // <-- o -->
    private boolean isDirected;
    private boolean isCanceled;

    public Friendships() {
    }

    private void addToCount(Integer n1, Integer n2, Float value) {
        if (n1 < n2) {
            counts.get(n1).put(n2, counts.get(n1).get(n2) + value);
        } else {
            counts.get(n2).put(n1, counts.get(n2).get(n1) + value);
        }
    }

    private Float getCount(Integer n1, Integer n2) {
        if (n1 < n2) {
            return counts.get(n1).get(n2);
        } else {
            return counts.get(n2).get(n1);
        }
    }
    
    @Override
    public void execute(GraphModel graphModel, AttributeModel attributeModel) {
        HierarchicalGraph graph = graphModel.getHierarchicalGraphVisible();
        execute(graph, attributeModel);
    }

    public void execute(HierarchicalGraph graph, AttributeModel attributeModel) {
        counts = new HashMap<Integer, HashMap<Integer, Float>>();
        Integer i = 0;

        graph.readLock();
        List<Edge> oldEdges = new ArrayList<Edge>();
        
        // Initialize indexes:
        for (Node n1 : graph.getNodes()) {
            for (Node n2 : graph.getNodes()) {
                if (n1.getId() < n2.getId()) {
                    if (!counts.containsKey(n1.getId())) {
                        HashMap<Integer, Float> val = new HashMap<Integer, Float>();
                        counts.put(n1.getId(), val);
                    }
                    counts.get(n1.getId()).put(n2.getId(), 0f);
                } else {
                    if (!counts.containsKey(n2.getId())) {
                        HashMap<Integer, Float> val = new HashMap<Integer, Float>();
                        counts.put(n2.getId(), val);
                    }
                    counts.get(n2.getId()).put(n1.getId(), 0f);
                }
            }
            
            if (isCanceled)
                break;
        }
        
        for (Edge e1 : graph.getEdges()) {
            oldEdges.add(e1);
            
            // Pattern: -->
            this.addToCount(
                    e1.getSource().getId(),
                    e1.getTarget().getId(),
                    p0Ratio * e1.getWeight()
            );
            
            // Parse edges connected to the target:
            for (Edge e2 : graph.getEdges(e1.getTarget())) {
                if (e1.getTarget() == e2.getSource())
                    // Pattern: --> o -->
                    this.addToCount(
                            e1.getSource().getId(),
                            e2.getTarget().getId(),
                            p1Ratio * (e1.getWeight() + e2.getWeight())
                    );
                else if (e1.getSource().getId() < e2.getSource().getId())
                    // Pattern: --> o <--
                    this.addToCount(
                            e1.getSource().getId(),
                            e2.getSource().getId(),
                            p2Ratio * (e1.getWeight() + e2.getWeight())
                    );
            }
            
            // Parse edges connected to the source:
            for (Edge e2 : graph.getEdges(e1.getSource())) {
                if (
                    e1.getSource() == e2.getSource() &&
                    e1.getTarget().getId() < e2.getTarget().getId()
                )
                    // Pattern: <-- o -->
                    this.addToCount(
                            e1.getTarget().getId(),
                            e2.getTarget().getId(),
                            p3Ratio * (e1.getWeight() + e2.getWeight())
                    );
            }

            if (isCanceled)
                break;
        }
        
        // Find distribution:
        Integer values = 0;
        Float sum = 0f;
        Float max = 0f;
        Float threshold;
        for (HashMap<Integer, Float> map : counts.values()) {
            for (Float value : map.values()) {
                max = Math.max(max, value);
                sum += value;
                values++;
            }
        }
        threshold = graph.getNodeCount() > 1000 ? (max + sum / values) / 2 : 1;

        graph.readUnlockAll();
        graph.writeLock();
        
        // Clear edges:
        graph.clearEdges();
        
        // Add new edges:
        Edge edge;
        for (Integer key : counts.keySet()) {
            HashMap<Integer, Float> map = counts.get(key);
            for (Integer key2 : map.keySet()) {
                Float value = map.get(key2);
                if (key != key2 && value > threshold) {
                    edge = Lookup.getDefault().lookup(GraphElementsController.class).createEdge(
                            graph.getNode(key),
                            graph.getNode(key2),
                            false
                    );
                    
                    edge.setWeight(value);
                }
            }
        }
        
        graph.writeUnlock();
        
        // Compute modularity:
        Modularity mod = new Modularity();
        mod.execute(graph.getGraphModel(), attributeModel);
        
        // Restore initial graph:
        graph.writeLock();
        graph.clearEdges();
        
        for (Edge e : oldEdges) {
            graph.addEdge(e);
        }
        
        graph.writeUnlock();
    }
    
    @Override
    public String getReport() {
        return "";
    }

    public float getP0Ratio() {
        return p0Ratio;
    }

    public void setP0Ratio(float p0Ratio) {
        this.p0Ratio = p0Ratio;
    }

    public float getP1Ratio() {
        return p1Ratio;
    }

    public void setP1Ratio(float p1Ratio) {
        this.p1Ratio = p1Ratio;
    }

    public float getP2Ratio() {
        return p2Ratio;
    }

    public void setP2Ratio(float p2Ratio) {
        this.p2Ratio = p2Ratio;
    }

    public float getP3Ratio() {
        return p3Ratio;
    }

    public void setP3Ratio(float p3Ratio) {
        this.p3Ratio = p3Ratio;
    }
}
