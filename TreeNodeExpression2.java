import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huiyan Wang on 2016/9/27.
 */
public class TreeNodeExpression2 {
    protected String attributeName;
    protected String operator;
    protected String valueSplit;
    protected String label;

    TreeNodeExpression2(){
        attributeName = "";
        operator = "";
        valueSplit = "";
        label = "";
    }
    TreeNodeExpression2(String attributeName1, String operator1,String value1,String label1){
        attributeName = attributeName1;
        operator = operator1;
        valueSplit = value1;
        label = label1;
    }
    TreeNodeExpression2(String line){
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
        String valuetest = parts[Integer.parseInt(attributeName)];
        if(operator.equals("<")){
            if(Integer.parseInt(valuetest)<Integer.parseInt(valueSplit))
                return true;
            else return false;
        }
        else if(operator.equals("<=")){
            if(Integer.parseInt(valuetest)<=Integer.parseInt(valueSplit))
                return true;
            else return false;
        }
        else if(operator.equals("=")){
            if(Integer.parseInt(valuetest)==Integer.parseInt(valueSplit))
                return true;
            else return false;
        }
        else if(operator.equals(">")){
            if(Integer.parseInt(valuetest)>Integer.parseInt(valueSplit))
                return true;
            else return false;
        }
        else if(operator.equals(">=")){
            if(Integer.parseInt(valuetest)>=Integer.parseInt(valueSplit))
                return true;
            else return false;
        }
        else
            return false;
    }
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


    public static void main(String [] args) throws IOException {
        String trainFile = "E:\\MT1\\MutantTest3\\20trainAll.arff";
        String testFile = "E:\\MT1\\MutantTest3\\20testAll.arff";
        String treeFile = "E:\\MT1\\MutantTest3\\tree.txt";

        int testNumber = 1;//Integer.parseInt(args[3]);
        int modify_type = 1;//Integer.parseInt(args[4]);
        int modify_strength = 1;//Integer.parseInt(args[5]);

        //if(modify_type == -1 && modify_strength == -1) {
        RetriveTreePathForTest retriver = new RetriveTreePathForTest(trainFile, testFile, treeFile);
        retriver.generateSingleTestFile(testNumber);
        retriver.displayList(retriver.getPathList(testNumber));

        //retriver.getPathList(testNumber);

        System.out.println();
        
        System.out.println("Score"+retriver.caculateScore("0,9,9,0,0,0,9,0,5,1"));
        //}
    }
}
