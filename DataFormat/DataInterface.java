package DataFormat;

/**
 * Created by Huiyan Wang on 2017/1/5.
 */
public interface DataInterface {

    public String getDataInstance(int instance_no) ;

    public int getDataSize();

    public void generateTargetHead(String targetFile);

}
