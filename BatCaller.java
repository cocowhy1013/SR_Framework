/**
 * Created by coco on 2016/5/18.
 */
import java.io.IOException;
import java.io.InputStream;


public class BatCaller {


    public static void main(String args[]){
        callCmd("E:\\MT\\wekaRunner\\wekaBIN\\runForResult0_10.bat");
    }
    public static void  callCmd(String locationCmd){
        try {
            Process child = Runtime.getRuntime().exec("cmd.exe /C start "+locationCmd);
            InputStream in = child.getInputStream();
            int c;
            while ((c = in.read()) != -1) {
            }
            in.close();
            try {
                child.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}