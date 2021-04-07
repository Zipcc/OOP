package DBServer;

public class ErrorCMD extends DBcmd{

    String info;

    ErrorCMD(String info){
        this.info = info;
    }

    String query() {
        return "[ERROR]: " + info;
    }
}
