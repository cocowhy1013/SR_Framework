//package dm13;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataModifier {

	public String modifyPlus(String path, double[][] attributes, double[] labels, double possibility, int modify_time) throws IOException{
		int plus = 1;
		BufferedReader reader = new BufferedReader(new FileReader(path));
		//String[] instanceBefore = reader.readLine().split(","); // attributes info
		PrintWriter pw = new PrintWriter(new File(path.replace(".csv", "_modify"+modify_time+".csv")));

		String line = reader.readLine();
		pw.println(line);
		//System.out.println(line);

		while((line = reader.readLine()) != null){

			String[] parts = line.split(",");
			String target = "";
			for(int i=0;i<parts.length-1;i++){
				if(isAttributeExist(attributes,i,Double.parseDouble(parts[i]))==false
						&& isChosedToModify(possibility)){
					int modify_attribute = (int)Double.parseDouble(parts[i]) + plus;

					//System.out.println(modify_attribute+"!="+Double.parseDouble(parts[i]));
					target = target + modify_attribute +",";
				}
				else
					target = target + (int)Double.parseDouble(parts[i]) +",";
			}
			int instancelabel = (int)Double.parseDouble(parts[parts.length-1]);

			if(isLabelExist(labels,instancelabel)==false && isChosedToModify(possibility)){
				int modify_label =  (int)instancelabel + plus;
				//System.out.println(modify_label+"!="+instancelabel);

				target = target + modify_label;
			}
			else {
				target = target + (int)instancelabel;
				//System.out.println(instancelabel);
			}
			pw.println(target);
		}

		reader.close();
		pw.close();

		return path.replace(".csv", "_modify"+modify_time+".csv");

	}

	public boolean isLabelExist(double[] labels, double a){

		for(int i=0;i<labels.length;i++){
			if(a==labels[i])
				return true;
		}
		return false;
	}
	public boolean isAttributeExist(double[][] labels, int attribute_no, double a){

		for(int i=0;i<labels.length;i++){
			if(a==labels[i][attribute_no])
				return true;
		}
		return false;
	}
	public boolean isChosedToModify(double possibility){
		if((Math.random()<possibility))
			return true;
		else
			return false;
	}

	public String modifyRandom(String path, double[][] attributes, double[] labels, double possibility, int modify_time, int k,String targetFile) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(path));
		//String[] instanceBefore = reader.readLine().split(","); // attributes info
		String pathString = "";
		if(targetFile=="")
			pathString = path.replace(".csv", "_modify_"+modify_time+".csv");
		else
			pathString = targetFile.replace(".csv", "_modify_"+modify_time+".csv");
		if(targetFile=="")
			pathString = path.replace(".arff", "_modify_"+modify_time+".arff");
		else
			pathString = targetFile.replace(".arff", "_modify_"+modify_time+".arff");
		PrintWriter pw = new PrintWriter(new File(pathString));

		String line = reader.readLine();
		pw.println(line);
//		System.out.println(line);

		while((line = reader.readLine()) != null){

			if(line.contains("@")||line.isEmpty()){
				pw.println(line);
			}
			else {
				String[] parts = line.split(",");
				String target = "";
				for (int i = 0; i < parts.length - 1; i++) {
					if (isAttributeExist(attributes, i, Double.parseDouble(parts[i])) == false
							&& isChosedToModify(possibility)) {
						int modify_attribute = (int) (Math.random() * 8);
						while (isAttributeExist(attributes, i, modify_attribute) == true) {
							modify_attribute = (int) (Math.random() * 8);
						}
						//System.out.println(modify_attribute+"!="+Double.parseDouble(parts[i]));
						target = target + modify_attribute + ",";
					} else
						target = target + (int) Double.parseDouble(parts[i]) + ",";
				}
				int instancelabel = (int) Double.parseDouble(parts[parts.length - 1]);

				target = target + (int) instancelabel;

				pw.println(target);
			}
		}

		reader.close();
		pw.close();


		return pathString;
	}
	public static void modifyFileWithSingleTest(String trainFile, String testFile, double possibility, int number,int k) throws IOException{
		Scanner scan = new Scanner(new File(testFile));
		ArrayList<Double> result = new ArrayList<Double>();
		String line = scan.nextLine();//remove first line
		//line = scan.nextLine();
		while(scan.hasNextLine()){
				line=scan.nextLine();
				DataModifier modifier = new DataModifier();
			String targetFile = modifier.modifyRandomStep(trainFile,line, possibility,number,k,testFile);
			//System.out.println("-----"+);

		}
		scan.close();
	}
	public String modifyRandomStep(String trainFilePath, String testLine,double possibility, int modify_time,int k,String targetFile)
		//generate follow training files using MR1: complex
			throws IOException{
		String[] parts = testLine.split(",");
		double[][] attribute = new double[1][parts.length-1];

		for(int i=0;i<parts.length-1;i++)
			attribute[0][i] = Double.parseDouble(parts[i]);
		double[] label = new double[1];
		label[0] = Double.parseDouble(parts[parts.length-1]);

		DataModifier modifier = new DataModifier();
		return modifier.modifyRandom(trainFilePath,
				attribute, label, possibility,modify_time,k,targetFile);
	}

	public String modifyTrainForFollowUp_2(String trainFilePath, String testLine,int operator, int modify_time,int k) throws IOException {
		//Parameter: operator: 1 plus; 2 minus; 3 multiple; 4 divide(not sure)
		BufferedReader reader = new BufferedReader(new FileReader(trainFilePath));
		//String[] instanceBefore = reader.readLine().split(","); // attributes info
		PrintWriter pw = new PrintWriter(new File(trainFilePath.replace(".csv", "_modify"+modify_time+"_"+k+".csv")));

		String line = reader.readLine();
		pw.println(line);
//		System.out.println(line);

		while((line = reader.readLine()) != null){
			String[] parts = line.split(",");
			String target = "";
			int instancelabel = (int)Double.parseDouble(parts[parts.length-1]);
			for(int i=0;i<parts.length-1;i++){
				if(operator==0) {
					target = target + ((int) Double.parseDouble(parts[i]) + 2) + ",";
					instancelabel = instancelabel + 2;
				}
				else if (operator==1) {
					target = target + ((int) Double.parseDouble(parts[i]) - 2) + ",";
					instancelabel = instancelabel - 2;
				}
				else if(operator==2) {
					target = target + ((int) Double.parseDouble(parts[i]) * 2) + ",";
					instancelabel = instancelabel * 2;
				}
				else {
					target = target + ((int) Double.parseDouble(parts[i])) + ",";
					instancelabel = instancelabel;
				}
			}

			target = target + (int)instancelabel;
			pw.println(target);
		}

		reader.close();
		pw.close();

		return trainFilePath.replace(".csv", "_modify" + modify_time + "_" + k + ".csv");
	}
	public String modifyTrainForFollowUp_3(String trainFilePath, String testLine,int operator, int modify_time,int k) throws IOException {
		//Parameter: operator: 1 plus; 2 minus; 3 multiple; 4 divide(not sure)
		BufferedReader reader = new BufferedReader(new FileReader(trainFilePath));
		//String[] instanceBefore = reader.readLine().split(","); // attributes info
		PrintWriter pw = new PrintWriter(new File(trainFilePath.replace(".csv", "_modify"+modify_time+"_"+k+".csv")));

		String line = reader.readLine();
		pw.println(line);
//		System.out.println(line);

		while((line = reader.readLine()) != null){
			String[] parts = line.split(",");
			String target = "";
			int instancelabel = (int)Double.parseDouble(parts[parts.length-1]);
			for(int i=0;i<parts.length-1;i++){
				if(operator==0) {
					target = target + ((int) Double.parseDouble(parts[i]) + 2) + ",";
					instancelabel = instancelabel +2;
				}
				else if (operator==1) {
					target = target + ((int) Double.parseDouble(parts[i]) - 2) + ",";
					instancelabel = instancelabel - 2;
				}
				else if(operator==2) {
					target = target + ((int) Double.parseDouble(parts[i]) * 2) + ",";
					instancelabel = instancelabel * 2;
				}
				else {
					target = target + ((int) Double.parseDouble(parts[i])) + ",";
					instancelabel = instancelabel;
				}
			}

			target = target + (int)instancelabel;
			pw.println(target);
		}

		reader.close();
		pw.close();

		return trainFilePath.replace(".csv", "_modify"+modify_time+"_"+k+".csv");
	}
	public String modifyTrainForFollowUp_1(String trainFilePath, String testLine,double possibility, int modify_time,int k)
		//Exacly the same as Function: modifyRandomStep in DataModifier.java
			throws IOException{
		String[] parts = testLine.split(",");
		double[][] attribute = new double[1][parts.length-1];

		for(int i=0;i<parts.length-1;i++)
			attribute[0][i] = Double.parseDouble(parts[i]);
		double[] label = new double[1];
		label[0] = Double.parseDouble(parts[parts.length-1]);

		DataModifier modifier = new DataModifier();
		return modifier.modifyRandom(trainFilePath,
				attribute, label, possibility,modify_time,k,"");
	}

	public String getLineFromARFFFile(String testFilePath,int k) throws IOException {//k:1,2,3
		String testLine = "";
		List<String> line = FileUtils.readLines(new File(testFilePath));
		for(int i=0;i<line.size();i++){
			String temp = line.get(i);
			if(temp.contains("@data")&&i<line.size()-k){
				testLine = line.get(i+k);
			}
		}
		return testLine;
	}
	//2016-5-22
	public String modifyARFFTrainForFollowUp_1(String trainFilePath, String testFilePath,double possibility, int modify_time,int k)throws IOException{

		String testLine = getLineFromARFFFile(testFilePath,1);
		String[] parts = testLine.split(",");
		double[][] attribute = new double[1][parts.length-1];

		for(int i=0;i<parts.length-1;i++)
			attribute[0][i] = Double.parseDouble(parts[i]);
		double[] label = new double[1];
		label[0] = Double.parseDouble(parts[parts.length-1]);

		DataModifier modifier = new DataModifier();
		return modifier.modifyRandom(trainFilePath,
				attribute, label, possibility,modify_time,k,"");
	}
	public String modifyARFFTrainForFollowUp_2(String trainFilePath, int operator, int modify_time,int operator_num) throws IOException {
		//Parameter: operator: 1 plus; 2 minus; 3 multiple; 4 divide(not sure)
		BufferedReader reader = new BufferedReader(new FileReader(trainFilePath));
		//String[] instanceBefore = reader.readLine().split(","); // attributes info
		PrintWriter pw = new PrintWriter(new File(trainFilePath.replace(".arff", "_modify_"+modify_time+".arff")));

		String line = reader.readLine();
		pw.println(line);
//		System.out.println(line);

		while((line = reader.readLine()) != null){
			if(line.contains("@attribute")&&!line.contains("@attribute 9")) {
				String[] attribute_parts = line.split("\\{");
				//System.out.println(attribute_parts[0]+"=="+attribute_parts[1]+"==="+attribute_parts[1].substring(0,attribute_parts[1].length()-1));
				String[] value = attribute_parts[1].substring(0,attribute_parts[1].length()-1).split(",");
				String target = "";
				for(int i=0;i<value.length;i++){

					if(operator==0) {
						target = target + ((int) Double.parseDouble(value[i]) + operator_num) + ",";
						//instancelabel = instancelabel + operator_num;
					}
					else if (operator==1) {
						target = target + ((int) Double.parseDouble(value[i]) - operator_num) + ",";
						//instancelabel = instancelabel - operator_num;
					}
					else if(operator==2) {
						target = target + ((int) Double.parseDouble(value[i]) * operator_num) + ",";
						//instancelabel = instancelabel * operator_num;
					}
					else {
						target = target + ((int) Double.parseDouble(value[i])) + ",";
						//instancelabel = instancelabel;
					}
				}
				line = attribute_parts[0]+"{"+target.substring(0,target.length()-1)+"}";
				pw.println(line);
				continue;
			}
			else if(line.isEmpty()||line.contains("@")){
				pw.println(line);
				continue;
			}
			String[] parts = line.split(",");
			String target = "";
			int instancelabel = (int)Double.parseDouble(parts[parts.length-1]);
			for(int i=0;i<parts.length-1;i++){
				if(operator==0) {
					target = target + ((int) Double.parseDouble(parts[i]) + operator_num) + ",";
					//instancelabel = instancelabel + operator_num;
				}
				else if (operator==1) {
					target = target + ((int) Double.parseDouble(parts[i]) - operator_num) + ",";
					//instancelabel = instancelabel - operator_num;
				}
				else if(operator==2) {
					target = target + ((int) Double.parseDouble(parts[i]) * operator_num) + ",";
					//instancelabel = instancelabel * operator_num;
				}
				else {
					target = target + ((int) Double.parseDouble(parts[i])) + ",";
					//instancelabel = instancelabel;
				}
			}

			target = target + (int)instancelabel;
			pw.println(target);
		}

		reader.close();
		pw.close();

		return trainFilePath.replace(".arff", "_modify" + modify_time +".arff");
	}

	public static void testFileConvert(String testFile,String testFile2) throws IOException {

		FileUtils.writeLines(new File(testFile2),FileUtils.readLines(new File(testFile)));
	}
	public static void main(String[] args) throws IOException {


		//modifier.modifyARFFTrainForFollowUp_2("E:\\MTfinishMR2\\20train.arff", 1, 1, 1);

		//1,2,3,4,5,6,7,0

		for(int k=0;k<10;k++) {
			for (int j = 0; j < 200; j++) {
				for (int i = 0; i < 500; i++) {
					int operator = (int) (Math.random() * 3);
					int operator_num = (int) (Math.random() * 500 + 1);
					DataModifier modifier = new DataModifier();
					modifier.modifyARFFTrainForFollowUp_1("E:\\MTfinishMR2\\DATA20\\20test" + k + "\\" + j + "\\20train.arff", "E:\\MTfinishMR2\\DATA20\\20test" + k + "\\" + j + "\\" + j + ".arff", 0.9, i, 0);

					modifier.testFileConvert("E:\\MTfinishMR2\\DATA20\\20test" + k + "\\" + j + "\\" + j + ".arff", "E:\\MTfinishMR2\\DATA20\\20test" + k + "\\" + j + "\\" + j + "_modify_" + i + ".arff");
					//modifier.modifyARFFTrainForFollowUp_2("E:\\MTfinishMR2_20part1\\20\\20test9\\"+j+"\\20train.arff", operator, i, operator_num);
					//modifier.modifyARFFTrainForFollowUp_2("E:\\MTfinishMR2_20part1\\20\\20test9\\"+j+"\\"+j+".arff", operator, i, operator_num);

				}
				System.out.println(k+":"+j);
			}

		}

	}
}
