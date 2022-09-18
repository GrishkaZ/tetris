package ru.tetris.controller;

import ru.tetris.logic.Logic;

public class LogicController {

    private Logic logic;

    public LogicController(Logic logic) {
        this.logic = logic;
    }

    public void start() {
        logic.start();
    }

    public void start(int fps, double cellStepOnPeriod) {
        long period = 1000/fps;
        logic.start(period, cellStepOnPeriod);
    }

    public void stop() {
        logic.stop();
    }

    public void pause() {
        logic.pause();
    }

    public void moveLeft() {
        logic.moveLeft();
    }

    public void moveRight() {
        logic.moveRight();
    }
}
