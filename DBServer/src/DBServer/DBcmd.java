package DBServer;

import java.util.*;

public abstract class DBcmd {

    private final List<String> colNames;
    private final List<String> tableNames;
    private final List<Condition> conditions;
    private String DBname;
    private String commandType;
    private String query;
    abstract String query();

    DBcmd(){
        tableNames = new ArrayList<>();
        colNames = new ArrayList<>();
        conditions = new ArrayList<>();
    }

    public String getDBname() {
        return DBname;
    }

    public void setDBname(String DBname) {
        this.DBname = DBname;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void addTableName(String tableName) {
        this.tableNames.add(tableName);
    }

    public List<String> getColNames() {
        return colNames;
    }

    public void addColName(String colName) {
        this.colNames.add(colName);
    }

    public List<Condition> getConditions(){
        return conditions;
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    // Calculate a RPN list of conditions, to a single set of row ids.
    public Set<Integer> calculateRPN(Table table) {

        Stack<Set<Integer>> stack = new Stack<>();

        while (!getConditions().isEmpty()) {
            Condition condition = getConditions().get(0);
            if (condition.isRelation() && stack.size() >= 2) {
                Set<Integer> set1 = stack.pop();
                Set<Integer> set2 = stack.pop();
                if (condition.getRelation().equals("and")) {
                    set1.retainAll(set2);
                    stack.push(set1);
                } else if (condition.getRelation().equals("or")) {
                    set1.addAll(set2);
                    stack.push(set1);
                }
            } else if (stack.push(condition.applyCondition(table)) == null) {
                return null;
            }
            getConditions().remove(0);
        }
        if (stack.size() == 1) {
            return stack.pop();
        } else {
            return null;
        }
    }
}
