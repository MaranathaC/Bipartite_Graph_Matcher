/**
 * Driver for Dinitz' algorithm in BipartiteGraph
 * @author Natha Chiu
 */
public class DinitzDriver {
    /**
     * Runs Dinitz' algorithm in main
     * pre: bipartite graph is properly implemented
     * post: output maximum number of matching
     */
    public static void main(String[] args) {
        BipartiteGraph bipartiteGraph = new BipartiteGraph();
        bipartiteGraph.dinitz();
    }
}
