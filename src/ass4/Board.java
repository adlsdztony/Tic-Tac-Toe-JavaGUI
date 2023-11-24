package ass4;

public class Board {
    private char[][] board;

    public Board() {
        board = new char[3][3];
        initializeBoard();
    }

    public synchronized char[][] getBoard() {
        return board;
    }

    public synchronized char getMark(int row, int col) {
        return board[row][col];
    }

    public void initializeBoard() {
        // initialize board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; ++j) {
                board[i][j] = '-';
            }
        }
    }

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

    public synchronized boolean makeMove(int position, char currentPlayer) {
        // place mark
        if ((position >= 1) && (position <= 9)) {
            int row = (position - 1) / 3;
            int col = (position - 1) % 3;
            return makeMove(row, col, currentPlayer);
        }
        return false;
    }

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
