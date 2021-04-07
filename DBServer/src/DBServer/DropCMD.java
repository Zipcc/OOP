package DBServer;

public class DropCMD extends DBcmd{

    String query() {

        if(getCommandType().equals("database")){
            dropDatabase();
        }
        if(getCommandType().equals("table")){
            dropTable();
        }
        return getQuery();
    }

    private void dropDatabase(){

        IO io = new IO();
        if(io.deleteFolder(getDBname())){
            setQuery("[OK]");
        }else{
            setQuery("[ERROR] : failed to delete database. Database doesn't exist?");
        }
    }

    private void dropTable(){

        IO io = new IO();
        if(io.deleteFile(getTableNames().get(0))){
            setQuery("[OK]");
        }else{
            setQuery("[ERROR] : failed to delete table. Table doesn't exist?");
        }
    }
}
