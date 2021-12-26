import java.util.Random;
import java.util.*;

public class Maze {
    Node[][] maze;
    Node start, target;

    public Maze(int rsize, int csize) {
        this.maze = new Node[rsize][csize];
        makeMaze();

        start = this.maze[0][0];
        target = this.maze[rsize-1][csize-1];
    }
    
    // Makes new maze
    public void makeMaze(){
        Random rand = new Random();
        for (int row = 0; row < this.maze.length; row++) {
            for (int col = 0; col < this.maze.length; col++) {
                this.maze[row][col] = new Node();
                this.maze[row][col].row = row;
                this.maze[row][col].col = col;

                int n = rand.nextInt(101);
                if(n < 30){
                    this.maze[row][col].isBlocked = true;
                }
            }
        }
    
        this.maze[0][0].isBlocked = false;
        this.maze[maze.length-1][maze.length-1].isBlocked = false;

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                this.generateAdj(i, j);
            }
        }
    }

    // Resets H Values for entire maze 
    public void setMazeHValues(int row, int col) {
        for (int i = 0; i < this.maze.length; i++) {
            for (int j = 0; j< this.maze.length; j++) {
                this.maze[i][j].h = calcH(i, j, row, col);
            }
        }
    }
    
    
    // Calculated Manhattan distance at given point
    public int calcH(int row, int col, int targetrow, int targetcol){
        return Math.abs(targetrow-row) + Math.abs(targetcol-col);
    }

    
    // Generates adjacent nodes for given cell
    public void generateAdj(int row, int col) {
        Node curr = this.maze[row][col];

        if (row-1 >= 0) {
            curr.adj.add(this.maze[row-1][col]);
        }

        if (row+1 < maze.length) {
            curr.adj.add(this.maze[row+1][col]);
        }

        if (col-1 >= 0) {
            curr.adj.add(this.maze[row][col-1]);
        }

        if (col+1 < maze.length) {
            curr.adj.add(this.maze[row][col+1]);
        }
    }

    // Prints maze
    public void printMaze(){
        for (int row = 0; row < this.maze.length; row++) {
            for (int col = 0; col < this.maze.length; col++) {
                if (this.maze[row][col].isBlocked) {
                    System.out.print("1");
                }
                else {
                    System.out.print("0");
                }
            }
            System.out.println();
        }
    }

    public void clearMaze(){
        for (int row = 0; row < this.maze.length; row++) {
            for (int col = 0; col < this.maze.length; col++) {
                this.maze[row][col].f = 0;
                this.maze[row][col].g = 0;
                this.maze[row][col].prev = null;
                this.maze[row][col].search = 0;
            }
        }
    }

}
