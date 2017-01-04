import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class RandomDataFile {

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
		//int i=0;
		for (int i = 1; i <= 50; i++) {
			randomFile("E:\\Dataset\\"+i+"trainAll.arff", 2000,i);
			randomFile("E:\\Dataset\\"+i+"testAll.arff", 1,i);
		}
	}
	
}
