package ru.tetris.presenter;


import ru.tetris.presenter.Figures.Figure;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class Drawer {

    protected int widthCellsCount;
    protected int heightCellsCount;
    protected Figure currentFigure;

    /***
     * в относительных координатах (по ячейкам)
     * @param x левый нижний угол
     * @param y левый нижний угол
     */
    public abstract void drawCurrentFigure(double x, double y);
    protected abstract void drawNextMiniFigure(Figure figure);

    public void setWidthCellsCount(int widthCellsCount) {
        this.widthCellsCount = widthCellsCount;
    }

    public void setHeightCellsCount(int heightCellsCount) {
        this.heightCellsCount = heightCellsCount;
    }

    public void setNewFigures(List<Figure> figures) {
        currentFigure = figures.get(0);
        drawNextMiniFigure(figures.get(1));
    }

    public abstract void drawBackground();


    public abstract void updateMatrix(Map<Integer, List<Integer>> matrix);

    public abstract void drawMatrix();
}
