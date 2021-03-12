package presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author James Kelley
 * Genome Navigator and Genome Navigator Data Processor code
 * Used in derBrowserKey for species color code
 */
public class CSVSpeciesColorFileReader {
	
	private ArrayList<String> usedColorList = new ArrayList<String>();
	
	public void readFile(String file, HashMap<String, String> speciesColorMap)
	{ 
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			processLine(line, speciesColorMap);
			while (line != null) {
				line = reader.readLine();
				processLine(line, speciesColorMap);
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
	
	private void processLine(String line, HashMap<String, String> speciesColorMap) {
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			String[] splitLine = line.split(",");
			if (splitLine[1].equals(Constants.EXON_COLOR_STRING)) {
				String replacementColor = "";
				for (int i = 0; i < Constants.DEFAULT_SPECIES_COLORS.length; i++) {
					if (!usedColorList.contains(Constants.DEFAULT_SPECIES_COLORS[i])) {
						replacementColor = Constants.DEFAULT_SPECIES_COLORS[i];
						usedColorList.add(replacementColor);
						break;
					}
				}
				if (replacementColor.length() > 0) {
					System.out.println(Constants.EXON_COLOR_STRING + " in Species_Name_Color_file is a reserved color for Exons, replacing with " + replacementColor + ".");
				} else {
					System.out.println(Constants.EXON_COLOR_STRING + " in Species_Name_Color_fileis a reserved color for Exons, replacing with " + Constants.DEFAULT_SPECIES_COLOR + ".");
				}
			} else {
				speciesColorMap.put(splitLine[0], splitLine[1]);
				usedColorList.add(splitLine[1]);
			}
		} catch (Exception e) {

		}
	}
	
	public static void main(String args[]) {
		CSVSpeciesColorFileReader reader = new CSVSpeciesColorFileReader();
		HashMap<String, String> speciesColorMap = new HashMap<String, String>();
		File file = new File("etc/bear/bear_4_colors-error.csv");
		reader.readFile(file.getAbsolutePath(), speciesColorMap);
	}

}

