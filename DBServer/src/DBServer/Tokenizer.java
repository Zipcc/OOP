package DBServer;

import java.util.*;

public class Tokenizer {

    final private ArrayList<String> Commands;
    private int index;

    Tokenizer(String incomingCommand) {

        Commands = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(preProcess(incomingCommand));
        // Store roughly split command strings into Commands for further tokenization.
        while (st.hasMoreTokens()) {
            Commands.add(st.nextToken("\t"));
        }
    }

// If no more token in Commands, return an extra null token.
    public Token nextToken() {

        if(index < Commands.size()){
            Token token = new Token(Commands.get(index));
            index++;
            return token;
        }else{
            return new Token("");
        }
    }

// Set index pointing to the previous token.
    public void withDraw(){

        if(index > 1){
            index--;
        }
    }

// Roughly split the command string with whitespace.
    private String preProcess(String s){

        // Whether the index is entering the quote field.
        boolean inQuote = false;
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // Reverse inQuote flag once meet a quote.
            if (c == '\'') {
                inQuote = !inQuote;
            }
            // If outside the quote field, replace whitespaces/split punctuations and characters with tab.
            if (!inQuote){
                if(c=='('||c==')'||c==','||c==';'||c=='='||c=='<'||c=='>'||c=='!'||c=='*') {
                    sb.append('\t');
                    sb.append(c);
                    sb.append('\t');
                }else if(c == ' '){
                    sb.append('\t');
                }else{
                    sb.append(c);
                }
            } else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

}

