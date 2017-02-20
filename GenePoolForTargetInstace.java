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
            node.display();
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
                    case "==":{
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
                    case "==":{
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
    public String generateFromFeaturePool(){
        return null;
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
        pool.processAllNodes();
        pool.poolDisplay();
    }
}
