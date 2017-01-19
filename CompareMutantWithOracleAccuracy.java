import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huiyan Wang on 2016/11/30.
 */
public class CompareMutantWithOracleAccuracy {
    public static boolean compare(File file1, File file2) throws IOException {
        byte[] string1 = FileUtils.readFileToByteArray(file1);
        byte[] string2 = FileUtils.readFileToByteArray(file2);
        if(string1.length!=string2.length)
            return false;
        for(int i=0;i<string1.length;i++){
            if(string1[i]!=string2[i])
                return false;
        }
        return true;
    }
    public static void display(byte[] string){
        for(int i=0;i<string.length;i++){
            System.out.print(string[i]);
        }
    }
    public static int precision(File file) throws IOException {
        List<String> list = FileUtils.readLines(file);
        int wrongNumber = 0;
        int pre = 0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).contains("+"))
                wrongNumber++;
        }
        return  wrongNumber;
    }
    public static ArrayList<String> returnFiles(String root){
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(root);
        for(File f: file.listFiles()){
            if(f.isDirectory()){
                File[] classFile= f.listFiles();
                for(int i=0;i<classFile.length;i++){
                    if(classFile[i].getName().endsWith(".class"))
                        list.add(classFile[i].getPath());
                }
            }
        }
        return list;
    }
    public static void crossValidator(String root) throws IOException {

        ArrayList<String> list = returnFiles(root);
        int num = 0;
        boolean flag = true;
        for(int i=0; i <list.size();i++){
            for(int j=i+1;j<list.size();j++){
                num++;
                System.out.println(list.get(i)+"-----------"+list.get(j));
                flag = compare(new File(list.get(i)),new File(list.get(j)));
                System.out.println(flag);
                System.out.println();
            }
        }
        System.out.println("num:"+num);
        System.out.println("flag:"+flag);
    }

    public static void classNumberConvertMethod(String classNumberFile) throws IOException {
        String classNumberPIT = classNumberFile;
        classNumberFile.equals("00");
        File target = new File(classNumberPIT.replace(".txt","new.csv"));
        FileUtils.writeStringToFile(target,"",false);
        List<String> list = FileUtils.readLines(new File(classNumberPIT));
        for(int i=0;i<list.size();i++){
            String line = list.get(i);
            if(!line.startsWith("!key"))
                break;
            String details = line.split(" ")[1];
            String mutator = details.split("_")[0];
            String detailline = details.replace(mutator+"_","");
            System.out.println(mutator+","+i+","+detailline);
            FileUtils.writeStringToFile(target,mutator+","+i+","+detailline+"\n",true);
        }

    }
    public static void main(String args[]) throws IOException {
        /*String root = "E:\\MT1\\Result_ChangeTest\\MutantManual\\C45ModelSelection\\C45ModelSelectionWAOR1.class";
        String oracle = "E:\\MT1\\Result_ChangeTest\\MutantManual\\C45ModelSelection\\C45ModelSelection.class";
        System.out.println(compare(new File(root),new File(oracle)));*/

        //System.out.println(returnFiles("E:\\MT1\\Result_ChangeTest\\MutantManual\\C45ModelSelection").size());
        //System.out.println(returnFiles("E:\\MT1\\Result_ChangeTest\\MutantManual\\C45ModelSelection").toString());
        String root = "E:\\Dataset\\manualMutants\\selectTest\\EntropyBasedSplitCrit\\5";
        //File oracle = new File("E:\\MT1\\SameChangeTest21\\oracle.txt");
        File target = new File("E:\\Dataset\\manualMutants\\selectTest\\EntropyBasedSplitCrit\\5\\predictResult.csv");
        //classNumberConvertMethod(root+"\\C45ModelSelection_classNumber.txt");

        FileUtils.writeStringToFile(target,"",false);
        for(int i=-1;i<1000;i++){
            File file = new File(root+"\\"+"EntropyBasedSplitCrit"+i+"_predict_5.txt");
            if(file.exists()){
                //System.out.println(file.getName());
                //System.out.println("Compare whole output: "+compare(file,oracle));
                //System.out.println("Oracle precision: "+(1-precision(oracle)/200.0));
                //System.out.println(""+(1-precision(file)/200.0));
                FileUtils.writeStringToFile(target,file.getName()+","+(1-precision(file)/200.0)+"\n",true);

                System.out.println(1-precision(file)/200.0);
            }
        }
    }
}
