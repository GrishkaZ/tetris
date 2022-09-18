package ru.tetris.presenter;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import ru.tetris.presenter.Figures.Figure;

import java.util.*;

public class DrawerFX extends Drawer {

    private Canvas canvas;
    private GraphicsContext gc;
    private DrawMatrix colorMatrix; //нумерация снизу (первый ряд самый нижний)

    DrawerFX() {
    }

    void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = this.canvas.getGraphicsContext2D();
        this.colorMatrix = new DrawMatrix();
    }

    public void drawBackground() {
//        gc.save();
//        gc.restore();

        double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.setFill(Color.AQUA);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double cellSize = canvas.getWidth() / widthCellsCount;

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);

        double stroke = cellSize;
        while (stroke < w) {
            gc.strokeLine(stroke, 0, stroke, h);
            stroke += cellSize;
        }
        stroke = cellSize;
        while (stroke < h) {
            gc.strokeLine(0, h - stroke, w, h - stroke);
            stroke += cellSize;
        }
    }

    @Override
    public void updateMatrix(Map<Integer, List<Integer>> coords) {
        if (coords == null) {
            colorMatrix.getMap().clear();
            return;
        }
        Color color = currentFigure.getColor();
        System.out.println(color.toString());
        DrawMatrix.ColorEntry rows = colorMatrix.get(color);
        coords.forEach(rows::put);
        /*colorMatrix.getMap().forEach((colorr, entry) -> {
            System.out.println(colorr.toString());
            System.out.println(entry.indexes);
            entry.rows.forEach(System.out::println);
        });*/
    }


    @Override
    public void drawCurrentFigure(double x, double y) {

        double cellSize = canvas.getWidth() / widthCellsCount;

        int realX = (int) (cellSize * x);
        int realY = (int) (cellSize * y);

        this.currentFigure.draw(gc, realX, realY, cellSize);
    }

    @Override
    protected void drawNextMiniFigure(Figure figure) {

    }

    @Override
    public void drawMatrix() {

        double cellSize = canvas.getWidth() / widthCellsCount;

        colorMatrix.getMap().forEach((color, rows) -> {
            gc.setFill(color);
            rows.indexes.forEach(y -> {
                int realY = (int) (cellSize * y);
//            rows.rows.get(y);
                rows.rows.get(rows.indexes.indexOf(y)).forEach(x -> {
                    int realX = (int) (cellSize * x);
                    gc.fillRect(realX, realY, cellSize, cellSize);
                    gc.strokeRect(realX, realY, cellSize, cellSize);
                });
            });
        });

    }


    void drawShapes() {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);
    }

}
