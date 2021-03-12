package presentation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class ConfigFileReader {
	
	public ConfigEntries configEntries = new ConfigEntries();
	private int count;

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
				line = reader.readLine();
				processLine(line);
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
	
	private void processLine(String line)
	{
		// need to escape pipe: http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
		try {
			if (count >= ConfigConstants.CONFIG_HEADER_LENGTH) {
				String[] splitLine = line.split("#");
				String[] entry = splitLine[0].split("=");
				if (count == ConfigConstants.PROJECT_PATH_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.PROJECT_PATH_DESCRIPTION)) {
						configEntries.projectPath = entry[1].trim();
					}
				}
				if (count == ConfigConstants.SAM_FILES_PATH_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.SAM_FILES_PATH_DESCRIPTION)) {
						configEntries.samFilesPath = entry[1].trim();
					}
				}
				if (count == ConfigConstants.VCF_FILES_PATH_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.VCF_FILES_PATH_DESCRIPTION)) {
						configEntries.vcfFilesPath = entry[1].trim();
					}
				}
				if (count == ConfigConstants.REF_FILE_PATH_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.REF_FILE_PATH_DESCRIPTION)) {
						configEntries.refFilePath = entry[1].trim();
					}
				}
				if (count == ConfigConstants.INTERVALS_FILE_PATH_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.INTERVALS_FILE_PATH_DESCRIPTION)) {
						configEntries.intervalsFilePath = entry[1].trim();
					}
				}
				if (count == ConfigConstants.MEAN_FILES_PATH_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.MEAN_FILES_PATH_DESCRIPTION)) {
						configEntries.meanFilesPath = entry[1].trim();
					}
				}
				if (count == ConfigConstants.GFF_FILE_PATH_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.GFF_FILE_PATH_DESCRIPTION)) {
						configEntries.gffFilePath = entry[1].trim();
					}
				}
				if (count == ConfigConstants.USE_FILE_NAMES_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.USE_FILE_NAMES_DESCRIPTION)) {
						if (entry[1].trim().equals("0")) {
							configEntries.useFileNames = false;
						} else if (entry[1].trim().equals("1")) {
							configEntries.useFileNames = true;
						} else {
							System.out.println(ConfigConstants.USE_FILE_NAMES_DESCRIPTION + " line is corrupted in the config file.");
						}
					}
				}
				if (count == ConfigConstants.FILE_SAMPLE_NAME_PATH_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.FILE_SAMPLE_NAME_PATH_DESCRIPTION)) {
						configEntries.fileSampleNameFilePath = entry[1].trim();
					}
				}
				if (count == ConfigConstants.SAMPLE_NAME_SPECIES_PATH_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.SAMPLE_NAME_SPECIES_PATH_DESCRIPTION)) {
						configEntries.sampleNameSpeciesFilePath = entry[1].trim();
					}
				}
				if (count == ConfigConstants.SPECIES_NAME_COLOR_PATH_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.SPECIES_NAME_COLOR_PATH_DESCRIPTION)) {
						configEntries.speciesNameColorFilePath = entry[1].trim();
					}
				}
				if (count == ConfigConstants.VCF_TYPE_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.VCF_TYPE_DESCRIPTION)) {
						configEntries.vcfType = entry[1].trim();
					}
				}
				if (count == ConfigConstants.INCL_SPEC_TYPE_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.INCL_SPEC_TYPE_DESCRIPTION)) {
						if (entry[1].trim().equals("0")) {
							configEntries.includeSpeciesInName = false;
						} else if (entry[1].trim().equals("1")) {
							configEntries.includeSpeciesInName = true;
						} else {
							System.out.println(ConfigConstants.INCL_SPEC_TYPE_DESCRIPTION + " line is corrupted in the config file.");
						}
					}
				}
				if (count == ConfigConstants.REFERENCE_SPECIES_NAME_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.REFERENCE_SPECIES_NAME_DESCRIPTION)) {
						configEntries.refSpecies = entry[1].trim();
					}
				}
				if (count == ConfigConstants.VCF_MAX_P_VALUE_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					configEntries.maxPValue = Constants.MAX_P_VALUE;
					String maxPValueEntry = entry[1].trim();
					if (UtilityMethods.isNumber(maxPValueEntry)) {
						double maxP = Double.parseDouble(maxPValueEntry);
						if (maxP >= 0 && maxP <= 1) {
							configEntries.maxPValue = maxP;
						} else {
							System.out.println(ConfigConstantsIVP.VCF_MAX_P_VALUE_DESCRIPTION + " entry is not valid. "
									+ "All P-values will be processed.");
						}
					} else {
						System.out.println(ConfigConstantsIVP.VCF_MAX_P_VALUE_DESCRIPTION + " entry is not valid. "
								+ "All P-values will be processed.");
					}
				}
				if (count == ConfigConstants.VCF_P_VALUE_COLUMNS_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.VCF_P_VALUE_COLUMNS_DESCRIPTION)) {
						configEntries.pValueFormatIDs = entry[1].trim();
					}
				}
				if (count == ConfigConstants.MEAN_VALUES_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstants.MEAN_VALUES_DESCRIPTION)) {
						configEntries.meanValues = entry[1].trim();
					}
				}
				if (count == ConfigConstants.MAX_INTERVAL_SIZE_LINE + ConfigConstants.CONFIG_HEADER_LENGTH) {
					configEntries.maxInterval = Constants.MAX_INTERVAL_WIDTH;
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.MAX_INTERVAL_SIZE_DESCRIPTION)) {
						String maxIntervEntry = entry[1].trim();
						if (UtilityMethods.isInteger(maxIntervEntry)) {
							int maxInterv = Integer.parseInt(maxIntervEntry);
							if (maxInterv > 0) {
								configEntries.maxInterval = maxInterv;
							} else {
								System.out.println(ConfigConstantsIVP.MAX_INTERVAL_SIZE_DESCRIPTION + " entry is not valid. "
										+ "Default value " + Constants.MAX_INTERVAL_WIDTH + " will be used.");
							}
						} else {
							System.out.println(ConfigConstantsIVP.MAX_INTERVAL_SIZE_DESCRIPTION + " entry is not valid. "
									+ "Default value " + Constants.MAX_INTERVAL_WIDTH + " will be used.");
						}
					}
				}
			}
			count += 1;			
		} catch (Exception e) {

		}
	}
	
	private boolean validConfigDescriptor(String entry, String defaultDescr)
	{
		if (!entry.equals(defaultDescr))
		{
			System.out.println(defaultDescr + " line is corrupted in the config file.");
			return false;
		}
		return true;
	}
	
	public static void main(String args[]) {
		ConfigFileReader reader = new ConfigFileReader();
//		reader.readFile("brown_bear_sv020119_config.conf");
		reader.readFile("test_config.conf");
		System.out.println(reader.configEntries);
		
	}
	
}

