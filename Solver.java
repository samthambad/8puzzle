import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Solver {


    MinPQ<Node> pq;
    Stack<Board> solutionStack = new Stack<>();
    private int numMoves = 0;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Argument is null!");
        }
        // node = board, num of moves to reach board, previous node
        Node first = new Node(initial, 0, null);
        pq = new MinPQ<>(pOrder());
        pq.insert(first);
        numMoves = 0;
        while (true) {
            Node parent = pq.delMin();
            // System.out.println("parent deleted: " + parent);
            if (parent.board.manhattan() == 0) {
                solutionStack.push(parent.board);
                break;
            }
            numMoves++;
            // add neighbors
            Iterable<Board> neighbours = parent.board.neighbors();
            for (Board n : neighbours) {
                Node newNode = new Node(n, numMoves, parent);
                // if the stack has nodes, check if the top one = the one being added
                if (!(!(solutionStack.isEmpty()) && n.equals(solutionStack.peek()))) {
                    pq.insert(newNode);
                }
            }
        }
        System.out.println(solutionStack);

    }

    // find a solution to the initial board (using the A* algorithm)
    private class Node {
        Board board;
        int moves;
        Node prevNode;

        private Node(Board board, int moves, Node prevNode) {
            this.board = board;
            this.moves = moves;
            this.prevNode = prevNode;
        }
    }

    private Comparator<Node> pOrder() {
        return new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                if (n1.board.manhattan() + n1.moves < n2.board.manhattan() + n2.moves) {
                    return 1;
                }
                else if (n1.board.manhattan() + n1.moves > n2.board.manhattan() + n2.moves) {
                    return -1;
                }
                else return 0;
            }
        };
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solutionStack.peek().hamming() == 0;
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
        System.out.println("before solver");
        Solver solver = new Solver(initial);
        System.out.println("solver completed");
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