import OXOExceptions.*;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

class OXOController
{
    OXOModel gameModel;
    int rowNum, colNum;
    Queue<OXOPlayer> moves = new LinkedList<>();

    public OXOController(OXOModel model)
    {
        gameModel = model;
        gameModel.setCurrentPlayer(model.getPlayerByNumber(0));
        for(int i=0; i < gameModel.getNumberOfPlayers(); i++){
            moves.offer(gameModel.getPlayerByNumber(i));
        }
    }

    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        if(gameModel.getWinner() != null){
            return;
        }
        identifierLength(command);
        identifierCharacter(command);
        rowNum = Character.toLowerCase(command.charAt(0))  - 'a';
        colNum = command.charAt(1) - '0' - 1;
        outsideCellRange(rowNum, colNum);

        OXOPlayer currentPlayer = gameModel.getCurrentPlayer();
        cellAlreadyTaken(rowNum, colNum, command);
        gameModel.setCellOwner(rowNum, colNum, currentPlayer);

        if (scanCells(gameModel.getWinThreshold())) {
            gameModel.setWinner(currentPlayer);
        }
        else{
            moves.offer(gameModel.getCurrentPlayer());
            moves.poll();
            gameModel.setCurrentPlayer(moves.peek());
        }
    }

    boolean scanCells(int winThreshold)
    {
        boolean notFull = false;

        for(int i=0; i<gameModel.getNumberOfRows(); i++){
            for(int j=0; j<gameModel.getNumberOfColumns(); j++) {
                if (isWin(i, j, winThreshold)) {
                    return true;
                }
                if (gameModel.getCellOwner(i, j) == null) {
                    notFull = true;
                }
            }
        }
        if (!notFull) {
            gameModel.setGameDrawn();
        }
        return false;
    }

    boolean isWin(int rowNum, int colNum, int winThreshold)
    {
        //vertical
        int flag = 0;
        for(int i = rowNum - (winThreshold - 1); i <= rowNum + (winThreshold - 1); i++){
            if(flag >= winThreshold){
                gameModel.setWinner(gameModel.getCurrentPlayer());
                return true;
            }
            if(i >= 0 && i < gameModel.getNumberOfRows()){
                if(gameModel.getCellOwner(i, colNum) == gameModel.getCurrentPlayer()){
                    flag ++;
                }
                else {
                    flag = 0;
                }
            }
        }
        //horizontal
        flag = 0;
        for(int j = colNum - (winThreshold - 1); j <= colNum + (winThreshold - 1); j++){
            if(flag >= winThreshold){
                gameModel.setWinner(gameModel.getCurrentPlayer());
                return true;
            }
            if(j >= 0 && j < gameModel.getNumberOfColumns()){
                if(gameModel.getCellOwner(rowNum, j) == gameModel.getCurrentPlayer()){
                    flag ++;
                }
                else {
                    flag = 0;
                }
            }
        }
        //diagonal '\'
        flag = 0;
        for(int m = -(winThreshold - 1); m <= +(winThreshold - 1); m++){
            if(flag >= winThreshold){
                gameModel.setWinner(gameModel.getCurrentPlayer());
                return true;
            }
            if(rowNum + m >= 0 && rowNum + m < gameModel.getNumberOfRows()
                    && colNum + m >= 0 && colNum + m < gameModel.getNumberOfColumns()){
                if(gameModel.getCellOwner((rowNum + m), (colNum + m)) == gameModel.getCurrentPlayer()){
                    flag ++;
                }
                else {
                    flag = 0;
                }
            }
        }
        //diagonal '/'
        flag = 0;
        for(int n = -(winThreshold - 1); n <= +(winThreshold - 1); n++){
            if(flag >= winThreshold){
                gameModel.setWinner(gameModel.getCurrentPlayer());
                return true;
            }
            if(rowNum + n >= 0 && rowNum + n < gameModel.getNumberOfRows()
                    && colNum -n >= 0 && colNum -n < gameModel.getNumberOfColumns())
            if(gameModel.getCellOwner((rowNum + n), (colNum - n)) == gameModel.getCurrentPlayer()){
                flag ++;
            }
            else {
                flag = 0;
            }
        }
        return false;
    }

    void identifierLength(String command) throws InvalidIdentifierLengthException
    {
        if(command.length() != 2){
            throw new InvalidIdentifierLengthException(command, command.length());
        }
    }

    void identifierCharacter(String command) throws InvalidIdentifierCharacterException
    {
        char row,col;

        row = Character.toLowerCase(command.charAt(0));
        col = command.charAt(1);
        if (!Character.isLowerCase(row)) {
            throw new InvalidIdentifierCharacterException(row, RowOrColumn.ROW);
        } else if (!Character.isDigit(col)) {
            throw new InvalidIdentifierCharacterException(col, RowOrColumn.COLUMN);
        }
    }

    void outsideCellRange(int rowNum, int colNum) throws OutsideCellRangeException
    {
        if(rowNum < 0 || rowNum >= gameModel.getNumberOfRows()){
            throw new OutsideCellRangeException(rowNum, colNum, RowOrColumn.ROW);
        }else if(colNum < 0 || colNum >= gameModel.getNumberOfColumns()){
            throw new OutsideCellRangeException(rowNum, colNum, RowOrColumn.COLUMN);
        }
    }

    void cellAlreadyTaken (int rowNum, int colNum, String command) throws CellAlreadyTakenException
    {
        if(gameModel.getCellOwner(rowNum, colNum) != null){
            throw new CellAlreadyTakenException(rowNum, colNum, command);
        }
    }
}
