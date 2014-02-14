## Antagonism-based community detection

Developped by [Alexis Jacomy](https://github.com/jacomyal) for the [Contropedia](http://contropedia.net/) project.
Plugin released under the [MIT License](https://github.com/jacomyal/FriendshipsPlugin/blob/master/LICENSE.txt).

This Gephi plugins aims to find communities when edges are connecting enemy nodes.

Here is how it works:

 1. The plugin computes a friendship score between each pair of nodes.
   - The more common enemies two nodes have, the highesr their score will be.
   - The more connected two nodes are, the lower their score will become.
 2. All original edges are removed.
 3. Each pair of nodes that has a score higher than the threshold will become connected.
 4. The Modularity algorithm is then used to detect communities, on the new graph.
 5. Finally, unless the `Override edges` flag is set to true, the new edges are removed and the original ones are restored again. Be aware that if you set the `Override edges` flag the orginal network will be lost (unless you reload the orginal data).

Finally, you can represent the communities as colors through the `Partition > Nodes` panel (selecting Modularity).

### Details of the algortihm for calculation the friendship score:

#### Parameters of the Algorithm:

 - **pd** = Direct Connection Penalty (should be a value below 0)
 - **ct** = Common neighbours score (transitive node, `A->C->B`)
 - **cg** = Common neighbours score (common traget node, `A->C<-B`)
 - **co** = Common neighbours score (common source node, `A<-C->B`)
 - **c** = Common neighbours score (undirected network, `A-C-B`). In the case of an undirected network the parameters ct, cg and co have to be set all to the same value (which we refer to as c from now on) otherwise the result of the algorithm will be depending on the internal representation of the undirected nodes in Gephi.
 - **t** = treshold for the establishement of a friendship link

#### Example
Network: A, B, C, D nodes; w1, w2, w3, w4, w5 edge weights

````
    D
   / \
 w4   w5
 /      \
A---w1---B
 \      /
 w2   w3
   \ /
    C
````

We calculate the friendship score `s` of A and B with the following formula (we only use an undirected network):
````
s(A, B) = pd * w1 + c * (w2 + w3 + w4 + w5)
````

And for A and C it is:
````
s(A, C) = pd * w2 + c * (w1 + w3)
````

All common neighbours of two nodes are contrasted with the weight multiplied by the penality factor `pd` of the conenctin between these two nodes.

A and B are linked if: `s(A, B) > t`
