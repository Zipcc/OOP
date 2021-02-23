package OXOExceptions;

public class InvalidIdentifierCharacterException extends InvalidIdentifierException
{
    private char character;
    private RowOrColumn type;

    public InvalidIdentifierCharacterException(char character, RowOrColumn type)
    {
        this.character = character;
        this.type = type;
    }

    public InvalidIdentifierCharacterException()
    {

    }

    @Override
    public String toString() {
        return "Invalid " + type + " identifier character--> "+ character + " <--" ;
    }
}
