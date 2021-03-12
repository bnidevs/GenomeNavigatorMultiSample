package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class CSVFileSampleFileReader {
	
	private ArrayList<String> sampleList = new ArrayList<String>();
	private ArrayList<String> aliasList = new ArrayList<String>();
	public boolean duplicateSample = false;
	public boolean duplicateAlias = false;
	
	public void readFile(String file, LinkedHashMap<String, String> fileSampleMap, 
			LinkedHashMap<String, String> fileAliasMap, LinkedHashMap<String, String> sampleAliasMap)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line, fileSampleMap, fileAliasMap, sampleAliasMap);
			while (line != null) {
				line = reader.readLine();
				processLine(line, fileSampleMap, fileAliasMap, sampleAliasMap);
			}
			fis.close();
			if (duplicateSample) {
				System.out.println("Duplicate sample detected in File_Sample_Name_file.");
			}
			if (duplicateAlias) {
				System.out.println("Duplicate alias detected in File_Sample_Name_file.");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void processLine(String line, LinkedHashMap<String, String> fileSampleNameMap, 
			LinkedHashMap<String, String> fileAliasMap, LinkedHashMap<String, String> sampleAliasMap) {
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			String[] splitLine = line.split(",");
//			for (int i = 0; i < splitLine.length; i++) {
//				System.out.println(splitLine[i]);
//			}	
			fileSampleNameMap.put(splitLine[0], splitLine[1]);
			if (!sampleList.contains(splitLine[1])) {
				sampleList.add(splitLine[1]);
			} else {
				duplicateSample = true;
			}
			// true if alias column is present
			if (splitLine.length > 2) {
				fileAliasMap.put(splitLine[0], splitLine[2]);
				sampleAliasMap.put(splitLine[1], splitLine[2]);
				if (!aliasList.contains(splitLine[1])) {
					aliasList.add(splitLine[1]);
					duplicateAlias = true;
				}
			}
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {
		CSVFileSampleFileReader reader = new CSVFileSampleFileReader();
		LinkedHashMap<String, String> fileSampleNameMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> fileAliasMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> sampleAliasMap = new LinkedHashMap<String, String>();
		File file = new File("etc/bear/bear_file_sample-error.csv");
		reader.readFile(file.getAbsolutePath(), fileSampleNameMap, fileAliasMap, sampleAliasMap);
		System.out.println(fileSampleNameMap);
		System.out.println(fileAliasMap);
		System.out.println(sampleAliasMap);
	}

}
