/**
 * Created by coco on 2016/5/5.
 */
public class cmdRunner {
    public void cmdRunnerTest(){

    }
    public static void main(String [] args){
        Runtime r=Runtime.getRuntime();
        Process p=null;
        try{
        String s="cmd /c start javac ";
        p=r.exec(s);
    }catch(Exception e){
        System.out.println("错误:"+e.getMessage());
        e.printStackTrace();
    }
    }
}

