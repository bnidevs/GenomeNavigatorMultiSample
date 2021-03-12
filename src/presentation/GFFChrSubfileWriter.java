package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class GFFChrSubfileWriter {
	
	HashMap<String, ArrayList<String>> chrEntriesMap = new HashMap<String, ArrayList<String>>();
	String feature = "";
	
	public void processFile(String file)
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
	
	private void processLine(String line)
	{
		try {
			//GeneralFeatureData gfData = new GeneralFeatureData();

			String[] splitLine = line.split("\t");
			if (splitLine[2].equals(feature)) {
				String chr = splitLine[0];
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
				String entry = chr + "\t" + splitLine[3] + "\t" + splitLine[4] + "\t" + gene + "\t" + splitLine[6];
				System.out.println(entry);
				if (chrEntriesMap.containsKey(chr)) {
					ArrayList<String> entries = chrEntriesMap.get(chr);
					entries.add(entry);
					chrEntriesMap.put(chr, entries);
				} else {
					ArrayList<String> entries = new ArrayList<String>();
					entries.add(entry);
					chrEntriesMap.put(chr, entries);
				}
			}
		} catch (Exception e) {

		}
	}
	
	public void writeChrSubfiles(TextFileWriter writer, String path) {
		for (String chr : chrEntriesMap.keySet()) {
			String content = "";
			for (int i = 0; i < chrEntriesMap.get(chr).size(); i++) {
				content += chrEntriesMap.get(chr).get(i) + "\n";
			}
			String outfilePath = path + "." + chr + ".txt";
			System.out.println(outfilePath);
			writer.writeFile(outfilePath, content);
		}
		System.out.println("Done");
	}
	
	public static void main(String args[]) {
		TextFileWriter writer = new TextFileWriter();
		String projectPath = "gff_test/";
		File dataDir = new File(projectPath);
		if (!dataDir.exists()) {
			dataDir.mkdir();
		}	
		//File gffFile = new File("F:/work_Documents/Java Workspace/GenomeNavigator1/etc/bear/GCF_000687225.1_UrsMar_1.0_genomic.gff");
		//File gffFile = new File(projectPath + "GCF_000687225.1_UrsMar_1.0_genomic.gff1.gff");
		File gffFile = new File("etc/tilapia_test/tilapia_GL831235-1_fict_CDS.gff");
		GFFChrSubfileWriter gffWriter = new GFFChrSubfileWriter();
		gffWriter.feature = "CDS";
		gffWriter.processFile(gffFile.getAbsolutePath());
		//System.out.println(gffWriter.chrEntriesMap);
		gffWriter.writeChrSubfiles(writer, projectPath + gffFile.getName());
	}
	
}

