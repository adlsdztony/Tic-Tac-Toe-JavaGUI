package game;

/**
 * TicTacToe class
 * It contains the board and the current player.
 * 
 * @author Zhou Zilong
 * @since 2023-11-27
 */
public class Board {
    private char[][] board;

    /**
     * Constructor
     * Initialize the game
     */
    public Board() {
        board = new char[3][3];
        initializeBoard();
    }

    /**
     * Get the board
     * 
     * @return the board
     */
    public synchronized char[][] getBoard() {
        return board;
    }

    /**
     * Get the mark at the given position
     * 
     * @param row
     * @param col
     * @return the mark at the given position
     */
    public synchronized char getMark(int row, int col) {
        return board[row][col];
    }

    /**
     * Initialize the board
     */
    public void initializeBoard() {
        // initialize board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; ++j) {
                board[i][j] = '-';
            }
        }
    }

    /**
     * Check if the board is full
     * 
     * @return true if the board is full
     */
    public boolean isBoardFull() {
        // check if board is full
        boolean isFull = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == '-') {
                    isFull = false;
                }
            }
        }
        return isFull;
    }

    /**
     * Check if the board is full
     * 
     * @return true if the board is full
     */
    public synchronized boolean makeMove(int row, int col, char currentPlayer) {
        // place mark
        if ((row >= 0) && (row < 3)) {
            if ((col >= 0) && (col < 3)) {
                if (board[row][col] == '-') {
                    board[row][col] = currentPlayer;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the board is full
     * 
     * @return true if the board is full
     */
    public synchronized boolean makeMove(int position, char currentPlayer) {
        // place mark
        if ((position >= 1) && (position <= 9)) {
            int row = (position - 1) / 3;
            int col = (position - 1) % 3;
            return makeMove(row, col, currentPlayer);
        }
        return false;
    }

    /**
     * Check if the board is full
     * 
     * @return true if the board is full
     */
    public void printBoard() {
        // print board
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }
}
