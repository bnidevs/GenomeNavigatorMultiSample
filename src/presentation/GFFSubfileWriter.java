package presentation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class GFFSubfileWriter {
	
	String content = "";
	int count = 0;
	int addCount = 0; 
	
	public void readFile(String file, ArrayList<String> genesList)
	{
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line, genesList);
			while (line != null) {
				if (!line.startsWith("#")) {
					line = reader.readLine();
					//System.out.println(line);
					processLine(line, genesList);
				}
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
	
	private void processLine(String line, ArrayList<String> genesList)
	{
		count += 1;
		System.out.println(count);
		try {
			//GeneralFeatureData gfData = new GeneralFeatureData();

			String[] splitLine = line.split("\t");
//			gfData.chr = splitLine[0];
//			gfData.featureName = splitLine[2];
			//if (splitLine[2].equals("exon")) {
//				gfData.start = Integer.parseInt(splitLine[3]);
//				gfData.end = Integer.parseInt(splitLine[4]);
//				gfData.strand = splitLine[6];
//				//Interval gfInterval = gfData.createInterval();
//				String key = gfData.chr;
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
//				gfData.attributesMap = attributesMap;
				//System.out.println(gfData);
				// check if any genes start with "LOC"
				String gene = attributesMap.get("gene").get(0);
				if (genesList.contains(gene)) {
					content += line + "\n";
					addCount += 1;
					System.out.println("a " + addCount);
				}
			//}
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {
		
	}
	
}
