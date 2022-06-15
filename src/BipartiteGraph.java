import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Class to represent a Bipartite Graph for Dinitz' algorithm
 * In addition to the nodes in a graph, 2 nodes, the source and the sink,
 * are added to the graph to construct a Network algorithm.
 * A graph for the Dinitz' algorithm consists of a few graphs,
 * and each graph is represented by an adjacency list.
 * The graphs include the regular graph, a residual graph, and a level graph.
 * This class is for producing maximum number of matching in a bipartite graph.
 * @author Natha Chiu
 */
public class BipartiteGraph {
    private List<Node> nodes;
    // adjacency lists for graphs
    private List<List<Edge>> graph;
    private List<List<ResidualEdge>> levelGraph;
    private List<List<ResidualEdge>> residualGraph;
    private int sinkIdx; // index of sink

    /**
     * constructor
     * pre: none
     * post: read from a text file and create an adjacency list and a list of nodes
     *       for a bipartite graph, with a source and a sink added.
     */
    public BipartiteGraph() {
        Scanner sc;
        try {
            nodes = new ArrayList<>();
            graph = new ArrayList<>();

            sc = new Scanner(new File("src/program3data.txt"));

            int size = sc.nextInt();
            sc.nextLine();

            nodes.add(new Node("SOURCE")); // source node is the first node
            graph.add(new ArrayList<>());

            // connect source node to the first halve of nodes in a bipartite graph
            // every edge has a unit capacity because each node can have 1 matching
            for(int i = 1; i <= size / 2; i++) {
                graph.get(0).add(new Edge(0, i, 1));
            }

            // read nodes from file; each node has a list of edges
            for(int i = 0; i < size; i++) {
                nodes.add(new Node(sc.nextLine()));
                graph.add(new ArrayList<>());
            }

            nodes.add(new Node("SINK")); // sink node is the last node
            graph.add(new ArrayList<>()); // edges of sink, which are none

            // connect sink node to the second halve of nodes in a bipartite graph
            sinkIdx = size + 1;
            for(int i = size / 2 + 1; i <= size; i++) {
                graph.get(i).add(new Edge(i, sinkIdx, 1));
            }

            int numOfEdges = sc.nextInt();

            // read edges from file; each edge has a capacity of one
            for(int i = 0; i < numOfEdges; i++) {
                int u = sc.nextInt();
                int v = sc.nextInt();
                graph.get(u).add(new Edge(u,v, 1));
            }

            sc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * create a residual graph from graph
     * pre: graph is created
     * post: a residual graph is created
     */
    private void createResidualGraph() {
        // create an adjacency list for residual graph
        residualGraph = new ArrayList<>();

        for(int i = 0; i < nodes.size(); i++) {
            residualGraph.add(new ArrayList<>());
        }

        for(List<Edge> edgeList : graph) {
            for(Edge edge : edgeList) {
                // go through all the edges and create a residual edge for each
                ResidualEdge residualEdge = new ResidualEdge(edge);

                // Exclude edges from sink. Edges would go out from sink if reversed
                if(residualEdge.getU() == sinkIdx) {
                    continue;
                }

                // add to adjacency list
                residualGraph.get(residualEdge.getU()).add(residualEdge);
            }
        }
    }

    /**
     * Breadth-First-Search
     * pre: a residual graph is created
     * post: creates a level graph from the residual graph
     *       returns true if the sink is reached, when there is an augmenting path
     */
    private boolean BFS() {
        // create a level graph
        levelGraph = new ArrayList<>();

        // for conducting Breadth-First-Search
        Queue<Integer> queue = new LinkedList<>();
        int nodeIdx;
        queue.offer(0); // BFS from source node

        int level = 0; // level in a level graph

        boolean[] seen = new boolean[nodes.size()]; // prevents backward edges

        while(!queue.isEmpty()) {
            int size = queue.size();

            for(int i = 0; i < size; i++) {
                nodeIdx = queue.poll(); // get index of node

                if(seen[nodeIdx]) { // already added edges from this node
                    continue;
                }

                seen[nodeIdx] = true;

                List<ResidualEdge> edgeList = residualGraph.get(nodeIdx); // get edges from node

                for(ResidualEdge edge : edgeList) {
                    if(seen[edge.getV()]) { // no backward edges
                        continue;
                    }

                    if(levelGraph.size() == level) { // check if it is a new level
                        levelGraph.add(new ArrayList<>());
                    }

                    levelGraph.get(level).add(edge); // add edge to current level

                    queue.offer(edge.getV()); // add v to next level

                }
            }

            level++;
        }

        return seen[sinkIdx]; // returns true if sink is reached
    }

    /**
     * Depth-First-Search
     * pre: a level graph is created
     * post: adjust the flow of the Bipartite graph
     */
    private void DFS() {
        // create an adjacency list for level graph
        List<List<ResidualEdge>> levelAdjacencyList = new ArrayList<>();

        for(int i = 0; i < nodes.size(); i++) {
            levelAdjacencyList.add(new ArrayList<>());
        }

        for(List<ResidualEdge> edgeList : levelGraph) {
            for(ResidualEdge edge : edgeList) {
                levelAdjacencyList.get(edge.getU()).add(edge);
            }
        }

        for(ResidualEdge edge : levelAdjacencyList.get(0)) { // DFS from source
            // check if there is an augmenting path
            if(DFS(levelAdjacencyList, edge.getV())) {
                edge.changeFlow();
            }
        }
    }

    /**
     * Depth-First-Search
     * pre: a level graph and an index of a node are passed in
     * post: adjust the flow of the Bipartite graph
     */
    private boolean DFS(List<List<ResidualEdge>> levelAdjacencyList, int nodeIdx) {
        if(nodeIdx == sinkIdx) { // reached sink
            return true;
        }

        // get all the edges of a node
        List<ResidualEdge> edgeList = levelAdjacencyList.get(nodeIdx);

        // go through all the edges
        for(int i = 0; i < edgeList.size(); i++) {
            ResidualEdge edge = edgeList.get(i);

            if(!DFS(levelAdjacencyList, edge.getV())) { // stuck
                edgeList.remove(i--);
            } else { // found a path
                edge.changeFlow();
                edgeList.remove(edge);
                return true;
            }
        }

        return false; // no path was found
    }

    /**
     * Dinitz' algorithm for bipartite matching
     * pre: a bipartite graph is created
     * post: Compute the maximum number of matching in a bipartite graph
     */
    public void dinitz() {
        // create residual graph from graph
        // which is the same graph in first iteration
        createResidualGraph();

        while (BFS()) { // create level graph from BFS
            DFS(); // find matching from DFS
            createResidualGraph(); // create new residual graph
        }

        // print and get maximum number of matching
        int numOfMatches = printMatches();
        System.out.println(numOfMatches + " Total Matches");
    }

    /**
     * print all matching
     * pre: the Dinitz' algorithm is ran
     * post: print all matching and return number of matching
     */
    private int printMatches() {
        int numOfMatches = 0;

        for(int i = 1; i < graph.size() / 2; i++) {
            List<Edge> edgeList = graph.get(i);
            for(Edge edge : edgeList) {
                // matching is indicated by whether there is flow in an edge
                if(edge.reachedCapacity()) {
                    String name1 = nodes.get(edge.getU()).getName();
                    String name2 = nodes.get(edge.getV()).getName();
                    System.out.println(name1 + " / " + name2);
                    numOfMatches++;
                }
            }
        }

        return numOfMatches;
    }

    public boolean duplicateMatchesTest() {
        Set<String> matches = new HashSet<>();
        for(int i = 1; i < graph.size() / 2; i++) {
            List<Edge> edgeList = graph.get(i);
            for(Edge edge : edgeList) {
                if(edge.reachedCapacity()) {
                    if(matches.contains(nodes.get(edge.getV()).getName())) {
                        return false;
                    }
                    if(matches.contains(nodes.get(edge.getU()).getName())) {
                        return false;
                    }
                    matches.add(nodes.get(edge.getU()).getName());
                    matches.add(nodes.get(edge.getV()).getName());
                }
            }
        }
        return true;
    }
}