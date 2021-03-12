package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/*
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class FAIChrIndexReader {
	
	public HashMap<String, Integer> scaffoldIndexMap = new HashMap<String, Integer>();
	
	public void readFile(String path)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(path);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			String line = reader.readLine();
			processLine(line);
			while (line != null) {
				line = reader.readLine();
				processLine(line);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void processLine(String line) {
		if (line != null) {
			String[] splitLine = line.split("\t");
			scaffoldIndexMap.put(splitLine[0], Integer.parseInt(splitLine[1]));	
		}
	}
	
	public static void main(String args[]) {
		File file = new File("E:/bear/GCF_000687225.1_UrsMar_1.0_genomic.fna.fai.indexes.txt");
		
		FAIChrIndexReader faiReader = new FAIChrIndexReader();
		faiReader.readFile(file.getAbsolutePath());
		
		System.out.println(faiReader.scaffoldIndexMap);
	}

}
