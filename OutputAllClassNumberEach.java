import java.io.IOException;

/**
 * Created by Huiyan Wang on 2016/11/28.
 */
public class OutputAllClassNumberEach {
    public static void main(String [] args) throws IOException {
        String trainFile = "E:\\MT1\\SameChangeTest21\\20trainAll.arff";//args[0];
        String testFile = "E:\\MT1\\SameChangeTest21\\20testAll.arff";//args[1];
        String treeFile = "E:\\MT1\\SameChangeTest21\\tree.txt";//args[2];

        //retriver.displayList(retriver.getPathList(testNumber));
        for (int testNumber = 1; testNumber <= 200; testNumber++) {
            System.out.println("testNumber:"+testNumber+"-----------------------");
            RetriveTreePathForTest retriver = new RetriveTreePathForTest(trainFile,testFile,treeFile);
            retriver.getPathList(testNumber);
            //System.out.println();
            //System.out.println("Score"+retriver.caculateScore("0,9,9,0,0,0,9,0,0,1"));

            retriver.read_Train_Data(trainFile);

            //double max = retriver.getMaxScore();
            //double min = retriver.getMinScore();
            for(int modify_type = 0; modify_type<4;modify_type++) {
                System.out.println("modify_type:"+modify_type);
                if (modify_type == 1 || modify_type == 3) {
                    if (retriver.isGeneratable()) {
                        //;System.out.println("Successfully generated!");
                    } else
                        System.out.println("Cannot be generated!");
                } else
                    ;//retriver.modifyFile(trainFile, modify_type, modify_strength);
            }
        }
    }
}
