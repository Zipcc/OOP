package DBServer;

public class TEST {
    public static void main(String[] args) {
/*
        Tokenizer tk = new Tokenizer("SE#LECT* FROM actors WHERE (awards > true) AND ((nationality == 'Br()( ) it ish') OR (nationality == 'Australian'));");
*/
        Tokenizer tk2 = new Tokenizer("select name from actores where age > 10 ;");
        while (tk2.nextToken() != null) {
        }
    }

}
