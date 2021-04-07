package DBServer;

public class alterCMD extends DBcmd{

    String query() {

        if(getCommandType().equals("add")){
            alter(true);
        }
        if(getCommandType().equals("drop")){
            alter(false);
        }
        return getQuery();
    }

    // true for add: Add " " into each value row.
    // false for drop.
    private void alter(boolean addOrDrop){

        IO io = new IO();
        String tableName = getTableNames().get(0);
        Table table;
        setQuery("[ERROR] : failed to alter attribute.");

        if((table = io.inputFile(tableName)) != null){
            if (addOrDrop && add(table) || !addOrDrop && drop(table)) {
                if (io.outputFile(tableName, table) == table.getRowNum()) {
                    setQuery("[OK]");
                }
            }
        }
    }

    private boolean add(Table table){

        return table.insertColumns(getColNames()) == 1;
    }

    private boolean drop(Table table){

        return table.deleteColumn(getColNames().get(0));
    }

}
