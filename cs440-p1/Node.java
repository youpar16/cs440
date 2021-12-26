import java.util.ArrayList;

public class Node {
    public int row, col;
	public int f, g, h;
    public int search;
	public Node prev;
	public boolean isBlocked;
    public ArrayList<Node> adj;

    public Node(){
        this.f = 0;
        this.g = 0;
        this.h = 0;
        this.search = 0;
        this.isBlocked = false;
        this.prev = null;
        this.adj = new ArrayList<Node>(4);
    }

    // Updates G, F, and prev values of node
    public void setValues(int gval, Node prev) {
        this.g = gval;
        this.f = this.h + this.g;
        this.prev = prev;
    }

    // checks if nodes are equal
    public boolean equals(Node n) {
        if (n == null) {
            return false;
        } 
        return (this.row == n.row) && (this.col == n.col);
    } 

    public void setHeuristicAdaptive(int goalCost){
        this.h = goalCost - this.g;
    }
}