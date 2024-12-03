package org.example;

import javax.swing.*;
import java.awt.*;

public class GameGUI extends JFrame {
    private Game game;
    private JButton[][] buttons;
    private JPanel boardPanel;
    private JLabel messageLabel;
    private String player1Name;
    private String player2Name;

    public GameGUI() {
        setTitle("Connect 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        initializeWelcomeScreen();
    }

    private void initializeWelcomeScreen() {
        JLabel titleLabel = new JLabel("Connect4", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(130, 0, 20, 0));

        JPanel modePanel = new JPanel();
        modePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton twoPlayerButton = new JButton("Two Player Game");
        JButton vsComputerButton = new JButton("Player vs Computer");

        twoPlayerButton.addActionListener(e -> initializePlayerNames(false));
        vsComputerButton.addActionListener(e -> initializePlayerNames(true));

        twoPlayerButton.setFont(new Font("Arial", Font.BOLD, 16));
        twoPlayerButton.setBackground(new Color(255,255,255));
        twoPlayerButton.setFocusPainted(false);
        twoPlayerButton.setPreferredSize(new Dimension(200, 50));

        vsComputerButton.setFont(new Font("Arial", Font.BOLD, 16));
        vsComputerButton.setBackground(new Color(255,255,255));
        vsComputerButton.setFocusPainted(false);
        vsComputerButton.setPreferredSize(new Dimension(200, 50));

        modePanel.add(twoPlayerButton);
        modePanel.add(vsComputerButton);

        add(titleLabel, BorderLayout.NORTH);
        add(modePanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void initializePlayerNames(boolean isComputerOpponent) {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel player1Label = new JLabel("Player 1 Name:");
        JTextField player1Field = new JTextField();
        JLabel player2Label = new JLabel("Player 2 Name:");
        JTextField player2Field = new JTextField();

        if (isComputerOpponent) {
            player2Field.setText("Computer");
            player2Field.setEnabled(false);
        }

        inputPanel.add(player1Label);
        inputPanel.add(player1Field);
        inputPanel.add(player2Label);
        inputPanel.add(player2Field);

        int result = JOptionPane.showConfirmDialog(
                this,
                inputPanel,
                "Enter Player Names",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            player1Name = player1Field.getText().trim();
            player2Name = player2Field.getText().trim();
            if (player1Name.isEmpty()) player1Name = "Player 1";
            if (player2Name.isEmpty()) player2Name = "Player 2";
            startGame(isComputerOpponent);
        } else {
            System.exit(0);
        }
    }

    private void startGame(boolean isComputerOpponent) {
        getContentPane().removeAll();

        game = new Game(isComputerOpponent, this, player1Name, player2Name);
        buttons = new JButton[6][7];
        boardPanel = new JPanel(new GridLayout(6, 7));

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(80, 80));
                buttons[i][j].setBackground(new Color(240, 240, 240));
                buttons[i][j].setOpaque(true);
                buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setForeground(Color.BLACK);

                final int column = j;
                buttons[i][j].addActionListener(e -> game.playTurn(column));
                boardPanel.add(buttons[i][j]);
            }
        }

        messageLabel = new JLabel(player1Name + "'s turn", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));

        add(boardPanel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    public void updateBoard() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                String piece = game.getBoard().getPiece(i, j);
                buttons[i][j].setText(piece);
                if (piece.equals("X")) {
                    buttons[i][j].setForeground(Color.RED);
                } else if (piece.equals("O")) {
                    buttons[i][j].setForeground(Color.BLUE);
                }
            }
        }
    }

    public void showMessage(String message) {
        messageLabel.setText(message);

        // After showing the message, prompt to play again
        int response = JOptionPane.showConfirmDialog(this, message + "\nDo you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            // Ask the player to choose the game mode again
            int modeResponse = JOptionPane.showOptionDialog(
                    this,
                    "Choose Game Mode:",
                    "Game Mode Selection",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Two Player Game", "Player vs Computer"},
                    "Two Player Game"
            );

            // Restart the game with the selected mode
            if (modeResponse == 0) {
                startGame(false); // Two-player game
            } else if (modeResponse == 1) {
                startGame(true); // Player vs Computer
            }
        } else {
            System.exit(0);  // Exit the game
        }
    }

    public void updateTurnLabel(String playerName) {
        messageLabel.setText(playerName + "'s turn");
    }
}


class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private GameGUI gui;
    private String player1Name;
    private String player2Name;

    public Game(boolean isComputerOpponent, GameGUI gui, String player1Name, String player2Name) {
        this.gui = gui;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        board = new Board();
        player1 = new HumanPlayer(1, "X");
        if (isComputerOpponent) {
            player2 = new ComputerPlayer(2, "O");
        } else {
            player2 = new HumanPlayer(2, "O");
        }
        currentPlayer = player1;
    }

    public void playTurn(int column) {
        if (!board.isGameOver()) {
            currentPlayer.placePiece(board, column);
            gui.updateBoard();
            if (board.getWinningPlayer() > 0) {
                String winnerName;
                if (board.getWinningPlayer() == 1) {
                    winnerName = player1Name;
                } else {
                    winnerName = (currentPlayer instanceof ComputerPlayer) ? "Computer" : player2Name;
                }
                gui.showMessage(winnerName + " Wins!");
            } else if (board.isDraw()) {
                gui.showMessage("It's a Draw!");
            } else {
                swapPlayerTurn();
                if (currentPlayer instanceof ComputerPlayer) {
                    ((ComputerPlayer) currentPlayer).placePiece(board, 0);
                    gui.updateBoard();
                    if (board.getWinningPlayer() > 0) {
                        String winnerName = (board.getWinningPlayer() == 1) ? player1Name : "Computer";
                        gui.showMessage(winnerName + " Wins!");
                    } else if (board.isDraw()) {
                        gui.showMessage("It's a Draw!");
                    } else {
                        swapPlayerTurn();
                    }
                }
            }
        }
    }

    private void swapPlayerTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        gui.updateTurnLabel(currentPlayer.getPlayerNumber() == 1 ? player1Name : player2Name);
    }

    public Board getBoard() {
        return board;
    }
}

