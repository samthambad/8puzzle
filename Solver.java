import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.Iterator;

public class Solver {

    // find a solution to the initial board (using the A* algorithm)
    private class Node {
        Board board;
        int moves;
        Node prevNode;
        private Node(Board board, int moves, Node prevNode){
            this.board = board;
            this.moves = moves;
            this.prevNode = prevNode;
        }
    }
    private Comparator<Node> pOrder() {
        Comparator<Node> nodeComparator = new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                if (n1.board.manhattan()+n1.moves < n2.board.manhattan()+n2.moves) {
                    return 1;
                }
                else if(n1.board.manhattan()+n1.moves > n2.board.manhattan()+n2.moves) {
                    return -1;
                }
                else return 0;
            }
        };
        return nodeComparator;
    }
    public Solver(Board initial){
        // node = board, num of moves to reach board, previous node
        Node first = new Node(initial, 0, null);
        MinPQ<Node> pq = new MinPQ<>(pOrder());
        pq.insert(first);
        int numMoves = 0;
        while(true){
            Node parent = pq.delMin(); // how to sort according to priority = moves + manhattan?
            if(parent.board.hamming() == 0){
                break;
            }
            numMoves++;
            // add neighbors
            Iterable<Board> neighbours = parent.board.neighbors();
            for (Board n :neighbours) {
                Node newNode = new Node(n, numMoves, parent);
                pq.insert(newNode);
            }
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable()

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()

    // test client (see below)
    public static void main(String[] args)

}
