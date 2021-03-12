package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class CSVSpeciesFileReader {
	
	public void readFile(String file, HashMap<String, String> sampleSpeciesMap)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line, sampleSpeciesMap);
			while (line != null) {
				line = reader.readLine();
				processLine(line, sampleSpeciesMap);
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
	
	private void processLine(String line, HashMap<String, String> sampleSpeciesMap) {
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			String[] splitLine = line.split(",");
//			for (int i = 0; i < splitLine.length; i++) {
//				System.out.println(splitLine[i]);
//			}	
			sampleSpeciesMap.put(splitLine[0], splitLine[1]);
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {
		CSVSpeciesFileReader reader = new CSVSpeciesFileReader();
		HashMap<String, String> sampleSpeciesMap = new HashMap<String, String>();
		File file = new File("etc/sam_files/tilapia_sample_species.csv");
		reader.readFile(file.getAbsolutePath(), sampleSpeciesMap);
		System.out.println(sampleSpeciesMap);
	}

}
