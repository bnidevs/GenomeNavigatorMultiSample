package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;

/**
 * Reads preprocessed file
 * @author James Kelley
 * Unsure if used by GNDP
 *
 */
public class GFFFileParentGeneMapCreator {
	
	//public ArrayList<String> geneNames = new ArrayList<String>();
	public HashMap<String, String> parentGeneNameMap = new HashMap<String, String>();
	
	public void readFile(String file, HashMap<String, String> parentGeneNameMap)
	{
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line, parentGeneNameMap);
			while (line != null) {
//				if (!line.startsWith("#")) {
					line = reader.readLine();
					//System.out.println(line);
					processLine(line, parentGeneNameMap);
//				}
			}
			fis.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,                
					"GFF file not found",                
					"File not found Error.",                                
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,                
					"File error",                
					"File error.",                                
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	private void processLine(String line, HashMap<String, String> parentGeneNameMap)
	{
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			GeneralFeatureData gfData = new GeneralFeatureData();

			String[] splitLine = line.split("\t");
			gfData.chr = splitLine[0];
			gfData.featureName = splitLine[2];
			if (splitLine[2].equals("mRNA")) {
				String[] values = splitLine[8].split(";");
				HashMap<String, ArrayList<String>> attributesMap = new HashMap<String, ArrayList<String>>();
				for (int i = 0; i < values.length; i++) {
					String[] valuesEntries = values[i].split("=");
					for (int j = 0; j < valuesEntries.length; j++) {
						//System.out.println(valuesEntries[j]);
						String attrKey = valuesEntries[0];
						String[] attrEntries = valuesEntries[1].split(",");
						ArrayList<String> attributes = new ArrayList<String>();
						for (int k = 0; k < attrEntries.length; k++) {
							attributes.add(attrEntries[k]);
						}
						attributesMap.put(attrKey, attributes);
					}
				}
				
				String geneNameString = attributesMap.get("Name").get(0);
				String geneName = "";
				if (geneNameString != null) {
					if (geneNameString.contains("-")) {
						geneName = geneNameString.substring(0, geneNameString.indexOf("-"));
					} else {
						// this does not occur
						geneName = geneNameString;
						System.out.println(geneName);
					}
//					System.out.println("Name " + geneName);
//					System.out.println("ID " + attributesMap.get("ID"));
					parentGeneNameMap.put(attributesMap.get("ID").get(0), geneName);
				}
			}
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {	
		File gFile = new File("etc/elephant/" + "Loxodonta_africana.loxAfr3.97.gff3");
		GFFFileParentGeneMapCreator parentGeneMapCreator = new GFFFileParentGeneMapCreator();
		HashMap<String, String> parentGeneNameMap = new HashMap<String, String>();
		parentGeneMapCreator.readFile(gFile.getAbsolutePath(), parentGeneNameMap);
		System.out.println(parentGeneNameMap);
	}
	
}