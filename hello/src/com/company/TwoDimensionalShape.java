package com.company;

abstract class TwoDimensionalShape {

    private Colour colour;

    {
        colour = Colour.RED;
    }

    public TwoDimensionalShape() {
    }

    void setColour(Colour colour){
        this.colour = colour;
    }
    Colour getColour(){
        return this.colour;
    }

    abstract double calculateArea();
    abstract int calculatePerimeterLength();
}