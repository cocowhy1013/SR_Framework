import DecisionTree.J48weka_Processor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by coco on 2016/9/6.
 */
public abstract class Automatic_Tester {
    private int predict_Array;
    private int testDataLength;

    private int[] tree_Executed;
    private ArrayList<Double> score_List;
    private int score_Test;
    private int[] analyzerResult;
    private int labelRange = 10;
    final double[] slot = {-1,-0.8,-0.1,0.1,0.8,1.0};

    public int[] read_Train_Data(String trainFilePath) throws IOException {
        List<String> lines= FileUtils.readLines(new File(trainFilePath));
        score_List = new ArrayList<Double>();
        for(int i=0;i<lines.size();i++){
            if(lines.get(i).contains("@")||lines.get(i).isEmpty())
                continue;
            double score = caculateScore(lines.get(i));
            //System.out.println(score);
            score_List.add(score);

        }
        return analyzer_Score_List();
    }

    protected int[] analyzer_Score_List(){
        analyzerResult = new int[4];

        for(int i=0;i<score_List.size();i++){
            // System.out.println("--"+score_Test);
            double temp = score_List.get(i);
            if(temp>=slot[0]&&temp<=slot[1])
                analyzerResult[0]++;
            else if(temp>=slot[2]&&temp<=slot[3])
                analyzerResult[1]++;
            else if(temp>=slot[4]&&temp<=slot[5])
                analyzerResult[2]++;
            else
                analyzerResult[3]++;
        }
        System.out.println("Negative: "+analyzerResult[0]);
        System.out.println("Irrelevant: "+analyzerResult[1]);
        System.out.println("Positive: "+analyzerResult[2]);
        System.out.println("Undecidable: "+analyzerResult[3]);
        return analyzerResult;
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
                modify_instance_Array = array_Random(modify_strength, positive_Sum);
            else
                modify_instance_Array = array_Random(positive_Sum,positive_Sum);
            //Arrays.sort(modify_instance_Array);
            int number = -1;
            for(int i=0;i<lines.size();i++) {
                if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                    continue;
                double score = caculateScore(lines.get(i));
                if(score<slot[4])//+0.8~1.0
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
                double score = caculateScore(lines.get(i));
                if(score>slot[1])//-1.0~-0.8
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
                double score = caculateScore(lines.get(i));
                if(score<=slot[2]||score>=slot[3])//-0.1~+0.1
                    continue;
                else number++;
                if(isExist(modify_instance_Array,number)){
                    lines.set(i,modifyLine(lines.get(i),modify_type));
                }
            }
        }
        FileUtils.writeLines(new File(trainFilePath.replace(".arff","_after1.arff")),lines,false);
    }
    public abstract double caculateScore(String line);

    protected abstract String modifyLine(String s, int modify_type);

    public boolean isExist(int[] array, int value){
        for(int i=0;i<array.length;i++){
            if(array[i]==value)
                return true;
        }
        return false;
    }
    public static void main(String[] args) throws IOException {
        Automatic_Tester tester;

    }

}
