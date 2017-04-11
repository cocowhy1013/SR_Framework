/**
 * Created by Huiyan Wang on 2016/10/31.
 */

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Coco on 2016/7/22.
 */
public class Analyzer {

    private int predict_Expected = 2;

    public void returnRootAnalyze(String root) throws IOException {
        //File rootfile = new File(root);
        for(int r_type=0;r_type<6;r_type++) {
            for (int i = 0; i < 2000; i = i + 1) {
                int result = 0;
                int flag = 0;
                for (int j = 0; j < 50; j++) {
                    File file = new File(root+"\\"+r_type+"\\predict_"+r_type+"_"+i+"_"+j+".txt");

                    if(file.exists()) {
                        int predict = returnPredictValue(root + "\\" + r_type + "\\predict_" + r_type + "_" + i + "_" + j + ".txt");
                        //System.out.println(predict);
                        if (predict != predict_Expected)
                            result++;
                        flag = 1;
                    }
                    //else flag = 0;
                }
                if(flag==1)
                    System.out.println("r_type:"+r_type+" i:"+i+" result:"+result);
            }
        }
    }
    public void returnPredictALLFiles(String root) throws IOException {
        File rootFile = new File(root);
        for(int i=0;i<=5000;i++){
            if(i==0){
                File file = new File(root+"\\predict.txt");
                if(file.exists()){
                    System.out.println("expect: "+returnPredictValue2(file));
                }
                else
                    System.out.println("no result");
            }
            else {
                File file = new File(root + "\\predictAllcertain" + i + ".txt");
                if (file.exists()) {
                    System.out.println(returnPredictValue2(file));
                } else
                    ;//System.out.println("no result");
            }
        }
    }
    public int returnPredictValue2(File resultFile) throws IOException {
        //System.out.println(resultFile);
        List<String> linesList = FileUtils.readLines(resultFile);
        for(int i=0;i<linesList.size();i++){
            if(linesList.get(i).contains("inst#")){
                String lineResult = linesList.get(i+1);
                String[] parts = lineResult.split(" |:");
                int flag = 0;
                for(int j=0;j<parts.length;j++){
                    //System.out.println(parts[j]);
                    if(parts[j].isEmpty()||parts[j].equals(" "))
                        continue;
                    else if(flag!=4)
                        flag++;
                    else return Integer.parseInt(parts[j]);
                }
                //return Integer.parseInt(parts[4]);
            }
        }
        return -1;
    }
    public int returnPredictValue(String resultFile) throws IOException {
        //System.out.println(resultFile);
        List<String> linesList = FileUtils.readLines(new File(resultFile));
        for(int i=0;i<linesList.size();i++){
            if(linesList.get(i).contains("inst#")){
                String lineResult = linesList.get(i+1);
                String[] parts = lineResult.split(" |:");
                int flag = 0;
                for(int j=0;j<parts.length;j++){
                    //System.out.println(parts[j]);
                    if(parts[j].isEmpty()||parts[j].equals(" "))
                        continue;
                    else if(flag!=4)
                        flag++;
                    else return Integer.parseInt(parts[j]);
                }
                //return Integer.parseInt(parts[4]);
            }
        }
        return -1;
    }

    public static void main(String []args) throws IOException {
        //int i = new J48weka_analyzer().returnPredictValue("E:\\MT1\\" +
        //        "wekaRunner\\predict_5_5_31.txt");
        //System.out.println(i);
        new Analyzer().returnPredictALLFiles("E:\\MT1\\UCI_data\\Abalone\\Golden\\GeneInsQ\\result");
    }
}
