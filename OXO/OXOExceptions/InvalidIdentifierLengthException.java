package OXOExceptions;

public class InvalidIdentifierLengthException extends InvalidIdentifierException
{
    int length;
    private String command;

    public InvalidIdentifierLengthException()
    {
    }

    public InvalidIdentifierLengthException(String inputcommand, int inputlength)
    {
        command = inputcommand;
        length = inputlength;
    }

    @Override
    public String toString() {
        return "command--> " + command + " <--with an invalid identifier length: " + length + " \n Command should consists of 2 characters.";
    }

}
