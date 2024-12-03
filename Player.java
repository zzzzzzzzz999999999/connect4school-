package org.example;

public class Player {
    public int playerNumber;
    public String symbol;

    public Player(int playerNumber, String symbol) {
        this.playerNumber = playerNumber;
        this.symbol = symbol;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public void placePiece(Board board, int column){
        System.out.println("Implement in child");
    }
}