import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huiyan Wang on 2016/11/21.
 */
public class MutantFormatForPIT {

    public static String getMutator(String line){
        //System.out.println(line);
        String[] mutator_parts = line.split("\\.");
        //System.out.println(mutator_parts.length);
        return mutator_parts[mutator_parts.length-1];
    }
    public static String getMethodName(String line){
        //System.out.println(line);
        String result = "";
        String[] descrip_parts = line.split(",");
        for(int i=0;i<descrip_parts.length;i++){
            String temp = descrip_parts[i];
            if(temp.contains("method=")){
                temp = temp.replace("method=","");
                result = temp;
            }
            if(temp.contains("lineNumber")){
                result = result + "_"+temp.replace("lineNumber=","line");
                break;
            }
        }
        result = result.replace(">","");
        result = result.replace("<","");
        result = result.replace(" ","");
        return result;
    }
    public static String getClassName(String filename){
        String[] parts = filename.split("_");
        return parts[parts.length-1].replace(".txt","");
    }

    public static boolean checkFileOrFolder(String folderName){

        File folder = new File(folderName);
        if(folder.exists()) {
            System.out.println("Exist!");
            return true;
        }
        else {
            System.out.println("Do not exist!");
            return false;
        }
    }
    public static boolean createFolder(String folderName){
        File f = new File(folderName);
            // create new folder
        f.mkdir();

        return true;
    }
    public static String test(String line ){
        line = line.replace(">","");
        line = line.replace("<","");
        line = line.replace(" ","");
        return line;
    }
    public static void main(String [] args) throws IOException {
        //System.out.println(test("E:\\mutantPIT\\J48\\Distribution\\NegateConditionalsMutator_ <init>_ line68_0"));

        String rootName = "E:\\mutantPIT\\J48\\";
        File root = new File(rootName);
        for(File file: root.listFiles()){
            if(file.getName().endsWith(".txt")&&!file.getName().equalsIgnoreCase("readme.txt")) {
                //System.out.println(file.getName());
                //System.out.println(getClassName(file.getName()));
                List<String> lines = FileUtils.readLines(file);
                //System.out.println(lines.get(1));
                //System.out.println(getMutator(lines.get(1)));
                //System.out.println(getMethodName(lines.get(2)));

                String className = getClassName(file.getName());
                String mutator = getMutator(lines.get(1));
                String methodNameLine = getMethodName(lines.get(2));

                if(!checkFileOrFolder(root+className))
                    createFolder(root+className);

                int i = 0;
                String sub_folder = rootName+className+"\\"+mutator+"_"+methodNameLine+"_"+i;
                System.out.println("Creating sub:"+sub_folder);
                while(checkFileOrFolder(sub_folder)){
                    i++;
                    sub_folder = rootName+className+"\\"+mutator+"_"+methodNameLine+"_"+i;
                }
                createFolder(sub_folder);

                FileUtils.copyFile(file,new File(sub_folder+"\\"+className+".txt"));
                FileUtils.copyFile(new File(file.getPath().replace(".txt",".class")),
                        new File(sub_folder+"\\"+className+".class"));


            }
        }
    }

}
