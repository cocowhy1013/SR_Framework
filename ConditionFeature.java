import java.util.ArrayList;

/**
 * Created by Huiyan Wang on 2017/2/20.
 */
public class ConditionFeature {
    private double max;
    private double min;
    private boolean isMax_Include;
    private boolean isMin_Include;
    private ArrayList<String> avodList;

    private int IoDoS;//0: double, 1: int, 2: String

    ConditionFeature(){
        avodList = new ArrayList<String>();
        max = Double.MAX_VALUE;
        min = Double.MIN_VALUE;
    }

    public int getIoDoS(){
        return IoDoS;
    }
    public void clearAvoid(){
        avodList.clear();
    }
    public void addAvoidValue(String avoid){
        avodList.add(avoid);
    }
    public void deleteAvoidValue(String avoid){
        if(avodList.contains(avoid))
            avodList.remove(avoid);
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
    public void setIoDoS(int temp){
        IoDoS = temp;
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
        result = result + "{";
        for(int i=0;i<avodList.size();i++){
            result = result + avodList.get(i)+",";
        }
        result = result + "}";
        return result;
    }
    public ArrayList<String> getCandidates(){return avodList;}
    public String returnSingleCandidate(){
        int rand = (int)Math.random()*avodList.size();
        return avodList.get(rand);
    }
}
