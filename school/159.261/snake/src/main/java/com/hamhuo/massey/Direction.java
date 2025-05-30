package com.hamhuo.massey;

public enum Direction {
    UP(0), DOWN(2), LEFT(1), RIGHT(3), READY(1);
    private final int value;
    Direction(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public static Direction getDirection(int value) {
        Direction[] values = Direction.values();
        for (Direction direction : values) {
            if (direction.getValue() == value) {
                return direction;
            }
        }
        return null;
    }
}
