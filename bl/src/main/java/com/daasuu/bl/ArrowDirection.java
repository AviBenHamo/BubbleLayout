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
    TOP_LEFT(6),
    BOTTOM_LEFT(7),
    TOP_START(8),
    BOTTOM_START(9),
    TOP_RIGHT(10),
    BOTTOM_RIGHT(11),
    TOP_END(12),
    BOTTOM_END(13),
    ;

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
