package com.ga.folding;

public class DirectionStruct {

    private int x, y;
    private char direction;

    public DirectionStruct(char direction, int x, int y) {
        this.direction = direction;
        this.x = x;
        this.y = y;
    }

    public char getDirection() {
        return this.direction;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
