package DBServer;

import java.util.*;

public class SelectCMD extends DBcmd{

    private Table table;
    private Set<Integer> selectedIds;

    String query() {

        if(getCommandType().equals("all")){
            selectAll();
        }
        if(getCommandType().equals("some")){
            selectCol();
        }
        return getQuery();
    }

    private void selectAll(){

        StringBuilder sb = new StringBuilder();

        if(selectRow()){
            sb.append("[OK]\n");
            sb.append(table.getRow(0));
            for(Row r: table.getValueRows()){
                sb.append(r);
            }
            setQuery(sb.toString());
        }
    }

    private void selectCol(){

        StringBuilder sb = new StringBuilder();

        if(selectRow()){
            // Get all column names
            Set<String> unexpectedCol = new TreeSet<>(table.getRow(0).getValueList());
            // Remove all selected column names, in order to delete columns.
            for(String colName:getColNames()){
                if(!unexpectedCol.remove(colName)){
                    setQuery("[ERROR]: Attribute does not exist");
                    return;
                }
            }
            sb.append("[OK]\n");
            for(String unexpectedNames:unexpectedCol){
                table.deleteColumn(unexpectedNames);
            }
            sb.append(table.getRow(0));
            for(Row r: table.getValueRows()){
                sb.append(r);
            }
            setQuery(sb.toString());
        }
    }

    // Select rows by deleting rows with ids which are not contained in selectedIds.
    private boolean selectRow(){

        IO io = new IO();
        String tableName = getTableNames().get(0);

        if((table = io.inputFile(tableName)) == null){
            setQuery("[ERROR]: Table does not exist");
            return false;
        }
        if(getConditions().size() >= 1){
            selectedIds = calculateRPN(table);
            if(selectedIds == null){
                setQuery("[ERROR]: Wrong condition.");
                return false;
            }
            for(Row r: table.getValueRows()) {
                if(!selectedIds.contains(r.getId())){
                    table.deleteRow(r);
                }
            }
        // Plus row No.0
        return selectedIds.size() == table.getRowNum() - 1;
        }
        return true;
    }

}
