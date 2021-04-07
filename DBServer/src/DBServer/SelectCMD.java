package DBServer;

import java.util.*;

public class SelectCMD extends DBcmd{

    private Table table;

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

        if(selectRow()){
            setQuery("[OK]\n" + table.toString());
        }
    }

    private void selectCol(){

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
            for(String unexpectedNames:unexpectedCol){
                table.deleteColumn(unexpectedNames);
            }
            setQuery("[OK]\n" + table.toString());
        }
    }

    // Select rows by deleting rows with ids which are not contained in selectedIds.
    private boolean selectRow(){

        IO io = new IO();
        String tableName = getTableNames().get(0);
        Set<Integer> selectedIds;

        if((table = io.inputFile(tableName)) == null){
            setQuery("[ERROR]: Table does not exist");
            return false;
        }
        if(getConditions().size() >= 1){
            try{
                selectedIds = calculateRPN(table);
            }catch (NumberFormatException e){
                setQuery("[ERROR]: Attribute cannot be converted to number");
                return false;
            }
            if(selectedIds == null){
                setQuery("[ERROR]: Wrong condition");
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
