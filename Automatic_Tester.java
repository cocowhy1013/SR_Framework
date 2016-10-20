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
    final double[] slot = {-1,-0.6,-0.2,0.2,0.6,1.0};
    private double maxScore;
    private double minScore;

    public double getMaxScore(){
        return maxScore;
    }
    public double getMinScore(){
        return minScore;
    }

    public boolean isGeneratable(){
        if(minScore<slot[3])
            return true;
        else return false;
    }
    public int[] read_Train_Data(String trainFilePath) throws IOException {
        List<String> lines= FileUtils.readLines(new File(trainFilePath));
        score_List = new ArrayList<Double>();
        int flag = 0;
        for(int i=0;i<lines.size();i++){
            if(lines.get(i).contains("@")||lines.get(i).isEmpty())
                continue;
            if(flag == 0){
                maxScore = generateMaxInstanceScore(lines.get(i));
                minScore = generateMinInstanceScore(lines.get(i));
                flag = 1;
            }
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
       /* 0: positive->negative
        1: positive->irrelevant
        2: negative->positive
        3: negative->irrelevant
        4: irrelevant->positive
        5: irrelevant->negative
        modify_strength: 0.1---1.0

        System.out.println("Negative: "+analyzerResult[0]);
        System.out.println("Irrelevant: "+analyzerResult[1]);
        System.out.println("Positive: "+analyzerResult[2]);
        System.out.println("Undecidable: "+analyzerResult[3]);
        */

        List<String> lines= FileUtils.readLines(new File(trainFilePath));

        if(modify_type==0||modify_type==1){
            int positive_Sum = analyzerResult[2];
            int target_num = 0;
            if(modify_type == 0)
                target_num = analyzerResult[0];
            else if(modify_type == 1)
                target_num = analyzerResult[1];

            int modify_num = 0;
            int[] modify_instance_Array;
            if(modify_strength<positive_Sum) {
                modify_instance_Array = array_Random(modify_strength, positive_Sum);
                modify_num = modify_strength;
            }
            else {
                modify_instance_Array = array_Random(positive_Sum, positive_Sum);
                modify_num = positive_Sum;
            }
            //Arrays.sort(modify_instance_Array);
            if(positive_Sum >= target_num) {
                int[] target_Array = array_Random(modify_num*target_num/positive_Sum,modify_num);
                int number = -1;
                int modify_k = -1;

                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                        continue;
                    double score = caculateScore(lines.get(i));
                    if (score < slot[4])//+0.8~1.0
                        continue;
                    else number++;
                    if (isExist(modify_instance_Array, number)) {
                        modify_k++;
                        if(isExist(target_Array,modify_k))
                            lines.set(i, modifyLine(lines.get(i), modify_type));
                        else
                            lines.set(i,"---");
                    }
                }
            }
            else{
                int number = -1;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                        continue;
                    double score = caculateScore(lines.get(i));
                    if (score < slot[4])//+0.8~1.0
                        continue;
                    else number++;
                    if (isExist(modify_instance_Array, number)) {
                        lines.set(i, modifyLine(lines.get(i), modify_type));
                    }
                }
                for(int j =0;j<modify_num*target_num/positive_Sum-modify_num;j++){
                    lines.add(selectCertainTypeLine(trainFilePath,modify_type));
                }
            }

        }
        else if(modify_type==2||modify_type==3){
            int Negative_Sum = analyzerResult[0];
            int target_num = 0;
            if(modify_type == 2)
                target_num = analyzerResult[2];
            else if(modify_type == 3)
                target_num = analyzerResult[1];

            int modify_num = 0;
            int[] modify_instance_Array;
            if(modify_strength<Negative_Sum) {
                modify_instance_Array = array_Random(modify_strength, Negative_Sum);
                modify_num = modify_strength;
            }
            else {
                modify_instance_Array = array_Random(Negative_Sum, Negative_Sum);
                modify_num = Negative_Sum;
            }
            //Arrays.sort(modify_instance_Array);
            if(Negative_Sum >= target_num) {
                int[] target_Array = array_Random(modify_num*target_num/Negative_Sum,modify_num);
                int number = -1;
                int modify_k = -1;

                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                        continue;
                    double score = caculateScore(lines.get(i));
                    if(score>slot[1])//-1.0~-0.8
                        continue;
                    else number++;
                    if (isExist(modify_instance_Array, number)) {
                        modify_k++;
                        if(isExist(target_Array,modify_k))
                            lines.set(i, modifyLine(lines.get(i), modify_type));
                        else
                            lines.set(i,"---");
                    }
                }
            }
            else{
                int number = -1;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                        continue;
                    double score = caculateScore(lines.get(i));
                    if(score>slot[1])//-1.0~-0.8
                        continue;
                    else number++;
                    if (isExist(modify_instance_Array, number)) {
                        lines.set(i, modifyLine(lines.get(i), modify_type));
                    }
                }
                for(int j =0;j<modify_num*target_num/Negative_Sum-modify_num;j++){
                    lines.add(selectCertainTypeLine(trainFilePath,modify_type));
                }
            }

        }
        else if(modify_type==4||modify_type==5){
            int IR_Sum = analyzerResult[1];
            int target_num = 0;
            if(modify_type == 4)
                target_num = analyzerResult[2];
            else if(modify_type == 5)
                target_num = analyzerResult[1];

            int modify_num = 0;
            int[] modify_instance_Array;
            if(modify_strength<IR_Sum) {
                modify_instance_Array = array_Random(modify_strength, IR_Sum);
                modify_num = modify_strength;
            }
            else {
                modify_instance_Array = array_Random(IR_Sum, IR_Sum);
                modify_num = IR_Sum;
            }
            //Arrays.sort(modify_instance_Array);
            if(IR_Sum >= target_num) {
                int[] target_Array = array_Random(modify_num*target_num/IR_Sum,modify_num);
                int number = -1;
                int modify_k = -1;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                        continue;
                    double score = caculateScore(lines.get(i));
                    if(score<=slot[2]||score>=slot[3])//-0.1~+0.1
                        continue;
                    else number++;
                    if (isExist(modify_instance_Array, number)) {
                        modify_k++;
                        if(isExist(target_Array,modify_k))
                            lines.set(i, modifyLine(lines.get(i), modify_type));
                        else
                            lines.set(i,"---");
                    }
                }
            }
            else{
                int number = -1;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                        continue;
                    double score = caculateScore(lines.get(i));
                    if(score<=slot[2]||score>=slot[3])//-0.1~+0.1
                        continue;
                    else number++;
                    if (isExist(modify_instance_Array, number)) {
                        lines.set(i, modifyLine(lines.get(i), modify_type));
                    }
                }
                for(int j =0;j<modify_num*target_num/IR_Sum-modify_num;j++){
                    lines.add(selectCertainTypeLine(trainFilePath,modify_type));
                }
            }

        }

        for(int k=0;k<lines.size();k++){
            if(lines.get(k).equals("---")) {
                lines.remove(k);
                k--;
            }
        }

        FileUtils.writeLines(new File(trainFilePath.replace(".arff","_after.arff")),lines,false);
    }
    public String selectCertainTypeLine(String trainFilePath,int modify_type) throws IOException {
        int[] modify_instance_Array;
        int certain = 1;
        List<String> lines= FileUtils.readLines(new File(trainFilePath));
        if(modify_type==2||modify_type==4) {
            int positive_Sum = analyzerResult[2];
            modify_instance_Array = array_Random(certain, positive_Sum);
            int number = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                    continue;
                double score = caculateScore(lines.get(i));
                if (score < slot[4])//+0.8~1.0
                    continue;
                else number++;
                if (isExist(modify_instance_Array, number)) {
                    return lines.get(i);
                }
            }
        }
        else if(modify_type==0||modify_type==5) {
            int negative_Sum = analyzerResult[0];
            modify_instance_Array = array_Random(certain, negative_Sum);
            int number = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                    continue;
                double score = caculateScore(lines.get(i));
                if(score>slot[1])//-1.0~-0.8
                    continue;
                else number++;
                if (isExist(modify_instance_Array, number)) {
                    return lines.get(i);
                }
            }
        }
        else if(modify_type==1||modify_type==3){
            int negative_Sum = analyzerResult[1];
            modify_instance_Array = array_Random(certain, negative_Sum);
            int number = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains("@") || lines.get(i).isEmpty())
                    continue;
                double score = caculateScore(lines.get(i));
                if(score<=slot[2]||score>=slot[3])//-0.1~+0.1
                    continue;
                else number++;
                if (isExist(modify_instance_Array, number)) {
                    return lines.get(i);
                }
            }
        }
        return null;
    }

    public void modifyFileOld(String trainFilePath, int modify_type,int modify_strength) throws IOException {
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
                modify_instance_Array = array_Random(modify_strength, negative_Sum);
            else
                modify_instance_Array = array_Random(negative_Sum,negative_Sum);
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
                modify_instance_Array = array_Random(modify_strength, negative_Sum);
            else
                modify_instance_Array = array_Random(negative_Sum, negative_Sum);
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
        FileUtils.writeLines(new File(trainFilePath.replace(".arff","_after.arff")),lines,false);
    }
    protected abstract double caculateScore(String line);

    protected abstract String modifyLine(String s, int modify_type);

    protected abstract double generateMaxInstanceScore(String line);

    protected abstract double generateMinInstanceScore(String line);


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
