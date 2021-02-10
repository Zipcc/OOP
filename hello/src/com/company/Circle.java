package com.company;

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
        return "Circle withvvvvvvvvv radius " + radius;
    }

    void setColour(Colour colour){
        this.colour = colour;
    }

    Colour getColour(){
        return this.colour;
    }

}
