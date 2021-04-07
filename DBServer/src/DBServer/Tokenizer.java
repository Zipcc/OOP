package DBServer;

import java.util.*;

public class Tokenizer {

    final private ArrayList<String> Commands;
    private int index;

    Tokenizer(String incomingCommand) {

        Commands = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(preprocessCommand(incomingCommand));
        // Store command tokens into Commands for further tokenization.
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
            index++;
            return new Token("");
        }
    }

    // Set index pointing to the previous token.
    public void withDraw(){

        if(index > 1){
            index--;
        }
    }

    // Roughly preprocess command string with tab.
    // Retain whitespaces inside quotes.
    private String preprocessCommand(String command){

        // Whether the index is inside the quote field.
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < command.length(); i++) {
            char c = command.charAt(i);
            // Reverse inQuote flag once meet a quote.
            if (c == '\'') {
                inQuotes = !inQuotes;
            }
            // If outside the quote field, replace whitespaces with tab, split punctuations and characters with tab.
            if (!inQuotes){
                // Split punctuations and characters
                if(c=='('||c==')'||c==','||c==';'||c=='='||c=='<'||c=='>'||c=='!'||c=='*') {
                    sb.append('\t');
                    sb.append(c);
                    sb.append('\t');
                // Replace whitespaces
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

