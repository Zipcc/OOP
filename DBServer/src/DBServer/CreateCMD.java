package DBServer;

public class CreateCMD extends DBcmd{

    String query() {

        if(getCommandType().equals("database")){
            createDatabase();
        }
        if(getCommandType().equals("table")){
            createTable();
        }
        return getQuery();
    }

    private void createDatabase(){

        IO io = new IO();
        if(io.createFolder(getDBname())){
            setQuery("[OK]");
        }else{
            setQuery("[ERROR]: failed to create database.");
        }
    }

    private void createTable(){

        IO io = new IO();
        String tableName = getTableNames().get(0);

        setQuery("[ERROR]: failed to create table.");
        if(io.createFile(tableName)){
            Table table = new Table(tableName);
            table.insertColumns(getColNames());
            if(io.outputFile(tableName, table) == 1){
                setQuery("[OK]");
            }
        }
    }

}
