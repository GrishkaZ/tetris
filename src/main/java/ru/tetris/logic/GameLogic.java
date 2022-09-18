package ru.tetris.logic;

import ru.tetris.controller.UIController;
import ru.tetris.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameLogic  implements Logic{

    private UIController uiController;
    private GameCycle gameCycle;

    private GameParameters params;

    private enum GameCondition {
        INIT,
        STARTED,
        RESUME,
        IN_PROCESS,
        STOPPED
    }

    public enum FigureType {
        RECT {

            @Override
            int currentWidth() {
                return 2;
            }

            @Override
            List<List<Integer>> getCoord() {
                List<List<Integer>> simpleCoords = Arrays.asList(
                        List.of(0,1),
                        List.of(0,1)
                );
                List<List<List<Integer>>> coords = new ArrayList<>();
                coords.add(simpleCoords);
                return coords.get(getOrientation().ordinal());
            }
        },
        LONG_STICK {
            @Override
            int currentWidth() {
                return getOrientation() == Orientation.TOP || getOrientation() == Orientation.BOTTOM ? 1 : 4;
            }

            @Override
            List<List<Integer>> getCoord() {
                return null;
            }
        },
        LEFT_STICK {
            @Override
            List<List<Integer>> getCoord() {
                return null;
            }
        },
        RIGHT_STICK {
            @Override
            List<List<Integer>> getCoord() {
                return null;
            }
        },
        LEFT_STEP {
            @Override
            List<List<Integer>> getCoord() {
                return null;
            }
        },
        RIGHT_STEP {
            @Override
            List<List<Integer>> getCoord() {
                return null;
            }
        };

        public enum Orientation {
            TOP,
            RIGHT,
            LEFT,
            BOTTOM
        }

        private Orientation orientation = Orientation.TOP;

        public Orientation getOrientation() {
            return  orientation;
        }

        void setOrientation(Orientation orientation) {
            this.orientation = orientation;
        }

        int currentWidth() {
            return getOrientation() == Orientation.TOP || getOrientation() == Orientation.BOTTOM ? 2 : 3;
        }

        abstract List<List<Integer>> getCoord();

    }

    //matrix size 10x25 в то числе учитываются дробные числа. отрисовка с нижнего левого угла

    public GameLogic(UIController uiController,
                     int widthCellsCount,
                     int heightCellsCount,
                     int fps, double cellStepOnPeriod) {
        gameCycle = new GameCycle();
        params = new GameParameters(GameCondition.INIT, widthCellsCount, heightCellsCount, fps, cellStepOnPeriod);

        this.uiController = uiController;
        this.uiController.initializeGameField(params.widthCellsCount, params.heightCellsCount);
    }


    private void logicCycleFunction(double cellStepOnPeriod) {
        if (params.gameCondition == GameCondition.INIT) {
            params.randomInitFigures();
            params.randomInitCoords(0,params.widthCellsCount - params.currentFigure.currentWidth(), 0);
            uiController.createFigures(params.currentFigures);
            params.gameCondition = GameCondition.STARTED;
        } else {
            params.gameCondition = GameCondition.IN_PROCESS;
        }

        if (params.y > params.heightCellsCount) {
            params.y = params.heightCellsCount;
            Map<Integer, List<Integer>> realCoords = updateMatrix();
            uiController.updateMatrix(realCoords);
            params.newFigure();
            params.randomInitCoords(0,params.widthCellsCount - params.currentFigure.currentWidth(), 0);
            uiController.createFigures(params.currentFigures);
        } else {
            uiController.drawCurrentFigure(params.x, params.y);
            params.y += cellStepOnPeriod;
        }

    }

    private Map<Integer, List<Integer>> updateMatrix() {
        int y = (int) params.y;
        List<List<Integer>> coords = params.currentFigure.getCoord();
        System.out.println(params.currentFigure.name());
        System.out.println(params.currentFigure.getCoord().size());
        Map<Integer, List<Integer>> realCoords = new HashMap<>();
        for (List<Integer> xCoords: coords) {
            List<Integer> realXCoords = xCoords.stream().mapToInt(v -> v + (int)params.x).boxed().collect(Collectors.toList());
            params.matrix.get(y - 1).addAll(realXCoords); //потому что нумерация с 0, отрисовка с нулевой строки, а заполнение с 1
            realCoords.put(y - 1, realXCoords);
            y--;
        }

        return realCoords;
    }

    @Override
    public void start() {
        start( 1000 / params.fps, params.cellStepOnPeriod);
    }

    @Override
    public void start(long period, double cellStepOnPeriod) {
        System.out.println(params.gameCondition);
        if (params.gameCondition != GameCondition.RESUME) {
            params.gameCondition = GameCondition.INIT;
        }
        try {
            gameCycle.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    logicCycleFunction(cellStepOnPeriod);
                }
            }, period);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        params.gameCondition = GameCondition.STOPPED;
        gameCycle.stop();
        params.resetCoords();
        params.clearFigures();
        uiController.updateMatrix(null);
    }

    @Override
    public void pause() {
        params.gameCondition = GameCondition.RESUME;
        gameCycle.stop();
    }

    @Override
    public void moveLeft() {
        if (params.gameCondition == GameCondition.STARTED || params.gameCondition == GameCondition.IN_PROCESS) {
            if (params.x != 0) {
                if (params.x < 1) {
                    params.x = 0;
                } else {
                    params.x -= 1;
                }
            }
        }
    }

    @Override
    public void moveRight() {
        if (params.gameCondition == GameCondition.STARTED || params.gameCondition == GameCondition.IN_PROCESS) {
            int border = params.widthCellsCount - params.currentFigure.currentWidth();
            if (params.x != border) {
                if (params.x + 1 > border) {
                    params.x = border;
                } else {
                    params.x += 1;
                }
            }
        }
    }

    private class GameParameters {
        int widthCellsCount;
        int heightCellsCount;

        int fps;
        double cellStepOnPeriod;

        double x;
        double y;
        GameCondition gameCondition;
        LinkedList<FigureType> currentFigures;
        FigureType currentFigure;

        LinkedList<HashSet<Integer>> matrix;
        int noEmptyRow;

        GameParameters(GameCondition gameCondition,
                       int widthCellsCount, int heightCellsCount,
                       int fps, double cellStepOnPeriod) {
            this.gameCondition = gameCondition;
            this.fps = fps;
            this.cellStepOnPeriod = cellStepOnPeriod;
            x = 0;
            y = 0;
            this.widthCellsCount = widthCellsCount;
            this.heightCellsCount = heightCellsCount;
            currentFigures = new LinkedList<>();
            initMatrix();
        }

        void initMatrix() {
            matrix = Stream.generate(() -> new HashSet<Integer>())
                    .limit(heightCellsCount).collect(Collectors.toCollection(LinkedList::new));
            noEmptyRow  = heightCellsCount;
        }

        void resetCoords() {
            x = 0;
            y = 0;
        }

        void clearFigures() {
            currentFigures.clear();
            currentFigure = null;
        }

        void randomInitCoords(double from, double to, int significantPoint) {
            y = 0;
            x = Utils.roundToSignificant(from + Math.random() * (to - from),significantPoint);
        }

        void randomInitFigures() {
//            currentFigures.add(Utils.randomChoice(FigureType.values()));
//            currentFigures.add(Utils.randomChoice(FigureType.values()));
            currentFigures.add(FigureType.RECT);
            currentFigures.add(FigureType.RECT);
            currentFigure = currentFigures.getFirst();
        }

        void newFigure() {
            currentFigures.pollFirst();
            currentFigure = currentFigures.getFirst();
//            currentFigures.add(Utils.randomChoice(FigureType.values()));
            currentFigures.add(FigureType.RECT);
        }


    }
}
