package DBServer;

import java.util.*;

import static DBServer.TokenType.*;

public class Parser {

    private final Tokenizer tokenizer;
    private Token token;
    private DBcmd cmd;

    Parser(String incomingCommand){
        tokenizer = new Tokenizer(incomingCommand);
    }

    DBcmd parse() {

        try {
            nextToken();
            if (checkToken(CT)) {
                switch (token.getValue().toLowerCase(Locale.ROOT)) {
                    case "create": create();break;
                    case "use"   : use();   break;
                    case "insert": insert();break;
                    case "select": select();break;
                    case "update": update();break;
                    case "drop"  : drop();  break;
                    case "alter" : alter(); break;
                    case "delete": delete();break;
                    case "join"  : join()  ;break;
                }
                return cmd;
            }else {
                throw new InvalidQueryException("Need command");
            }
        }catch(ParserException e) {
            return new ErrorCMD(e.toString());
        }
    }

    // Check current token type.
    private boolean checkToken(TokenType tokenType){

        return token.getType() == tokenType;
    }

    // Check current token value.
    private boolean checkToken(String value){

        return token.getValue().equalsIgnoreCase(value);
    }

    // Check whether value of the next token is equal to value.
    // If not, throw exception.
    private void checkSyntax(String value) throws ParserException {

        nextToken();
        if(!checkToken(value)){
            throw new InvalidQueryException("Need " + value);
        }
    }

    // Check whether type of the next token is equal to tt.
    // If not, throw exception.
    private void checkSyntax(TokenType tokenType) throws ParserException {

        nextToken();
        if(!checkToken(tokenType)){
            throw new InvalidQueryException("Need" + tokenType);
        }
    }

    // Check whether a command misses a semicolon at the end
    // or attached with other invalid tokens;
    private void checkEnd() throws ParserException {

        nextToken();
        if(checkToken(NUL)){
            throw new InvalidQueryException("Semi colon missing at end of line");
        }else if (!checkToken(EOC)) {
            throw new InvalidQueryException();
        }
    }

    // Get the next token in the tokenizer.
    private void nextToken(){

        token = tokenizer.nextToken();
    }

    // Take one step back of nextToken().
    private void stepBack(){

        tokenizer.withDraw();
    }

    // If next token type is ID, add it into cmd.TableNames
    private void tableName() throws ParserException {

        nextToken();
        if(checkToken(ID)) {
            cmd.addTableName(token.getValue());
        }else{
            throw new InvalidQueryException("Need table name");
        }
    }

    // If next token type is ID, add it into cmd.ColNames
    private void attribute() throws ParserException {

        nextToken();
        if(checkToken(ID)) {
            cmd.addColName(token.getValue());
        }else{
            throw new InvalidQueryException("Need attribute");
        }
    }

    // Return true if is a valid list.
    private boolean list(String listType) throws ParserException {

        switch (listType){
            case "attribute"    : attribute();
                                  break;
            case "value"        : value();
                                  break;
            // Add name into cmd.ColNames, then add the pair value into cmd.ColNames.
            case "nameValue"    : attribute();
                                  checkSyntax("=");
                                  value();
                                  break;
        }

        nextToken();
        if(checkToken(CM)){
            return list(listType);
        }else{
            stepBack();
        }
        return true;
    }

    // If next token type is BOL/FLO/INT/STR, add it into cmd.ColNames
    private void value() throws ParserException {

        nextToken();
        if(checkToken(BOL)||checkToken(FLO)||checkToken(INT)){
            cmd.addColName(token.getValue());
        }else if(checkToken(STR)){
            if(token.getValue().length() == 2){
                // Empty value: ''
                cmd.addColName(" ");
            }else{
                // Decode string (Remove quotes).
                cmd.addColName(token.getValue().replaceAll("'", ""));
            }
        }else{
            throw new InvalidQueryException("Need value");
        }
    }

    private void create() throws ParserException {

        nextToken();
        if(checkToken(ST)){
            switch (token.getValue().toLowerCase(Locale.ROOT)){
                case "database": createDatabase();break;
                case "table"   : createTable();   break;
            }
        }else{
            throw new InvalidQueryException("Need structure");
        }
    }

    private void createDatabase() throws ParserException {

        cmd = new CreateCMD();
        cmd.setCommandType("database");

        checkSyntax(ID);
        cmd.setDBname(token.getValue());

        checkEnd();
    }

    private void createTable() throws ParserException {

        cmd = new CreateCMD();
        cmd.setCommandType("table");

        tableName();

        nextToken();
        if(checkToken(LB)){
            list("attribute");
            checkSyntax(RB);
        }else{
            stepBack();
        }

        checkEnd();
    }

    private void use() throws ParserException {

        cmd = new UseCMD();

        checkSyntax(ID);
        cmd.setDBname(token.getValue());

        checkEnd();
    }

    private void insert() throws ParserException {

        cmd = new InsertCMD();

        checkSyntax("into");

        tableName();

        checkSyntax("values");

        checkSyntax(LB);

        list("value");

        checkSyntax(RB);

        checkEnd();
    }

    private void select() throws ParserException {

        cmd = new SelectCMD();

        nextToken();
        if(checkToken(WAL)){
            cmd.setCommandType("all");
        }else{
            cmd.setCommandType("some");
            stepBack();
            list("attribute");
        }

        checkSyntax("from");

        tableName();

        nextToken();
        if(checkToken("where")){
            conditionsRPN();
        }else {
            stepBack();
        }

        checkEnd();
    }

    // Parse conditions into cmd.conditions List in RPN order.
    private void conditionsRPN() throws ParserException {

        Stack<String> RelationStack = new Stack<>();
        // State points: Two conditions must combined with a relation(or/and).
        // Once meet ( , if has met ) before, there must be a relation in between.
        // Once meet ), set meetRB true, once meet relation, set hasRelation true.
        // If relation == true && meetRB == true when meeting (, syntax right, reset meetRB and hasRelation to be false.
        boolean meetRB = false,hasRelation = false;
        nextToken();
        while(!( checkToken(EOC) || checkToken(NUL) )){
            if(checkToken(RB) && (meetRB = true)){
                // If RB has no pair LB, error
                if(!RelationStack.contains("(")){
                    throw new InvalidQueryException("Condition syntax wrong");
                }
                // Once meet ), pop all elements in stack till (
                while(!RelationStack.peek().equals("(")){
                    Condition cdt = new Condition();
                    cdt.setRelation(RelationStack.pop());
                    cmd.addCondition(cdt);
                }
                // Remove ( in the stack
                RelationStack.pop();
            }else if(checkToken(LB)){
                if(meetRB && !hasRelation){
                    throw new InvalidQueryException("Condition syntax wrong");
                }else{
                    RelationStack.push(token.getValue());
                    meetRB = false;
                    hasRelation = false;
                }
            }else if(checkToken("and") || checkToken("or")){
                RelationStack.push(token.getValue());
                hasRelation = true;
            }else{
                cmd.addCondition(singleCondition());
            }
            nextToken();
        }
        // Compound conditions
        if(cmd.getConditions().size() >= 2){
            // Pop the last relationship in the stack
            if(RelationStack.size() == 1){
                Condition cdt = new Condition();
                cdt.setRelation(RelationStack.pop());
                cmd.addCondition(cdt);
            }else{
                throw new InvalidQueryException("Condition syntax wrong");
            }
        }
        stepBack();
    }

    // Check and generate a new condition.
    private Condition singleCondition() throws ParserException {

        Condition cdt = new Condition();

        if(checkToken(ID)) {
            cdt.setAttributeName(token.getValue());
        }else{
            throw new InvalidQueryException("Need attribute");
        }

        nextToken();
        if(checkToken(OP)) {
            cdt.setOperator(token.getValue());
        }else{
            throw new InvalidQueryException("Need operator");
        }
        if(checkToken("=") || checkToken("!")){
            equalCondition(cdt);
        }else if(checkToken("<") || checkToken(">")){
            greaterCondition(cdt);
        }else if(checkToken("like")){
            likeCondition(cdt);
        }
        return cdt;
    }

    // == !=
    private void equalCondition(Condition cdt) throws ParserException {

        checkSyntax("=");

        nextToken();
        if(checkToken(BOL)||checkToken(FLO)||checkToken(INT)){
            cdt.setValue(token.getValue());
        }else if(checkToken(STR)){
            if(token.getValue().length() == 2){
                // Empty value: ''
                cdt.setValue(" ");
            }else{
                // Decode string (Remove quotes).
                cdt.setValue(token.getValue().replaceAll("'", ""));
            }
        }else{
            throw new InvalidQueryException("Need value");
        }
    }

    // < <= > >= must associate with numbers
    private void greaterCondition(Condition cdt) throws ParserException {

        nextToken();
        if(checkToken("=")){
            cdt.addEqual();
        }else{
            stepBack();
        }

        nextToken();
        if(checkToken(FLO) || checkToken(INT)){
            cdt.setValue(token.getValue());
        } else{
            throw new InvalidQueryException("Need number");
        }
    }

    // LIKE must associate with non-empty string
    private void likeCondition(Condition cdt) throws ParserException {

        nextToken();
        // length should > 2 because there shouldn't be empty inside ''
        if(checkToken(STR) && token.getValue().length() > 2){
            // Decode string (Remove quotes).
            cdt.setValue(token.getValue().replaceAll("'", ""));
        }else{
            throw new InvalidQueryException("String expected");
        }
    }

    private void update() throws ParserException {

        cmd = new UpdateCMD();

        tableName();

        checkSyntax("set");

        list("nameValue");

        checkSyntax("where");
        conditionsRPN();
        checkEnd();
    }

    private void drop() throws ParserException {

        String structure;
        cmd = new DropCMD();

        nextToken();
        if(checkToken(ST)){
            structure = token.getValue();
        }else{
            throw new InvalidQueryException("Need structure");
        }

        checkSyntax(ID);

        switch (structure.toLowerCase(Locale.ROOT)){
            case "database": cmd.setCommandType("database");cmd.setDBname(token.getValue())   ;break;
            case "table"   : cmd.setCommandType("table")   ;cmd.addTableName(token.getValue());break;
        }

        checkEnd();
    }

    private void alter() throws ParserException {

        checkSyntax("table");
        cmd = new alterCMD();

        tableName();

        nextToken();
        if(checkToken("add")){
            cmd.setCommandType("add");
        }else if(checkToken("drop")){
            cmd.setCommandType("drop");
        }

        attribute();

        checkEnd();
    }

    private void delete() throws ParserException {

        checkSyntax("from");
        cmd = new deleteCMD();

        tableName();

        checkSyntax("where");

        conditionsRPN();

        checkEnd();
    }

    private void join() throws ParserException {

        cmd = new joinCMD();

        tableName();

        checkSyntax("and");

        tableName();

        checkSyntax("on");

        attribute();

        checkSyntax("and");

        attribute();

        checkEnd();
    }

}

