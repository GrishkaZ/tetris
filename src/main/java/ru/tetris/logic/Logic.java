package ru.tetris.logic;

public interface Logic {
    void start();

    void start(long period, double cellStepOnPeriod);

    void stop();

    void pause();

    void moveLeft();

    void moveRight();
}
