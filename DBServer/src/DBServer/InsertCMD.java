package DBServer;

public class InsertCMD extends DBcmd{

    String query() {

        insertValues();
        return getQuery();
    }

    private void insertValues(){

        IO io = new IO();
        String tableName = getTableNames().get(0);
        Table table;

        setQuery("[ERROR]: Failed to insert value.");
        if((table = io.inputFile(tableName)) != null && table.getColNum() == getColNames().size() + 1 ){
            table.insertRow(getColNames());
            if(io.outputFile(tableName, table) == table.getRowNum()){
                setQuery("[OK]");
            }
        }
    }
}
