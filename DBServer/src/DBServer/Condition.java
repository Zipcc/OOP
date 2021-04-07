package DBServer;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class Condition {

    // False for condition, true for relation or/and
    private boolean isRelation;
    private String relation;
    private String attributeName;
    private String operator;
    private String value;

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setOperator(String operator) {
        this.operator = operator.toLowerCase(Locale.ROOT);
    }

    public void addEqual(){
        operator = operator + "=";
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRelation(){
        return isRelation;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation.toLowerCase(Locale.ROOT);
        this.isRelation = true;
    }

    // Apply a condition to current table, among value rows.
    // apply the condition and return row ids fitting to given condition.
    public Set<Integer> applyCondition(Table table){

        int colNameIndex = table.getRow(0).getValueList().indexOf(attributeName);
        Set<Integer> ids = null;

        switch(operator){
            case "="  : ids = equal(table, colNameIndex, true);
                break;
            case "!"  : ids = equal(table, colNameIndex, false);
                break;
            case ">"  : ids = compare(table, colNameIndex, true);
                break;
            case "<"  : ids = compare(table, colNameIndex, false);
                break;
            case ">=" : ids = compare(table, colNameIndex, true);
                ids.addAll(equal(table, colNameIndex, true));
                break;
            case "<=" : ids = compare(table, colNameIndex, false);
                ids.addAll(equal(table, colNameIndex, true));
                break;
            case"like": ids = like(table, colNameIndex);
                break;
        }
        return ids;
    }

    // If equal == true , handle "=" condition.
    // If equal == false, handle "!" condition.
    private Set<Integer> equal(Table table, int colNameIndex, boolean equal){

        Set<Integer> ids = new TreeSet<>();

        for(Row r: table.getValueRows()){
            if(r.getValueList().get(colNameIndex).equals(value) == equal){
                ids.add(r.getId());
            }
        }
        return ids;
    }

    // If greater == true, handle  ">" condition.
    // If greater == false, handle "<" condition.
    private Set<Integer> compare(Table table, int colNameIndex, boolean greater){

        Set<Integer> ids = new TreeSet<>();
        float number = Float.parseFloat(value);

        for(Row r: table.getValueRows()){
            if(greater && Float.parseFloat(r.getValueList().get(colNameIndex)) > number){
                ids.add(r.getId());
            }
            if(!greater && Float.parseFloat(r.getValueList().get(colNameIndex)) < number){
                ids.add(r.getId());
            }
        }
        return ids;
    }

    private Set<Integer> like(Table table, int colNameIndex){

        Set<Integer> ids = new TreeSet<>();

        for(Row r: table.getValueRows()){
            if(r.getValueList().get(colNameIndex).contains(value)){
                ids.add(r.getId());
            }
        }
        return ids;
    }


}
