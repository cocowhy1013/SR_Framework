/**
 * Created by Huiyan Wang on 2017/1/17.
 */
public class entropy {

    public static  double entropyC (double sum, double[] child){

        double result = 0.00;

        double all = 0.0;
        for(int i=0;i<child.length;i++){
            all = all + child[i];
            result = result - child[i]/sum*((Math.log(child[i]/sum))/Math.log(2.0));
            System.out.println(result);

        }
        if(all-sum<1e-5)
            System.out.println("result:"+result);
        else
            System.out.println("wrong");
        return result;
    }
    public static void main(String[] args){
        double D1 = 1059;
        double [] D1c = {503,556};

        entropyC(D1,D1c);


    }
}
