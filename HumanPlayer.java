package org.example;

public class HumanPlayer extends Player {
    public HumanPlayer(int playerNumber, String symbol) {
        super(playerNumber, symbol);
    }

    @Override
    public void placePiece(Board board, int column) {
        if (!board.isColumnFull(column + 1)) {
            int row = board.getNextAvailableSlot(column);
            board.setPiece(row, column, symbol);
        }
    }
}
