package Tetris;

import java.awt.*;


public class Shapes {
    final private int[][] references;
    public int[][] cords;
    public boolean moveX = false;
    long deltaTime;
    Color color;
    Board board;
    int deltaX;
    public static int normal = 600;
    int fast = 50;
    private int x, y;
    private long time, lastTime;
    static int speed;
    private boolean collision = false;
    private int timeToCollision = -1;


    public Shapes(int[][] cords, Board board, Color color) {
        this.cords = cords;
        this.board = board;
        this.color = color;
        deltaX = 0;
        x = 4;
        y = 0;
        speed = normal;
        time = 0;
        lastTime = System.currentTimeMillis();
        references = new int[cords.length][cords[0].length];
        System.arraycopy(cords, 0, references, 0, cords.length);
    }

    public void update() {
        moveX = true;
        deltaTime = System.currentTimeMillis() - lastTime;
        time += deltaTime;
        lastTime = System.currentTimeMillis();

        if (collision && timeToCollision > 100) {
            for (int row = 0; row < cords.length; row++) {
                for (int col = 0; col < cords[0].length; col++) {
                    if (cords[row][col] != 0) {
                        board.getBoardGame()[y + row][x + col] = color;
                    }
                }
            }
            checkLine();
            board.score();
            board.setCurrentShape();
            timeToCollision = -1;
        }

        if (!(x + deltaX + cords[0].length > 10) && !(x + deltaX < 0)) {
            for (int row = 0; row < cords.length; row++) {
                for (int col = 0; col < cords[row].length; col++) {
                    if (cords[row][col] != 0) {
                        if (board.getBoardGame()[y + row][x + deltaX + col] != null) {
                            moveX = false;
                        }

                    }
                }
            }
            if (moveX) {
                x += deltaX;
            }

        }

        if (timeToCollision == -1) {
            if (!(y + 1 + cords.length > 20)) {
                for (int row = 0; row < cords.length; row++) {
                    for (int col = 0; col < cords[row].length; col++) {
                        if (cords[row][col] != 0) {
                            if (board.getBoardGame()[y + 1 + row][x + col] != null) {
                                collision();
                            }
                        }
                    }
                }
                if (time > speed) {
                    y++;
                    time = 0;
                }
            } else {
                collision();
            }
        } else {
            timeToCollision += deltaTime;
        }

        deltaX = 0;
    }

    public void collision() {
        collision = true;
        timeToCollision = 0;
    }

    public void render(Graphics g) {
        g.setColor(color);
        for (int row = 0; row < cords.length; row++) {
            for (int col = 0; col < cords[0].length; col++) {
                if (cords[row][col] != 0) {
                    g.fillRect(col * 30 + x * 30, row * 30 + y * 30, Board.sizeOfBlock, Board.sizeOfBlock);
                }
            }
        }
    }

    private void checkLine() {
        int size = board.getBoardGame().length - 1;
        for (int i = size; i > 0; i--) {
            int count = 0;
            for (int j = 0; j < board.getBoardGame()[0].length; j++) {
                if (board.getBoardGame()[i][j] != null) {
                    count++;
                }
                board.getBoardGame()[size][j] = board.getBoardGame()[i][j];
            }

            if (count < board.getBoardGame()[0].length) {
                size--;
            }
        }
    }

    public void rotateShape() {

        int[][] rotatedShape = null;

        rotatedShape = transposeMatrix(cords);

        rotatedShape = reverseRows(rotatedShape);

        if ((x + rotatedShape[0].length > 10) || (y + rotatedShape.length > 20)) {
            return;
        }

        for (int row = 0; row < rotatedShape.length; row++) {
            for (int col = 0; col < rotatedShape[row].length; col++) {
                if (rotatedShape[row][col] != 0) {
                    if (board.getBoardGame()[y + row][x + col] != null) {
                        return;
                    }
                }
            }
        }
        cords = rotatedShape;
    }


    private int[][] transposeMatrix(int[][] matrix) {
        int[][] temp = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp[j][i] = matrix[i][j];
            }
        }
        return temp;
    }

    public int[][] reverseRows(int[][] matrix) {
        int middle = matrix.length / 2;
        for (int i = 0; i < middle; i++) {
            int[] temp = matrix[i];
            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length - i - 1] = temp;
        }
        return matrix;
    }

    public Color getColor() {
        return color;
    }

    public int[][] getCords() {
        return cords;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public void speedUp() {
        speed = fast;
    }

    public void speedDown() {
        speed = normal;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}