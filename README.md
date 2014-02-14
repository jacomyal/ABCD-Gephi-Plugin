## Friendship-based community detection

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

Details of the algortihm for calculation the friendship score:
Example Network: A,B,C nodes; w1,w2,w3 edge weights
A---w1--B
\      /
 w2   w3
   \ /
    C

We calculate the friendship score of A and B with the following formula:
