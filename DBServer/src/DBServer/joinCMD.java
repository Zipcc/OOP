package DBServer;

import java.util.*;

public class joinCMD extends DBcmd{


    String query() {

        join();
        return getQuery();
    }

    private void join(){

        IO io = new IO();
        Table leftTable, rightTable;
        Table jointTable = new Table("jointTable");
        List<String> jointColNames = new ArrayList<>();
        List<String> jointValues = new ArrayList<>();
        // Input source tables.
        if((leftTable = io.inputFile(getTableNames().get(0))) == null || (rightTable = io.inputFile(getTableNames().get(1))) == null){
            setQuery("[ERROR]: Table does not exist");
            return;
        }
        // Set index of specified attribute.
        int leftIndex = leftTable.getRow(0).getValueList().indexOf(getColNames().get(0));
        int rightIndex = rightTable.getRow(0).getValueList().indexOf(getColNames().get(1));
        if(leftIndex < 0 || rightIndex < 0){
            setQuery("[ERROR]: Attribute does not exist");
            return;
        }
        // Create attribute row in joint table.
        for(String name:leftTable.getRow(0).getValueList().subList(1, leftTable.getColNum())){
            jointColNames.add(leftTable.getTableName() + "." + name);
        }
        for(String name:rightTable.getRow(0).getValueList().subList(1, rightTable.getColNum())){
            jointColNames.add(rightTable.getTableName() + "." + name);
        }
        jointTable.insertColumns(jointColNames);
        // Join rows.
        for(Row leftRow: leftTable.getValueRows()){
            for(Row rightRow: rightTable.getValueRows()){
                if(rightRow.getValueList().get(rightIndex).equals(leftRow.getValueList().get(leftIndex))){
                    jointValues.addAll(leftRow.getValueList().subList(1, leftTable.getColNum()));
                    jointValues.addAll(rightRow.getValueList().subList(1, rightTable.getColNum()));
                    jointTable.insertRow(jointValues);
                    jointValues.clear();
                }
            }
        }
        setQuery("[OK]\n" + jointTable.toString());
    }
}
