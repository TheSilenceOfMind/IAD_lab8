package com;

public class Point {
    private float x;
    private float y;
    private float r;
    private boolean isInside;

    public Point(float x, float y, float r, boolean inside) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isInside = inside;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return this.r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public boolean getIsInside() {
        return this.isInside;
    }

    public String getIsInsideString() {
        if (this.isInside)
            return "IN";
        else
            return "OUT";
    }

    public void setIsInside(boolean inside) {
        this.isInside = inside;
    }

    public String toString() {
        return this.x + ", " + this.y + ", " + this.r + ": " + this.isInside;
    }
}