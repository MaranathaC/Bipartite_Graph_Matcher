/**
 * Class to represent a node in a graph
 * a node only stores a name
 * @author Natha Chiu
 */
public class Node {
    private final String name;

    /**
     * constructor
     * pre: none
     * post: sets name
     */
    public Node(String name) {
        this.name = name;
    }

    /**
     * getter for name
     * pre: none
     * post: returns name
     */
    public String getName() {
        return name;
    }
}
