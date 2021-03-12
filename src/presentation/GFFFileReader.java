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
 * @author James Kelley
 * Unsure if used by GNDP
 *
 */
public class GFFFileReader {
	
	//public ArrayList<String> geneNames = new ArrayList<String>();
	public LinkedHashMap<String, ArrayList<GeneralFeatureData>> chrIntervalGFDataListMap = new LinkedHashMap<String, ArrayList<GeneralFeatureData>>();

	public void readFile(String file, LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap)
	{
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line, chrIntervalsMap);
			while (line != null) {
//				if (!line.startsWith("#")) {
					line = reader.readLine();
					//System.out.println(line);
					processLine(line, chrIntervalsMap);
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
	
	private void processLine(String line, LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap)
	{
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			GeneralFeatureData gfData = new GeneralFeatureData();

			String[] splitLine = line.split("\t");
			gfData.chr = splitLine[0];
			gfData.featureName = splitLine[2];
			if (splitLine[2].equals("exon")) {
				gfData.start = Integer.parseInt(splitLine[3]);
				gfData.end = Integer.parseInt(splitLine[4]);
				gfData.strand = splitLine[6];
				//Interval gfInterval = gfData.createInterval();
				String key = gfData.chr;
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
				gfData.attributesMap = attributesMap;
				//System.out.println(gfData);
				// check if any genes start with "LOC"
				String gene = attributesMap.get("gene").get(0);
//				if (!geneNames.contains(gene)) {
//					geneNames.add(gene);
//				}
				if (!gene.startsWith("LOC")) {
					if (!chrIntervalGFDataListMap.containsKey(key)) {
						ArrayList<GeneralFeatureData> gfDataList = new ArrayList<GeneralFeatureData>();
						gfDataList.add(gfData);
						chrIntervalGFDataListMap.put(key, gfDataList);
					} else {
						ArrayList<GeneralFeatureData> gfDataList = chrIntervalGFDataListMap.get(key);
						gfDataList.add(gfData);
						chrIntervalGFDataListMap.put(key, gfDataList);
					}
				}
			}
		} catch (Exception e) {

		}
	}
	
	public LinkedHashMap<String, HashMap<String, ArrayList<GeneralFeatureData>>> createChrIntervalGeneDataMap() {
		LinkedHashMap<String, HashMap<String, ArrayList<GeneralFeatureData>>> chrIntervalGeneDataMap = new LinkedHashMap<String, HashMap<String, ArrayList<GeneralFeatureData>>>();
		//System.out.println(chrIntervalGFDataListMap);
		for (String s : chrIntervalGFDataListMap.keySet()) {
			for (int i = 0; i < chrIntervalGFDataListMap.get(s).size(); i++) {
				String chr = chrIntervalGFDataListMap.get(s).get(i).chr;
				String gene = chrIntervalGFDataListMap.get(s).get(i).attributesMap.get("gene").get(0);
				int start = chrIntervalGFDataListMap.get(s).get(i).start;
				int end = chrIntervalGFDataListMap.get(s).get(i).end;
				String strand = chrIntervalGFDataListMap.get(s).get(i).strand;
				String featureName = chrIntervalGFDataListMap.get(s).get(i).featureName;

				// generic version of class, no attributes
				GeneralFeatureData gfdEntry = new GeneralFeatureData();
				gfdEntry.chr = chr;
				gfdEntry.start = start;
				gfdEntry.end = end;
				gfdEntry.strand = strand;
				gfdEntry.featureName = featureName;
				if (chrIntervalGeneDataMap.containsKey(s)) {
					if (chrIntervalGeneDataMap.get(s).containsKey(gene)) {
						ArrayList<GeneralFeatureData> dataList = chrIntervalGeneDataMap.get(s).get(gene);
						//System.out.println("entry " + gfdEntry);
						//System.out.println(dataList);
						boolean present = false;
						for (int j = 0; j < dataList.size(); j++) {
							//System.out.println(dataList.get(j).start);
							//System.out.println(dataList.get(j).end);
							if (gfdEntry.start == dataList.get(j).start && 
									gfdEntry.end == dataList.get(j).end) {
								present = true;
							}
						}
						//System.out.println(present);
						if (!present) {
							ArrayList<GeneralFeatureData> dataList1 = chrIntervalGeneDataMap.get(s).get(gene);
							dataList1.add(gfdEntry);
							HashMap<String, ArrayList<GeneralFeatureData>> geneDataMap = chrIntervalGeneDataMap.get(s);
							geneDataMap.put(gene, dataList1);
							chrIntervalGeneDataMap.put(s, geneDataMap);
						}
					} else {
						ArrayList<GeneralFeatureData> dataList = new ArrayList<GeneralFeatureData>();
						dataList.add(gfdEntry);
						HashMap<String, ArrayList<GeneralFeatureData>> geneDataMap = chrIntervalGeneDataMap.get(s);
						geneDataMap.put(gene, dataList);
						chrIntervalGeneDataMap.put(s, geneDataMap);
					}
				} else {
					ArrayList<GeneralFeatureData> dataList = new ArrayList<GeneralFeatureData>();
					dataList.add(gfdEntry);
					HashMap<String, ArrayList<GeneralFeatureData>> geneDataMap = new HashMap<String, ArrayList<GeneralFeatureData>>();
					geneDataMap.put(gene, dataList);
					chrIntervalGeneDataMap.put(s, geneDataMap);
				}
			}
		}
		//System.out.println(chrIntervalGeneDataMap);
		return chrIntervalGeneDataMap;
	}
	
	public static void main(String args[]) {
		
		
	}
	
}

