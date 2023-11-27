
package game;

/**
 * TicTacToe class
 * It contains the board and the current player.
 * 
 * @author Zhou Zilog
 * @since 2023-11-27
 */
public class TicTacToe {
    // map of the board
    private Board board;
    private char currentPlayer;

    /**
     * Constructor
     * Initialize the game
     */
    public TicTacToe() {
        newGame();
    }

    /**
     * Start a new game
     */
    public void newGame() {
        board = new Board();
        currentPlayer = 'x';
        initializeBoard();
    }

    /**
     * Get the current player
     * 
     * @return the current player
     */
    public synchronized char getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Get the board
     * 
     * @return the board
     */
    public synchronized Board getBoard() {
        return board;
    }

    /**
     * Get the board
     * 
     * @return the board
     */
    private void initializeBoard() {
        // initialize board
        board.initializeBoard();
    }

    /**
     * Check if the board is full
     * 
     * @return true if the board is full
     */
    public boolean isBoardFull() {
        // check if board is full
        return board.isBoardFull();
    }

    private boolean checkRowsForWin() {
        // check rows for win
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board.getMark(i, 0), board.getMark(i, 1), board.getMark(i, 2))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumnsForWin() {
        // check columns for win
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board.getMark(0, i), board.getMark(1, i), board.getMark(2, i))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalsForWin() {
        // check diagonals for win
        return ((checkRowCol(board.getMark(0, 0), board.getMark(1, 1), board.getMark(2, 2)))
                || (checkRowCol(board.getMark(0, 2), board.getMark(1, 1), board.getMark(2, 0))));
    }

    private boolean checkRowCol(char c1, char c2, char c3) {
        // check if all values are the same
        return ((c1 != '-') && (c1 == c2) && (c2 == c3));
    }

    /**
     * Check if the game is over
     * 
     * @return true if the game is over
     */
    public boolean checkForWin() {
        return (checkRowsForWin() || checkColumnsForWin() || checkDiagonalsForWin());
    }

    /**
     * Change the player
     */
    public void changePlayer() {
        // change player
        if (currentPlayer == 'x') {
            currentPlayer = 'o';
        } else {
            currentPlayer = 'x';
        }
    }

    /**
     * Make a move
     * 
     * @param row
     * @param col
     * @return true if the move is valid
     */
    public synchronized boolean makeMove(int row, int col) {
        return board.makeMove(row, col, currentPlayer);
    }

    /**
     * Make a move
     * 
     * @param position
     * @return true if the move is valid
     */
    public synchronized boolean makeMove(int position) {
        return board.makeMove(position, currentPlayer);
    }

    /**
     * Print the board
     */
    public void printBoard() {
        // print board
        board.printBoard();
    }
}
