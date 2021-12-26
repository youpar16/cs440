import java.util.*;


public class RepeatedAStar {
    static int mazeSize = 101;

    public static void main(String[] args){
        experiment();
        Maze m = new Maze(mazeSize, mazeSize);
        // forwardsA(m, 0);
        // forwardsA(m, 1);
        // backwardsA(m, 1);
        // adaptiveA(m);
    }

    public static int forwardsA(Maze maze, int compare){
        int counter = 0;
        int expanded = 0;
        HashSet<Node> blockedSet = new HashSet<Node>();
        Path agent = null;

        Node start = maze.maze[0][0];
        Node target = maze.maze[mazeSize - 1][mazeSize - 1];
        maze.setMazeHValues(target.row, target.col);

        // Set comparator settings based on compare parameter
        Comparator<Node> comparator;
        if (compare == 0){
            comparator = new NodeComparator.smallerG();
        } else if (compare == 1) {                
            comparator = new NodeComparator.higherG();
        } else {
            // System.out.println("???\n");
            return -1;
        }
        
        while (!(start.equals(target))){
            counter++;
            start.search = counter;
            target.search = counter;
            start.setValues(0, null);
            target.setValues(Integer.MAX_VALUE, null);

            HashSet<Node> closedList = new HashSet<Node>();
            PriorityQueue<Node> openList = new PriorityQueue<Node>(10, comparator);
            
            // Add blocked adj. nodes of current node to blockedSet
            for (int i = 0; i < start.adj.size(); i++) {
                Node temp = start.adj.get(i);
                if (temp != null && temp.isBlocked) {
                    blockedSet.add(temp);
                }
            }

            openList.add(start);
            computePath(openList, closedList, blockedSet, counter, target);
            
            expanded += closedList.size();
            if(openList.isEmpty()){
            		// System.out.println("I cannot reach target.");
                return -1;
            }
            
            // Keep track of shortest path in stack for easy access to recreate shortest path
            Stack<Node> path = new Stack<Node>();
            Node ptr = target;
            while (ptr != null) {
                path.push(ptr);
                ptr = ptr.prev;
            }

            /* Now use stack move agent along shortest path until path is not the real shortest (e.g. going through
               blocked cells)
            */
            while (!path.isEmpty() && !(path.peek().isBlocked)) {
                if (agent != null && agent.next != null && path.peek().equals(agent.next.node)) {
                    agent = agent.next;
                }
                // Moves agent to next available cell from stack
                agent = Path.add(path.pop(), agent);
                
                // Once agent is moved, check its adj nodes and add any blocked ones to blockedSet
                for (int i = 0; i < agent.node.adj.size(); i++) {
                    Node temp = agent.node.adj.get(i);
                    if (temp != null && temp.isBlocked) {
                        blockedSet.add(temp);
                    }
                }
            }

            // Update agent's start position
            start = agent.node;
        }

        // System.out.println("Expanded nodes in Forward A*: " + expanded);
        maze.clearMaze();
        // displayMaze(maze, agent);
        System.out.print("\n");
        return expanded;
    }

    public static int backwardsA(Maze maze, int compare){
        int counter = 0;
        int expanded = 0;
        HashSet<Node> blockedSet = new HashSet<Node>();
        Path agent = null;

        Node start = maze.maze[0][0];
        Node target = maze.maze[mazeSize - 1][mazeSize - 1];
        maze.setMazeHValues(start.row, start.col);

        // Set comparator settings based on compare parameter
        Comparator<Node> comparator;
        if (compare == 0){
            comparator = new NodeComparator.smallerG();
        } else if (compare == 1) {                
            comparator = new NodeComparator.higherG();
        } else {
            // System.out.println("???\n");
            return -1;
        }
        
        while (!(start.equals(target))){
            counter++;
            start.search = counter;
            target.search = counter;
            start.setValues(Integer.MAX_VALUE, null);
            target.setValues(0, null);

            HashSet<Node> closedList = new HashSet<Node>();
            PriorityQueue<Node> openList = new PriorityQueue<Node>(10, comparator);
            
            // Add blocked adj. nodes of current node to blockedSet
            for (int i = 0; i < start.adj.size(); i++) {
                Node temp = start.adj.get(i);
                if (temp != null && temp.isBlocked) {
                    blockedSet.add(temp);
                }
            }

            openList.add(target);
            computePath(openList, closedList, blockedSet, counter, start);
            
            expanded += closedList.size();
            if(openList.isEmpty()){
            	// System.out.println("I cannot reach target.");
                return -1;
            }
            
            /* Now move agent along shortest path until path is not the real shortest (e.g. going through
               blocked cells)
            */
            Node ptr = start;
            while (ptr != null && ptr.prev != null && !(ptr.prev.isBlocked)) { 
                // Once agent is moved, check its adj nodes and add any blocked ones to blockedSet
                for (int i = 0; i < ptr.adj.size(); i++) {
                    Node temp = ptr.adj.get(i);
                    if (temp != null && temp.isBlocked) {
                        blockedSet.add(temp);
                    }
                }
                
                if (agent != null && agent.next != null && ptr.prev.equals(agent.next.node)) {
                    agent = agent.next;
                }
                else {
                    agent = Path.add(ptr, agent);
                }
                ptr = ptr.prev;
            }

            if (ptr != null) {
                agent = Path.add(ptr, agent);
            }

            // Update agent's start position
            start = ptr;
            // Update all Maze H values since we updated our target cell
            maze.setMazeHValues(start.row, start.col);
        }

        // System.out.println("Expanded nodes in Backwards A*: " + expanded);
        maze.clearMaze();
        // displayMaze(maze, agent);
        System.out.print("\n");
        return expanded;
    }

    public static int adaptiveA(Maze maze) {
        int counter = 0;
        int expanded = 0;
        HashSet<Node> blockedSet = new HashSet<Node>();
        Path agent = new Path();

        Node start = maze.maze[0][0];
        Node target = maze.maze[mazeSize - 1][mazeSize - 1];
        maze.setMazeHValues(target.row, target.col);

        // Set comparator settings based on compare parameter
        Comparator<Node> comparator = new NodeComparator.higherG();
        
        while (!(start.equals(target))){
            counter++;
            start.search = counter;
            target.search = counter;
            start.setValues(0, null);
            target.setValues(Integer.MAX_VALUE, null);

            HashSet<Node> closedList = new HashSet<Node>();
            PriorityQueue<Node> openList = new PriorityQueue<Node>(10, comparator);
            
            // Add blocked adj. nodes of current node to blockedSet
            for (int i = 0; i < start.adj.size(); i++) {
                Node temp = start.adj.get(i);
                if (temp != null && temp.isBlocked) {
                    blockedSet.add(temp);
                }
            }

            openList.add(start);
            computePath(openList, closedList, blockedSet, counter, target);
            
            expanded += closedList.size();
            if(openList.isEmpty()){
            	// System.out.println("I cannot reach target.");
                return -1;
            }

            Object[] updateCells = closedList.toArray();
            int closedListSize = closedList.size();
            for (int i = 0; i< closedListSize; i++) {
                Node toUpdate = (Node) updateCells[i];
                toUpdate.setHeuristicAdaptive(target.g);
            }
            
            // Keep track of shortest path in stack for easy access to recreate shortest path
            Stack<Node> path = new Stack<Node>();
            Node ptr = target;
            while (ptr != null) {
                path.push(ptr);
                ptr = ptr.prev;
            }

            /* Now use stack move agent along shortest path until path is not the real shortest (e.g. going through
               blocked cells)
            */
            while (!path.isEmpty() && !(path.peek().isBlocked)) {
                if (agent != null && agent.next != null && path.peek().equals(agent.next.node)) {
                    agent = agent.next;
                }
                // Moves agent to next available cell from stack
                agent = Path.add(path.pop(), agent);
                
                // Once agent is moved, check its adj nodes and add any blocked ones to blockedSet
                for (int i = 0; i < agent.node.adj.size(); i++) {
                    Node temp = agent.node.adj.get(i);
                    if (temp != null && temp.isBlocked) {
                        blockedSet.add(temp);
                    }
                }
            }

            // Update agent's start position
            start = agent.node;
        }

        // System.out.println("Expanded nodes in Adaptive A*: " + expanded);
        maze.clearMaze();
        // displayMaze(maze, agent);
        System.out.print("\n");
        return expanded;
    }

    public static void computePath(PriorityQueue<Node> openList, HashSet<Node> closedList, HashSet<Node> blockedSet, int counter, Node target){
        while(!(openList.isEmpty()) && (target.g > openList.peek().f)){
            Node curr = openList.poll();
            closedList.add(curr);
            int adjTotal = curr.adj.size();
            for(int i = 0 ; i < adjTotal ; i++){
                Node adjNode = curr.adj.get(i);
                if(adjNode != null && !(blockedSet.contains(adjNode)) && !(closedList.contains(adjNode))){
                    if(adjNode.search < counter){
                        adjNode.g = Integer.MAX_VALUE;
                        adjNode.search = counter;
                    }
                    if(adjNode.g > (curr.g + 1)){
                        adjNode.setValues((curr.g+1), curr);
                        openList.remove(adjNode);
                        openList.add(adjNode);
                    }
                }
            }
        }
        return;
    }

    public static void experiment() {
        int[][] stats = new int[50][4];
        int i = 0;

        while (i < 50) {
            Maze m1 = new Maze(mazeSize, mazeSize);
            m1.makeMaze();
            int check = forwardsA(m1, 0);

            if (check > 0){
                //fowards small g
                stats[i][0] = forwardsA(m1, 0);
                //fowards large g
                stats[i][1] = forwardsA(m1, 1);
                //backwards large g
                stats[i][2] = backwardsA(m1, 1);
                //adaptive fowards large g
                stats[i][3] = adaptiveA(m1);                
                i++;
            }
        }
        
        for (int r = 0; r < 50; r++) {
            for (int c = 0; c < 4; c++) {
                System.out.print(stats[r][c] + "\t");
            }
            System.out.print("\n");
        }

        // Calculating and printing averages
        int forwardSmallerAvg = 0, forwardHigherAvg = 0, backwardAvg = 0, adaptiveAvg = 0;
        for (i = 0; i < 50; i++){
            forwardSmallerAvg += stats[i][0];
            forwardHigherAvg += stats[i][1];
            backwardAvg += stats[i][2];
            adaptiveAvg += stats[i][3];
        }
        forwardSmallerAvg /= 50;
        forwardHigherAvg /= 50;
        backwardAvg /= 50;
        adaptiveAvg /= 50;
        System.out.println("Average cost of Forwards A* by smaller g : " + forwardSmallerAvg);
        System.out.println("Average cost of Forwards A* by larger g : " + forwardHigherAvg);
        System.out.println("Average cost of Backwards A* by larger g : " + backwardAvg);
        System.out.println("Average cost of Adaptive Forwards A* by larger g : " + adaptiveAvg);
        
    }

    public static void displayMaze(Maze m, Path path){
        for (int i = 0; i < mazeSize; i++){
            for (int j = 0; j < mazeSize; j++){
                if (m.maze[i][j].isBlocked){
                    System.out.print("1");
                } else {
                    boolean inPath = false;
                    Path ptr = path;
                    while (ptr != null) {
                        if (m.maze[i][j].equals(ptr.node)) {
                            System.out.print(".");
                            inPath = true;
                            break;
                        }
                        ptr = ptr.next; 
                    }
                    if (!inPath){
                        System.out.print("0");
                    }
                }
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
}
