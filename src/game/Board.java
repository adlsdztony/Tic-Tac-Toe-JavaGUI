package game;

/**
 * game.Board class
 * a board of the game
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
     * @param row the row of the position
     * @param col the column of the position
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
     * Make a move
     * 
     * @param row the row of the move
     * @param col the column of the move
     * @param currentPlayer the current player
     * @return true if the move is valid
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
     * Make a move
     * 
     * @param position the position of the move
     * @param currentPlayer the current player
     * @return true if the move is valid
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

}
