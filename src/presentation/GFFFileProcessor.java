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

/**
 * use this class to write gff file for Data Table, 1st and 3rd windows 
 * @author James Kelley
 * Used by GNDP
 *
 */
public class GFFFileProcessor {

	String content = "";
	String cdsContent = "";
	
	public void readFile(String file, LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap)
	{
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			//processLine(line);
			while (line != null) {
//				if (!line.startsWith("#")) {
					line = reader.readLine();
					//System.out.println(line);
					processLine(line, chrIntervalsMap);
//				}
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
	
	private void processLine(String line, LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap)
	{
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			GeneralFeatureData gfData = new GeneralFeatureData();
			
			String[] splitLine = line.split("\t");
//			System.out.println(splitLine[0]);
			if (chrIntervalsMap.containsKey(splitLine[0])) {
				gfData.start = Integer.parseInt(splitLine[3]);
				gfData.end = Integer.parseInt(splitLine[4]);
//				gfData.strand = splitLine[6];
				Interval gfInterval = gfData.createInterval();
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
//				System.out.println(gfInterval);
				ArrayList<Interval> testIntervals = chrIntervalsMap.get(splitLine[0]);
				if (splitLine[2].equals("exon")) {
					for (int t = 0; t < testIntervals.size(); t++) {
						if (UtilityMethods.isIntervalPortionVisible(gfInterval, testIntervals.get(t))) {
							//System.out.println(line);
							content += line + "\n";
						}
					}
				}
				if (splitLine[2].equals("CDS")) {
					cdsContent += line + "\n";
				}
			}			
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {
		String directory = "etc/bear/";
		
		IntervalFileChrFileProcessor iProc = new IntervalFileChrFileProcessor();
		String intervalsFilename = "intervals-brown_ref012619.txt";
		File f = new File(directory + intervalsFilename);
		iProc.readFile(f.getAbsolutePath(), 1, 2);
		
		System.out.println(iProc.chrIntervalsMap);
		
		GFFFileProcessor gProc = new GFFFileProcessor();
		
		File file = new File("etc/bear/ref_brown/" + "GCF_003584765.1_ASM358476v1_genomic.gff");
//	if (Constants.STUDY_NAME.equals(Constants.BEAR_STUDY_NAME_PANDA_REF)) {
//		directory = "etc/bear/";
//		intervalsFilename = Constants.BEAR_INTERVALS_FILENAME_PANDA_REF;
//		file = new File(directory + Constants.BEAR_GFF_PANDA_REF_FILENAME);
//	}
		gProc.readFile(file.getAbsolutePath(), iProc.chrIntervalsMap);
		
		String outputDir = "etc/bear/ref_brown-gff/";
		File dir = new File(outputDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		TextFileWriter writer = new TextFileWriter();
		String outfileName = "GCF_003584765.1_ASM358476v1_genomic-sv.gff";
//		String outfileName = "GCF_000687225.1_UrsMar_1.0_genomic-out.gff";
//		if (Constants.STUDY_TYPE.equals("SV")) {
//			outfileName = Constants.BEAR_GFF_SV_FILENAME;
//		} else if (Constants.STUDY_TYPE.equals("INDEL")) { 
//			outfileName = Constants.BEAR_GFF_INDEL_FILENAME;
//		}
		writer.writeFile(outputDir + outfileName, gProc.content);
		
		System.out.println("Done");
	}
	
}



