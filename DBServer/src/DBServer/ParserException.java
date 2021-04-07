package DBServer;

public class ParserException extends RuntimeException{

    private String errorMessage;

    ParserException(){}

    ParserException(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String toString(){
        return errorMessage;
    }
}
