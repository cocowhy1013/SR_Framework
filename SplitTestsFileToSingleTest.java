import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by coco on 2016/5/9.
 */
public class SplitTestsFileToSingleTest {
    public static boolean makeDirs(String folder1) {
        String folderName = folder1;
        if (folderName == null || folderName.isEmpty()) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }
    public static void splitTestsFileToSingleTest(String testfile,int times) throws IOException {
        trainTXTtoCSV(testfile.replace("test.txt","train.txt"));
        File file = new File(testfile);
        List<String> lines = FileUtils.readLines(file);
        String logTitle = "0,1,2,3,4,5,6,7,8,9";//lines.get(0);
        String path = file.getPath();
        for(int i=1;i<lines.size();i++){
            makeDirs(path.replace(".txt","\\"+i));
            File output = new File(path.replace(".txt","\\"+i+"\\"+i+".csv"));
            FileUtils.writeStringToFile(output,"",false);//logOutputFile.
            FileUtils.writeStringToFile(output,logTitle+"\n",true);//logOutputFile.
            FileUtils.writeStringToFile(output,lines.get(i)+"\n",true);//logOutputFile.
            for(int j=0;j<times;j++) {
                DataModifier.modifyFileWithSingleTest(testfile.replace("test.txt", "train.csv"),
                        output.getPath(), 1, j, 1);
            }
        }
        System.out.println("Finish "+testfile);

    }
    public static void trainTXTtoCSV(String txtfile) throws IOException {
        File file = new File(txtfile);
        List<String> lines = FileUtils.readLines(file);
        File filecsv = new File(file.getPath().replace(".txt",".csv"));

        FileUtils.writeStringToFile(filecsv,"",false);//logOutputFile.
        FileUtils.writeStringToFile(filecsv,"0,1,2,3,4,5,6,7,8,9"+"\n",true);//logOutputFile.
        for(int i=1;i<lines.size();i++){
            FileUtils.writeStringToFile(filecsv,lines.get(i)+"\n",true);//logOutputFile.
        }
//        FileUtils.copyFile(file,filecsv);
    }
    public static void convertCSVtoARFF(String root,File batFile) throws IOException {
        System.out.println(root);
        File fileroot = new File(root);
        for(File file : fileroot.listFiles()) {
            if (file.isFile()) {
                //if(file.getName().contains("log"))
                //    file.delete();
                if (file.getName().endsWith(".csv")) {
                    FileUtils.writeStringToFile(batFile, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib weka.core.converters.CSVLoader "
                            + file.getPath() + " > " + file.getPath().replace(".csv", ".arff") + "\n", true);//logOutputFile.
                    //System.out.println(file.getPath());
                }
            } else if (file.isDirectory()) {
                convertCSVtoARFF(root + "\\" + file.getName(),batFile);
            }
        }
    }
    public static void convertARFFtoPredict(String root,File batFile) throws IOException {
        System.out.println(root);
        File fileroot = new File(root);
        for(File file : fileroot.listFiles()) {
            if (file.isFile()) {
                //if(file.getName().contains("log"))
                //    file.delete();
                if (file.getName().endsWith(".arff")&&file.getName().contains("train_modify")) {

                    int thisnumber = Integer.parseInt(file.getName().replace("20train_modify_","").replace(".arff",""));
                    if(thisnumber>20)
                        continue;

                    String modify_path = file.getPath();
                    String[] parts = modify_path.split("\\\\");
                    String testfile_path = file.getPath().replace("20train",parts[parts.length-2]);

                    FileUtils.writeStringToFile(batFile, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib -Xms32m -Xmx512m " +
                            "weka.classifiers.bayes.NaiveBayes -t " + file.getPath() +
                            " -i -k -d " + file.getPath().replace(".arff", ".model") + " -c last > nul\n", true);//logOutputFile.
                    FileUtils.writeStringToFile(batFile, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib -Xms32m -Xmx512m " +
                            "weka.classifiers.bayes.NaiveBayes -l " + file.getPath().replace(".arff", ".model") +
                            " -T " + testfile_path + " -c last > "
                            +file.getPath().replace(".arff", "result.txt")+"\n", true);
                    //System.out.println(file.getPath());
                }
            } else if (file.isDirectory()) {
                convertARFFtoPredict(root + "\\" + file.getName(),batFile);
            }
        }
    }
    public static void changeNumericAttribute(String root) throws IOException {
        System.out.println(root);
        File fileroot = new File(root);
        for (File file : fileroot.listFiles()) {
            if (file.isFile()) {
                //if(file.getName().contains("log"))
                //    file.delete();
                String modify_path = file.getPath();
                String[] parts = modify_path.split("\\\\");
                String testfile_path = file.getParent() + "\\" + parts[parts.length - 1].charAt(0) + ".arff";
                if (file.getName().endsWith(".arff") ) {
                    List<String> lines = FileUtils.readLines(file);
                    //File filecsv = new File(file.getPath().replace(".txt", ".csv"));
                    FileUtils.writeStringToFile(file, "", false);//logOutputFile.
                    for (int i = 0; i < lines.size(); i++) {
                        String modifyLine = lines.get(i).replace("numeric", "{0,1,2,3,4,5,6,7,8,9}");
                        FileUtils.writeStringToFile(file, modifyLine + "\n", true);//logOutputFile.
                    }
                }
            }
            else if (file.isDirectory()) {
                changeNumericAttribute(root + "\\" + file.getName());
            }
        }
    }

    public static void batGen_CSVtoARFF(String root) throws IOException {
        File batFile = new File(root+"\\logForARFF.txt");
        FileUtils.writeStringToFile(batFile,"",false);
        convertCSVtoARFF(root,batFile);
    }
    public static void batGen_ARFFtoPredict(String root) throws IOException {
        File batFile = new File(root+"\\runForResult.txt");
        FileUtils.writeStringToFile(batFile,"",false);
        convertARFFtoPredict(root, batFile);
    }
    public static void batGen_BeforeModified(String root)throws IOException{//F:\MTfinish\10
        File batFile = new File(root+"\\runBeforeModified.txt");
        FileUtils.writeStringToFile(batFile,"",false);
        String[] parts = root.split("\\\\");
        int number = Integer.parseInt(parts[parts.length-1]);
        File trainFile = new File(root+"\\"+number+"train.arff");
        for(File f:new File(root+"\\"+number+"test\\").listFiles()) {
            if(f.isDirectory()){
                FileUtils.writeStringToFile(batFile, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib -Xms32m -Xmx512m " +
                        "weka.classifiers.bayes.NaiveBayes -t " + trainFile.getPath() +
                        " -i -k -d " + trainFile.getPath().replace(".arff", f.getName()+".model") + " -c last  > nul\n", true);//logOutputFile.
                FileUtils.writeStringToFile(batFile, "java -Djava.ext.dirs=E:\\MT\\wekaRunner\\lib -Xms32m -Xmx512m " +
                        "weka.classifiers.bayes.NaiveBayes -l " + trainFile.getPath().replace(".arff", f.getName()+".model") +
                        " -T " + f.getPath()+"\\"+f.getName()+".arff" + " -c last > "
                        +trainFile.getPath().replace("train.arff", "test\\"+f.getName()+"result.txt")+"\n", true);
            }
        }
    }
    public static void splitTestFiles(String testFilePath) throws IOException {
        List<String> list = FileUtils.readLines(new File(testFilePath + "\\20test.arff"));
        int flag = -1;
        int attrnum = 0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).isEmpty()||list.get(i).contains("@")){
                flag = 0;
                attrnum++;

            }
            else flag = 1;
            if(flag == 1){
                int instance_no = i-attrnum;

                FileUtils.copyFile(new File(testFilePath + "\\20train.arff"),new File(testFilePath+"\\"+instance_no+"\\"+"20train.arff"));

                File testSingle = new File(testFilePath+"\\"+instance_no+"\\"+instance_no+".arff");
                FileUtils.writeStringToFile(testSingle,"",false);
                FileUtils.writeStringToFile(testSingle,"@relation 20test\n",true);
                FileUtils.writeStringToFile(testSingle,"\n",true);
                FileUtils.writeStringToFile(testSingle,"@attribute 0 {0,1,2,3,4,5,6,7,8,9}\n",true);
                FileUtils.writeStringToFile(testSingle,"@attribute 1 {0,1,2,3,4,5,6,7,8,9}\n",true);
                FileUtils.writeStringToFile(testSingle,"@attribute 2 {0,1,2,3,4,5,6,7,8,9}\n",true);
                FileUtils.writeStringToFile(testSingle,"@attribute 3 {0,1,2,3,4,5,6,7,8,9}\n",true);
                FileUtils.writeStringToFile(testSingle,"@attribute 4 {0,1,2,3,4,5,6,7,8,9}\n",true);
                FileUtils.writeStringToFile(testSingle,"@attribute 5 {0,1,2,3,4,5,6,7,8,9}\n",true);
                FileUtils.writeStringToFile(testSingle,"@attribute 6 {0,1,2,3,4,5,6,7,8,9}\n",true);
                FileUtils.writeStringToFile(testSingle,"@attribute 7 {0,1,2,3,4,5,6,7,8,9}\n",true);
                FileUtils.writeStringToFile(testSingle,"@attribute 8 {0,1,2,3,4,5,6,7,8,9}\n",true);
                FileUtils.writeStringToFile(testSingle,"@attribute 9 {0,1,2,3,4,5,6,7,8,9}\n",true);
                FileUtils.writeStringToFile(testSingle,"\n",true);
                FileUtils.writeStringToFile(testSingle,"@data\n",true);
                FileUtils.writeStringToFile(testSingle,list.get(i)+"\n",true);
            }
        }
    }
    public static void splitIntoTrainAndSingleTest(String source,int num) throws IOException {
        File sourceFile = new File(source);
        if(!sourceFile.exists())
            return;
        List<String> list = FileUtils.readLines(sourceFile);
        File targetTrain = new File(source.replace("trainAll",num+"trainAll"));
        File targetTest = new File(source.replace("trainAll",num+"testAll"));
        FileUtils.writeStringToFile(targetTrain, "",false);
        FileUtils.writeStringToFile(targetTest, "",false);
        //int flag = 0;
        int temp = 0;
        for(int i=0;i<list.size();i++){
            String line = list.get(i);
            //if(flag == 0){
            FileUtils.writeStringToFile(targetTrain, line+"\n",true);
            FileUtils.writeStringToFile(targetTest, line+"\n",true);
           // }
            if(line.contains("@data")) {
                //flag = 1;
                temp = i;
                break;
            }
        }
        for(int j = temp+1; j<list.size();j++){
            String line = list.get(j);
            if(j-temp == num)
                FileUtils.writeStringToFile(targetTest, line+"\n",true);
            else
                FileUtils.writeStringToFile(targetTrain, line+"\n",true);
        }
    }
    public static void main(String [] args) throws IOException {
        for(int num = 1; num<4177; num++)
            splitIntoTrainAndSingleTest("E:\\MT1\\UCI_data\\Split\\Abalone\\Abalone_trainAll.arff",num);
        //splitTestFiles("E:\\MT\\dataset\\dataset_NBC\\20");
        //for(int i=22;i<205;i++)
        //    FileUtil.createDir("E:\\MT\\dataset\\dataset_NBC\\20\\"+i);
          //  String root = "E:\\MTfinishMR2_20part4\\20";// = "F:\\MTfinish\\10";
        //batGen_BeforeModified(root);
        //batGen_ARFFtoPredict(root);
        //splitTestFiles(root);
       // root = "E:\\MTfinishMR2\\10";
       // batGen_BeforeModified(root);
        /*root = "F:\\MTfinish\\13";
        batGen_BeforeModified(root);
        batGen_ARFFtoPredict(root);

        root = "F:\\MTfinish\\14";
        batGen_BeforeModified(root);
        batGen_ARFFtoPredict(root);*/
        //batGen_CSVtoARFF(root);
        /*for(int i = 32;i<71;i++)
            changeNumericAttribute(root+"\\15test\\"+i);

        root = "F:\\MTfinish\\16";
        // batGen_ARFFtoPredict(root);
        //batGen_CSVtoARFF(root);
        changeNumericAttribute(root);
        root = "F:\\MTfinish\\17";
        // batGen_ARFFtoPredict(root);
        //batGen_CSVtoARFF(root);
        changeNumericAttribute(root);*/
       /* root = "F:\\MTfinish\\18";;
        // batGen_ARFFtoPredict(root);
        //batGen_CSVtoARFF(root);
        changeNumericAttribute(root);
        root = "F:\\MTfinish\\19";
        // batGen_ARFFtoPredict(root);
        //batGen_CSVtoARFF(root);
    changeNumericAttribute(root);*/
       /* splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\0\\0test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\1\\1test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\2\\2test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\3\\3test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\4\\4test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\10\\10test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\11\\11test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\12\\12test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\13\\13test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\14\\14test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\15\\15test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\16\\16test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\17\\17test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\18\\18test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\19\\19test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\20\\20test.txt",500);
        splitTestsFileToSingleTest("E:\\MT\\dataset\\dataset_NBC\\50\\50test.txt",500);*/
}

}
