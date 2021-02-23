package OXOExceptions;

import java.util.StringJoiner;

public class CellAlreadyTakenException extends OXOMoveException
{

    String command;
    public CellAlreadyTakenException()
    {
    }

    public CellAlreadyTakenException(int row, int column, String inputcommand)
    {
        super(row, column);
        command = inputcommand;
    }

    @Override
    public String toString() {
        return "Cell-->  " + command + "   <--AlreadyTaken.....";
    }
}


