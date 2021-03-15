public class father {
     int i,j,k;


    public father() {
        i=1;j=2;k=3;
        System.out.println("father()   " + i + j + k);
        get();
    }

    public father(int i, int s){

        this.i = i;
        this.k = 7;


        System.out.println("fatheris   " + i + j + k);
        get();
    }

    public void get(){
        System.out.println("fathergetthis   "+ this.i + this.j + this.k);
    }


}
