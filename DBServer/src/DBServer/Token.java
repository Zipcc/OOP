package DBServer;

import java.util.Locale;

public class Token {

    private TokenType type;
    private final String value;
    static final String NULregex = "";
    static final String EOCregex = ";";
    static final String CTregex  = "use|create|drop|alter|insert|select|update|delete|join";
    static final String STregex  = "database|table";
    static final String WALregex = "\\*";
    static final String LBregex  = "\\(";
    static final String RBregex  = "\\)";
    static final String CMregex  = ",";
    static final String KWregex  = "into|values|from|where|set|and|on|add|or";
    static final String STRregex = "^'[^'\t]*'$";
    static final String BOLregex = "true|false";
    static final String FLOregex = "^(-?\\d+)(\\.\\d+)$";
    static final String INTregex = "^(-?\\d+)$";
    static final String OPregex  = "=|>|<|!|like";
    static final String IDregex  = "^[A-Za-z0-9]*[A-Za-z]+[A-Za-z0-9]*$";

    Token(String token) {

        value = token;
        // If token's value matches one of token types, set that and break loop.
        for(TokenType tt: TokenType.values()){
            if(setType(tt))break;
        }
    }

    // Check and set token type if token value matches regex
    // If value matches none case below, set type to be UNK and return false.
    private Boolean setType(TokenType type){

        String regex;

        switch(type){
            case NUL:  regex = NULregex;break;
            case EOC:  regex = EOCregex;break;
            case CT :  regex = CTregex; break;
            case ST :  regex = STregex; break;
            case WAL:  regex = WALregex;break;
            case LB :  regex = LBregex; break;
            case RB :  regex = RBregex; break;
            case CM :  regex = CMregex; break;
            case KW :  regex = KWregex; break;
            case STR:  regex = STRregex;break;
            case BOL:  regex = BOLregex;break;
            case FLO:  regex = FLOregex;break;
            case INT:  regex = INTregex;break;
            case OP :  regex = OPregex; break;
            case ID :  regex = IDregex; break;
            default :  regex = null;    break;
        }
        // whether matches one Token Type.
        if(regex != null && value.toLowerCase(Locale.ROOT).matches(regex)){
            this.type = type;
            return true;
        // If matches none of previous ones, set type to be UNKNOWN.
        }else{
            this.type = TokenType.UNK;
            return false;
        }
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

}


