import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.MagicNumberFileFilter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by coco on 2016/9/6.
 */
public class RetriveTreePathForTest extends Automatic_Tester {
    private String trainfile;
    private String testfile;
    private String modelfile;
    private ArrayList<TreeNodeExpression> pathNodeList;
    private int labelRange = 5;
    private int attriRange = 10;
    private int predict_Array;
    private String testLineBase;

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
                if (number > data_size) {
                    System.out.println("out of size");
                    testLineBase = lineslist.get(i + 1);
                    return testLineBase;
                }
                else {
                    FileUtils.writeStringToFile(targetFile, lineslist.get(i + number)+"\n",true);
                    testLineBase = lineslist.get(i+number);
                    return testLineBase;
                }
            }
        }

        return null;
    }

    public void generateSingleTestFile(int number) throws IOException {

        File single = new File(testfile.replace("All.arff","Single.arff"));
        if(single.exists())
            FileUtils.forceDelete(single);
        File testSingle = new File(testfile.replace("All.arff","Single.arff"));
        FileUtils.writeStringToFile(testSingle,"",false);
        FileUtils.writeStringToFile(testSingle,"@relation 20test\n",true);
        FileUtils.writeStringToFile(testSingle,"\n",true);
        FileUtils.writeStringToFile(testSingle,"@attribute 0 real\n",true);
        FileUtils.writeStringToFile(testSingle,"@attribute 1 real\n",true);
        FileUtils.writeStringToFile(testSingle,"@attribute 2 real\n",true);
        FileUtils.writeStringToFile(testSingle,"@attribute 3 real\n",true);
        FileUtils.writeStringToFile(testSingle,"@attribute 4 real\n",true);
        FileUtils.writeStringToFile(testSingle,"@attribute 5 real\n",true);
        FileUtils.writeStringToFile(testSingle,"@attribute 6 real\n",true);
        FileUtils.writeStringToFile(testSingle,"@attribute 7 real\n",true);
        FileUtils.writeStringToFile(testSingle,"@attribute 8 real\n",true);
        FileUtils.writeStringToFile(testSingle,"@attribute 9 {0,1,2,3,4,5,6,7,8,9}\n",true);
        FileUtils.writeStringToFile(testSingle,"\n",true);
        FileUtils.writeStringToFile(testSingle,"@data\n",true);
        FileUtils.writeStringToFile(testSingle,getTest(number)+"\n",true);


    }
    public ArrayList<TreeNodeExpression> getPathListIns(String instance) throws IOException {
        if(instance==null)
            return null;
        pathNodeList.clear();
        String[] testline = instance.split(",");//getTest(number).split(",");
        double [] parts_Test = new double[testline.length];
        for(int i = 0;i<parts_Test.length;i++) {
            parts_Test[i] = Double.parseDouble(testline[i]);
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
                    String line = treelines.get(j);
                    if (line.isEmpty())
                        continue;
                    else if (line.startsWith(start)) {
                        if(line.startsWith(":")){
                            TreeNodeExpression nodeExpression = getNodeFromLine(line);
                            pathNodeList.add(nodeExpression);
                            if(!nodeExpression.label.equals("")){
                                flag = 0;break;
                            }
                        }

                        if (line.charAt(start.length()) > '9' ||
                                line.charAt(start.length()) < '0')
                            continue;
                        TreeNodeExpression nodeExpression = getNodeFromLine(line);

                        if(nodeExpression.operator.equals("<")){
                            if(parts_Test[Integer.parseInt(nodeExpression.attributeName)]<
                                    Double.parseDouble(nodeExpression.valueSplit)) {
                                pathNodeList.add(nodeExpression);
                                start = start + "|   ";
                                if(!nodeExpression.label.equals("")){
                                    flag = 0;break;
                                }
                            }
                        }
                        else if(nodeExpression.operator.equals("<=")){
                            if(parts_Test[Integer.parseInt(nodeExpression.attributeName)]<=
                                    Double.parseDouble(nodeExpression.valueSplit)) {
                                pathNodeList.add(nodeExpression);
                                start = start + "|   ";
                                if(!nodeExpression.label.equals("")){
                                    flag = 0;break;
                                }
                            }
                        }
                        else if(nodeExpression.operator.equals("=")){
                            if(parts_Test[Integer.parseInt(nodeExpression.attributeName)]==
                                    Double.parseDouble(nodeExpression.valueSplit)
                                    ||Math.abs(parts_Test[Integer.parseInt(nodeExpression.attributeName)]-Double.parseDouble(nodeExpression.valueSplit))<1e-5) {
                                pathNodeList.add(nodeExpression);
                                start = start + "|   ";
                                if(!nodeExpression.label.equals("")){
                                    flag = 0;break;
                                }
                            }

                        }
                        else if(nodeExpression.operator.equals(">")){
                            if(parts_Test[Integer.parseInt(nodeExpression.attributeName)]>
                                    Double.parseDouble(nodeExpression.valueSplit)) {
                                pathNodeList.add(nodeExpression);
                                start = start + "|   ";
                                if(!nodeExpression.label.equals("")){
                                    flag = 0;break;
                                }
                            }

                        }
                        else if(nodeExpression.operator.equals(">=")){
                            if(parts_Test[Integer.parseInt(nodeExpression.attributeName)]>=
                                    Double.parseDouble(nodeExpression.valueSplit)) {
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
    public ArrayList<TreeNodeExpression> getPathList(int number) throws IOException {

        //ArrayList<TreeNodeExpression> pathNodeList = new ArrayList<TreeNodeExpression>();
        return getPathListIns(getTest(number));
    }

    public ArrayList<TreeNodeExpression> getPathNodeList(){
        return pathNodeList;
    }
    public void displayList(ArrayList<TreeNodeExpression> treeNodeList){
        if(treeNodeList==null)
            return;
        for(int i=0;i<treeNodeList.size();i++){

            treeNodeList.get(i).display();
            System.out.print(";");
        }
        System.out.println();
    }
    public TreeNodeExpression getNodeFromLine(String line){
        line = line.replaceAll("\\|   ","");

        String[] parts = line.split(" ");
        if(line.startsWith(":")){
            TreeNodeExpression treeNodeExpression = new TreeNodeExpression("","result",
                    parts[0].replace(":",""),parts[1]);
            return treeNodeExpression;
        }
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



    public double caculateScoreConditionCoverNoMerge(String line){
        double dominator = pathNodeList.size();

        double numerator = 0;
        //displayList(pathNodeList);
        //System.out.println("size"+pathNodeList.size());W
        for(int i=0;i<pathNodeList.size();i++){
            if(pathNodeList.get(i).isAttributeSatisfied(line)) {
                numerator++;//System.out.println("1");
            }
            else
                ;//System.out.println("-1");
        }
        //System.out.println("size"+pathNodeList.size());
        //System.out.println("dominator"+numerator);
        if(pathNodeList.get(pathNodeList.size()-1).isLabelSatisfied(line))
            return numerator/dominator*1;
        else return numerator/dominator*(-1);

    }
    public double caculateScorePathCover(String line){//PathCover
        double dominator = pathNodeList.size();

        double numerator = 0;
        //displayList(pathNodeList);
        //System.out.println("size"+pathNodeList.size());W
        for(int i=0;i<pathNodeList.size();i++){
            if(pathNodeList.get(i).isAttributeSatisfied(line)) {
                numerator++;//System.out.println("1");
            }
            else
                break;//System.out.println("-1");
        }
        //System.out.println("size"+pathNodeList.size());
        //System.out.println("dominator"+numerator);
        if(pathNodeList.get(pathNodeList.size()-1).isLabelSatisfied(line))
            return numerator/dominator*1;
        else return numerator/dominator*(-1);

    }

    public double caculateScoreConditionCoverMerge(String line){
        double dominator = pathNodeList.size();
        double numerator = 0;
        HashMap<String, Integer> pathListTogether = new HashMap<String, Integer>();

        //System.out.println("line:"+line);
        for(int i=0;i<pathNodeList.size();i++){
            if(pathNodeList.get(i).isAttributeSatisfied(line)) {
                String attribute = pathNodeList.get(i).getAttributeName();
                if(pathListTogether.containsKey(attribute))
                    pathListTogether.put(attribute,pathListTogether.get(attribute)*1);
                else
                    pathListTogether.put(attribute,1);

            }
            else{
                String attribute = pathNodeList.get(i).getAttributeName();
                if(pathListTogether.containsKey(attribute))
                    pathListTogether.put(attribute,pathListTogether.get(attribute)*0);
                else
                    pathListTogether.put(attribute,0);

            }
        }
        Iterator iterator = pathListTogether.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iterator.next();
            if(entry.getValue()>0)
                numerator ++;


        }

        if(pathNodeList.get(pathNodeList.size()-1).isLabelSatisfied(line))
            return numerator/pathListTogether.size()*1;
        else return numerator/pathListTogether.size()*(-1);
    }
    public int int_RandomExcept(int except){
        int value = (int)(Math.random()*labelRange);
        while(value == except){
            value = (int)(Math.random()*labelRange);
        }
        return value;

    }
    public double double_RandomExcept(ArrayList<Double> list, double except){
        int number = 0;
        for(int i=0;i<list.size();i++) {
            if (Math.abs(list.get(i) - except)< 1e-5)
                continue;
            else
                number++;
        }
        System.out.println("number:"+number);
        System.out.println("except:"+except);
        int rand = (int)(Math.random()*number);
        System.out.println("rand:"+rand);
        int select = 0;
        int i;
        for(i=0;i<list.size();i++) {
            if (Math.abs(list.get(i) - except) < 1e-5)
                continue;
            else{
                if(rand==select)
                    return list.get(i);
                else
                    select ++;
            }
        }
        //System.out.println("select:"+select);
        return 0.0;
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
    @Override
    protected double generateMaxInstanceScore(String line){

        String [] parts = line.split(",");
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
        return Math.abs(caculateScore(result));
    }
    @Override
    protected double generateMinInstanceScore(String line){
        String [] parts = line.split(",");
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
        return Math.abs(caculateScore(result));

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



    protected String modifyLineNewHighQuality(String trainLine, int modify_type) throws IOException {
        String resultLine = "";
        if(modify_type==0){//0: positive->negative

            resultLine = generateFullScoreInstance(pathNodeList,modify_type);
        }

        else if(modify_type==1){//1: positive->irrelevant
            String[] parts = trainLine.split(",");

            resultLine = generateMinInstance(parts);

        }

        else if(modify_type==2){//2: negative->positive
            resultLine = generateFullScoreInstance(pathNodeList,modify_type);
        }

        else if(modify_type==3){//3: negative->irrelevant
            String[] parts = trainLine.split(",");
            resultLine = generateMinInstance(parts);
        }

        else if(modify_type==4){//4: irrelevant->positive
            String[] parts = trainLine.split(",");
            resultLine = generateMaxInstance(parts);
            resultLine = modifyLine(resultLine,2);
        }

        else if(modify_type==5){//5: irrelevant->negative
            String[] parts = trainLine.split(",");
            resultLine = generateMaxInstance(parts);
            resultLine = modifyLine(resultLine,0);
        }
        else if(modify_type==-1) {//-1: generate positive 100%
            String testLine = testLineBase;
            String[] test_parts = testLine.split(",");
            for(int i=0;i<test_parts.length-1;i++){
                resultLine = resultLine+test_parts[i]+",";
            }
            resultLine = resultLine+ predict_Array;
        }
        else if(modify_type==-2){//-1: generate negative 100%
            String testLine = testLineBase;
            String[] test_parts = testLine.split(",");
            for(int i=0;i<test_parts.length-1;i++){
                resultLine = resultLine+test_parts[i]+",";
            }
            resultLine = resultLine+ int_RandomExcept(predict_Array);
        }
        //System.out.println(trainLine+"--->"+resultLine);
        return resultLine;
    }
    //@Override
    protected String modifyLineHighQuality(String trainLine, int modify_type) throws IOException {
        String resultLine = "";
        if(modify_type==0){//0: positive->negative
            String testLine = testLineBase;
            //System.out.println("testLine:"+testLineBase);
            String[] test_parts = testLine.split(",");
            for(int i=0;i<test_parts.length-1;i++){
                resultLine = resultLine+test_parts[i]+",";
            }
            resultLine = resultLine+ int_RandomExcept(predict_Array);
        }

        else if(modify_type==1){//1: positive->irrelevant
            String[] parts = trainLine.split(",");

            resultLine = generateMinInstance(parts);

        }

        else if(modify_type==2){//2: negative->positive
            String testLine = testLineBase;
            String[] test_parts = testLine.split(",");
            for(int i=0;i<test_parts.length-1;i++){
                resultLine = resultLine+test_parts[i]+",";
            }
            resultLine = resultLine+ predict_Array;
        }

        else if(modify_type==3){//3: negative->irrelevant
            String[] parts = trainLine.split(",");
            resultLine = generateMinInstance(parts);
        }

        else if(modify_type==4){//4: irrelevant->positive
            String[] parts = trainLine.split(",");
            resultLine = generateMaxInstance(parts);
            resultLine = modifyLine(resultLine,2);
        }

        else if(modify_type==5){//5: irrelevant->negative
            String[] parts = trainLine.split(",");
            resultLine = generateMaxInstance(parts);
            resultLine = modifyLine(resultLine,0);
        }
        else if(modify_type==-1) {//-1: generate positive 100%
            String testLine = testLineBase;
            String[] test_parts = testLine.split(",");
            for(int i=0;i<test_parts.length-1;i++){
                resultLine = resultLine+test_parts[i]+",";
            }
            resultLine = resultLine+ predict_Array;
        }
        else if(modify_type==-2){//-1: generate negative 100%
            String testLine = testLineBase;
            String[] test_parts = testLine.split(",");
            for(int i=0;i<test_parts.length-1;i++){
                resultLine = resultLine+test_parts[i]+",";
            }
            resultLine = resultLine+ int_RandomExcept(predict_Array);
        }
        //System.out.println(trainLine+"--->"+resultLine);
        return resultLine;
    }


    //@Override
    protected String modifyLineGenerate(String trainLine, int modify_type) throws IOException {
    /*modify_timestamp: 20161215;modifyLineGenerate: each time we modify according to their proporation and control type, but donot control
        to generate 100% positive(similarity:+1) or 100% negative(similarity:-1).
        when need to generate more target type than source, we select its type from history
        */
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

            resultLine = generateMaxInstance(parts);
            resultLine = modifyLine(resultLine,2);
        }

        else if(modify_type==5){//5: irrelevant->negative
            resultLine = generateMaxInstance(parts);
            resultLine = modifyLine(resultLine,0);
        }

        //System.out.println(trainLine+"--->"+resultLine);
        return resultLine;
    }

    public String modifyLineSolver(ArrayList<TreeNodeExpression> conditionList){
        return null;
    }


    public String generateFullScoreInstance(ArrayList<TreeNodeExpression> treeNodeList, int pos_Neg){
    //In order to generate a full score instance for negative or positive when feeding a certain testing instance
    //may return an exactly same instance as the testing instance, but mostly different but with 100% pos/Neg

        ArrayList<ArrayList<Double>> candidateList = new ArrayList<ArrayList<Double>>();
        //initial candidate pools
        for(int i=0;i<9;i++){
            ArrayList<Double> temp = new ArrayList<>();
            for(int j=0;j<attriRange;j++)
                temp.add(j, Double.valueOf(j));
            candidateList.add(temp);
        }
        ArrayList<Double> temp = new ArrayList<>();
        for(int j=0;j<labelRange;j++)
            temp.add(j, Double.valueOf(j));
        candidateList.add(temp);

        //System.out.println(candidateList.toString());

        for(int num = 0;num<treeNodeList.size();num++){
            TreeNodeExpression condition = treeNodeList.get(num);
            String attribute = condition.getAttributeName();
            String operator = condition.getOperator();
            String split = condition.getValueSplit();
            //System.out.println(attribute+" "+operator+" "+split);
            if(attribute.isEmpty())
                continue;
            if(operator.equals("<")){
                ArrayList<Double> attr_candidates = candidateList.get(Integer.parseInt(attribute));
                for(int i = 0;i<attr_candidates.size();i++){
                    if(attr_candidates.get(i)<Double.parseDouble(split))//"<",满足条件的留着
                        continue;
                    else
                        attr_candidates.set(i,Double.valueOf(-1));
                }
            }
            else if(operator.equals("<=")){
                ArrayList<Double> attr_candidates = candidateList.get(Integer.parseInt(attribute));
                for(int i = 0;i<attr_candidates.size();i++){
                    if(attr_candidates.get(i)<Double.parseDouble(split)||Math.abs(attr_candidates.get(i)-Double.parseDouble(split))<1e-5)//"<=",满足条件的留着
                        continue;
                    else
                        attr_candidates.set(i,Double.valueOf(-1));
                }
            }
            else if(operator.equals("=")){
                ArrayList<Double> attr_candidates = candidateList.get(Integer.parseInt(attribute));
                for(int i = 0;i<attr_candidates.size();i++){
                    if(Math.abs(attr_candidates.get(i)-Double.parseDouble(split))<1e-5)//"=",满足条件的留着
                        continue;
                    else
                        attr_candidates.set(i,Double.valueOf(-1));
                }
            }
            else if(operator.equals(">")) {
                ArrayList<Double> attr_candidates = candidateList.get(Integer.parseInt(attribute));
                for(int i = 0;i<attr_candidates.size();i++){
                    if(attr_candidates.get(i)>Double.parseDouble(split))//">",满足条件的留着
                        continue;
                    else
                        attr_candidates.set(i,Double.valueOf(-1));
                }
            }
            else if(operator.equals(">=")){
                ArrayList<Double> attr_candidates = candidateList.get(Integer.parseInt(attribute));
                for(int i = 0;i<attr_candidates.size();i++){
                    if(attr_candidates.get(i)>Double.parseDouble(split)||Math.abs(attr_candidates.get(i)-Double.parseDouble(split))<1e-5)//">=",满足条件的留着
                        continue;
                    else
                        attr_candidates.set(i,Double.valueOf(-1));
                }

            }
           // System.out.println(candidateList.toString());

        }


        //System.out.println(candidateList.toString());
        //System.out.println(candidateList.toString());

        String result = "";
        for(int i=0;i<candidateList.size()-1;i++){

            ArrayList<Double> attr_candidates = candidateList.get(i);
            result = result + (int)double_RandomExcept(attr_candidates,-1)+",";

        }
        if( pos_Neg == 0 ){//To generate hundred percent negative instance
            ArrayList<Double> label_candidates = candidateList.get(candidateList.size()-1);
            System.out.println(label_candidates.toString());
            result = result + (int)double_RandomExcept(label_candidates,Double.parseDouble(treeNodeList.get(treeNodeList.size()-1).getLabel()));
        }
        else if( pos_Neg == 2 ){
            //To generate hundred percent negative instance
            result = result + Integer.parseInt(treeNodeList.get(treeNodeList.size()-1).getLabel());
        }
        //System.out.println("score:"+calculateScore(result));
        System.out.println("result:"+result);
        return result;
    }
    protected String modifyLine(String trainLine, int modify_type) throws IOException {
        return modifyLineNewHighQuality(trainLine, modify_type);
        //return modifyLineHighQuality(trainLine, modify_type);
        //return modifyLineGenerate(trainLine, modify_type);
    }

    public double caculateScore(String line){
        //return caculateScoreConditionCoverNoMerge(line);
        return caculateScorePathCover(line);
        //return caculateScoreConditionCoverMerge(line);
    }
    public static void main(String[] args) throws IOException {

        //int fileNumber = 1;

        String trainFile = "E:\\Dataset\\miniStudy\\Golden\\PathCover\\GeneInsQ\\1trainAll.arff";
        String testFile = "E:\\Dataset\\miniStudy\\Golden\\PathCover\\GeneInsQ\\1testAll.arff";
        String treeFile = "E:\\Dataset\\miniStudy\\Golden\\PathCover\\GeneInsQ\\treeG.txt";

        int testNumber = 1;
        int modify_type =2;
        int modify_strength = 10;

        if(modify_type == -1 && modify_strength == -1){
            RetriveTreePathForTest retriver = new RetriveTreePathForTest(trainFile,testFile,treeFile);
            retriver.generateSingleTestFile(testNumber);
            //retriver.displayList(retriver.getPathList(testNumber));
            retriver.getPathList(testNumber);

            //System.out.println();
            //System.out.println("Score"+retriver.caculateScore("0,9,9,0,0,0,9,0,0,1"));
            int[] PartitionNum = retriver.read_Train_Data(trainFile);
            File file = new File(args[6]);
            FileUtils.writeStringToFile(file,"",false);

            for(int percent=10;percent<=100;percent=percent+10) {
                for(int i=0;i<4;i++) {
                    int j;
                    int classflag = 0;
                    if(i==0||i==1){
                        if(PartitionNum[2]<100 && PartitionNum[2]>20)
                            classflag = -1;
                        else if(PartitionNum[2]<=20)
                            classflag = -2;
                    }
                    else if(i==2||i==3){
                        if(PartitionNum[0]<100 && PartitionNum[0]>20)
                            classflag = -1;
                        else if(PartitionNum[0]<=20)
                            classflag = -2;
                    }
                    else{
                        if(PartitionNum[1]<100 && PartitionNum[1]>20)
                            classflag = -1;
                        else if(PartitionNum[1]<=20)
                            classflag = -2;
                    }

                    if(classflag == 0){
                        if(i==0||i==1)
                            j = percent*PartitionNum[2]/100;
                        else if(i==2||i==3)
                            j = percent*PartitionNum[0]/100;
                        else
                            j = percent*PartitionNum[1]/100;
                        for(int k=0;k<25;k++) {

                            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner" +
                                    "\\lib RetriveTreePathForTest 20train.arff 20testAll.arff tree.txt " + testNumber+" "+ i + " " + j + "\n", true);
                            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib " +
                                    "weka.classifiers.trees.J48 -C 0.25 -M 3 -t 20train_after.arff -d model.model > tree1.txt\n", true);
                            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib " +
                                    "weka.classifiers.trees.J48 -p 10 -l model.model -T 20testSingle.arff > "+i+"\\predict_"+i+"_"+j+"_"+k+".txt\n\n", true);

                        }
                        //System.out.println(j);
                    }
                    else if(classflag == -1){

                        if(i==0||i==1)
                            j = percent*PartitionNum[2]/100;
                        else if(i==2||i==3)
                            j = percent*PartitionNum[0]/100;
                        else
                            j = percent*PartitionNum[1]/100;
                        for(int k=0;k<25;k++) {

                            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner" +
                                    "\\lib RetriveTreePathForTest 20train.arff 20testAll.arff tree.txt " + testNumber+" "+ i + " " + j + "\n", true);
                            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib " +
                                    "weka.classifiers.trees.J48 -C 0.25 -M 3 -t 20train_after.arff -d model.model > tree1.txt\n", true);
                            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib " +
                                    "weka.classifiers.trees.J48 -p 10 -l model.model -T 20testSingle.arff > "+i+"\\predict_"+i+"_"+j+"_"+k+".txt\n\n", true);
                        }
                        //System.out.println(j);
                    }


                }
            }

            String ProcessFile2 = "AnalyzerOld.java";
            List<String> lines2 = FileUtils.readLines(new File(ProcessFile2));
            for(int i=0;i<lines2.size();i++){
                if(lines2.get(i).contains("????????")){
                    lines2.set(i,lines2.get(i).replace("????????",retriver.predict_Array+""));
                }
            }
            FileUtils.writeLines(new File(ProcessFile2.replace("Old.java",".java")),lines2,false);



        }
        else {
            RetriveTreePathForTest retriver = new RetriveTreePathForTest(trainFile,testFile,treeFile);
            //retriver.displayList(retriver.getPathList(testNumber));
            //retriver.getPathList(testNumber);

            System.out.print("test:");
            retriver.displayList(retriver.getPathList(testNumber));

            /*for(int i=1;i<=2000;i++) {
                //System.out.print(i+",");
                retriver.displayList(retriver.getPathListIns(RandomDataFile.selectIns(trainFile, i)));
            }
            System.out.println();*/

            //retriver.generateFullScoreInstance(retriver.getPathNodeList(),0);
            //System.out.println("Score"+retriver.caculateScore("0,9,9,0,0,0,9,0,0,1"));

            retriver.read_Train_Data(trainFile);

            for(int i=1;i<=2000;i++)
                retriver.modifyFileCertain(trainFile,2,i);

            for(int i=1;i<=2000;i++)
                retriver.modifyFileCertain(trainFile,0,i);

            //double max = retriver.getMaxScore();
            //double min = retriver.getMinScore();
            /*if(modify_type==1||modify_type==3) {
                if(retriver.isGeneratable()){
                    retriver.modifyFile(trainFile,modify_type,modify_strength);
                }
                else
                    System.out.println("Cannot be generated!");
            }
            else
                retriver.modifyFile(trainFile,modify_type,modify_strength);*/
            //retriver.read_Train_Data("E:\\MT1\\MutantTest3\\20trainAll_after1.arff");

        }
        //System.out.println(retriver.getTest(4));

    }

    /*public static void main(String[] args) throws IOException {

        String trainFile = "E:\\MT1\\MutantTest3\\20trainAll.arff";
        String testFile = "E:\\MT1\\MutantTest3\\20testAll.arff";
        String treeFile = "E:\\MT1\\MutantTest3\\tree.txt";

        int testNumber = 5;
        int modify_type = 5;
        int modify_strength = 50;

        RetriveTreePathForTest retriver = new RetriveTreePathForTest(trainFile,testFile,treeFile);
        //System.out.println(retriver.getTest(4));
        retriver.displayList(retriver.getPathList(testNumber));

        //System.out.println();
        //System.out.println("Score"+retriver.caculateScore("0,9,9,0,0,0,9,0,0,1"));

        retriver.read_Train_Data(trainFile);

        //double max = retriver.getMaxScore();
        //double min = retriver.getMinScore();
        if(modify_type==1||modify_type==3) {
            if(retriver.isGeneratable()){
                retriver.modifyFile(trainFile,modify_type,modify_strength);
            }
            else
                System.out.println("Cannot be generated!");
        }
        else
            retriver.modifyFile(trainFile,modify_type,modify_strength);
        retriver.read_Train_Data("E:\\MT1\\MutantTest3\\20trainAll_after1.arff");

    }*/
}
