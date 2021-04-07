package DBServer;

import java.util.*;

public class Table {

    private final String tableName;
    private int rowNum,colNum;
    private int maxId;
    private final Row columnNames;
    private final ArrayList<Row> valueRows;

    public String getTableName() {
        return tableName;
    }

    // Create a table containing a row named columnNames whose id is 0,
    // This row holds a list of attributes.
    public Table(String tableName){

        this.tableName = tableName;
        columnNames = new Row(0);
        valueRows = new ArrayList<>();
        rowNum = 1;
        colNum = 1;
        maxId = 0;
        System.out.println("Table created --> " + tableName);
    }

    //  Return number of columns successfully inserted.
    public int insertColumns(List<String> colNames){

        int i = 0;

        if(colNames.isEmpty()){
            return i;
        }
        for(String name : colNames) {
            if (!columnNames.getValueList().contains(name)) {
                columnNames.getValueList().add(name);
                i++;
                System.out.println("Column inserted --> " + name);
            }
        }
        if(valueRows.size() > 0) {
            for (Row row : valueRows) {
                row.setValue(colNum, " ");
            }
        }
        colNum += i;
        return i;
    }

    public boolean deleteColumn(String colName){

        int index = columnNames.getValueList().indexOf(colName);
        if(index < 0){
            return false;
        }
        columnNames.getValueList().remove(colName);
        if(valueRows.size() > 0) {
            for (Row row : valueRows) {
                row.getValueList().remove(index);
            }
        }
        colNum--;
        return true;
    }

    public void inputRow(List<String> values) {

        Row row;
        int id = Integer.parseInt(values.get(0));

        maxId = Math.max(id, maxId);
        if(values.size()==1){
            // Row with no value.
            row = new Row(id,null);
        }else{
            row = new Row(id, values.subList(1, values.size()));
        }
        valueRows.add(row);
        rowNum++;
    }

    public void insertRow(List<String> values){

        Row row = new Row(++maxId,values);
        valueRows.add(row);
        rowNum++;
        System.out.println("Row inserted into Table --> " + row);
    }

    public void deleteRow(Row row){

        valueRows.remove(row);
        rowNum--;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColNum(){
        return colNum;
    }

    public List<Row> getValueRows(){

        return new ArrayList<>(valueRows);
    }

    public Row getRow(int index){

        if(index < 0 || index > rowNum - 1){
            return null;
        }
        if(index == 0){
            return columnNames;
        }
        return valueRows.get(index - 1);
    }

    public String toString(){

        StringBuilder sb = new StringBuilder();

        sb.append(columnNames);
        for(Row row: valueRows){
            sb.append(row);
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
