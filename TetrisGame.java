package Tetris;

import javax.swing.*;

public class TetrisGame {
    public static final int width = 500;
    public static final int height = 720;
    private JFrame window;
    private Board board;

    public TetrisGame() {
        window = new JFrame();
        window.setSize(width, height);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
        board = new Board();
    }

    public void startGame(){
        window.addKeyListener(board);
        window.add(board);
        board.startGame();
        window.revalidate();
    }

    public static void main(String[] args) {
        TetrisGame tetrisGame = new TetrisGame();
        tetrisGame.startGame();
    }
}
