import OXOExceptions.*;

class OXOController
{
    OXOModel gameModel;

    public OXOController(OXOModel model)
    {
        gameModel = model;
        gameModel.setCurrentPlayer(model.getPlayerByNumber(0));
    }

    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        int rownum = 0, colnum = 0;
        rownum = command.charAt(0) - 'a';
        colnum = command.charAt(1) - '0';

    }

}
