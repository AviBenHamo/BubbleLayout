package com.daasuu.bl;

/**
 * Created by sudamasayuki on 16/06/14.
 */
public enum ArrowDirection {
    LEFT(0),
    START(1),
    RIGHT(2),
    END(3),
    TOP(4),
    BOTTOM(5),
    ;
    /*// CENTER
    LEFT_CENTER(4),
    RIGHT_CENTER(5),
    TOP_CENTER(6),
    BOTTOM_CENTER(7),
    // HORIZONTAL > RIGHT
    TOP_RIGHT(8),
    BOTTOM_RIGHT(9);*/


    private int value;

    ArrowDirection(int value) {
        this.value = value;
    }

    public static ArrowDirection fromInt(int value) {
        for (ArrowDirection arrowDirection : ArrowDirection.values()) {
            if (value == arrowDirection.getValue()) {
                return arrowDirection;
            }
        }
        return LEFT;
    }

    public int getValue() {
        return value;
    }
}
