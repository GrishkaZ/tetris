package ru.tetris.presenter.Figures;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.tetris.utils.Utils;

public abstract class Figure {

    private static final Color[] COLORS = {Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN};

    protected GraphicsContext gc;
    protected Color color;
    private int orientation;

    public static final int TOP = 0;
    public static final int RIGHT = 1;
    public static final int LEFT = 2;
    public static final int BOTTOM = 3;

    public Figure() {
        color = Utils.randomChoice(COLORS);
//        color = Color.YELLOW;
        orientation = 0;
    }

    protected void setGC(GraphicsContext gc) {
        this.gc = gc;
        gc.setFill(color);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
    }

    public void setOrientation(int orientation) {
        if (orientation >=0 && orientation < 4) {
            this.orientation = orientation;
        } else {
            throw new RuntimeException("Incorrect orientation code: " + orientation + " from [0; 3]");
        }

    }

    public Color getColor() {
        for (Color color: COLORS) {
            if (this.color.equals(color)) {
                return color;
            }

        }
        return null;
    }

    /***
     * Реальный координаты канваса
     * @param x левый нижний угол x
     * @param y левый нижний угол  y
     * @param cellSize размер ячейки
     */
    public abstract void draw(GraphicsContext gc, int x, int y, double cellSize);


    /**
     * рисует с левого верхнего угла
     */
    protected void fillCell(double x, double y, double cellSize) {
        gc.fillRect(x, y , cellSize, cellSize);
        gc.strokeRect(x, y, cellSize, cellSize);
    }
}
