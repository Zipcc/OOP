import org.w3c.dom.ls.LSOutput;

public class Main {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
//        String string="";
//        for (int i = 0; i < 99999; i++) {
//            string+=i;
//        }
//        System.out.println(string);
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < 99999; i++) {
            sb.append(i);
        }
        long end=System.currentTimeMillis();
        System.out.println(end-start);
    }


}
