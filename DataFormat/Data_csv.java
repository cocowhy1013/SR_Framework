package DataFormat;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huiyan Wang on 2017/1/5.
 */
public class Data_csv implements DataInterface{
    private boolean isHead = true; //true: first line is head names
    private List<String> dataList = new ArrayList<>();


    Data_csv(String sourceFile, boolean head){
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

    public void setHead(boolean head){
        isHead = head;
    }

    public List<String> getDataList(){
        return dataList;
    }

    /**
     *instance number is set from 1 to maxSize
     **/
    @Override
    public String getDataInstance(int instance_no) {



        if(isHead==true)
            instance_no++;
        int temp_no = 0;
        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).isEmpty())
                continue;
            else
                temp_no++;
            if(temp_no==instance_no)
                return dataList.get(i);
        }

        System.out.println("Exception: input data file out of size!");
        return getDataInstance(1);//return null;

    }

    /**
     *return data size from 1 to end
     **/
    @Override
    public int getDataSize() {

        List<String> list = dataList;
        int number =0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).isEmpty())
                continue;
            else number ++;
        }
        if(isHead == true)
            return --number;
        else return number;

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

        if(isHead){

            for(int i=0;i<dataList.size();i++) {
                if (dataList.get(i).isEmpty())
                    continue;
                else
                    try {
                        FileUtils.writeStringToFile(target,dataList.get(i)+"\n",false);
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }


    }
    public static void main(String [] args){
        Data_csv data_csv = new Data_csv("a.txt",true);
        //data_txt.setHead(false);
        System.out.println(data_csv.getDataSize());
        System.out.println(data_csv.getDataInstance(0));
        data_csv.generateTargetHead("b.txt");
    }
}
