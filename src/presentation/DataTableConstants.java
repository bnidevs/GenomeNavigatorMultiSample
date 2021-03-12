package presentation;

import java.awt.Color;
import java.util.Arrays;

public class DataTableConstants {
	
	public static final int TABLE_WIDTH = 920;
	public static final int TABLE_HEIGHT = 600;

	public static final String SAMPLE_COLUMN_NAME = "Sample";
	public static final String CHROMOSOME_COLUMN_NAME = "Chr";
	public static final String SV_TYPE_COLUMN_NAME = "SV Type";
	public static final String COORDINATES_FROM_COLUMN_NAME = "From";
	public static final String COORDINATES_TO_COLUMN_NAME = "To";
	public static final String NUM_SAMPLES_COLUMN_NAME = "# Samples";
	public static final String INTERVAL_COLUMN_NAME = "Interval";
	public static final String INFO_COLUMN_NAME = "Info";	
	public static final String OVERLAP_FEATURES_COLUMN_NAME = "Overlap Features";
	public static final String NEARBY_FEATURES_COLUMN_NAME = "Nearby Features";
	
	public static final String[] COLUMN_NAMES = 
		{
		SAMPLE_COLUMN_NAME, 	
		CHROMOSOME_COLUMN_NAME,	
		SV_TYPE_COLUMN_NAME,
		COORDINATES_FROM_COLUMN_NAME,
		COORDINATES_TO_COLUMN_NAME,
		NUM_SAMPLES_COLUMN_NAME,
		INTERVAL_COLUMN_NAME,
		INFO_COLUMN_NAME		
		};
	
	public static final String[] COLUMN_NAMES_WITH_GENES = 
		{
		SAMPLE_COLUMN_NAME, 	
		CHROMOSOME_COLUMN_NAME,	
		SV_TYPE_COLUMN_NAME,
		COORDINATES_FROM_COLUMN_NAME,
		COORDINATES_TO_COLUMN_NAME,
		NUM_SAMPLES_COLUMN_NAME,
		INTERVAL_COLUMN_NAME,
		INFO_COLUMN_NAME, 		
		OVERLAP_FEATURES_COLUMN_NAME,
		NEARBY_FEATURES_COLUMN_NAME
		};
	
	public static java.util.List<String> columnsList = Arrays.asList(COLUMN_NAMES);
	public static java.util.List<String> columnsListWithGenes = Arrays.asList(COLUMN_NAMES_WITH_GENES);
	
	public static final int SAMPLE_COLUMN_INDEX = columnsList.indexOf(SAMPLE_COLUMN_NAME);
	public static final int CHROMOSOME_COLUMN_INDEX = columnsList.indexOf(CHROMOSOME_COLUMN_NAME);
	public static final int SV_TYPE_COLUMN_INDEX = columnsList.indexOf(SV_TYPE_COLUMN_NAME);
	public static final int COORDINATES_FROM_COLUMN_INDEX = columnsList.indexOf(COORDINATES_FROM_COLUMN_NAME);
	public static final int COORDINATES_TO_COLUMN_INDEX = columnsList.indexOf(COORDINATES_TO_COLUMN_NAME);
	public static final int NUM_SAMPLES_COLUMN_INDEX = columnsList.indexOf(NUM_SAMPLES_COLUMN_NAME);
	public static final int INTERVAL_COLUMN_INDEX = columnsList.indexOf(INTERVAL_COLUMN_NAME);
	public static final int INFO_COLUMN_INDEX = columnsList.indexOf(INFO_COLUMN_NAME);	
	
	public static final int OVERLAP_FEATURES_COLUMN_INDEX = columnsListWithGenes.indexOf(OVERLAP_FEATURES_COLUMN_NAME);
	public static final int NEARBY_FEATURES_COLUMN_INDEX = columnsListWithGenes.indexOf(NEARBY_FEATURES_COLUMN_NAME);
	
	public static final int SAMPLE_COLUMN_WIDTH = 100; 
	public static final int CHROMOSOME_COLUMN_WIDTH = 110;
	public static final int SV_TYPE_COLUMN_WIDTH = 70; 
	public static final int COORDINATES_FROM_COLUMN_WIDTH = 60;
	public static final int COORDINATES_TO_COLUMN_WIDTH = 60;
	public static final int NUM_SAMPLES_COLUMN_WIDTH = 60;
	public static final int INTERVAL_COLUMN_WIDTH = 60;
	public static final int INFO_COLUMN_WIDTH = 120;	
	public static final int OVERLAP_FEATURES_COLUMN_WIDTH = 110;
	public static final int NEARBY_FEATURES_COLUMN_WIDTH = 110;
	
	public static final Color INTERVAL_HIGHLIGHT_COLOR = new Color(212, 212, 212);
	//public static final Color INTERVAL_HIGHLIGHT_COLOR = Color.LIGHT_GRAY;
	
}

