
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by coco on 2016/5/16.
 */
public class ARFFresultAnalyzer {

    public static ArrayList<Double> predictResult(File file) throws IOException {

        int flag = 0;
        ArrayList<Double> resultArray = new ArrayList<Double>();
        //File file = new File(resultFilePath);
        List<String> lineList = FileUtils.readLines(file);
        for(int i=0;i<lineList.size();i++){
            //System.out.println(lineList.get(i));
            if(lineList.get(i).contains("<-- classified as")){
                flag = 1;
                continue;
            }
            if(flag == 1){
                String line = lineList.get(i);
                String[] parts = line.split(" ");
                int result = 0;
                for(int j=0;j<parts.length;j++){
                    //System.out.println("------"+parts[j]+"---"+j);
                    if(parts[j].length()==0)
                        continue;
                    if(parts[j].equals("|"))
                        break;
                    if(Integer.parseInt(parts[j])==1)
                        resultArray.add(new Double(result));
                    result ++;
                }
            }

        }
        return resultArray;
    }
    public static ArrayList<Double> predictResultUnderRoot(String resultFileRoot) throws IOException {
        //  Root: 1result.txt ......
        ArrayList<Double> resultList = new ArrayList<Double>();
        for(File f: (new File(resultFileRoot).listFiles())){
            if(f.getName().endsWith("result.txt")){
                //System.out.println(f.getName());
                ArrayList<Double> resultForSingleFile = predictResult(f);
                if(resultForSingleFile.size() == 0)
                    resultList.add(new Double(-1));
                else resultList.add(resultForSingleFile.get(0));
            }
        }
        return resultList;
    }
    public static void predictResultUnderRoot2(String resultFileRoot,File file) throws IOException {
        //  Root: 1result.txt ......
        ArrayList<Double> resultList = new ArrayList<Double>();
        for(File f: (new File(resultFileRoot).listFiles())){
            if(f.getName().endsWith("result.txt")){
                //System.out.println(f.getName());
                ArrayList<Double> resultForSingleFile = predictResult(f);
                if(resultForSingleFile.size() == 0)
                    resultList.add(new Double(-1));
                else resultList.add(resultForSingleFile.get(0));
                FileUtils.writeStringToFile(file, f.getName().replace("result.txt","="),true);
                FileUtils.writeStringToFile(file, String.valueOf(resultForSingleFile.get(0))+" ",true);

            }
        }
        //return resultList;
    }
    public static String toStringResult(ArrayList<Double> result){
        //System.out.println(result.toString().charAt(0));
        String stringResult = result.toString();
        return stringResult.substring(1,stringResult.length()-1);
    }

    public static String AnalyzerFileFurtherResult(File resultFile) throws IOException {
        //File file = new File();
        HashMap<Integer,Double> expectedResult = new HashMap<Integer, Double>();
        List<String> listLine = FileUtils.readLines(resultFile);
        //ArrayList<Integer> numList = new ArrayList<Integer>();
        int[] numList = new int[200];
        int length = 0;
        for(int i=0;i<numList.length;i++)
            numList[i]=0;
        if(listLine.size()<11)
            return -1+"";
        for(int i=0;i<listLine.size();i++){
            if(listLine.isEmpty())
                continue;
            String[]parts = listLine.get(i).split(" ");
            if(parts[0].endsWith("test")){
                for(int j=1;j<parts.length;j++){
                    String[]value = parts[j].split("=");
                    //System.out.println("j:"+j+" "+value[0]+"="+value[1]);
                    expectedResult.put(Integer.parseInt(value[0]), Double.parseDouble(value[1]));
                    //numList.add(j-1,0);
                }
            }
            else{
                //System.out.println(listLine.get(i));
                String[] expectParts = parts[0].split("\\\\");
                //System.out.println(parts[0]+"---"+expectParts.length);
                double expect = expectedResult.get(Integer.parseInt(expectParts[expectParts.length-1]));
                length = parts.length-1;
                for(int j=1;j<parts.length;j++){
                    //System.out.print(parts[j]);
                    if(parts[j].isEmpty())
                        continue;
                    double actual = Double.parseDouble(parts[j].replace(",",""));

                    if(actual-expect>1e-5&&actual>0){
                        numList[i-1]++;
                        //numList.add(i-1,numList.get(i-1)+1);
                        //System.out.println("i:"+i+" "+numList[i-1]);
                    }
                    else if(actual<0)
                        ;//return -1;

                }
            }

        }
        int max = 0;
        for(int i=0;i<numList.length;i++){
            if(numList[i]>max)
                max = numList[i];
        }
        return max+"|"+length;
    }
    public static void main(String[] args) throws IOException {
        File file = new File("F:\\WEKAResult_MR1\\1");
        for (File f : file.listFiles()) {
            //System.out.println(f.getName()+"---");
            if (f.getName().contains("analyzer")) {
                System.out.println(f.getName());
                String a = AnalyzerFileFurtherResult(f);
                if (!a.startsWith("0|")) {
                    //System.out.println();
                    System.out.println(f.getName()
                            + "-----result:" + a);
                }
            }

        }
    }
    /*public static void main(String []args) throws IOException {
        //SplitTestsFileToSingleTest.batGen_ARFFtoPredict("F:\\MTfinish\\0");
        //File file = new File("F:\\MTfinish\\0\\0test\\1");
        String root = args[0];
        File fileAnalyzer = new File(root+"\\analyzer"+args[1]+".txt");
        FileUtils.writeStringToFile(fileAnalyzer,"",false);

        FileUtils.writeStringToFile(fileAnalyzer, root + " ", true);
        predictResultUnderRoot2(root,fileAnalyzer);
        //String line1 = toStringResult(result1);
        FileUtils.writeStringToFile(fileAnalyzer, "\n", true);

        //FileUtils.writeStringToFile(fileAnalyzer, line1 + "\n", true);

        for(File f: (new File(root).listFiles())) {
            if(f.isDirectory()) {
                //System.out.print(f.getPath() + "---- ");
                //System.out.print(f.getPath() + "---- ");
                ArrayList<Double> result = predictResultUnderRoot(f.getPath());
                String line = toStringResult(result);
                FileUtils.writeStringToFile(fileAnalyzer, f.getPath() + " ", true);
                FileUtils.writeStringToFile(fileAnalyzer, line + "\n", true);
            }
        }
    }*/
}
