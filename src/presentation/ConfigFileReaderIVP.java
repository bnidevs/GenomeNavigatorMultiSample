package presentation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class ConfigFileReaderIVP {
	
	public ConfigEntries configEntries = new ConfigEntries();
	private int count;
	private ArrayList<GFFInfo> gffInfoList = new ArrayList<GFFInfo>();
	private int gffColorCount = 0;

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
			if (count >= ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
				String[] splitLine = line.split("#");
				String[] entry = splitLine[0].split("=");
				if (count == ConfigConstantsIVP.PROJECT_PATH_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.PROJECT_PATH_DESCRIPTION)) {
						configEntries.projectPath = entry[1].trim();
					}
				}
				if (count == ConfigConstantsIVP.SAM_FILES_PATH_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.SAM_FILES_PATH_DESCRIPTION)) {
						configEntries.samFilesPath = entry[1].trim();
					}
				}
				if (count == ConfigConstantsIVP.VCF_FILES_PATH_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.VCF_FILES_PATH_DESCRIPTION)) {
						configEntries.vcfFilesPath = entry[1].trim();
					}
				}
				if (count == ConfigConstantsIVP.REF_FILE_PATH_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.REF_FILE_PATH_DESCRIPTION)) {
						configEntries.refFilePath = entry[1].trim();
					}
				}
				if (count == ConfigConstantsIVP.MEAN_FILES_PATH_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {					
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.MEAN_FILES_PATH_DESCRIPTION)) {
						configEntries.meanFilesPath = entry[1].trim();
					}
				}
				if (count == ConfigConstantsIVP.MIN_MAX_INS_SIZE_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.MIN_MAX_INS_SIZE_DESCRIPTION)) {
						configEntries.meanValues = entry[1].trim();
					}
				}
				if (count == ConfigConstantsIVP.VCF_MAX_P_VALUE_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					configEntries.maxPValue = Constants.MAX_P_VALUE;
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.VCF_MAX_P_VALUE_DESCRIPTION)) {
						String maxPValueEntry = entry[1].trim();
						if (maxPValueEntry.length() > 0) {
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
					}
				}
				if (count == ConfigConstantsIVP.VCF_P_VALUE_COLUMNS_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.VCF_P_VALUE_COLUMNS_DESCRIPTION)) {
						configEntries.pValueFormatIDs = entry[1].trim();
					}
				}
				if (count == ConfigConstantsIVP.SAMPLE_NAME_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.SAMPLE_NAME_DESCRIPTION)) {
						configEntries.sampleName = entry[1].trim();
					}
				}
				if (count == ConfigConstantsIVP.MAX_INTERVAL_SIZE_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					configEntries.maxInterval = Constants.MAX_INTERVAL_WIDTH;
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.MAX_INTERVAL_SIZE_DESCRIPTION)) {
						String maxIntervEntry = entry[1].trim();
						if (maxIntervEntry.length() > 0) {
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
				if (count == ConfigConstantsIVP.INTERVALS_SUBFILES_SIZE_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					configEntries.intervalsSubFilesSize = Constants.INTERVALS_SUBFILES_SIZE;
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.INTERVALS_SUBFILES_SIZE_DESCRIPTION)) {
						String intSubFileSize = entry[1].trim();
						if (intSubFileSize.length() > 0) {
							if (UtilityMethods.isInteger(intSubFileSize)) {
								int size = Integer.parseInt(intSubFileSize);
								if (size > 0) {
									configEntries.intervalsSubFilesSize = size;
								} else {
									System.out.println(ConfigConstantsIVP.INTERVALS_SUBFILES_SIZE_DESCRIPTION + " entry is not valid. "
											+ "Default value " + Constants.INTERVALS_SUBFILES_SIZE + " will be used.");
								}
							} else {
								System.out.println(ConfigConstantsIVP.MAX_INTERVAL_SIZE_DESCRIPTION + " entry is not valid. "
										+ "Default value " + Constants.MAX_INTERVAL_WIDTH + " will be used.");
							}
						}
					}
				}
				if (count == ConfigConstantsIVP.REFERENCE_SPECIES_NAME_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.REFERENCE_SPECIES_NAME_DESCRIPTION)) {
						configEntries.refSpecies = entry[1].trim();
					}
				}
				if (count >= ConfigConstantsIVP.GFF_INFO_LINE + ConfigConstantsIVP.CONFIG_HEADER_LENGTH) {
					processGFFInfoEntries(entry[0], entry[1], ConfigConstantsIVP.GFF_INFO_DESCRIPTION);
//					if (validConfigDescriptor(entry[0], ConfigConstantsIVP.GFF_INFO_DESCRIPTION)) {
//						String gffEntry = entry[1].trim();
//						String[] gffEntries = gffEntry.split(",");
//						GFFInfo gffInfo = new GFFInfo();
//						if (gffEntries.length > 0) {
//							gffInfo.path = gffEntries[0];
//						} else {
//							System.out.println("GFF Info line not valid");
//						}
//						if (gffEntries.length > 1) {
//							gffInfo.feature = gffEntries[1];
//						} else {
//							System.out.println("GFF Info feature not valid");
//						}
//						if (gffEntries.length > 2) {
//							gffInfo.color = gffEntries[2];
//						} else {
//							gffInfo.color = Constants.DEFAULT_GFF_COLORS[0];
//							gffColorCount += 1;
//						}
//					}
				}
			}
			count += 1;			
		} catch (Exception e) {

		}
	}
	
	private void processGFFInfoEntries(String entry, String value, String defaultDescr) {
		int splitIndex = ConfigConstantsIVP.GFF_INFO_DESCRIPTION.length();
		String processedEntry = entry.substring(0, splitIndex);
		if (!processedEntry.equals(defaultDescr)) {
			System.out.println(entry + " line is corrupted in the config file.");
		} else {
			String gffEntry = value.trim();
			String[] gffEntries = gffEntry.split(",");
			GFFInfo gffInfo = new GFFInfo();
			if (gffEntries.length > 0) {
				gffInfo.path = gffEntries[0];
			} else {
				System.out.println("GFF Info line not valid");
			}
			if (gffEntries.length > 1) {
				gffInfo.feature = gffEntries[1];
			} else {
				System.out.println("GFF Info feature not valid");
			}
			if (gffEntries.length > 2) {
				gffInfo.color = gffEntries[2];
			} else {
				gffInfo.color = Constants.DEFAULT_GFF_COLORS[gffColorCount];
				gffColorCount += 1;
			}
			gffInfoList.add(gffInfo);
			configEntries.gffInfoList = gffInfoList;
		}
	}
	
	private boolean validConfigDescriptor(String entry, String defaultDescr)
	{
		//System.out.println(entry);
		if (!entry.equals(defaultDescr))
		{
			System.out.println(defaultDescr + " line is corrupted in the config file.");
			return false;
		}
		return true;
	}
	
	public static void main(String args[]) {
		ConfigFileReaderIVP reader = new ConfigFileReaderIVP();
		//reader.readFile("etc/tilapia_test/tilapia_test_ivp_GROM_config1.conf");
		reader.readFile("etc/tilapia_test/tilapia_test_ivp_no_mean_file_config-local.conf");
		System.out.println(reader.configEntries);
		
	}
	
}
