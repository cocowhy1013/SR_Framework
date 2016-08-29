import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Coco on 2016/8/29.
 */
public class LogResultAnalyzer {
    private String result_CSV;
    private String[] rootName;
    LogResultAnalyzer(String result_CSV1, String[] rootName1) throws IOException {
        result_CSV = result_CSV1;
        rootName = rootName1;
        FileUtils.writeStringToFile(new File(result_CSV),"",false);
        FileUtils.writeStringToFile(new File(result_CSV),"Number,Operator,Times,Result/per\n", true);

    }
    public void rootProcessor() throws IOException {

        for(int i=0;i<rootName.length;i++) {

            File root = new File(rootName[i]);
            for (File f : root.listFiles()) {
                if (f.getName().contains("result") && f.getName().endsWith(".txt")) {
                    resultFileProcessor(f);
                }
            }
        }
    }
    public void resultFileProcessor(File file) throws IOException {
        File target = new File(result_CSV);

        String fileNumber = file.getName().
                replace("result", "").replace(".txt", "");

        List<String> lineList = FileUtils.readLines(file);
        for(int i=0;i<lineList.size();i++){
            String line = lineList.get(i);
            System.out.println(line);
            String[] part  = line.split(" |:");
            //System.out.println(part[0]+"-"+part[1]+"-"+part[2]+"-"+part[3]);
            //System.out.println(part.length);
            if(part.length==6) {
                FileUtils.writeStringToFile(target, fileNumber+",", true);
                FileUtils.writeStringToFile(target, part[1] + ",", true);
                FileUtils.writeStringToFile(target, part[3] + ",", true);
                FileUtils.writeStringToFile(target, part[5], true);
            }
            FileUtils.writeStringToFile(target, "\n", true);
        }
    }
    public static void main(String [] args) throws IOException {
        String[] rootList = {"E:\\MT1\\wekaRunner\\FileTest","E:\\MT1\\wekaRunner\\FileTest1"};
        LogResultAnalyzer analyzer = new LogResultAnalyzer("E:\\MT1\\wekaRunner\\FileTest\\final.txt"
        ,rootList);
       analyzer.rootProcessor();
       //analyzer.resultFileProcessor("E:\\MT1\\wekaRunner\\FileTest\\result1.txt");
    }
}
