package DataFormat;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Huiyan Wang on 2017/4/11.
 */
public class ReadResultPValue {

    public static void main(String[] args) throws IOException {
        File p_valueFile = new File("E:\\MT1\\Result_NewDataset50\\所有数据\\随机数据" +
                "\\Goldenversion\\PathTest2_40_Pos_Result.txt");
        List<String> lines = FileUtils.readLines(p_valueFile);

        for(int a = 1;a <lines.size();a++){
            String line = lines.get(a);
            String[] parts = line.split(" ");

            int flag = 0;
            int i;
            for(i=0;i<parts.length;i++){
                if(parts[i].equals(" ")||parts[i].isEmpty())
                    continue;
                else if(flag == 0)
                        flag = 1;
                else
                    break;
            }
            System.out.println(parts[i]);
        }
    }
}
