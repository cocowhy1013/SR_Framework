package DataFormat;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huiyan Wang on 2017/1/5.
 */
public class Data_arff implements DataInterface {
    private boolean isHead = true; //true: first line is head names
    private List<String> dataList = new ArrayList<>();

    public Data_arff(String sourceFile, boolean head){
        dataList.clear();
        try {
            dataList = FileUtils.readLines(new File(sourceFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        isHead = head;
    }
    public void refresh(String sourceFile, boolean head){
        dataList.clear();
        try {
            dataList = FileUtils.readLines(new File(sourceFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        isHead = head;
    }

    @Override
    public String getDataInstance(int instance_no) {


        int temp_no = 0;
        int flag = 0;
        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).isEmpty())
                continue;

            if(flag == 1){
                temp_no++;
                if(temp_no==instance_no)
                    return dataList.get(i);
            }
            else {
                if(dataList.get(i).equals("@data"))
                    flag = 1;
            }
        }

        System.out.println("Exception: input data file out of size!");
        return getDataInstance(1);//return null;

    }

    @Override
    public int getDataSize() {
        int temp_no = 0;
        int flag = 0;
        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).isEmpty())
                continue;

            if(flag == 1){
                temp_no++;

            }
            else {
                if(dataList.get(i).equals("@data"))
                    flag = 1;
            }
        }
        return temp_no;

    }
    @Override
    public void generateTargetHead(String targetFile) {
        File target = new File(targetFile);
        if(target.exists())
            target.delete();

        try {
            FileUtils.writeStringToFile(target,"",false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<dataList.size();i++){

            if(dataList.get(i).equals("@data")){
                try {
                    FileUtils.writeStringToFile(target,dataList.get(i)+"\n",true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            try {
                FileUtils.writeStringToFile(target,dataList.get(i)+"\n",true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args){
        Data_arff data_arff = new Data_arff("a.txt",true);
        //data_txt.setHead(false);
        System.out.println(data_arff.getDataSize());
        System.out.println(data_arff.getDataInstance(8));
        data_arff.generateTargetHead("b.txt");
    }
}
