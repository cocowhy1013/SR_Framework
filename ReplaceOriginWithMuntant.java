import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by coco on 2016/9/1.
 */
public class ReplaceOriginWithMuntant {

    public void replaceClassFile(String sourcePath, String targetPath) throws IOException {
        File file = new File(targetPath);
        if(file.exists())
            FileUtils.forceDelete(file);

        FileUtils.copyFile(new File(sourcePath),new File(targetPath));
    }
    public static void main(String [] args) throws IOException {

        int number = Integer.parseInt(args[1]);
        if (number >= 0) {
            MutantGetter m = new MutantGetter();
            m.getMutantsClass("E:\\Mutation\\result\\weka.classifiers.trees.J48");
            Map<String, String> map = m.get_allpath();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println("!key= " + entry.getKey() + " and value= " + entry.getValue());
            }
            System.out.println(map.size());
            if(number <map.size()) {
                //System.out.println(m.returnSpecificClassPath(2));
                String sourcepath = m.returnSpecificClassPath(number);
                String targetpath = args[0];

                ReplaceOriginWithMuntant replaceOriginWithMuntant = new ReplaceOriginWithMuntant();
                replaceOriginWithMuntant.replaceClassFile(sourcepath, targetpath);

                System.out.println(sourcepath);
            }
        }
    }
}
