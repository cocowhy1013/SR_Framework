import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Coco on 2016/8/25.
 */
public class FileInputProcessor {

    private String trainfile;
    private String testfile;
    private String modelfile;
    FileInputProcessor(String trainfile1, String testfile1, String modelfile1){
        trainfile = trainfile1;
        testfile = testfile1;
        modelfile = modelfile1;
    }
    public String getTest(int number) throws IOException {//start with 1.2.3...
        List<String> lineslist = FileUtils.readLines(new File(testfile));
        File targetFile = new File(testfile.replace("testAll","test"));
        FileUtils.writeStringToFile(targetFile,"",false);

        int flag = 0;
        for(int i=0;i<lineslist.size();i++){
            if(flag == 0){
                FileUtils.writeStringToFile(targetFile,lineslist.get(i)+"\n",true);
            }
            if(lineslist.get(i).contains("@data")) {
                flag = 1;
                int data_size = lineslist.size() - i - 1;
                if (number > data_size)
                    System.out.println("out of size");
                else {
                    FileUtils.writeStringToFile(targetFile, lineslist.get(i + number)+"\n",true);


                    return lineslist.get(i+number);
                }
            }
        }

        return null;
    }


    public String getPathforTest(int number) throws IOException {
        String[] testline = getTest(number).split(",");
        int [] parts_Test = new int[testline.length];
        int [] result = new int[parts_Test.length];
        for(int i = 0;i<parts_Test.length;i++) {
            parts_Test[i] = Integer.parseInt(testline[i]);
            result[i] = -1;
        }
        List<String> treelines = FileUtils.readLines(new File(modelfile));
        int flag = 0;
        String start="";
        for(int i=0;i<treelines.size();i++){

            if(treelines.get(i).contains("J48 pruned tree"))
                flag = 1;
            if(flag == 0)
                continue;
            else{

                for(int j=i;j<treelines.size();j++){
                    String line = treelines.get(i);
                    if(line.isEmpty())
                        continue;
                    else if(line.startsWith(start)){
                        if(line.charAt(start.length())>'9'||
                                line.charAt(start.length())<'0')
                            continue;
                        String[] part1 = line.split(" = ");
                        part1[0] = part1[0].replaceAll(" ","");
                        part1[0] = part1[0].replaceAll("\\|","");
                        int feature_number = Integer.parseInt(part1[0]);

                        String[] part2 = part1[1].split(":");
                        int feature_value = Integer.parseInt(part2[0]);
                        if(parts_Test[feature_number]==feature_value) {
                            start = start + "|   ";
                            result[feature_number] = feature_value;
                            if(part1[1].contains(": ")){
                                String[] temp = part2[1].split("\\(");
                                temp[0] = temp[0].replaceAll(" ","");
                                result[result.length-1] = Integer.parseInt(temp[0]);
                                flag = 0;
                                break;
                            }
                        }

                    }
                    else
                        continue;
                    if(treelines.get(j).contains("Number of Leaves"))
                        break;

                }
            }

            if(treelines.get(i).contains("Number of Leaves"))
                break;
        }
        String resultLine = "";
        for(int i=0;i<result.length-1;i++)
            resultLine = resultLine+result[i]+",";
        resultLine = resultLine+"="+result[result.length-1];
        return resultLine;
    }

    public int getPredict(String result){
        String[] parts = result.split(",=");
        return Integer.parseInt(parts[1]);
    }
    public String gettestpath(String result){
        String[] parts = result.split(",=");

        return parts[0];
    }

    public static void main(String[] args) throws IOException {
        String trainingFile = "E:\\MT1\\wekaRunner\\FileTest\\20trainAll.arff";
        String testingFile = "E:\\MT1\\wekaRunner\\FileTest\\20testAll.arff";
        String modelFile = "E:\\MT1\\wekaRunner\\FileTest\\tree.txt";

        //String ProcessFile = "E:\\MT1\\wekaRunner\\wekaSRC\\J48weka_ProcessorOrigin.java";
        FileInputProcessor processor = new FileInputProcessor(trainingFile,testingFile,modelFile);
        System.out.println(processor.getTest(1).toString());
        String temp = processor.getPathforTest(1).toString();//treeline,=predictlabel
        System.out.println(processor.gettestpath(temp));
        System.out.println(processor.getPredict(temp));

        String ProcessFile = "E:\\MT1\\wekaRunner\\wekaSRC\\J48weka_ProcessorOld.java";
        List<String> lines = FileUtils.readLines(new File(ProcessFile));
        for(int i=0;i<lines.size();i++){
            if(lines.get(i).contains("########")){
                lines.set(i, lines.get(i).replace("########", processor.gettestpath(temp)));
            }
            else if(lines.get(i).contains("********")){
                lines.set(i, lines.get(i).replace("********", processor.getTest(1).toString()));
            }
            else if(lines.get(i).contains("????????")){
                lines.set(i,lines.get(i).replace("????????",processor.getPredict(temp)+""));
            }
        }
        FileUtils.writeLines(new File(ProcessFile.replace("Old.java",".java")),lines,false);

    }
}
