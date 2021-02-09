package com.company;

public class test{

    public static void main(String[] args){

        TwoDimensionalShape rectangle = new Rectangle(2,3);
        TwoDimensionalShape triangle = new Triangle(1,2,3);
        TwoDimensionalShape cool = new Circle(2);
        triangle.setColour(Colour.BROWN);
        rectangle.setColour(Colour.GREEN);

        System.out.println(triangle.getColour());
        System.out.println(rectangle.getColour());
        System.out.println(cool.getColour());
    }
}

