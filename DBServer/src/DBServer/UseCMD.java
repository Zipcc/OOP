package DBServer;

public class UseCMD extends DBcmd{

    String query() {

        useDatabase();
        return getQuery();
    }

    private void useDatabase(){

        IO io = new IO();

        if(io.openFolder(getDBname())){
            setQuery("[OK]");
        }else{
            setQuery("[ERROR] : Unknown database");
        }
    }

}
