package com.company;

public class test{

    public static void main(String[] args){

    TwoDimensionalShape[] hundred = new TwoDimensionalShape[10];

    for(int i=0; i < 10; i++){
        double random = Math.random();
        System.out.println(random);
        if( random < 0.3){
            hundred[i] = new Circle( (int)(100 *random));
        }
        else if( random < 0.6){
            hundred[i] = new Rectangle((int)(100 *random),(int)(100 *random));
        }
        else if( random >= 0.6){
            hundred[i] = new Triangle((int)(100 *random), (int)(100 *random), (int)(100 *random));
        }
    }

    int count =0;

        TwoDimensionalShape[] shapes = new TwoDimensionalShape[100];
        shapes[0] = new Triangle(3,3,3);
        TwoDimensionalShape firstShape = shapes[0];
        Triangle firstTriangle = (Triangle)firstShape;
        TriangleVariant variant = firstTriangle.getVariant();
        System.out.println(variant);

    for(TwoDimensionalShape hun : hundred){
        if(hun instanceof MultiVariantShape) {
            count++;
        }
    }
       // System.out.println(count +"triangles\n\n");
        //System.out.println(        Triangle.getPopulation()+"\n");
    }
}

