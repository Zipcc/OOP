package com.company;

abstract class TwoDimensionalShape {

    public TwoDimensionalShape() {
    }

    abstract double calculateArea();
    abstract int calculatePerimeterLength();
}

class Rectangle extends TwoDimensionalShape {
    int width;
    int height;

    public Rectangle(int w, int h) {
        width = w;
        height = h;
    }

    double calculateArea() {
        return width * height;
    }

    int calculatePerimeterLength() {
        return 2 * (width + height);
    }

    public String toString() {
        return "Rectangle of vvv dimensions " + width + " x " + height;
    }
}
class Circle extends TwoDimensionalShape {
    int radius;

    public Circle(int r) {
        radius = r;
    }

    double calculateArea() {
        return (int)Math.round(Math.PI * radius * radius);
    }

    int calculatePerimeterLength() {
        return (int)Math.round(Math.PI * radius * 2.0);
    }

    public String toString() {
        return "Circle with radius " + radius;
    }
}
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
public class test{

    public static void main(String[] args){

        TwoDimensionalShape rectangle = new Rectangle(2,3);
        TwoDimensionalShape triangle = new Triangle(1,2,3);
        TwoDimensionalShape circle = new Circle(2);
        System.out.println(circle);
        System.out.println(876);
    }
}

