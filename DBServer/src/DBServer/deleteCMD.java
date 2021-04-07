package DBServer;

import java.util.Set;

public class deleteCMD extends DBcmd{

    private Table table;
    private Set<Integer> selectedIds;

    String query() {

        delete();
        return getQuery();
    }

    private void delete(){

        Table table;
        IO io = new IO();
        String tableName = getTableNames().get(0);

        if((table = io.inputFile(tableName)) == null){
            setQuery("[ERROR]: Table does not exist");
            return;
        }
        if(getConditions().isEmpty() || (selectedIds = calculateRPN(table)) == null ){
            setQuery("[ERROR]: Wrong condition.");
            return;
        }
        for(Row r: table.getValueRows()) {
            if(selectedIds.contains(r.getId())){
                table.deleteRow(r);
            }
        }
        if (io.outputFile(tableName, table) == table.getRowNum()) {
            setQuery("[OK]");
        }
    }
}
