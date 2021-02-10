package com.company;

public class test{

    public static void main(String[] args){

        TwoDimensionalShape rectangle = new Rectangle(2,3);
        TwoDimensionalShape triangle = new Triangle(1,2,3);
        TwoDimensionalShape circle = new Circle(2);

      //  triangle.setColour(Colour.BROWN);

        rectangle.setColour(Colour.YELLOW);
        circle.setColour(Colour.GREEN);
        System.out.println(circle.getColour());

        System.out.println(rectangle.getColour());




    }
}

