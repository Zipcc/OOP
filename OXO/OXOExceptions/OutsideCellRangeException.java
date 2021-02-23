package OXOExceptions;

public class OutsideCellRangeException extends CellDoesNotExistException
{
    private int position;
    RowOrColumn type;

    public OutsideCellRangeException()
    {

    }

    public OutsideCellRangeException(int row, int column, RowOrColumn or)
    {
        super(row, column);
        type = or;
        if(type == RowOrColumn.ROW){
            position = row;
        }else if(type == RowOrColumn.COLUMN){
            position = column;
        }
    }

    public String toString(){
        return "Position --> " + position + " <--out of range. Check " + type + " ?" ;
    }

}
