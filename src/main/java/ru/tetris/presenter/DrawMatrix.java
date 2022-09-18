package ru.tetris.presenter;

import javafx.scene.paint.Paint;

import java.util.*;

public class DrawMatrix {

    private Map<Paint, ColorEntry> colorMatrix;

    DrawMatrix() {
        colorMatrix = new HashMap<>();
    }

    Map<Paint, ColorEntry> getMap() {
        return colorMatrix;
    }

    public ColorEntry get(Paint color) {
        if (!colorMatrix.containsKey(color)) {
            colorMatrix.put(color, new ColorEntry());
        }
        return colorMatrix.get(color);
    }

    public void removeRow(int y) {
        colorMatrix.forEach((color, entry) -> entry.removeRow(y));
    }


    static class ColorEntry {
        List<Integer> indexes;
        LinkedList<List<Integer>> rows;

        //первый входящий индекс строки самый нижний
        ColorEntry() {
            indexes = new ArrayList<>();
            rows = new LinkedList<>();
        }

        public void put(Integer y, List<Integer> xx) {
            if (!indexes.contains(y)) {
                indexes.add(y);
                rows.addLast(xx);
            } else {
                List<Integer> xCoords = rows.get(indexes.indexOf(y));
                xx.forEach(x -> {
                    if (!xCoords.contains(x)) {
                        xCoords.add(x);
                    }
                });
            }
        }

        public void removeRow(int y) {
            List<Integer> newIndexes = new ArrayList<>();
            indexes.forEach(ind -> {
                if (y > ind) {
                    newIndexes.add(ind + 1);
                } else if (y == ind) {
                    rows.remove(indexes.indexOf(y));
                } else {
                    newIndexes.add(ind);
                }
            });

            indexes = newIndexes;

        }





    }
}



