import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huiyan Wang on 2016/12/10.
 */
public class ClassNumberExcelConverter {
    public static void main(String [] args) throws IOException {
        String classNumberPIT = "E:\\MT1\\Result_ChangeTest\\MUTANTresult\\PIT\\C45ModelSelection\\C45ModelSelection_classNumber.txt";
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
}
