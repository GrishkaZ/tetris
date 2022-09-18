package ru.tetris.logic;

import java.util.Timer;
import java.util.TimerTask;

public class GameCycle {

    private Timer timer;

    GameCycle() {

    }

    Timer getTimer() {
        return timer;
    }

    void scheduleAtFixedRate(TimerTask task, long period) {
        if (timer != null) {
            stop();
        }
        this.timer = new Timer("MainTimer", true);
        timer.scheduleAtFixedRate(task, 100, period);
    }

    void stop() {
       if (timer != null) {
           timer.cancel();
           timer.purge();
           timer = null;
       }
    }










}
