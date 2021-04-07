package DBServer;

import java.util.*;

class Row {

    private final Integer id;
    private final ArrayList<String> valueList;

    // A row with a single "id" attribute
    public Row(Integer id){

        valueList = new ArrayList<>();
        this.id = id;
        if(id == 0){
            valueList.add("id");
        }
    }

    public Row(Integer id, List<String> values){

        valueList = new ArrayList<>();
        this.id = id;
        valueList.add(id.toString());
        if(values != null){
            valueList.addAll(values);
        }
    }

    public String toString(){

        StringBuilder sb = new StringBuilder();

        for (String s:valueList) {
            sb.append(s).append("\t");
        }
        // Replace the last '\t' with '\n' .
        sb.deleteCharAt(sb.length()-1);
        sb.append("\n");
        return sb.toString();
    }

    public int getId() {

        return id;
    }

    public List<String> getValueList(){

        return valueList;
    }

    public void setValue(int index, String element){

        if(index == valueList.size()){
            valueList.add(element);
        }
        else{
            valueList.set(index, element);
        }
    }


}
