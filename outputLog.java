import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Coco on 2016/8/25.
 */
public class outputLog {
    /*
javac -Djava.ext.dirs=E:\MT\wekaRunner\lib DeleteRepo.java
java -Djava.ext.dirs=E:\MT\wekaRunner\lib DeleteRepo E:\MT1\FileTest5
javac -Djava.ext.dirs=E:\MT\wekaRunner\lib weka\classifiers\trees\J48.java

javac -Djava.ext.dirs=E:\MT\wekaRunner\lib FileInputProcessor.java
java -Djava.ext.dirs=E:\MT\wekaRunner\lib FileInputProcessor 1

javac -Djava.ext.dirs=E:\MT\wekaRunner\lib J48weka_Processor.java
javac -Djava.ext.dirs=E:\MT\wekaRunner\lib Analyzer.java

java -Djava.ext.dirs=E:\MT\wekaRunner\lib J48weka_Processor 20train.arff -1 -1 log.bat

java -Djava.ext.dirs=E:\MT\wekaRunner\lib weka.classifiers.trees.J48 -C 0.25 -M 3 -t 20trainAll.arff -d model.model > tree.txt
java -Djava.ext.dirs=E:\MT\wekaRunner\lib weka.classifiers.trees.J48 -p 10 -l model.model -T 20testAll.arff > predict.txt

call log.bat

java -Djava.ext.dirs=E:\MT\wekaRunner\lib Analyzer E:\\MT1\\wekaRunner\\FileTest > result1.txt
    * */
    public static void main(String [] args) throws IOException {
        File file = new File("E:\\MT1\\RunInitial5.bat");

        FileUtils.writeStringToFile(file,"",false);
        for(int i=160;i<200;i++) {

            FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib DeleteRepo.java\n", true);
            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib DeleteRepo E:\\MT1\\FileTest5\n", true);

            FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib weka\\classifiers\\trees\\J48.java\n", true);
            FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib FileInputProcessor.java\n", true);
            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib FileInputProcessor " + i + "\n", true);
            FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib J48weka_Processor.java\n", true);
            FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib Analyzer.java\n", true);
            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib J48weka_Processor 20train.arff -1 -1 log.bat\n", true);
            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib weka.classifiers.trees.J48 -C 0.25 -M 3 -t 20trainAll.arff -d model.model > tree.txt\n", true);
            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib weka.classifiers.trees.J48 -p 10 -l model.model -T 20testAll.arff > predict.txt\n", true);
            FileUtils.writeStringToFile(file, "call log.bat\n", true);
            FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib Analyzer E:\\\\MT1\\\\FileTest5 > result" + i + ".txt\n", true);
            FileUtils.writeStringToFile(file,"\n\n",true);
        }
    }
}
