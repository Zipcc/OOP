package com.company;

class Triangle extends TwoDimensionalShape implements MultiVariantShape {

    private final int x;
    private final int y;
    private final int z;
    static int population = 0;
    private final TriangleVariant variant;

    public Triangle(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        population++;
        if (x <= 0 || y <= 0 || z <= 0) {
            variant = TriangleVariant.ILLEGAL;
        } else if (2 * getLongestSide() > calculatePerimeterLength()) {
            variant = TriangleVariant.IMPOSSIBLE;
        } else if (2 * getLongestSide() == calculatePerimeterLength()) {
            variant = TriangleVariant.FLAT;
        } else if (x * x == y * y + z * z || x * x + y * y == z * z || x * x + z * z == y * y) {
            variant = TriangleVariant.RIGHT;
        } else if (x == y && y == z) {
            variant = TriangleVariant.EQUILATERAL;
        } else if (x == y || y == z || x == z) {
            variant = TriangleVariant.ISOSCELES;
        } else {
            variant = TriangleVariant.SCALENE;
        }
    }

    public String toString() {
        return "\n\n\nThis is a triangle" + x + y + z;
    }

    int getLongestSide() {
        int max;
        max = Math.max(x, y);
        if (z >= max) {
            max = z;
        }
        return max;
    }

    public static int getPopulation(){
        return population;
    }

    public double calculateArea() {
        int s;

        s = (this.x + this.y + this.z) / 2;
        return Math.sqrt(s * (s - this.x) * (s - this.y) * (s - this.z));
    }

    public int calculatePerimeterLength() {
        return this.x + this.y + this.z;
    }

    public TriangleVariant getVariant() {
        return variant;
    }
}
