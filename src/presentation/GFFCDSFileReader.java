package presentation;

import java.io.BufferedReader;
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
 * @author jkelley
 * Used by GNDP
 */
public class GFFCDSFileReader {
	
	//public ArrayList<String> geneNames = new ArrayList<String>();
	public LinkedHashMap<String, LinkedHashMap<String, ArrayList<GeneralFeatureData>>> geneIsoformGFCDSDataListMap = new LinkedHashMap<String, LinkedHashMap<String, ArrayList<GeneralFeatureData>>>();

	public void readFile(String file)
	{
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line);
			while (line != null) {
//				if (!line.startsWith("#")) {
					line = reader.readLine();
					//System.out.println(line);
					processLine(line);
//				}
			}
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.println("GFF file not found");
//			JOptionPane.showMessageDialog(null,                
//					"GFF file not found",                
//					"File not found Error.",                                
//					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File error");
//			JOptionPane.showMessageDialog(null,                
//					"File error",                
//					"File error.",                                
//					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	private void processLine(String line)
	{
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			GeneralFeatureData gfData = new GeneralFeatureData();

			String[] splitLine = line.split("\t");
			gfData.chr = splitLine[0];
			gfData.featureName = splitLine[2];
			gfData.start = Integer.parseInt(splitLine[3]);
			gfData.end = Integer.parseInt(splitLine[4]);
			gfData.strand = splitLine[6];
			//Interval gfInterval = gfData.createInterval();
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
			String gene = attributesMap.get("gene").get(0);
			String isoform = attributesMap.get("ID").get(0);
			
			
			if (geneIsoformGFCDSDataListMap.containsKey(gene)) {
				LinkedHashMap<String, ArrayList<GeneralFeatureData>> isoformGFCDSDataListMap = geneIsoformGFCDSDataListMap.get(gene);
				if (isoformGFCDSDataListMap.containsKey(isoform)) {
					ArrayList<GeneralFeatureData> gfCDSDataList = isoformGFCDSDataListMap.get(isoform);
					gfCDSDataList.add(gfData);
					isoformGFCDSDataListMap.put(isoform, gfCDSDataList);
					geneIsoformGFCDSDataListMap.put(gene, isoformGFCDSDataListMap);
				} else {
					ArrayList<GeneralFeatureData> gfCDSDataList = new ArrayList<GeneralFeatureData>();
					gfCDSDataList.add(gfData);
					isoformGFCDSDataListMap.put(isoform, gfCDSDataList);
					geneIsoformGFCDSDataListMap.put(gene, isoformGFCDSDataListMap);
				}
			} else {
				LinkedHashMap<String, ArrayList<GeneralFeatureData>> isoformGFCDSDataListMap = new LinkedHashMap<String, ArrayList<GeneralFeatureData>>();
				ArrayList<GeneralFeatureData> gfCDSDataList = new ArrayList<GeneralFeatureData>();
				gfCDSDataList.add(gfData);
				isoformGFCDSDataListMap.put(isoform, gfCDSDataList);
				geneIsoformGFCDSDataListMap.put(gene, isoformGFCDSDataListMap);
			}				
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {
		GFFCDSFileReader gffCDSReader = new GFFCDSFileReader();
		//gffCDSReader.readFile("polar040319indel_plus/gff_cds_data.txt");
		gffCDSReader.readFile("isoform_test.gff");
		System.out.println(gffCDSReader.geneIsoformGFCDSDataListMap);
	}
	
}

