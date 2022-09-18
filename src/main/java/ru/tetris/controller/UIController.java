package ru.tetris.controller;

import ru.tetris.logic.GameLogic;
import ru.tetris.presenter.Drawer;
import ru.tetris.presenter.Figures.Figure;
import ru.tetris.presenter.Figures.Rectangle;
import ru.tetris.presenter.Presenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UIController {

    private Presenter presenter;
    private Drawer drawer;

    public UIController(Presenter presenter, Drawer drawer) {
        this.presenter = presenter;
        this.drawer = drawer;
    }

    public void initializeGameField(int widthCellsCount, int heightCellsCount) {
        presenter.initialize(widthCellsCount, heightCellsCount, 10);
    }

    public void drawCurrentFigure(double x, double y) {
        drawer.drawBackground();
        drawer.drawCurrentFigure(x,y);
        drawer.drawMatrix();
    }

    public void createFigures(List<GameLogic.FigureType> figureTypes) {
//        figureTypes = List.of(GameLogic.FigureType.RECT, GameLogic.FigureType.RECT);
        List<Figure> figures = figureTypes.stream().map(this::figureOf).collect(Collectors.toList());
        drawer.setNewFigures(figures);
    }

    private Figure figureOf(GameLogic.FigureType figureType) {
        switch (figureType){
            case RECT:
                return new Rectangle();
            case LONG_STICK:
            case LEFT_STICK:
            case RIGHT_STICK:
            case LEFT_STEP:
            case RIGHT_STEP:
            default:
                return null;
        }
    }

    public void updateMatrix(Map<Integer, List<Integer>> matrix) {
        drawer.updateMatrix(matrix);
    }
}
