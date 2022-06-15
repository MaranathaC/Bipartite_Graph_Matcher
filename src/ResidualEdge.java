/**
 * Class to represent an edge in a residual graph
 * A residual edge is represented by the indices of 2 connecting nodes
 * u and v are determined by whether an edge has reached the capacity
 * if an edge has reached the capacity, the residual edge is a reversed edge
 */
public class ResidualEdge {
    private final Edge edge;
    private final boolean reversed;
    private final int u;
    private final int v;

    /**
     * constructor
     * pre: edge is initialized
     * post: u, v, and reversed are initialized
     *       edge is stored for future possible modification of flow
     */
    public ResidualEdge(Edge edge) {
        this.reversed = edge.reachedCapacity();
        this.edge = edge;

        if(reversed) { // swap u and v
            this.u = edge.getV();
            this.v = edge.getU();
        } else {
            this.u = edge.getU();
            this.v = edge.getV();
        }
    }

    /**
     * getter for u
     * pre: none
     * post: return index of node u
     */
    public int getU() {
        return u;
    }

    /**
     * getter for v
     * pre: none
     * post: return index of node v
     */
    public int getV() {
        return v;
    }

    /**
     * change the flow of an edge
     * pre: edge is initialized
     * post: add a flow or subtract a flow in edge
     */
    public void changeFlow() {
        if(reversed) {
            edge.revertFlow();
        } else {
            edge.addFlow();
        }
    }
}
