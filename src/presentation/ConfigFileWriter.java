package presentation;

/**
 * @author James Kelley
 * Genome Navigator Data Processor code
 */
public class ConfigFileWriter {
	
	public void writeDefaultConfigFile(String path) {
		TextFileWriter writer = new TextFileWriter();
		
		String content = "";
		
		content += ConfigConstants.CONFIG_HEADER_COMMENT1 + "\n";
		content += ConfigConstants.CONFIG_HEADER_COMMENT2 + "\n";
		content += ConfigConstants.CONFIG_HEADER_COMMENT3 + "\n";

		content += ConfigConstants.PROJECT_PATH_DESCRIPTION + "= ";
		content += ConfigConstants.PROJECT_PATH_COMMENT + "\n";
		
		content += ConfigConstants.SAM_FILES_PATH_DESCRIPTION + "= ";
		content += ConfigConstants.SAM_FILES_PATH_COMMENT + "\n";
		
		content += ConfigConstants.VCF_FILES_PATH_DESCRIPTION + "= ";
		content += ConfigConstants.VCF_FILES_PATH_COMMENT + "\n";
		
		content += ConfigConstants.REF_FILE_PATH_DESCRIPTION + "= ";
		content += ConfigConstants.REF_FILE_PATH_COMMENT + "\n";
		
		content += ConfigConstants.INTERVALS_FILE_PATH_DESCRIPTION + "= ";
		content += ConfigConstants.INTERVALS_FILE_PATH_COMMENT + "\n";
		
		content += ConfigConstants.MEAN_FILES_PATH_DESCRIPTION + "= ";
		content += ConfigConstants.MEAN_FILES_PATH_COMMENT + "\n";
		
		content += ConfigConstants.GFF_FILE_PATH_DESCRIPTION + "= ";
		content += ConfigConstants.GFF_FILE_PATH_COMMENT + "\n";
		
		content += ConfigConstants.USE_FILE_NAMES_DESCRIPTION + "=" + ConfigConstants.DEFAULT_USE_FILE_NAMES_ENTRY + " ";
		content += ConfigConstants.USE_FILE_NAMES_COMMENT + "\n";
		
		content += ConfigConstants.FILE_SAMPLE_NAME_PATH_DESCRIPTION + "= ";
		content += ConfigConstants.FILE_SAMPLE_NAME_PATH_COMMENT + "\n";
		
		content += ConfigConstants.SAMPLE_NAME_SPECIES_PATH_DESCRIPTION + "= ";
		content += ConfigConstants.SAMPLE_NAME_SPECIES_PATH_COMMENT + "\n";
		
		content += ConfigConstants.SPECIES_NAME_COLOR_PATH_DESCRIPTION + "= ";
		content += ConfigConstants.SPECIES_NAME_COLOR_PATH_COMMENT + "\n";
		
		content += ConfigConstants.VCF_TYPE_DESCRIPTION + "=" + Constants.DEFAULT_VCF_FILE_TYPE + " ";
		content += ConfigConstants.VCF_TYPE_COMMENT + "\n";
		
		content += ConfigConstants.INCL_SPEC_TYPE_DESCRIPTION + "=" + Constants.DEFAULT_INCLUDE_SPECIES_IN_SAMPLE_NAME + " ";
		content += ConfigConstants.INCL_SPEC_TYPE_COMMENT + "\n";
		
		content += ConfigConstants.REFERENCE_SPECIES_NAME_DESCRIPTION + "= ";
		content += ConfigConstants.REFERENCE_SPECIES_NAME_COMMENT + "\n";
		
		content += ConfigConstants.VCF_MAX_P_VALUE_DESCRIPTION + "= ";
		content += ConfigConstants.VCF_MAX_P_VALUE_COMMENT + "\n";
		
		content += ConfigConstants.VCF_P_VALUE_COLUMNS_DESCRIPTION + "= ";
		content += ConfigConstants.VCF_P_VALUE_COLUMNS_COMMENT + "\n";
		
		content += ConfigConstants.MEAN_VALUES_DESCRIPTION + "= ";
		content += ConfigConstants.MEAN_VALUES_COMMENT + "\n";
		
		content += ConfigConstants.MAX_INTERVAL_SIZE_DESCRIPTION + "= ";
		content += ConfigConstants.MAX_INTERVAL_SIZE_COMMENT + "\n";
		
		writer.writeFile(path, content);
	}
	
	public static void main(String[] args) {
		ConfigFileWriter writer = new ConfigFileWriter();
		writer.writeDefaultConfigFile("test_config.conf");
		
	}

}
