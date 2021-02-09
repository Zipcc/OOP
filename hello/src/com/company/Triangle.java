package com.company;

class Triangle extends TwoDimensionalShape {
    int x;
    int y;
    int z;

    public Triangle(int tx, int ty, int tz){
        x = tx;
        y = ty;
        z = tz;
    }

    public String toString(){
        return "\n\n\nThis is asdf" + x + y + z ;
    }

    int getLongestSide(){
        int max;
        max = Math.max(x, y);
        if(z >= max){
            max = z;
        }
        return max;
    }

    public double calculateArea() {
        return -1;
    }

    public int calculatePerimeterLength() {
        return -1;
    }
}