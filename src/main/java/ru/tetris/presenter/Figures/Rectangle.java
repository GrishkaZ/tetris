package ru.tetris.presenter.Figures;

import javafx.scene.canvas.GraphicsContext;

public class Rectangle extends Figure{

    @Override
    public void draw(GraphicsContext gc, int x, int y, double cellSize) {
        setGC(gc);
        if (this.gc == null) {
            throw new RuntimeException("gc = null on Figure, method draw");
        }
        double y1 = y - cellSize;
        double y2 = y1 - cellSize;
        double x2 = x + cellSize;
        this.fillCell(x, y1, cellSize);
        this.fillCell(x2, y1, cellSize);
        this.fillCell(x, y2, cellSize);
        this.fillCell(x2, y2, cellSize);
    }
}
