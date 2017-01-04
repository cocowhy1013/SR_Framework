import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by coco on 2016/9/6.
 */
public class TreeNodeExpression {
    protected String attributeName;
    protected String operator;
    protected String valueSplit;
    protected String label;

    TreeNodeExpression(){
        attributeName = "";
        operator = "";
        valueSplit = "";
        label = "";
    }
    TreeNodeExpression(String attributeName1, String operator1,String value1,String label1){
        attributeName = attributeName1;
        operator = operator1;
        valueSplit = value1;
        label = label1;
    }
    TreeNodeExpression(String line){
        attributeName = "";
        operator = "";
        valueSplit = "";
        label = "";
        String[] parts = line.split(" ");

        if(line.contains(":")){
            attributeName = parts[0];
            operator = parts[1];
            valueSplit = parts[2].replace(":","");
            label = parts[3];
        }
        else{
            attributeName = parts[0];
            operator = parts[1];
            valueSplit = parts[2].replace(":","");
            label = "";
        }
    }
    public boolean isAttributeSatisfied(String line){
        //System.out.println("line ---- "+line);
        String[] parts = line.split(",");
        if(attributeName.isEmpty())
            return true;
        String valuetest = parts[Integer.parseInt(attributeName)];
        if(operator.equals("<")){
            if(Double.parseDouble(valuetest)<Double.parseDouble(valueSplit))
                return true;
            else return false;
        }
        else if(operator.equals("<=")){
            if(Double.parseDouble(valuetest)<=Double.parseDouble(valueSplit))
                return true;
            else return false;
        }
        else if(operator.equals("=")){
            if(Double.parseDouble(valuetest)==Double.parseDouble(valueSplit)
                    ||Double.parseDouble(valuetest)-Double.parseDouble(valueSplit)<1e-5)
                return true;
            else return false;
        }
        else if(operator.equals(">")){
            if(Double.parseDouble(valuetest)>Double.parseDouble(valueSplit))
                return true;
            else return false;
        }
        else if(operator.equals(">=")){
            if(Double.parseDouble(valuetest)>=Double.parseDouble(valueSplit))
                return true;
            else return false;
        }
        else
            return false;
    }
    public String getAttributeName() {
        return attributeName;
    }
    public String getOperator(){return operator;}
    public String getValueSplit(){return valueSplit;}
    public String getLabel(){return label;}
    public boolean isLabelSatisfied(String line){
        String[] parts = line.split(",");
        String labeltest = parts[parts.length-1];
        if(labeltest.equals(label))
            return true;
        else
            return false;
    }
    public void display(){
        System.out.print(""+attributeName);
        System.out.print(""+operator);
        System.out.print(""+valueSplit);
        System.out.print(""+label);
    }

}
