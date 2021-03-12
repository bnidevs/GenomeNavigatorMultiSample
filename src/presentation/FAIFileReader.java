package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class FAIFileReader {
	
	//int index = 0;
	
	public ArrayList<String> scaffolds = new ArrayList<String>();
	public int lineLength = 0;
	
	public void readFile(String file)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			String line = reader.readLine();
			String[] splitLine = line.split("\t");
			lineLength = Integer.parseInt(splitLine[3]);
			processLine(line);
			while (line != null) {
				line = reader.readLine();
				processLine(line);
			}
			fis.close();
			//System.out.println(scaffolds);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void processLine(String line) {
		try {
			String[] splitLine = line.split("\t");
			scaffolds.add(splitLine[0]);
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {
		File file = new File("E:/bear/GCF_000687225.1_UrsMar_1.0_genomic.fna.fai");
		FAIFileReader reader = new FAIFileReader();
		reader.readFile(file.getAbsolutePath());
		
	}

}



