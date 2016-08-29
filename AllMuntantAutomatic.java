import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by coco on 2016/5/18.
 */
public class AllMuntantAutomatic {
    public static void main(String[] args) throws IOException {
        int number = 20;
        File batRun = new File("E:\\MT\\wekaRunner\\wekaBIN_1\\run"+number+"_MTfinishMR2part.bat");
        FileUtils.writeStringToFile(batRun, "", false);
        MutantGetter m = new MutantGetter();
        m.getMutantsClass("E:/Mutation/result/weka.classifiers.bayes.NaiveBayes");
        Map<String, String> map = m.get_allpath();
        int number1 = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String mutantFile = m.returnSpecificClassPath(number1);
            FileUtils.writeStringToFile(batRun,"javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib MutantBatRunner" +
                    ".java\n",true);
            FileUtils.writeStringToFile(batRun,"java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib -Xms32m -Xmx512m MutantBatRunner" +
                    " "+mutantFile+"\n",true);
            FileUtils.writeStringToFile(batRun,"call runBeforeModified2_"+number+"part1.bat\n",true);
            FileUtils.writeStringToFile(batRun,"call runForResult2_"+number+"part1.bat\n",true);
            FileUtils.writeStringToFile(batRun,"javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib ARFFresultAnalyzer.java\n",true);
            FileUtils.writeStringToFile(batRun,"java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib -Xms32m -Xmx512m ARFFresultAnalyzer " +
                    "E:\\MTfinishMR2_20part1\\"+number+"\\"+number+"test "+number1+"\n",true);
            number1 ++;
            FileUtils.writeStringToFile(batRun,"\n",true);
        }
    }
}
