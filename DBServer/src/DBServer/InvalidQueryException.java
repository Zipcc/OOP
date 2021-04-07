package DBServer;

public class InvalidQueryException extends ParserException {

    private final String errorMessage;

    InvalidQueryException(){

        this.errorMessage = "Invalid query";
    }

    InvalidQueryException(String errorMessage){

        this.errorMessage = errorMessage;
    }

    public String toString(){
        return errorMessage;
    }

}
