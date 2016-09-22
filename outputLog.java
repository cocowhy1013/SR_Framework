import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
        File file = new File("E:\\MT1\\RunMutant5.bat");

        FileUtils.writeStringToFile(file,"",false);
        for(int mutantNumber = 280;mutantNumber<350;mutantNumber++) {

            for (int i = 1; i < 50; i++) {

                FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib DeleteRepo.java\n", true);
                FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib DeleteRepo E:\\MT1\\MutantTest5\n", true);

                FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib weka\\classifiers\\trees\\J48.java\n", true);

                FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib ReplaceOriginWithMutant.java\n");
                FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib ReplaceOriginWithMutant weka\\classifiers\\trees\\J48.java -1\n");

                FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib MutantGetter.java\n", true);
                FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib MutantGetter E:\\Mutation\\result\\weka.classifiers.trees.J48 "+mutantNumber+" weka\\classifiers\\trees\\J48.class\n", true);

                FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib FileInputProcessor.java\n", true);
                FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib FileInputProcessor " + i + "\n", true);

                FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib J48weka_Processor.java\n", true);
                FileUtils.writeStringToFile(file, "javac -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib Analyzer.java\n", true);

                FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib J48weka_Processor 20train.arff -1 -1 log.bat\n", true);
                FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib weka.classifiers.trees.J48 -C 0.25 -M 3 -t 20trainAll.arff -d model.model > tree.txt\n", true);
                FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib weka.classifiers.trees.J48 -p 10 -l model.model -T 20testAll.arff > predict.txt\n", true);
                FileUtils.writeStringToFile(file, "call log.bat\n", true);
                FileUtils.writeStringToFile(file, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib Analyzer E:\\\\MT1\\\\MutantTest5 > result_"+mutantNumber+"_" + i + ".txt\n", true);
                FileUtils.writeStringToFile(file, "\n\n", true);
            }
        }
    }
}
