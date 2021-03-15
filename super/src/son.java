public class son extends father{

    //int i; this 和super 只有在 子类覆盖父类的时候区分，否则都是类的。
    //int i,j,k;
    son(){
        //super(); 隐藏着一个无参super
       // i = 99; 如果不初始化，就是继承了默认的值的变量
        //super(2,4); 有参构造器也会继承没被修改过的的默认变量

       // super(5,56);
        this.i = 11;
        this.j = 12;
        k = 13;

        get();


        super.get();

    }

    son(int x){
        super(5,6);
        this.i = x;
        this.j = x;
        this.k = x;

        System.out.println("son x" + this.i + super.i);
    }

    public void getx(){
        System.out.print("songetthis   "+ this.i + this.j + this.k);
        System.out.println("songetsuper  " + super.i + super.j + super.k);
    }
}


