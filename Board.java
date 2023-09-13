package Tetris;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class Board extends JPanel implements KeyListener {
    static final int sizeOfBlock = 30;
    static Shapes currentShape, nextShape;
    static ArrayList<VisibleShapes> visibleShapes;
    public static int currentLevel = 1;
    private static boolean gameOver = false;
    private final int boardH = 50;
    private final int boardW = 10;
    private final Random random = new Random();
    private final Color[][] boardGame = new Color[boardH][boardW];
    private final Color[] colors = {new Color(237, 28, 36), new Color(255, 127, 39), new Color(255, 242, 0),
            new Color(34, 177, 76), new Color(0, 162, 232), new Color(163, 73, 164),
            new Color(63, 72, 204)};
    public static Shapes[] shapes = new Shapes[7];
    public int fps;
    public int delay;
    Timer loop;
    int totalScore;
    int score;

    public Board() {
        fps = 60;
        delay = 1000 / fps;
        loop = new Timer(delay, new GameLooper());
        shapes[0] = new Shapes(new int[][]{{1, 1, 1, 1}}, this, colors[0]);
        shapes[1] = new Shapes(new int[][]{{1, 1, 1}, {0, 1, 0}}, this, colors[1]);
        shapes[2] = new Shapes(new int[][]{{1, 1, 1}, {1, 0, 0}}, this, colors[2]);
        shapes[3] = new Shapes(new int[][]{{1, 1, 1}, {0, 0, 1}}, this, colors[3]);
        shapes[4] = new Shapes(new int[][]{{0, 1, 1}, {1, 1, 0}}, this, colors[4]);
        shapes[5] = new Shapes(new int[][]{{1, 1, 0}, {0, 1, 1}}, this, colors[5]);
        shapes[6] = new Shapes(new int[][]{{1, 1}, {1, 1}}, this, colors[6]);
        visibleShapes = new ArrayList<>();
        visibleShapes.add(new VisibleShapes(new int[][]{{1, 1, 1, 1}}, colors[1], 330, 110));
        visibleShapes.add(new VisibleShapes(new int[][]{{1, 1, 1}, {0, 1, 0}}, colors[2], 330, 160));
        visibleShapes.add(new VisibleShapes(new int[][]{{1, 1}, {1, 1}}, colors[3], 340, 225));
        visibleShapes.add(new VisibleShapes(new int[][]{{0, 1, 1}, {1, 1, 0}}, colors[4], 330, 300));
        visibleShapes.add(new VisibleShapes(new int[][]{{1, 1, 0}, {0, 1, 1}}, colors[5], 330, 370));
        visibleShapes.add(new VisibleShapes(new int[][]{{1, 1, 1, 1}, {0, 0, 0, 1}}, colors[6], 330, 440));
    }

    private void update() {
        if (gameOver) {
            return;
        }
        currentShape.update();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(20, 82, 21));
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int row = 0; row < boardGame.length; row++) {
            for (int col = 0; col < boardGame[row].length; col++) {
                if (boardGame[row][col] != null) {
                    g.setColor(boardGame[row][col]);
                    g.fillRect(col * sizeOfBlock, row * sizeOfBlock, sizeOfBlock, sizeOfBlock);
                }
            }
        }
        g.setColor(nextShape.getColor());
        for (int row = 0; row < nextShape.getCords().length; row++) {
            for (int col = 0; col < nextShape.getCords()[0].length; col++) {
                if (nextShape.getCords()[row][col] != 0) {
                    g.fillRect(col * 30 + 330, row * 30 + 580, sizeOfBlock, sizeOfBlock);
                }
            }
        }
        for (VisibleShapes shapes1 : visibleShapes) {
            shapes1.renderShape(g);
        }
        currentShape.render(g);
        nextLevel();
        if (gameOver) {
            g.setColor(Color.white);
            g.setFont(new Font("Georgia", Font.BOLD, 15));
            g.drawString("GameOver", 340, 250);
        }
        g.setColor(Color.white);
        g.setFont(new Font("Georgia", Font.BOLD, 25));
        g.drawString("Score :  " + totalScore, 310, 30);
        g.drawString("Level : " + currentLevel, 310, 70);
        g.setFont(new Font("Georgia", Font.BOLD, 20));
        g.drawString("Next shape", 310, 550);
        g.setColor(new Color(0, 0, 0));
        //vertical
        for (int i = 0; i <= boardH - 30; i++) {
            g.drawLine(0, i * sizeOfBlock, boardW * sizeOfBlock, i * sizeOfBlock);
        }
        //horizontal
        for (int i = 0; i <= boardW; i++) {
            g.drawLine(i * sizeOfBlock, 0, i * sizeOfBlock, boardH * 12);
        }
    }

    public void nextLevel() {
       int points = 5;
        int increase = score / points;
        if (score >= points) {
            currentLevel += increase;
            score %= points;
        }
    }

    public void setNextShape() {
        int index = random.nextInt(shapes.length);
        int colorIndex = random.nextInt(colors.length);
        nextShape = new Shapes(shapes[index].getCords(), this, colors[colorIndex]);
    }

    public void setCurrentShape() {
        currentShape = nextShape;
        setNextShape();
        for (int row = 0; row < currentShape.getCords().length; row++) {
            for (int col = 0; col < currentShape.getCords()[0].length; col++) {
                if (currentShape.getCords()[row][col] != 0) {
                    if (boardGame[currentShape.getY() + row][currentShape.getX() + col] != null) {
                        gameOver = true;
                    }
                }
            }
        }
    }


    public void startGame() {
        stopGame();
        setNextShape();
        setCurrentShape();
        gameOver = false;
        loop.start();
        totalScore = 0;
        currentLevel = 1;
    }


    public void stopGame() {
        score = 0;
        for (Color[] value : boardGame) {
            Arrays.fill(value, null);
        }
        loop.stop();
    }

    public Shapes[] getShape() {
        return shapes;
    }

    public Color[][] getBoardGame() {
        return boardGame;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            currentShape.rotateShape();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentShape.setDeltaX(1);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            currentShape.setDeltaX(-1);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.speedUp();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            startGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.speedDown();
        }
    }

    public void score() {
        totalScore++;
        score++;
    }

    class GameLooper implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            update();
            repaint();
        }
    }
}
