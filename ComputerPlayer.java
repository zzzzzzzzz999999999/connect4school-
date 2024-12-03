package org.example;

public class ComputerPlayer extends Player {
    public ComputerPlayer(int playerNumber, String symbol) {
        super(playerNumber, symbol);
    }

    @Override
    public void placePiece(Board board, int column) {
        while (board.isColumnFull(column + 1)) {
            column = (int) (Math.random() * 7);
        }

        int row = board.getNextAvailableSlot(column);
        board.setPiece(row, column, symbol);
    }
}