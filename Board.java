package org.example;

public class Board {
    private String[][] board;

    public Board() {
        board = new String[6][7];
        newBoard();
    }

    private void newBoard() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = " ";
            }
        }
    }

    public boolean isColumnFull(int column) {
        return !board[0][column - 1].equals(" ");
    }

    public int getNextAvailableSlot(int column) {
        int position = 5;
        while (position >= 0) {
            if (board[position][column].equals(" ")) {
                return position;
            }
            position--;
        }
        return -1;
    }

    public void setPiece(int row, int column, String symbol) {
        board[row][column] = symbol;
    }

    public String getPiece(int row, int column) {
        return board[row][column];
    }

    public int getWinningPlayer() {
        String checkVerticalWinner = checkVerticalWinner();
        if(checkVerticalWinner!= null){
            return checkVerticalWinner.equals("X") ? 1 : 2;
        }

        String checkHorizontalWinner = checkHorizontalWinner();
        if(checkHorizontalWinner !=null){
            return checkHorizontalWinner.equals("X") ? 1 : 2;
        }

        String checkLeftDiagonalWinner = checkLeftDiagonalWinner();
        if(checkLeftDiagonalWinner!=null){
            return checkLeftDiagonalWinner.equals("X") ? 1 : 2;
        }

        String checkRightDiagonalWinner = checkRightDiagonalWinner();
        if(checkRightDiagonalWinner!=null){
            return checkRightDiagonalWinner.equals("X") ? 1 : 2;
        }
        return 0; // No winner
    }

    public boolean isDraw() {
        return isBoardFull() && getWinningPlayer() == 0;
    }

    public boolean isGameOver() {
        return isDraw() || getWinningPlayer() > 0;
    }

    private boolean isBoardFull() {
        for (int j = 0; j < 7; j++) {
            if (board[0][j].equals(" ")) {
                return false;
            }
        }
        return true;
    }

    private String checkVerticalWinner() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (!board[i][j].equals(" ") &&
                        board[i][j].equals(board[i + 1][j]) &&
                        board[i][j].equals(board[i + 2][j]) &&
                        board[i][j].equals(board[i + 3][j])) {
                    return board[i][j];
                }
            }
        }
        return null;
    }

    private String checkHorizontalWinner() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (!board[i][j].equals(" ") &&
                        board[i][j].equals(board[i][j + 1]) &&
                        board[i][j].equals(board[i][j + 2]) &&
                        board[i][j].equals(board[i][j + 3])) {
                    return board[i][j];
                }
            }
        }
        return null;
    }

    private String checkLeftDiagonalWinner() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (!board[i][j].equals(" ") &&
                        board[i][j].equals(board[i + 1][j + 1]) &&
                        board[i][j].equals(board[i + 2][j + 2]) &&
                        board[i][j].equals(board[i + 3][j + 3])) {
                    return board[i][j];
                }
            }
        }
        return null;
    }

    private String checkRightDiagonalWinner() {
        for (int i = 0; i < 3; i++) {
            for (int j = 3; j < 7; j++) {
                if (!board[i][j].equals(" ") &&
                        board[i][j].equals(board[i + 1][j - 1]) &&
                        board[i][j].equals(board[i + 2][j - 2]) &&
                        board[i][j].equals(board[i + 3][j - 3])) {
                    return board[i][j];
                }
            }
        }
        return null;
    }
}
