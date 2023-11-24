
package ass4;

public class TicTacToe {
    // map of the board
    private Board board;
    private char currentPlayer;

    public static void main(String[] args) {
        // sample input list
        int inputList[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int turn = 0;

        TicTacToe game = new TicTacToe();
        game.newGame();
        while (!game.isBoardFull()) {
            game.printBoard();
            // get input from player
            int input = inputList[turn];
            turn++;
            // place input on board
            game.makeMove(input);

            if (game.checkForWin()) {
                game.changePlayer();
                game.printBoard();
                System.out.println("Player " + game.getCurrentPlayer() + " wins!");
                break;
            }
        }
        
    }

    public TicTacToe() {
        newGame();
    }

    public void newGame() {
        board = new Board();
        currentPlayer = 'x';
        initializeBoard();
    }

    public synchronized char getCurrentPlayer() {
        return currentPlayer;
    }

    public synchronized Board getBoard() {
        return board;
    }
    
    private void initializeBoard() {
        // initialize board
        board.initializeBoard();
    }

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
        return ((checkRowCol(board.getMark(0, 0), board.getMark(1, 1), board.getMark(2, 2))) || (checkRowCol(board.getMark(0, 2), board.getMark(1, 1), board.getMark(2, 0))));
    }

    private boolean checkRowCol(char c1, char c2, char c3) {
        // check if all values are the same
        return ((c1 != '-') && (c1 == c2) && (c2 == c3));
    }

    public boolean checkForWin() {
        return (checkRowsForWin() || checkColumnsForWin() || checkDiagonalsForWin());
    }


    public void changePlayer() {
        // change player
        if (currentPlayer == 'x') {
            currentPlayer = 'o';
        } else {
            currentPlayer = 'x';
        }
    }

    public synchronized boolean makeMove(int row, int col) {
        return board.makeMove(row, col, currentPlayer);
    }

    public synchronized boolean makeMove(int position) {
        return board.makeMove(position, currentPlayer);
    }

    public void printBoard() {
        // print board
        board.printBoard();
    }
}
