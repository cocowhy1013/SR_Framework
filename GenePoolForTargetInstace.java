import DataFormat.Data_arff;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Huiyan Wang on 2017/2/13.
 */
public class GenePoolForTargetInstace {
    private ArrayList<TreeNodeExpression> pathNodeList;
    private HashMap<String, ConditionFeature> conditionPool;

    private int attribute_num;

    GenePoolForTargetInstace(){
        pathNodeList = new ArrayList<TreeNodeExpression>();
        conditionPool = new HashMap<String, ConditionFeature>();
    }
    public void setPathNodeList(ArrayList<TreeNodeExpression> pathNodeList1){
        pathNodeList = pathNodeList1;
    }
    public void initPathNodeListFromTXT(String txtpath){
        try {
            List<String> lines = FileUtils.readLines(new File(txtpath));
            for(String line: lines) {
                TreeNodeExpression treeNodeExpression = new TreeNodeExpression(line);
                pathNodeList.add(treeNodeExpression);
                treeNodeExpression.display();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void processAllNodes(){
        for(int i = 0; i<pathNodeList.size();i++){
            TreeNodeExpression node = pathNodeList.get(i);//Each node represents a single condition extracted from paths considered
            String feature_name = node.getAttributeName();
            //node.display();
            if(conditionPool.containsKey(feature_name)){
                ConditionFeature feature = conditionPool.get(feature_name);

                switch (node.getOperator()){
                    case ">": {
                        if (Double.parseDouble(node.getValueSplit()) - feature.getMin()> 1e-5) {//bigger than
                            feature.setMin(Double.parseDouble(node.getValueSplit()),false);
                        }
                        else if (Math.abs(Double.parseDouble(node.getValueSplit()) - feature.getMin())< 1e-5) {//equal
                            feature.setMin(Double.parseDouble(node.getValueSplit()),false);
                        }
                        break;
                    }
                    case ">=":{
                        if (Double.parseDouble(node.getValueSplit()) - feature.getMin()> 1e-5)
                            feature.setMin(Double.parseDouble(node.getValueSplit()),true);
                        break;
                    }
                    case "=":{
                        feature.setMin(Double.parseDouble(node.getValueSplit()),true);
                        feature.setMax(Double.parseDouble(node.getValueSplit()),true);
                        break;
                    }
                    case "<":{
                        if (Double.parseDouble(node.getValueSplit()) - feature.getMax()< 1e-5)
                            feature.setMax(Double.parseDouble(node.getValueSplit()),false);
                        else if (Math.abs(Double.parseDouble(node.getValueSplit()) - feature.getMax())< 1e-5) {//equal
                            feature.setMax(Double.parseDouble(node.getValueSplit()),false);
                        }
                        break;
                    }
                    case "<=":{
                        if (Double.parseDouble(node.getValueSplit()) - feature.getMax()< 1e-5)
                            feature.setMax(Double.parseDouble(node.getValueSplit()),true);
                        break;
                    }
                }
                conditionPool.put(feature_name,feature);
            }
            else{
                ConditionFeature feature = new ConditionFeature();
                switch (node.getOperator()){
                    case ">": {
                        if (Double.parseDouble(node.getValueSplit()) - feature.getMin()> 1e-5) {//bigger than
                            feature.setMin(Double.parseDouble(node.getValueSplit()),false);
                        }
                        else if (Math.abs(Double.parseDouble(node.getValueSplit()) - feature.getMin())< 1e-5) {//equal
                            feature.setMin(Double.parseDouble(node.getValueSplit()),false);
                        }
                        break;
                    }
                    case ">=":{
                        if (Double.parseDouble(node.getValueSplit()) - feature.getMin()> 1e-5)
                            feature.setMin(Double.parseDouble(node.getValueSplit()),true);
                        break;
                    }
                    case "=":{
                        feature.setMin(Double.parseDouble(node.getValueSplit()),true);
                        feature.setMax(Double.parseDouble(node.getValueSplit()),true);
                        break;
                    }
                    case "<":{
                        if (Double.parseDouble(node.getValueSplit()) - feature.getMax()< 1e-5)
                            feature.setMax(Double.parseDouble(node.getValueSplit()),false);
                        else if (Math.abs(Double.parseDouble(node.getValueSplit()) - feature.getMax())< 1e-5) {//equal
                            feature.setMax(Double.parseDouble(node.getValueSplit()),false);
                        }
                        break;
                    }
                    case "<=":{
                        if (Double.parseDouble(node.getValueSplit()) - feature.getMax()< 1e-5)
                            feature.setMax(Double.parseDouble(node.getValueSplit()),true);
                        break;
                    }
                }
                conditionPool.put(feature_name, feature);
            }
        }
    }

    public void scanDataFile(String trainFile){
        Data_arff data_arff = new Data_arff(trainFile,true);
        //data_txt.setHead(false);

        String line = data_arff.getDataInstance(1);
        String[] parts = line.split(",");
        attribute_num = parts.length-1;

        for(int i = 0;i<parts.length;i++){
            ConditionFeature feature = new ConditionFeature();
            feature.setMaxAndMin(Double.MIN_VALUE,true,Double.MAX_VALUE,true);
            conditionPool.put(i+"",feature);
        }
        for(int i=1;i<=data_arff.getDataSize();i++){
            //System.out.println(data_arff.getDataInstance(i));
            String linetemp = data_arff.getDataInstance(i);
            String []parttemp = linetemp.split(",");
            for(int j = 0;j<parttemp.length;j++){
                ConditionFeature feature = conditionPool.get(j+"");
                double value = Double.parseDouble(parttemp[j]);
                if(value<feature.getMin())
                    feature.setMin(value,true);
                if(value>feature.getMax())
                    feature.setMax(value,true);
                conditionPool.put(j+"",feature);
            }

        }
        //poolDisplay();
    }
    public String generateFullScoreInstance(ArrayList<TreeNodeExpression> treeNodeList, String trainFile, int pos_Neg,int[] isInteger){

        setPathNodeList(treeNodeList);
        scanDataFile(trainFile);
        processAllNodes();
        //poolDisplay();
        String lineFinal = "";
        String actual_Label = treeNodeList.get(treeNodeList.size()-1).getLabel();
        for(int i = 0;i < attribute_num; i++){
            ConditionFeature feature = conditionPool.get(i+"");
            String solve = solveValueUnderSingleCondition(feature, isInteger[i] == 0 ? false : true);
            lineFinal = lineFinal + solve +",";
        }
        ConditionFeature labelfeature = conditionPool.get(attribute_num+"");
        String label = solveValueUnderSingleCondition(labelfeature, isInteger[attribute_num] == 0 ? false : true);

        if( pos_Neg == 0 ){//To generate hundred percent negative instance
            while(label.equals(actual_Label))
                label = solveValueUnderSingleCondition(labelfeature, isInteger[attribute_num] == 0 ? false : true);
        }
        else if( pos_Neg == 2 ){
            label = actual_Label;
        }

        return lineFinal + label;
    }
    public String generateFromFeaturePool(boolean [] isIntForAllFeature){
        return null;
    }

    public String solveValueUnderSingleCondition(ConditionFeature feature, boolean isInteger){
        double max = feature.getMax();
        double min = feature.getMin();
        if(isInteger){
            if(!feature.getMinIncluded())
                min = feature.getMin() + 1;
            if(feature.getMaxIncluded())
                max = feature.getMax() + 1;
            return "" + (int)(Math.random()*(max - min) + min);
        }
        else{
            double a = (Math.random()*(max - min) + min);
            double result = ((double) ((int)(a * 10000)))/10000.0;
            return "" + result;
        }
    }
    public void poolDisplay(){
        System.out.println("Pool display: ");
        for(Map.Entry<String, ConditionFeature> feature: conditionPool.entrySet()){
            System.out.println(feature.getKey()+"----"+feature.getValue().toString());
        }
    }

    public static void main(String [] args){
        GenePoolForTargetInstace pool = new GenePoolForTargetInstace();
        pool.initPathNodeListFromTXT("E:\\path.txt");
        //pool.processAllNodes();
        pool.poolDisplay();

        GenePoolForTargetInstace pool2 = new GenePoolForTargetInstace();

        int [] a = {1,1,1,1,1,1,1,1,1,1};
        String result = pool2.generateFullScoreInstance(pool.pathNodeList,"E:\\Dataset\\1trainAll.arff",0,a);
        System.out.println();
        System.out.println(result);
    }
}
