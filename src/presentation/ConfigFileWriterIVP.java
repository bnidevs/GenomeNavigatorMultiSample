package presentation;

public class ConfigFileWriterIVP {
	
	public void writeDefaultConfigFile(String path) {
		TextFileWriter writer = new TextFileWriter();
		
		String content = "";
		
		content += ConfigConstantsIVP.CONFIG_HEADER_COMMENT1 + "\n";
		content += ConfigConstantsIVP.CONFIG_HEADER_COMMENT2 + "\n";
		content += ConfigConstantsIVP.CONFIG_HEADER_COMMENT3 + "\n";

		content += ConfigConstantsIVP.PROJECT_PATH_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.PROJECT_PATH_COMMENT + "\n";
		
		content += ConfigConstantsIVP.SAM_FILES_PATH_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.SAM_FILES_PATH_COMMENT + "\n";
		
		content += ConfigConstantsIVP.VCF_FILES_PATH_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.VCF_FILES_PATH_COMMENT + "\n";
		
		content += ConfigConstantsIVP.REF_FILE_PATH_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.REF_FILE_PATH_COMMENT + "\n";
		
		content += ConfigConstantsIVP.MEAN_FILES_PATH_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.MEAN_FILES_PATH_COMMENT + "\n";
		
		content += ConfigConstantsIVP.MIN_MAX_INS_SIZE_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.MIN_MAX_INS_SIZE_COMMENT + "\n";
		
		content += ConfigConstantsIVP.VCF_MAX_P_VALUE_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.VCF_MAX_P_VALUE_COMMENT + "\n";
		
		content += ConfigConstantsIVP.VCF_P_VALUE_COLUMNS_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.VCF_P_VALUE_COLUMNS_COMMENT + "\n";
		
		content += ConfigConstantsIVP.SAMPLE_NAME_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.SAMPLE_NAME_COMMENT + "\n";
		
		content += ConfigConstantsIVP.MAX_INTERVAL_SIZE_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.MAX_INTERVAL_SIZE_COMMENT + "\n";
		
		content += ConfigConstantsIVP.INTERVALS_SUBFILES_SIZE_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.INTERVALS_SUBFILES_SIZE_COMMENT + "\n";
		
		content += ConfigConstantsIVP.REFERENCE_SPECIES_NAME_DESCRIPTION + "= ";
		content += ConfigConstantsIVP.REFERENCE_SPECIES_NAME_COMMENT + "\n";
		
		content += ConfigConstantsIVP.GFF_INFO_DESCRIPTION + "_1= ";
		content += ConfigConstantsIVP.GFF_INFO_COMMENT + "\n";
		
		writer.writeFile(path, content);
	}
	
	public static void main(String[] args) {
		ConfigFileWriterIVP writer = new ConfigFileWriterIVP();
		writer.writeDefaultConfigFile("test_ivp_config.conf");
		
	}

}
