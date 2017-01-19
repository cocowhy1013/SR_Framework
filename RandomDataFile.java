import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RandomDataFile {

	public static void selectData(String source, int target_instancesum,int allsum) throws IOException {
		List<String> list = FileUtils.readLines(new File(source));
		File target = new File(source.replace(".arff","_selectTest.arff"));
		int [] array = Automatic_Tester.array_Random(target_instancesum,allsum);
		FileUtils.writeStringToFile(target,"",false);
		for(int i=0;i<array.length;i++)
			System.out.print(array[i]+" ");
		//System.out.println(array.toString());
		System.out.println();
		for(int i=0;i<list.size();i++){
			FileUtils.writeStringToFile(target,list.get(i)+"\n",true);

			if(list.get(i).equals("@data")) {
				for(int j=0;j<200;j++){
					System.out.println(j+":"+list.get(i+array[j]+1));
					FileUtils.writeStringToFile(target,list.get(i+array[j]+1)+"\n",true);
				}
				break;
			}
		}


	}

	public static String selectIns(String source, int num) throws IOException {
		List<String> list = FileUtils.readLines(new File(source));

		for(int i=0;i<list.size();i++){
			//FileUtils.writeStringToFile(target,list.get(i)+"\n",true);

			if(list.get(i).equals("@data")&&i+num<list.size()) {
				return list.get(i+num);
			}
		}
		return null;
	}
	public static void randomFile(String filepath,int instance,int dataset) throws IOException {
		File file = new File(filepath);

		//PrintWriter pw = new PrintWriter();
		//int instance = 1000;

		FileUtils.writeStringToFile(file,"",false);
		FileUtils.writeStringToFile(file,"@relation "+dataset+"test\n",true);
		FileUtils.writeStringToFile(file,"\n",true);
		FileUtils.writeStringToFile(file,"@attribute 0 real\n",true);
		FileUtils.writeStringToFile(file,"@attribute 1 real\n",true);
		FileUtils.writeStringToFile(file,"@attribute 2 real\n",true);
		FileUtils.writeStringToFile(file,"@attribute 3 real\n",true);
		FileUtils.writeStringToFile(file,"@attribute 4 real\n",true);
		FileUtils.writeStringToFile(file,"@attribute 5 real\n",true);
		FileUtils.writeStringToFile(file,"@attribute 6 real\n",true);
		FileUtils.writeStringToFile(file,"@attribute 7 real\n",true);
		FileUtils.writeStringToFile(file,"@attribute 8 real\n",true);
		FileUtils.writeStringToFile(file,"@attribute 9 {0,1,2,3,4,5,6,7,8,9}\n",true);
		FileUtils.writeStringToFile(file,"\n",true);
		FileUtils.writeStringToFile(file,"@data\n",true);
		//FileUtils.writeStringToFile(testSingle,list.get(i)+"\n",true);

		int attribute = 9;//without label
		int[] attr_scale = {10,10,10,10,10,10,10,10,10,10};
		int label_scale = 5;
		String line = "";
	/*	for(int k = 0; k < attribute+1;k++){
			if (k != attribute)
				line = line + 1 + ",";
			else 
				line = line + 1;
		}
		pw.println(line);
	*/
		for(int i = 0; i < instance; i++){
			line = "";
			for(int j = 0; j < attribute+1; j++){
				if (j != attribute)
					line = line + (int)(Math.random()*attr_scale[j]) + ",";
				else 
					line = line + (int)(Math.random()*label_scale);
			}
			FileUtils.writeStringToFile(file,line+"\n",true);
		}

	} 

	public static void main(String[] args) throws IOException {
		for(int i=1;i<=50;i++)
			selectData("E://Dataset//"+i+"trainAll.arff",200,2000);

		//int i=0;
		/*for (int i = 1; i <= 50; i++) {
//			randomFile("E:\\Dataset\\"+i+"trainAll.arff", 2000,i);
			randomFile("E:\\Dataset\\"+i+"Analyzer_testAll.arff", 200,i);
		}*/
	}
	
}
