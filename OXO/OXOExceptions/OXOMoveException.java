package OXOExceptions;

public class OXOMoveException extends Exception
{
    private int rowNumber;
    private int columnNumber;
    private RowOrColumn rowOrColumn;

    public OXOMoveException()
    {
    }

    public OXOMoveException(int row, int column)
    {
        rowNumber = row;
        columnNumber = column;
    }
    
    protected int getRow()
    {
        return rowNumber;
    }

    protected int getColumn()
    {
        return columnNumber;
    }


}
