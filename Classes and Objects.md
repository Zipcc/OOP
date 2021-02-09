```  
public class Example {

|--->int a;
|
| public static void method1(int a, int b(换个名字的a)){
|---->this.a = a;
   or 
   a = b;
  } 
  
}  
```  
成员函数内部调用成员函数不需要 .  

* 定义在函数内部的是本地变量  
* 本地变量（必须主动初始化）生存周期和作用域都是函数内部  
* 成员变量（初始化为0）的生存期是对象的生存期，作用域是类内部的成员函数。
> 构造函数--overload（重载）可以在第一行 this() 调用一次没有参数的构造函数  
