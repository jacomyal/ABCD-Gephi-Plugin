/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.jcml.gephi.plugins.abcd;

import javax.swing.JPanel;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = StatisticsUI.class)
public class ABCDUI implements StatisticsUI {

    private final StatSettings settings = new StatSettings();
    private ABCDPanel panel;
    private ABCD abcd;

    @Override
    public JPanel getSettingsPanel() {
        panel = new ABCDPanel();
        return panel;
    }

    @Override
    public void setup(Statistics statistics) {
        this.abcd = (ABCD) statistics;
        if (panel != null) {
            settings.load(abcd);
            panel.setP0Ratio(abcd.getP0Ratio());
            panel.setP1Ratio(abcd.getP1Ratio());
            panel.setP2Ratio(abcd.getP2Ratio());
            panel.setP3Ratio(abcd.getP3Ratio());
            panel.setThreshold(abcd.getThreshold());
            panel.setOverrideEdges(abcd.getOverrideEdges());
            panel.setIgnoreEdgeWeights(abcd.getIgnoreEdgeWeights());
        }
    }

    @Override
    public void unsetup() {
        if (panel != null) {
            abcd.setP0Ratio(panel.getP0Ratio());
            abcd.setP1Ratio(panel.getP1Ratio());
            abcd.setP2Ratio(panel.getP2Ratio());
            abcd.setP3Ratio(panel.getP3Ratio());
            abcd.setThreshold(panel.getThreshold());
            abcd.setOverrideEdges(panel.getOverrideEdges());
            abcd.setIgnoreEdgeWeights(panel.getIgnoreEdgeWeights());
            settings.save(abcd);
        }
        panel = null;
        abcd = null;
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return ABCD.class;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "ABCD Algorithm";
    }

    @Override
    public String getCategory() {
        return StatisticsUI.CATEGORY_NETWORK_OVERVIEW;
    }

    @Override
    public int getPosition() {
        return 800;
    }

    @Override
    public String getShortDescription() {
        return "Antagonism Based Community Detection";
    }

    private static class StatSettings {

        private float p0Ratio = -2;
        private float p1Ratio = 1;
        private float p2Ratio = 1;
        private float p3Ratio = 1;
        private float threshold = 1;
        private boolean overrideEdges = false;
        private boolean ignoreEdgeWeights = false;

        private void save(ABCD stat) {
            this.p0Ratio = stat.getP0Ratio();
            this.p1Ratio = stat.getP1Ratio();
            this.p2Ratio = stat.getP2Ratio();
            this.p3Ratio = stat.getP3Ratio();
            this.threshold = stat.getThreshold();
            this.overrideEdges = stat.getOverrideEdges();
            this.ignoreEdgeWeights = stat.getIgnoreEdgeWeights();
        }

        private void load(ABCD stat) {
            stat.setP0Ratio(p0Ratio);
            stat.setP1Ratio(p1Ratio);
            stat.setP2Ratio(p2Ratio);
            stat.setP3Ratio(p3Ratio);
            stat.setThreshold(threshold);
            stat.setOverrideEdges(overrideEdges);
            stat.setIgnoreEdgeWeights(ignoreEdgeWeights);
        }
    }
}