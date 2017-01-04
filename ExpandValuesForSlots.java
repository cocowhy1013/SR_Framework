/**
 * Created by Huiyan Wang on 2017/1/4.
 */
public class ExpandValuesForSlots {
    private double[] valuesMerge;
    private double scale;//sum
    private double percent_start;
    private double percent_end;
    private double percent_slot;

    public void setValues(double[] values, double sc){
        valuesMerge = values;
        scale = sc;
    }
    public void setPercent(double start,double end,double slot){
        percent_start = start;
        percent_end = end;
        percent_slot = slot;
    }
    public double[] expandValues(){
        int len = (int)((percent_end-percent_start)/percent_slot)+1;
        double[] result = new double[len];
        double last_p = percent_start;
        double last_v = valuesMerge[0];
        int mv = 0;
        for(int i = 0; i < len; i++){
            if((int)(scale*(percent_start+i*percent_slot))==(int)(last_p*scale)) {
                result[i] = last_v;
                System.out.println("result "+i+":"+last_v);
            }
            else {
                System.out.println("result mv"+i+":"+mv);
                last_p = percent_start+i*percent_slot;
                System.out.println("last p :"+last_p);
                mv++;
                if(mv>=valuesMerge.length)
                    break;
                last_v = valuesMerge[mv];
                result[i] = last_v;
            }
        }
        for(int i=0;i<result.length;i++)
            System.out.println(result[i]+" ");
        return result;
    }

    public static void main(String[] args){

        ExpandValuesForSlots expandValuesForSlots = new ExpandValuesForSlots();
        expandValuesForSlots.setPercent(0.02,0.50,0.02);
        double[] a = {9.9, 19.9, 29.9,1,2,3,4,5,6,7};
        expandValuesForSlots.setValues(a,9);
        expandValuesForSlots.expandValues();

    }
}
