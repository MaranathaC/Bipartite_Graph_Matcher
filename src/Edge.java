/**
 * Class to represent a directed edge between 2 nodes
 * An edge is represented by the indices of 2 connecting nodes,
 * a flow, and a capacity
 * @author Natha Chiu
 */
public class Edge {
    private int flow;
    private final int capacity;
    private final int u; // outgoing from node u
    private final int v; // incoming to node v

    /**
     * constructor
     * pre: u, v, and capacity are non-negative
     * post: u, v, and capacity are initialized
     */
    public Edge(int u, int v, int capacity) {
        this.u = u;
        this.v = v;
        this.flow = 0;
        this.capacity = capacity;
    }

    /**
     * reachedCapacity
     * pre: flow and capacity are initialized
     * post: returns true if flow is equal to capacity,
     *       false otherwise
     */
    public boolean reachedCapacity() {
        return flow == capacity ;
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
     * addFlow
     * pre: none
     * post: add a flow to edge
     */
    public void addFlow() {
        flow++;
    }

    /**
     * revertFlow
     * pre: none
     * post: reduces flow by one
     */
    public void revertFlow() {
        flow--;
    }
}
