import java.util.ArrayList;

/**
 * Created by Huiyan Wang on 2017/2/20.
 */
public class ConditionFeature {
    private double max;
    private double min;
    private boolean isMax_Include;
    private boolean isMin_Include;
    private ArrayList<Double> avodList;

    private boolean isInt;

    ConditionFeature(){
        avodList = new ArrayList<Double>();
        max = Double.MAX_VALUE;
        min = Double.MIN_VALUE;
    }
    public void setMaxAndMin(double maxT, boolean isMax_IncludeT, double minT,boolean isMin_IncludeT){
        max = maxT; isMax_Include = isMax_IncludeT;
        min = minT; isMin_Include = isMin_IncludeT;
    }

    public void setMax(double maxT, boolean isMax_IncludeT){
        max = maxT;
        isMax_Include = isMax_IncludeT;
    }
    public void setMin(double minT, boolean isMin_IncludeT){
        min = minT;
        isMin_Include = isMin_IncludeT;
    }
    public void addAvoidValue(double avoid){
        avodList.add(new Double(avoid));
    }

    public double getMax(){
        return max;
    }
    public boolean getMaxIncluded(){ return isMax_Include;}
    public double getMin(){
        return min;
    }
    public boolean getMinIncluded(){ return isMin_Include;}
    public String toString(){
        String result = "";
        if(isMin_Include)
            result = "[" + min + ",";
        else
            result = "(" + min + ",";
        if(isMax_Include)
            result = result + max + "]";
        else
            result = result + max + ")";
        return result;
    }
}
