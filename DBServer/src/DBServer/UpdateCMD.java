package DBServer;

import java.util.*;

public class UpdateCMD extends DBcmd{

    String query() {

        update();
        return getQuery();
    }

    public void update(){

        IO io = new IO();
        String tableName = getTableNames().get(0);
        List<String> name  = new ArrayList<>();
        List<String> value = new ArrayList<>();
        Set<Integer> selectedIds;
        int index;
        Table table;

        if((table = io.inputFile(tableName)) == null){
            setQuery("[ERROR]: Table does not exist");
            return;
        }
        // Split name value pairs.
        for(int i = 0; i < getColNames().size(); i+=2){
            name.add(getColNames().get(i));
            value.add(getColNames().get(i+1));
        }
        // Handle conditions
        if(getConditions().isEmpty() || (selectedIds = calculateRPN(table)) == null ){
            setQuery("[ERROR]: Wrong condition.");
            return;
        }
        // Find column index and update values in value rows
        for(int i = 0; i < name.size(); i++){
            index = table.getRow(0).getValueList().indexOf(name.get(i));
            if(index == -1){
                setQuery("[ERROR]: Attribute does not exist");
                return;
            }
            for(Row r: table.getValueRows()) {
                if(selectedIds.contains(r.getId())){
                    r.setValue(index,value.get(i));
                }
            }
        }
        if (io.outputFile(tableName, table) == table.getRowNum()) {
            setQuery("[OK]");
        }
    }

}
