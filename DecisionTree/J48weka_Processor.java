package DecisionTree;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Coco on 2016/7/21.
 */
public class J48weka_Processor {
    private int predict_Array;
    private int testDataLength;

    private int[] tree_Executed;
    private ArrayList<Integer> score_List;
    private int score_Test;
    private int[] analyzerResult;
    private int labelRange = 10;
    final double[] slot = {-1,-0.2,-0.2,0.2,0.2,1.0};

    public void setTree(int[] tree_Executed1, int predict_Array1){
        tree_Executed = tree_Executed1.clone();
        predict_Array = predict_Array1;
    }
    public void setTestScore(int score){
        score_Test = score;
    }
    public int testLine_Score(String testLine){
        String[] test_Array = testLine.split(",");
        int score = 0;
        for(int i=0;i<test_Array.length-1;i++){
            int temp = Integer.parseInt(test_Array[i]);

            if(temp==tree_Executed[i])
                score++;
        }
        if(predict_Array != Integer.parseInt(test_Array[test_Array.length - 1]))
            score = score * -1;
        return score;
    }
    public int[] read_Train_Data(String trainFilePath) throws IOException {
        List<String> lines= FileUtils.readLines(new File(trainFilePath));
        score_List = new ArrayList<Integer>();
        for(int i=0;i<lines.size();i++){
            if(lines.get(i).contains("@")||lines.get(i).isEmpty())
                continue;
            int score = testLine_Score( lines.get(i));
            //System.out.println(score);
            score_List.add(score);

        }
        return analyzer_Score_List();
    }
    public int[] analyzer_Score_List(){
        analyzerResult = new int[4];
        int[] analyzer = new int[21];
        for(int i=0;i<score_List.size();i++){
            // System.out.println("--"+score_Test);
            int group = 10+(int)(((double)score_List.get(i))/((double)score_Test*0.1));
            analyzer[group]++;
        }
        for(int i=(int)(slot[0]*10+10);i<(int)(slot[1]*10+10);i++){//relevant, negative
            analyzerResult[0] = analyzerResult[0]+analyzer[i];
        }
        for(int i=(int)(slot[2]*10+10);i<(int)(slot[3]*10+10);i++){//irrelevant
            analyzerResult[1] = analyzerResult[1]+analyzer[i];
        }
        for(int i=(int)(slot[4]*10+10)+1;i<(int)(slot[5]*10+10)+1;i++){//relevant, positive
            analyzerResult[2] = analyzerResult[2]+analyzer[i];
        }
        for(int i=(int)(slot[1]*10+10)+1;i<(int)(slot[2]*10+10)+1;i++){//out of scope
            analyzerResult[3] = analyzerResult[3]+analyzer[i];
        }
        for(int i=(int)(slot[3]*10+10)+1;i<(int)(slot[4]*10+10)+1;i++){//out of scope
            analyzerResult[3] = analyzerResult[3]+analyzer[i];
        }
        System.out.println("Negative: "+analyzerResult[0]);
        System.out.println("Irrelevant: "+analyzerResult[1]);
        System.out.println("Positive: "+analyzerResult[2]);
        System.out.println("Undecidable: "+analyzerResult[3]);
        return analyzerResult;
    }
    public void modifyFile(String trainFilePath, int modify_type,int modify_strength) throws IOException {
        /*  0: positive->negative
            1: positive->irrelevant
            2: negative->positive
            3: negative->irrelevant
            4: irrelevant->positive
            5: irrelevant->negative
            modify_strength: 0.1---1.0
         */
        List<String> lines= FileUtils.readLines(new File(trainFilePath));

        if(modify_type==0||modify_type==1){
            int positive_Sum = analyzerResult[2];
            int[] modify_instance_Array;
            if(modify_strength<positive_Sum)
                modify_instance_Array = J48weka_Processor.array_Random(modify_strength,positive_Sum);
            else
                modify_instance_Array = J48weka_Processor.array_Random(positive_Sum,positive_Sum);
            //Arrays.sort(modify_instance_Array);
            int number = -1;
            for(int i=0;i<lines.size();i++) {
                if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                    continue;
                int score = testLine_Score(lines.get(i));
                if(score<score_Test*slot[4])//+0.8~1.0
                    continue;
                else number++;
                if(isExist(modify_instance_Array,number)){
                    lines.set(i,modifyLine(lines.get(i), modify_type));
                }
            }

        }else if(modify_type==2||modify_type==3){
            int negative_Sum = analyzerResult[0];
            int[] modify_instance_Array;
            if(modify_strength<negative_Sum)
                modify_instance_Array = J48weka_Processor.array_Random(modify_strength,negative_Sum);
            else
                modify_instance_Array = J48weka_Processor.array_Random(negative_Sum,negative_Sum);
            //Arrays.sort(modify_instance_Array);
            int number = -1;
            for(int i=0;i<lines.size();i++) {
                if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                    continue;
                int score = testLine_Score(lines.get(i));
                if(score>score_Test*slot[1])//-1.0~-0.8
                    continue;
                else number++;
                if(isExist(modify_instance_Array,number)){
                    lines.set(i,modifyLine(lines.get(i),modify_type));
                }
            }

        }else if(modify_type==4||modify_type==5){
            int negative_Sum = analyzerResult[1];
            int[] modify_instance_Array;
            if(modify_strength<negative_Sum)
                modify_instance_Array = J48weka_Processor.array_Random(modify_strength,negative_Sum);
            else
                modify_instance_Array = J48weka_Processor.array_Random(negative_Sum,negative_Sum);
            //Arrays.sort(modify_instance_Array);
            int number = -1;
            for(int i=0;i<lines.size();i++) {
                if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                    continue;
                int score = testLine_Score(lines.get(i));
                if(score<=score_Test*slot[2]||score>=score_Test*slot[3])//-0.1~+0.1
                    continue;
                else number++;
                if(isExist(modify_instance_Array,number)){
                    lines.set(i,modifyLine(lines.get(i),modify_type));
                }
            }
        }
        FileUtils.writeLines(new File(trainFilePath.replace(".arff","_after1.arff")),lines,false);
    }
    public boolean isExist(int[] array, int value){
        for(int i=0;i<array.length;i++){
            if(array[i]==value)
                return true;
        }
        return false;
    }
    public String modifyLine(String trainLine, int modify_type){

        String[] parts = trainLine.split(",");
        String resultLine = "";
        if(modify_type==0){//0: positive->negative
            for(int i=0;i<parts.length-1;i++){
                resultLine = resultLine+parts[i]+",";
            }
            resultLine = resultLine+ int_RandomExcept(predict_Array);


        }else if(modify_type==1){//1: positive->irrelevant
            for(int i=0;i<parts.length-1;i++){
                if(Integer.parseInt(parts[i]) == tree_Executed[i])
                    parts[i] = int_RandomExcept(tree_Executed[i])+"";
            }
            for(int i=0;i<parts.length-1;i++){
                resultLine = resultLine+parts[i]+",";
            }
            resultLine = resultLine + parts[parts.length-1];

        }else if(modify_type==2){//2: negative->positive
            for(int i=0;i<parts.length-1;i++){
                resultLine = resultLine+parts[i]+",";
            }
            resultLine = resultLine + predict_Array;


        }else if(modify_type==3){//3: negative->irrelevant
            for(int i=0;i<parts.length-1;i++){
                if(Integer.parseInt(parts[i]) == tree_Executed[i])
                    parts[i] = int_RandomExcept(tree_Executed[i])+"";
            }
            for(int i=0;i<parts.length-1;i++){
                resultLine = resultLine+parts[i]+",";
            }
            resultLine = resultLine + parts[parts.length-1];


        }else if(modify_type==4){//4: irrelevant->positive
            for(int i=0;i<parts.length-1;i++){
                if(Integer.parseInt(parts[i]) != tree_Executed[i]&&tree_Executed[i]!=-1)
                    parts[i] = tree_Executed[i]+"";
            }
            for(int i=0;i<parts.length-1;i++){
                resultLine = resultLine+parts[i]+",";
            }
            resultLine = resultLine + predict_Array;


        }else if(modify_type==5){//5: irrelevant->negative
            for(int i=0;i<parts.length-1;i++){
                if(Integer.parseInt(parts[i]) != tree_Executed[i]&&tree_Executed[i]!=-1)
                    parts[i] = tree_Executed[i]+"";
            }
            for(int i=0;i<parts.length-1;i++){
                resultLine = resultLine+parts[i]+",";
            }
            resultLine = resultLine + int_RandomExcept(predict_Array);
        }
        // System.out.println(trainLine+"--->"+resultLine);
        return resultLine;
    }
    public static int[] array_Random(int size, int valueRange){
        ArrayList<Integer> selected = new ArrayList<Integer>();
        int[] array = new int[size];
        for(int i=0;i<size;i++){
            int value = (int)(Math.random()*valueRange);
            while(selected.contains(value)){
                value = (int)(Math.random()*valueRange);
            }
            array[i] = value;
            selected.add(value);
            //System.out.print(value+"| ");
        }
        Arrays.sort(array);
        //for(int i=0;i<size;i++)
        //    System.out.print(array[i]+"| ");
        return array;
    }
    public int int_RandomExcept(int except){
        int value = (int)(Math.random()*labelRange);
        while(value == except){
            value = (int)(Math.random()*labelRange);
        }

        return value;
    }
    public static void main(String[] args) throws IOException {

     /*   File file = new File("E:\\MT1\\wekaRunner\\logNew22.txt");
        String fileName = "E:\\MT1\\wekaRunner\\wekaSRC6\\20train.arff";
        int times = 505;
        int modify_type = 0;//Integer.parseInt(args[1]);
        int modify_times = 5;//Integer.parseInt(args[2]);

        int[] tree = {-1,-1,-1,6,3,9,-1,-1,1};
        String testLine = "5,0,1,6,3,9,5,7,3,2";
        int predict_Result = 3;
        J48weka_Processor j48weka_processor = new J48weka_Processor();
        j48weka_processor.setTree(tree, predict_Result);
        j48weka_processor.setTestScore(j48weka_processor.testLine_Score(testLine));
        int[] PartitionNum = j48weka_processor.read_Train_Data(fileName);*/

/*                if(i==0||i==1)
                    times = 280;
                else if(i==2||i==3)
                    times = 1451;
                else if(i==4||i==5)
                    times = 69;
*/

       /* for(int j=205;j<times;j=j+10) {

            for(int i=0;i<6;i++) {

                for(int k=0;k<50;k++) {
                    FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner" +
                            "\\lib J48weka_Processor 20train.arff " + i + " " + j + "\n", true);
                    FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib " +
                            "weka.classifiers.trees.J48 -C 0.25 -M 3 -t 20train_after.arff -d model.model > tree1.txt\n", true);
                    FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib " +
                            "weka.classifiers.trees.J48 -p 10 -l model.model -T 20test.arff > "+i+"\\predict_"+i+"_"+j+"_"+k+".txt\n\n", true);

                }
                System.out.println(j);
            }

        }*/
        int modify_type = 0;//Integer.parseInt(args[1]);
        int modify_times = 5;//Integer.parseInt(args[2]);

        int[] tree = {-1,-1,-1,6,3,9,-1,-1,1};
        String testLine = "5,0,1,6,3,9,5,7,3,2";
        int predict_Result = 3;
        J48weka_Processor1 j48weka_processor1 = new J48weka_Processor1();
        j48weka_processor1.setTree(tree, predict_Result);
        j48weka_processor1.setTestScore(j48weka_processor1.testLine_Score(testLine));
        j48weka_processor1.read_Train_Data(args[0]);
        j48weka_processor1.modifyFile(args[0],modify_type,modify_times);
        //j48weka_processor.read_Train_Data("E:\\MT1\\wekaRunner\\wekaSRC\\20train.arff");
        //j48weka_processor.modifyFile("E:\\MT1\\wekaRunner\\wekaSRC\\20train.arff",5,3);
        //System.out.println(new J48weka_Processor().testLine_Score(tree,testLine));
        //j48weka_processor.analyzer_Score_List();
        //J48weka_Processor.array_Random(10,10);*/
    }

}
