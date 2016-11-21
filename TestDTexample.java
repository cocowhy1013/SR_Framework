/**
 * Created by Coco on 2016/10/12.
 */
public class TestDTexample {
    public static double entropy(double a,double b){
        double result = -(a/(a+b))*Math.log(a/(a+b))/Math.log(2.0)
                -(b/(a+b))*Math.log(b/(a+b))/Math.log(2.0);
        return result;
    }
    public static void main(String[] args){
        System.out.println(TestDTexample.entropy(1,1));
    }
}
