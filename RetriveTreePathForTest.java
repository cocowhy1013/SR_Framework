import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by coco on 2016/9/6.
 */
public class RetriveTreePathForTest extends Automatic_Tester {
    private String trainfile;
    private String testfile;
    private String modelfile;
    private ArrayList<TreeNodeExpression> pathNodeList;
    private int labelRange = 10;
    private int attriRange = 10;
    private int predict_Array;

    RetriveTreePathForTest(String trainfile1, String testfile1, String modelfile1){
        trainfile = trainfile1;
        testfile = testfile1;
        modelfile = modelfile1;

        pathNodeList = new ArrayList<TreeNodeExpression>();
    }

    public String getTest(int number) throws IOException {//start with 1.2.3...
        List<String> lineslist = FileUtils.readLines(new File(testfile));
        File targetFile = new File(testfile.replace("testAll","test"));
        FileUtils.writeStringToFile(targetFile,"",false);

        int flag = 0;
        for(int i=0;i<lineslist.size();i++){
            if(flag == 0){
                FileUtils.writeStringToFile(targetFile,lineslist.get(i)+"\n",true);
            }
            if(lineslist.get(i).contains("@data")) {
                flag = 1;
                int data_size = lineslist.size() - i - 1;
                if (number > data_size)
                    System.out.println("out of size");
                else {
                    FileUtils.writeStringToFile(targetFile, lineslist.get(i + number)+"\n",true);


                    return lineslist.get(i+number);
                }
            }
        }

        return null;
    }

    public ArrayList<TreeNodeExpression> getPathList(int number) throws IOException {

        //ArrayList<TreeNodeExpression> pathNodeList = new ArrayList<TreeNodeExpression>();

        String[] testline = getTest(number).split(",");
        int [] parts_Test = new int[testline.length];
        for(int i = 0;i<parts_Test.length;i++) {
            parts_Test[i] = Integer.parseInt(testline[i]);
        }
        List<String> treelines = FileUtils.readLines(new File(modelfile));
        int flag = 0;
        String start="";
        for(int i=0;i<treelines.size();i++) {

            if (treelines.get(i).contains("J48 pruned tree"))
                flag = 1;
            if (flag == 0)
                continue;
            else {
                for(int j=i;j<treelines.size();j++) {
                    String line = treelines.get(i);
                    if (line.isEmpty())
                        continue;
                    else if (line.startsWith(start)) {

                        if (line.charAt(start.length()) > '9' ||
                                line.charAt(start.length()) < '0')
                            continue;
                        TreeNodeExpression nodeExpression = getNodeFromLine(line);
                        if(nodeExpression.operator.equals("<")){
                            if(parts_Test[Integer.parseInt(nodeExpression.attributeName)]<
                                    Integer.parseInt(nodeExpression.valueSplit)) {
                                pathNodeList.add(nodeExpression);
                                start = start + "|   ";
                                if(!nodeExpression.label.equals("")){
                                    flag = 0;break;
                                }
                            }
                        }
                        else if(nodeExpression.operator.equals("<=")){
                            if(parts_Test[Integer.parseInt(nodeExpression.attributeName)]<=
                                    Integer.parseInt(nodeExpression.valueSplit)) {
                                pathNodeList.add(nodeExpression);
                                start = start + "|   ";
                                if(!nodeExpression.label.equals("")){
                                    flag = 0;break;
                                }
                            }
                        }
                        else if(nodeExpression.operator.equals("=")){
                            if(parts_Test[Integer.parseInt(nodeExpression.attributeName)]==
                                    Integer.parseInt(nodeExpression.valueSplit)) {
                                pathNodeList.add(nodeExpression);
                                start = start + "|   ";
                                if(!nodeExpression.label.equals("")){
                                    flag = 0;break;
                                }
                            }

                        }
                        else if(nodeExpression.operator.equals(">")){
                            if(parts_Test[Integer.parseInt(nodeExpression.attributeName)]>
                                    Integer.parseInt(nodeExpression.valueSplit)) {
                                pathNodeList.add(nodeExpression);
                                start = start + "|   ";
                                if(!nodeExpression.label.equals("")){
                                    flag = 0;break;
                                }
                            }

                        }
                        else if(nodeExpression.operator.equals(">=")){
                            if(parts_Test[Integer.parseInt(nodeExpression.attributeName)]>=
                                    Integer.parseInt(nodeExpression.valueSplit)) {
                                pathNodeList.add(nodeExpression);
                                start = start + "|   ";
                                if(!nodeExpression.label.equals("")){
                                    flag = 0;break;
                                }
                            }
                        }

                    } else
                        continue;
                    if (treelines.get(j).contains("Number of Leaves"))
                        break;
                    }
                }
        }
        predict_Array = Integer.parseInt(pathNodeList.get(pathNodeList.size()-1).label);

        //System.out.println("predict:"+predict_Array);
        return pathNodeList;
    }

    public void displayList(ArrayList<TreeNodeExpression> treeNodeList){
        for(int i=0;i<treeNodeList.size();i++){
            //System.out.println();
            treeNodeList.get(i).display();
        }
    }
    public TreeNodeExpression getNodeFromLine(String line){
        line = line.replaceAll("\\|   ","");

        String[] parts = line.split(" ");

        if(line.contains(":")){
            TreeNodeExpression treeNodeExpression = new TreeNodeExpression(parts[0],parts[1],
                    parts[2].replace(":",""),parts[3]);
            return treeNodeExpression;
        }
        else{
            TreeNodeExpression treeNodeExpression = new TreeNodeExpression(parts[0],parts[1],parts[2],"");
            return treeNodeExpression;
        }
    }

    public double caculateScore(String line){
        double dominator = pathNodeList.size();
        double numerator = 0;
        //System.out.println("line:"+line);
        for(int i=0;i<pathNodeList.size();i++){
            if(pathNodeList.get(i).isAttributeSatisfied(line)) {
                numerator++;//System.out.println("1");
            }
            else
                ;//System.out.println("-1");
        }
        if(pathNodeList.get(pathNodeList.size()-1).isLabelSatisfied(line))
            return numerator/dominator*1;
        else return numerator/dominator*(-1);

    }
    public int int_RandomExcept(int except){
        int value = (int)(Math.random()*labelRange);
        while(value == except){
            value = (int)(Math.random()*labelRange);
        }

        return value;
    }
    public String replaceAttributeValue(String[] parts, int loc, int value){
        parts[loc] = value+"";
        String line ="";
        for(int i=0;i<parts.length-1;i++){
            line = line +parts[i]+ ",";
        }
        line = line + parts[parts.length-1];
        return line;
    }
    public String generateMaxInstance(String[] parts){
        String result = "";
        for(int i=0;i<parts.length-1;i++){
            double maxScore = 0.0;
            int maxLoc = 0;
            for(int j=0;j<attriRange;j++){
                double score = caculateScore(replaceAttributeValue(parts,i,j));
                if(Math.abs(score)>Math.abs(maxScore)) {
                    maxScore = score;
                    maxLoc = j;
                }

            }
            parts[i] = maxLoc+"";
            result = result + maxLoc + "," ;
        }
        result = result + parts[parts.length-1];
        return result;
    }

    public String generateMinInstance(String[] parts){
            String result = "";
            for(int i=0;i<parts.length-1;i++){
                double minScore = 1.0;
                int minLoc = 0;
                for(int j=0;j<attriRange;j++){
                    double score = caculateScore(replaceAttributeValue(parts,i,j));
                    if(Math.abs(score)<Math.abs(minScore)) {
                        minScore = score;
                        minLoc = j;
                    }

                }
                parts[i] = minLoc+"";
                result = result + minLoc + "," ;
            }
        result = result + parts[parts.length-1];
        return result;
    }
    @Override
    protected String modifyLine(String trainLine, int modify_type) {
        String[] parts = trainLine.split(",");
        String resultLine = "";
        if(modify_type==0){//0: positive->negative
            for(int i=0;i<parts.length-1;i++){
                resultLine = resultLine+parts[i]+",";
            }
            resultLine = resultLine+ int_RandomExcept(predict_Array);
        }

        else if(modify_type==1){//1: positive->irrelevant

            resultLine = generateMinInstance(parts);

        }

        else if(modify_type==2){//2: negative->positive
            for(int i=0;i<parts.length-1;i++){
                resultLine = resultLine+parts[i]+",";
            }
            resultLine = resultLine + predict_Array;
        }

        else if(modify_type==3){//3: negative->irrelevant
            resultLine = generateMinInstance(parts);
        }

        else if(modify_type==4){//4: irrelevant->positive

            resultLine = generateMinInstance(parts);
            resultLine = modifyLine(resultLine,2);
        }

        else if(modify_type==5){//5: irrelevant->negative
            resultLine = generateMinInstance(parts);
            resultLine = modifyLine(resultLine,0);
        }
        //System.out.println(trainLine+"--->"+resultLine);
        return resultLine;
    }
    public String modifyLineSolver(ArrayList<TreeNodeExpression> conditionList){
        return null;
    }
    public static void main(String[] args) throws IOException {

        String trainFile = "E:\\MT1\\MutantTest3\\20trainAll.arff";
        String testFile = "E:\\MT1\\MutantTest3\\20testAll.arff";
        String treeFile = "E:\\MT1\\MutantTest3\\tree.txt";

        int testNumber = 1;
        //int modify_type = 1;
        //int modify_strength = 50;

        for(int i=testNumber;i<=200;i++) {
            RetriveTreePathForTest retriver = new RetriveTreePathForTest(trainFile, testFile, treeFile);
            //System.out.println(retriver.getTest(4));
            //retriver.displayList();
            System.out.println("=====testNumber:"+i);
            retriver.getPathList(i);
            //System.out.println();
            //System.out.println("Score"+retriver.caculateScore("0,9,9,0,0,0,9,0,0,1"));

            retriver.read_Train_Data(trainFile);
        }
        //retriver.modifyFile(trainFile,modify_type,modify_strength);
        //retriver.read_Train_Data("E:\\MT1\\MutantTest3\\20trainAll_after1.arff");

    }
}
