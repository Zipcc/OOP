package DBServer;

import java.util.*;

import static DBServer.TokenType.*;

public class Parser {

    private final Tokenizer tkn;
    private Token tk;
    private DBcmd cmd;

    Parser(String incomingCommand){
        tkn = new Tokenizer(incomingCommand);
    }

    DBcmd parse() {

        try {
            nextToken();
            if (checkType(CT)) {
                switch (tk.getValue().toLowerCase(Locale.ROOT)) {
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
                throw new InvalidQueryException();
            }
        }catch(DBServerException e) {
            if(e instanceof SemiColonMissingException){
                return new ErrorCMD("no ;;;;;;;");
            }
            return new ErrorCMD("parse catched EXpt..");
        }
    }

    // Check current token type.
    private boolean checkType(TokenType tt){
        return tk.getType() == tt;
    }

    // Check current token value.
    private boolean checkValue(String s){
        return tk.getValue().equalsIgnoreCase(s);
    }

    // Check whether value of the next token is equal to s.
    // If not, throw exception.
    private void checkSyntax(String s) throws DBServerException{

        nextToken();
        if(!checkValue(s)){
            throw new InvalidQueryException();
        }
    }

    // Check whether value of the next token is equal to s.
    // If not, throw exception.
    private void checkSyntax(TokenType tt) throws DBServerException{

        nextToken();
        if(!checkType(tt)){
            throw new InvalidQueryException();
        }
    }

    // Check whether a command misses a semicolon at the end
    // or attached with other invalid tokens;
    private void checkEnd() throws DBServerException{

        nextToken();
        if(checkType(NUL)){
            throw new SemiColonMissingException();
        }else if (!checkType(EOC)) {
            throw new InvalidQueryException();
        }
    }

    // Get the next token in the tokenizer.
    private void nextToken(){
        tk = tkn.nextToken();
    }

    // Withdraw one step of nextToken().
    private void stepBack(){
        tkn.withDraw();
    }

    private void tableName() throws DBServerException {

        nextToken();
        if(checkType(ID)) {
            cmd.addTableName(tk.getValue());
        }else{
            throw new InvalidQueryException();
        }
    }

    private void create() throws DBServerException{

        nextToken();
        if(checkType(ST)){
            switch (tk.getValue().toLowerCase(Locale.ROOT)){
                case "database": createDatabase();break;
                case "table"   : createTable();   break;
            }
        }else{
            throw new InvalidQueryException();
        }
    }

    private void createDatabase() throws DBServerException {

        nextToken();
        if(checkType(ID)){
            cmd = new CreateCMD();
            cmd.setCommandType("database");
            cmd.setDBname(tk.getValue());
        }else{
            throw new InvalidQueryException();
        }

        checkEnd();
    }

    private void createTable() throws DBServerException {

        cmd = new CreateCMD();
        cmd.setCommandType("table");

        tableName();

        nextToken();
        if(checkType(LB)){
            if(!list("attribute")){
                throw new InvalidQueryException();
            }
            checkSyntax(RB);
        }else{
            stepBack();
        }

        checkEnd();
    }

    // Return true if is a valid list.
    private boolean list(String listType) throws DBServerException{

        switch (listType){
            case "attribute"    : attribute();break;
            case "value"        : value()    ;break;
            case "nameValue"    : attribute(); checkSyntax("="); value();break;
        }

        nextToken();
        if(checkType(CM)){
            return list(listType);
        }else{
            stepBack();
        }
        return true;

    }

    private void attribute() throws DBServerException {

        nextToken();
        if(checkType(ID)) {
            cmd.addColName(tk.getValue());
        }else{
            throw new InvalidQueryException();
        }
    }

    private void value() throws DBServerException{

        nextToken();
        if(checkType(BOL)||checkType(FLO)||checkType(INT)){
            cmd.addColName(tk.getValue());
        }else if(checkType(STR)){
            if(tk.getValue().length() == 2){
                // Empty value: ''
                cmd.addColName(" ");
            }else{
                // Decode string (Remove quotes).
                cmd.addColName(tk.getValue().replaceAll("'", ""));
            }
        }else{
            throw new InvalidQueryException();
        }
    }

    private void use() throws DBServerException {

        nextToken();
        if(checkType(ID)){
            cmd = new UseCMD();
            cmd.setDBname(tk.getValue());
        }else{
            throw new InvalidQueryException();
        }

        checkEnd();
    }

    private void insert() throws DBServerException{

        checkSyntax("into");
        cmd = new InsertCMD();

        tableName();

        checkSyntax("values");

        nextToken();
        if(checkType(LB) && !list("value")){
            throw new InvalidQueryException();
        }

        checkSyntax(RB);

        checkEnd();
    }

    private void select() throws DBServerException{

        cmd = new SelectCMD();

        nextToken();
        if(checkType(WAL)){
            cmd.setCommandType("all");
        }else {
            cmd.setCommandType("some");
            stepBack();
            if(!list("attribute")){
                throw new InvalidQueryException();
            }
        }

        checkSyntax("from");

        tableName();

        nextToken();
        if(checkValue("where")){
            conditionsRPN();
        }else {
            stepBack();
        }

        checkEnd();
    }

    // Parse conditions into DBcmd.conditions List in RPN order.
    private void conditionsRPN() throws DBServerException{

        Stack<String> RelationStack = new Stack<>();
        // State points: Two conditions must combined with a relation(or/and).
        // Once meet ( , if has met ) before, there must be a relation in between.
        // Once meet ), set meetRB true, once meet relation, set hasRelation true.
        // If relation == true && meetRB == true when meeting (, syntax right, reset meetRB and hasRelation to be false.
        boolean meetRB = false,hasRelation = false;
        nextToken();
        while(! (checkType(EOC) || checkType(NUL)) ){
            if(checkType(RB) && (meetRB = true)){
                // If RB has no pair LB, error
                if(!RelationStack.contains("(")){
                    throw new InvalidQueryException();
                }
                // Once meet ), pop all elements in stack till (
                while(!RelationStack.peek().equals("(")){
                    Condition cdt = new Condition();
                    cdt.setRelation(RelationStack.pop());
                    cmd.addCondition(cdt);
                }
                // Remove ( in the stack
                RelationStack.pop();
            }else if(checkType(LB)){
                if(meetRB && !hasRelation){
                    throw new InvalidQueryException();
                }else{
                    RelationStack.push(tk.getValue());
                    meetRB = false;
                    hasRelation = false;
                }
            }else if(checkValue("and") || checkValue("or")){
                RelationStack.push(tk.getValue());
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
                throw new InvalidQueryException();
            }
        }
        stepBack();
    }

    // Check and generate a new condition.
    private Condition singleCondition() throws DBServerException{

        Condition cdt = new Condition();

        if(checkType(ID)) {
            cdt.setAttributeName(tk.getValue());
        }else{
            throw new InvalidQueryException();
        }

        nextToken();
        if(checkType(OP)) {
            cdt.setOperator(tk.getValue());
        }else{
            throw new InvalidQueryException();
        }
        if(checkValue("=") || checkValue("!")){
            equalCondition(cdt);
        }else if(checkValue("<") || checkValue(">")){
            greaterCondition(cdt);
        }else if(checkValue("like")){
            likeCondition(cdt);
        }
        return cdt;
    }

    // == !=
    private void equalCondition(Condition cdt) throws DBServerException{

        checkSyntax("=");

        nextToken();
        if(checkType(BOL)||checkType(FLO)||checkType(INT)){
            cdt.setValue(tk.getValue());
        }else if(checkType(STR)){
            if(tk.getValue().length() == 2){
                // Empty value: ''
                cdt.setValue(" ");
            }else{
                // Decode string (Remove quotes).
                cdt.setValue(tk.getValue().replaceAll("'", ""));
            }
        }else{
            throw new InvalidQueryException();
        }
    }

    // < <= > >= must associate with numbers
    private void greaterCondition(Condition cdt) throws DBServerException{

        stepBack();
        if(tk.getType() != FLO || tk.getType() != INT){
            throw new InvalidQueryException();
        }
        nextToken();

        nextToken();
        if(checkValue("=")){
            cdt.addEqual();
        }else{
            stepBack();
        }

        nextToken();
        if(checkType(FLO) || checkType(INT)){
            cdt.setValue(tk.getValue());
        } else{
            throw new InvalidQueryException();
        }
    }

    // LIKE must associate with non-empty string
    private void likeCondition(Condition cdt) throws DBServerException{

        nextToken();
        // > 2 because there shouldn't be empty inside ''
        if(checkType(STR) && tk.getValue().length() > 2){
            // Decode string (Remove quotes).
            cdt.setValue(tk.getValue().replaceAll("'", ""));
        }else{
            throw new InvalidQueryException();
        }
    }

    private void update() throws DBServerException{

        cmd = new UpdateCMD();

        tableName();

        checkSyntax("set");

        if(!list("nameValue")){
            throw new InvalidQueryException();
        }

        checkSyntax("where");
        conditionsRPN();
        checkEnd();
    }

    private void drop() throws DBServerException{

        String structure;

        nextToken();
        if(checkType(ST)){
            cmd = new DropCMD();
            structure = tk.getValue();
        }else{
            throw new InvalidQueryException();
        }

        checkSyntax(ID);

        switch (structure.toLowerCase(Locale.ROOT)){
            case "database": cmd.setCommandType("database");cmd.setDBname(tk.getValue())   ;break;
            case "table"   : cmd.setCommandType("table")   ;cmd.addTableName(tk.getValue());break;
        }

        checkEnd();
    }

    private void alter() throws DBServerException{

        checkSyntax("table");
        cmd = new alterCMD();

        tableName();

        nextToken();
        if(checkValue("add")){
            cmd.setCommandType("add");
        }else if(checkValue("drop")){
            cmd.setCommandType("drop");
        }

        attribute();

        checkEnd();
    }

    private void delete() throws DBServerException{

        checkSyntax("from");
        cmd = new deleteCMD();

        tableName();

        checkSyntax("where");

        conditionsRPN();

        checkEnd();
    }

    private void join() throws DBServerException{

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

