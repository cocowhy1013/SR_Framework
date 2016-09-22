package DecisionTree;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Coco on 2016/7/22.
 */
public class J48weka_analyzer {

    private int predict_Expected = 3;

    public void returnRootAnalyze(String root) throws IOException {
        //File rootfile = new File(root);
        for(int r_type=0;r_type<6;r_type++) {
            for (int i = 5; i < 190; i = i + 10) {
                int result = 0;
                for (int j = 0; j < 50; j++) {
                    int predict = returnPredictValue(root+"\\"+r_type+"\\predict_"+r_type+"_"+i+"_"+j+".txt");
                    if(predict!=predict_Expected)
                        result++;
                }
                System.out.println("r_type:"+r_type+" i:"+i+" result:"+result);
            }
        }
    }
    public int returnPredictValue(String resultFile) throws IOException {
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
        int i = new J48weka_analyzer().returnPredictValue("E:\\MT1\\OriginTest\\5\\predict_5_0_0.txt");
        System.out.println(i);
        //new J48weka_analyzer().returnRootAnalyze(args[0]);
    }
}
