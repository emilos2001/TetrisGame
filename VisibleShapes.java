package Tetris;

import java.awt.*;

public class VisibleShapes {
    private int[][] shape;
    Color color;
    private int x, y;

    public VisibleShapes(int[][] shape, Color color, int x, int y){
        this.shape = shape;
        this.color = color;
        this.x = x;
        this.y = y;
    }


    public void renderShape(Graphics g){
        g.setColor(color);
        for (int row = 0; row < shape.length; row++){
            for (int col = 0; col < shape[row].length; col++){
                if (shape[row][col] != 0){
                    int size = 25;
                    g.fillRect(x+ col * size, y + row * size, size, size);
                }
            }
        }
    }
}
