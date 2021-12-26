public class Path {
    public Node node;
    public Path next;

    public Path() {
        this.node = null;
        this.next = null;
    }

    public Path(Node n) {
        this.node = n;
        this.next = null;
    }

    public static Path add(Node n, Path p) {
        if (n == null) {
            return p;
        }

        Path newPath = new Path(n);

        if (p == null) {
            return newPath;
        }

        if (n.equals(p.node)) {
            return p;
        }

        newPath.next = p;

        return newPath;
    }
}