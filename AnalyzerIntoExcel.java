import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Huiyan Wang on 2016/9/22.
 */
public class AnalyzerIntoExcel {

    public void processLotIntoExcel(String logFile, String condition, String excelFile) throws IOException {
        File source = new File(logFile);
        File target = new File(excelFile);

        FileUtils.writeStringToFile(target,"\n",false);
        int flag = 0;
        List<String> logLines = FileUtils.readLines(source);
        String output = "";
        for(int i = 0;i<logLines.size();i++){
            //if(flag==0&&!logLines.get(i).contains("{"))
            //    continue;
            String line = logLines.get(i);
            if(flag==0&&!line.contains(condition))
                continue;
            else {
                flag = 1;
            }
            if(line.contains("{")&&!line.contains(condition))
                flag = 0;

            if(flag == 1){
                System.out.println(line);
                if(line.contains("=====testNumber:")){

                    output = "" + line.replace("=====testNumber:","");
                }
                else if(line.contains("Negative:")){
                    output = output + "," + line.replace("Negative: ","");
                }
                else if(line.contains("Irrelevant:")){
                    output = output + "," + line.replace("Irrelevant: ","");
                }
                else if(line.contains("Positive:")){
                    output = output + "," + line.replace("Positive: ","");
                }
                else if(line.contains("Undecidable:")){
                    output = output + "," + line.replace("Undecidable: ","");
                    FileUtils.writeStringToFile(target,output+"\n",true);
                }

            }
        }
    }
    public static void main(String[] args) throws IOException {
        String logFile = "E:\\MT1\\ClassNumber.txt";
        String excelFile = "E:\\MT1\\Excel.csv";
        String condition = "{-1,-0.4,-0.3,0.3,0.4,1.0}";
        AnalyzerIntoExcel analyzerIntoExcel = new AnalyzerIntoExcel();
        analyzerIntoExcel.processLotIntoExcel(logFile,condition,excelFile);
    }
}
