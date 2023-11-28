import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

public class Board {

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private int[][] board;
    private int length;

    public Board(int[][] tiles) {
        board = tiles;
        length = board.length;
    }

    // string representation of this board
    public String toString() {
        String toreturn = "";
        toreturn += dimension() + "\n";
        for (int[] row : board) {
            for (int num : row) {
                toreturn += " ";
                toreturn += "" + num;
                toreturn += " ";
            }
            toreturn += "\n";
        }
        return toreturn;
    }

    // board dimension n
    public int dimension() {
        length = board[0].length;
        return length;
    }

    // number of tiles out of place
    public int hamming() {
        // first find the endgame pos for each tile
        // 0,0 has 1
        // 1,0 has 4
        // dimen*1st digit + 2nd digit+1
        int counter = 0;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                // don't count the empty spot
                if (board[i][j] == 0) {
                    continue;
                }
                else if (board[i][j] != length * i + j + 1) {
                    counter++;
                }
            }
        }
        return counter;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // go through all the spots
        int accumulator = 0;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0) {
                    continue;
                }
                // whenever there is a point that doesn't match
                if (board[i][j] != length * i + j + 1) {
                    int num = board[i][j];
                    int row = num / length;
                    int col = (num - (row * length)) - 1;
                    int distance = Math.abs(row - i) + Math.abs(col - j);
                    accumulator += distance;
                }
            }
        }
        return accumulator;

    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y.getClass() != this.getClass())
            throw new IllegalArgumentException("Wrong type of argument!");
        Board myy = (Board) y;
        if (this.length == myy.length) {
            for (int i = 0; i < board[0].length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (this.board[i][j] != myy.board[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // same boards but the blank square is beside
        // find the location of blank square
        int index = -256;
        int row = 0;
        int col = 0;
        Queue<Board> neighbourQueue = new Queue<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    index = i * length + j;
                }
            }
        }
        // checking neighbours
        // check up
        if (index - length >= 0) {
            int[][] boardCopy = new int[board.length][board.length];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    boardCopy[i][j] = board[i][j];
                }
            }
            int tmp = boardCopy[row][col];
            if (row > 0) {
                boardCopy[row][col] = boardCopy[row - 1][col];
                boardCopy[row - 1][col] = tmp;
                Board neighborBoard = new Board(boardCopy);
                neighbourQueue.enqueue(neighborBoard);
            }
        }
        // check down
        if (index + length >= 0) {
            int[][] boardCopy = new int[board.length][board.length];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    boardCopy[i][j] = board[i][j];
                }
            }
            int tmp = boardCopy[row][col];
            if (row < length - 1) {
                boardCopy[row][col] = boardCopy[row + 1][col];
                boardCopy[row + 1][col] = tmp;
                Board neighborBoard = new Board(boardCopy);
                neighbourQueue.enqueue(neighborBoard);
            }
        }
        // check left
        if (index - 1 >= 0) {
            int[][] boardCopy = new int[board.length][board.length];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    boardCopy[i][j] = board[i][j];
                }
            }
            int tmp = boardCopy[row][col];
            if (col > 0) {
                boardCopy[row][col] = boardCopy[row][col - 1];
                boardCopy[row][col - 1] = tmp;
                Board neighborBoard = new Board(boardCopy);
                neighbourQueue.enqueue(neighborBoard);
            }
        }
        // check right
        if (index + 1 >= 0) {
            int[][] boardCopy = new int[board.length][board.length];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    boardCopy[i][j] = board[i][j];
                }
            }
            int tmp = board[row][col];
            if (col < length - 1) {
                boardCopy[row][col] = boardCopy[row][col + 1];
                boardCopy[row][col + 1] = tmp;
                Board neighborBoard = new Board(boardCopy);
                neighbourQueue.enqueue(neighborBoard);
            }
        }
        return neighbourQueue;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] boardCopy = new int[length][length];
        int origRow = StdRandom.uniformInt(length);
        int origCol = StdRandom.uniformInt(length);
        int otherRow;
        int otherCol;
        do {
            otherRow = StdRandom.uniformInt(length);
            otherCol = StdRandom.uniformInt(length);
        } while (otherRow == origRow || otherCol == origCol);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                boardCopy[i][j] = board[i][j];
            }

        }
        int tmp = board[origRow][origCol];
        boardCopy[origRow][origCol] = boardCopy[otherRow][otherCol];
        boardCopy[otherRow][otherCol] = tmp;
        Board twinBoard = new Board(boardCopy);
        return twinBoard;
    }

    // unit testing (not graded)
    // public static void main(String[] args)

}
