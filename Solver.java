import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {


    private Stack<Board> solutionStack = new Stack<>();
    private int numMoves = 0;
    private Board firstBoard;
    private boolean solvable = false;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        firstBoard = initial;
        // node = board, num of moves to reach board, previous node
        Node node = new Node(firstBoard, 0, null);
        Node nodeTwin = new Node(firstBoard.twin(), 0, null);
        MinPQ<Node> pq = new MinPQ<>(pOrder);
        MinPQ<Node> pqTwin = new MinPQ<>(pOrder);
        pq.insert(node);
        numMoves = 0;
        Iterable<Board> neighbours;
        Iterable<Board> neighboursTwin;
        while (true) {
            numMoves++;
            // add neighbors for every min removed
            neighbours = node.board.neighbors();
            neighboursTwin = nodeTwin.board.neighbors();
            for (Board n : neighbours) {
                // only add nodes which are not the same as removed node
                if (n.equals(node.board)) {
                    continue;
                }
                Node newNode = new Node(n, numMoves, node);
                pq.insert(newNode);
            }
            for (Board nTwin : neighboursTwin) {
                // only add nodes which are not the same as removed node
                if (nTwin.equals(nodeTwin.board)) {
                    continue;
                }
                Node newNode = new Node(nTwin, numMoves, node);
                pqTwin.insert(newNode);
            }
            node = pq.delMin();
            nodeTwin = pqTwin.delMin();
            if (node.board.isGoal()) {
                solutionStack.push(node.board);
                while (node.prevNode != null) {
                    node = node.prevNode;
                    solutionStack.push(node.board);
                }
                solvable = true;
                break;
            }
            else if (nodeTwin.board.isGoal()) {
                solvable = false;
                break;
            }
        }

    }

    // find a solution to the initial board (using the A* algorithm)
    private class Node {
        Board board;
        int moves;
        Node prevNode;
        int manhattanVal;

        private Node(Board board, int moves, Node prevNode) {
            this.board = board;
            this.moves = moves;
            this.prevNode = prevNode;
            this.manhattanVal = board.manhattan();
        }
    }

    private Comparator<Node> pOrder = new Comparator<Node>() {
        public int compare(Node n1, Node n2) {
            if (n1.manhattanVal + n1.moves > n2.manhattanVal + n2.moves) {
                return 1;
            }
            else if (n1.manhattanVal + n1.moves < n2.manhattanVal + n2.moves) {
                return -1;
            }
            else return 0;
        }
    };

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return numMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return solutionStack;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
