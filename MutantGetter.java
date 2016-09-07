import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by coco on 2016/5/18.
 */
public class MutantGetter {
    private Map<String, String> op_path_mutants;
    private int num_of_mutants;
    private String classname;

    public MutantGetter() {
        num_of_mutants = 0;
        op_path_mutants = new HashMap<String, String>();
    }

    // get the mutation operator of specific mutant's class
    public String get_path(String op) {
        return op_path_mutants.get(op);
    }

    public Map<String, String> get_allpath() {
        return op_path_mutants;
    }

    public String get_classname() {
        return classname;
    }

    public String get_op(String path) {
        String[] parts = path.split("\\\\");
        String mutate_op = parts[parts.length - 2];
        //System.out.println(parts[parts.length - 1]);
        String[] name = (parts[parts.length - 1]).split("\\.");
        //System.out.println(name[0]);
        classname = name[0];
        return mutate_op;
    }
    public String get_opInstrument(String path) {
        String[] parts = path.split("\\\\");
        String mutate_op = parts[parts.length - 3];
        //System.out.println(parts[parts.length - 1]);
        String[] name = (parts[parts.length - 1]).split("\\.");
        //System.out.println(name[0]);
        classname = name[0];
        return mutate_op;
    }
    public void getMutantsClass(String rootPath) {
        // get operator and mutants' path information for further analysis
        File root = new File(rootPath);
        File[] files = root.listFiles();
        for (File file : files) {
            //System.out.println(file.getName());
            //System.out.println("---" + file.getPath());
            if (file.isDirectory())
                getMutantsClass(file.getAbsolutePath());
            if (file.getName().endsWith(".class")) {
                // System.out.println("***********"+file.getName());
                String classpath = file.getPath();
                String mutant_op = get_op(classpath);

                if (!mutant_op.equalsIgnoreCase("original")) {
                    //System.out.println("op:" + get_op(classpath));
                    op_path_mutants.put(mutant_op, classpath);
                    num_of_mutants++;
                }
                // System.out.println(get_op(classpath));
            }
        }

    }
    public void getMutantsClassSourceIn(String rootPath) {
        // get operator and mutants' path information for further analysis
        File root = new File(rootPath);
        File[] files = root.listFiles();
        for (File file : files) {
            //System.out.println(file.getName());
            //System.out.println("---" + file.getPath());
            if (file.isDirectory())
                getMutantsClassSourceIn(file.getAbsolutePath());
            if (file.getName().endsWith(".class")&&file.getPath().contains("instrument")) {
                // System.out.println("***********"+file.getName());
                String classpath = file.getPath();
                String mutant_op = get_opInstrument(classpath);

                if (!mutant_op.equalsIgnoreCase("original")) {
                    //System.out.println("op:" + get_opInstrument(classpath));
                    op_path_mutants.put(mutant_op, classpath);
                    num_of_mutants++;
                }
                // System.out.println(get_op(classpath));
            }
        }
    }
    public void getInstrumentClass(String beginString, String endString,
                                   String rootPath) {
        // get operator and mutants' path information for further analysis
        File root = new File(rootPath);
        File[] files = root.listFiles();
        for (File file : files) {
            //System.out.println(file.getName());
            //System.out.println("---" + file.getPath());
            if (file.isDirectory())
                getMutantsClass(file.getAbsolutePath());
            if (file.getName().endsWith(endString)
                    && file.getName().startsWith(beginString)) {
                // System.out.println("***********"+file.getName());
                String classpath = file.getPath();
                String mutant_op = get_op(classpath);

                if (!mutant_op.equalsIgnoreCase("original")) {
                    //System.out.println("op:" + get_op(classpath));
                    op_path_mutants.put(mutant_op, classpath);
                    num_of_mutants++;
                }
                // System.out.println(get_op(classpath));
            }
        }

    }

    public String returnSpecificClassPath(int number){
        int i = 0;
        String result = "";
        for (Map.Entry<String, String> entry : op_path_mutants.entrySet()) {
            if(i==number){
                result = entry.getValue();
                break;
            }
            i++;
        }
        return result;
    }
    public void display_oppath_map() {
        // display operator and its specific path information map
        System.out.println("in display");
        for (Map.Entry<String, String> entry : op_path_mutants.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
        }

    }
    public void replaceClassFile(String sourcePath, String targetPath) throws IOException {
        File file = new File(targetPath);
        if(file.exists())
            FileUtils.forceDelete(file);

        FileUtils.copyFile(new File(sourcePath),new File(targetPath));
    }

    public static void main(String[] args) throws IOException {

        String mutantRoot = args[0];//"E:\\Mutation\\result\\weka.classifiers.trees.J48";
        int mutantNumber = Integer.parseInt(args[1]);//"1");
        String originFile = args[2];//"E:\\MT\\J48.class";
        MutantGetter m = new MutantGetter();
        m.getMutantsClass(mutantRoot);
        Map<String, String> map = m.get_allpath();
        /*for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("!key= " + entry.getKey() + " and value= "+ entry.getValue());
        }*/
        //System.out.println(map.size());
        //System.out.println(map.size());
        //System.out.println(m.returnSpecificClassPath(2));

        if(mutantNumber<map.size())
            m.replaceClassFile(m.returnSpecificClassPath(mutantNumber),originFile);

    }

}

