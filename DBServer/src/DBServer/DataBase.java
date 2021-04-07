package DBServer;

import java.util.*;

public class DataBase {

    private String DBname;
    private ArrayList<Table> TableList = new ArrayList<>();

    public DataBase(String name){

        DBname = name;
        System.out.println("Database:" + name + " created.");
    }

    public void addTable(Table table){

        if(TableList.add(table)){
            System.out.println("Table:" + table + " added into database:" + DBname);
        }
    }

    public Table getTable(int index){
        return TableList.get(index);
    }



}
