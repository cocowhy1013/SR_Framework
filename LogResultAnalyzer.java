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
                if (f.getName().startsWith("result") && f.getName().endsWith(".txt")) {
                    resultFileProcessor(f);
                }
            }
        }
    }
    public void resultFileProcessor(File file) throws IOException {
        File target = new File(result_CSV);
        System.out.println(file.getPath());

        if(!file.getName().contains("result"))
            return;
        String fileNumber = file.getName().
                replace("result", "").replace(".txt", "");

        String r_type = "";
        List<String> lineList = FileUtils.readLines(file);
        for(int i=0;i<lineList.size();i++){
            String line = lineList.get(i);
            //System.out.println(line);
            String[] part  = line.split(" |:");


            //System.out.println(part[0]+"-"+part[1]+"-"+part[2]+"-"+part[3]);
            //System.out.println(part.length);
            if(part.length==6) {
                if(part[1].equals(r_type)){
                    FileUtils.writeStringToFile(target, part[5]+",", true);
                }
                else {
                    //if(!r_type.isEmpty())
                    FileUtils.writeStringToFile(target, "\n", true);
                    FileUtils.writeStringToFile(target, fileNumber+",", true);
                    FileUtils.writeStringToFile(target, part[1] + ",", true);
                    FileUtils.writeStringToFile(target, part[3] + ",", true);
                    FileUtils.writeStringToFile(target, part[5]+",", true);
                    r_type = part[1];
                }
            }
        }
    }
    public static void main(String [] args) throws IOException {
        String[] rootList = {"E:\\MT1\\Result_NewDataset50\\MutantResult\\ConditionCoverNoMerge_NewHigh\\mutant1"};
        LogResultAnalyzer analyzer = new LogResultAnalyzer("E:\\MT1\\Result_NewDataset50\\MutantResult\\ConditionCoverNoMerge_NewHigh\\mutant1\\finalAll.csv"
        ,rootList);
       analyzer.rootProcessor();
       //analyzer.resultFileProcessor("E:\\MT1\\wekaRunner\\FileTest\\result1.txt");
    }
}
