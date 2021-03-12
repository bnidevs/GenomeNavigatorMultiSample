package presentation;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;

public class Constants {
	
	/*
	 * Constants related to window sizes and appearance
	 */
	public static final String TITLE = "Variant Navigator";
	public static final String TITLE_SINGLE_SAMPLE = "Variant Navigator";
	public static final String VARIANTS_WINDOW_TITLE = "Variants";
	public static final String P_VALUE_WINDOW_TITLE = "P-Values";
	public static final String READS_WINDOW_TITLE = "Reads";
	
	public static final String MAIN_IMAGE_PATH_16 = "etc/vn-logo16.jpg";
	public static final String MAIN_IMAGE_PATH_32 = "etc/vn-logo32.jpg";
	public static final String VARIANTS_WINDOW_IMAGE_PATH_16 = "etc/vn-logo16.jpg";
	public static final String VARIANTS_WINDOW_IMAGE_PATH_32 = "etc/vn-logo32.jpg";
	public static final String P_VALUE_WINDOW_IMAGE_PATH_16 = "etc/vn-logo16.jpg";
	public static final String P_VALUE_WINDOW_IMAGE_PATH_32 = "etc/vn-logo32.jpg";
	public static final String READS_WINDOW_IMAGE_PATH_16 = "etc/vn-logo16.jpg";
	public static final String READS_WINDOW_IMAGE_PATH_32 = "etc/vn-logo32.jpg";
	
	public static final int VARIANTS_WINDOW_WIDTH = 760;
	public static final int VARIANTS_WINDOW_HEIGHT = 600;
	public static final int P_VALUE_WINDOW_WIDTH = 760;
	public static final int P_VALUE_WINDOW_HEIGHT = 600;
	public static final int READS_WINDOW_WIDTH = 900;
	public static final int READS_WINDOW_HEIGHT = 600;
	
	public static final int VARIANTS_WINDOW_FRAME_WIDTH = 770;
	public static final int VARIANTS_WINDOW_FRAME_HEIGHT = 670;
	public static final int P_VALUE_WINDOW_FRAME_WIDTH = 770;
	public static final int P_VALUE_WINDOW_FRAME_HEIGHT = 670;
	public static final int READS_WINDOW_FRAME_WIDTH = 910;
	public static final int READS_WINDOW_FRAME_HEIGHT = 670;
	
	public static final int KEY_WINDOW_WIDTH = 500;
	public static final int KEY_WINDOW_HEIGHT = 100;
	
	//Decreased size of this variable to reduce the black bar on KEY windows
	public static final int KEY_FRAME_WIDTH = 505;
	public static final int KEY_FRAME_HEIGHT = 130;
	
	public static final int KEY_WINDOW_READS_HEIGHT = 400;
	public static final int KEY_FRAME_READS_HEIGHT = 420;
	public static final int KEY_WINDOW_READS_WIDTH = 650;
	public static final int KEY_FRAME_READS_WIDTH = 665;
	
	public static final int VARIANTS_KEY_FRAME_TEXT_HEIGHT_CORRECTION = 40;
	public static final int P_VALUE_KEY_FRAME_TEXT_HEIGHT_CORRECTION = 70;
	
	public static final int KEY_FRAME_BLACK_BORDER_THICKNESS = 6;
	
	public static final int READS_WINDOW_RANDOM_X_RANGE = 200;
	// reduces from 50 to fix bug where window sometimes was cut off by task bar on Windows 7
	// due to size change when adding menu bar
	public static final int READS_WINDOW_RANDOM_Y_RANGE = 30;
	
	public static final String VAR_BUTTON_TEXT = "Show Variant";
	public static final String VAR_TWO_WINDOWS_BUTTON_TEXT = "Show Variant in Two Windows";
	
	// Fonts
	public static final String OBJ_FONT = "Helvetica";
	public static final int OBJ_FONT_SIZE = 10;
	public static final int OBJ_FONT_TYPE = Font.PLAIN;
	public static final String OBJ_FONT_TYPE_STRING = "Plain";
	public static final String SCALE_FONT = "Helvetica";
	public static final int SCALE_FONT_SIZE = 10;
	public static final int SCALE_FONT_TYPE = Font.PLAIN;
	public static final String SCALE_FONT_TYPE_STRING = "Plain";
	public static final String MENU_FONT = "TimesRoman";
	public static final int MENU_FONT_SIZE = 12;
	public static final int MENU_FONT_TYPE = Font.PLAIN;
	public static final String MENU_FONT_TYPE_STRING = "Plain";
	public static final String SEQ_FONT = "Helvetica";
	public static final int SEQ_FONT_SIZE = 8;
	public static final int SEQ_FONT_TYPE = Font.PLAIN;
	
	// these actually move the y position of text
	public static final int EXON_FH = 11; 
	public static final int CLONES_FH = 11;
	public static final int SEQ_FH = 11;
	
	// corrects positions to account for differences in OS display
	public static final int BUTTONS_Y_CORRECTION = 2;
	public static final int TEXT_FIELDS_Y_CORRECTION = 2;
	
	// used to extract sample name from clones name in variants window
	public static final String SAMPLE_NAME_TYPE_CONNECTOR = "-";
	
	// used by GNDP only
	public static final String PLOT_COLOR_STRING = "C0C0C0";
	public static final String PLOT_AXIS_COLOR_STRING = "000000";
	
	/*
	 * Used in freezing components in variants and reads windows
	 */
	public static final int PLOT_HEIGHT = 40;
	public static final int EXON_HEIGHT = 21;
	public static final int REF_SEQ_HEIGHT = 21;
	public static final int REF_SEQ_HEIGHT_CORRECTION = 3;
	public static final int ABOVE_REF_RECTANGLE_CORR = 4;
	public static final int BELOW_REF_SEQ_HEIGHT_CORR = 3;
	public static final int EXON_Y_POS_CORRECTION_READS = 6;
	public static final int EXON_Y_POS_CORRECTION_VAR = -1;
	
	// fixed bug where mouseover and selecting object coordinates were off a bit
	public static final int OVER_OBJ_CORR = 3;
	
	// used in reads key and GNDP
	public static final String DEFAULT_GRAY_COLOR_STRING = "E8E8E8";
	public static final String SHADE_COLOR_STRING = "F8F8F8";
	public static final String DELETION_CIGAR_COLOR_STRING = "A00000";
	public static final String N_COLOR_STRING = DELETION_CIGAR_COLOR_STRING;
	public static final String P_COLOR_STRING = DELETION_CIGAR_COLOR_STRING;
	public static final String DELETION_COLOR_STRING = "FFA0A0";
	public static final String INSERTION_COLOR_STRING = "A0A0FF";
	//public static final String INDEL_INSERTION_LINES_COLOR_STRING = "A0A0FF";
	public static final String INDEL_INSERTION_LINES_COLOR_STRING = "000000";
	public static final String INVERSION_COLOR_STRING = "33FFD3";
	public static final String INSERTION_SYMBOL_COLOR = "000000";
	public static final String DUPLICATION_COLOR_STRING = "A0FFA0";
	public static final String EXON_COLOR_STRING = "FFFFF0";
	public static final String READ_MATE_DIFF_CHR_COLOR_STRING = "E8A050";
	
	public static final Color READ_WITH_PAIR_DATA_BORDER_COLOR = Color.black;
	
	public static final String GFF_DATA_TYPE = "gff";
	
	public static final String READ_MATE_DIFF_CHR_DATA_TYPE = "dna-data";
	// names of reads stripes in Display
	public static final String READS_STRIPE_DESCRIPTION = "reads";
	public static final String READS_SUPPORTING_VARIANT_STRIPE_DESCRIPTION = "reads-variant";
	public static final String READS_NOT_SUPPORTING_VARIANT_STRIPE_DESCRIPTION = "reads-no_variant";
	public static final String READS_MATES_DIFF_CHR_STRIPE_DESCRIPTION = "reads-mate-diff_chr";
	
	// used to make insertions easier to select
	public static final int INSERTION_SYMBOL_START_CORRECTION = 2;
	public static final int INSERTION_SYMBOL_END_CORRECTION = -1;
	
	// use neutral color if no data is available
	public static final String DEFAULT_SPECIES_COLOR = "A0A0A0";
	public static final String[] DEFAULT_SPECIES_COLORS = {"FFA0A0", "A0FFA0", "A0A0FF", 
		"FFFFA0", "FF7FA0", "E066FF", "D2691E", "00F5FF", "A0A0A0"};
	
	// use neutral color if no data is available
	public static final String DEFAULT_GFF_COLOR = EXON_COLOR_STRING;
	public static final String[] DEFAULT_GFF_COLORS = {"FFA0A0", "A0FFA0", "A0A0FF", 
			"FFFFA0", "FF7FA0", "E066FF", "D2691E", "00F5FF", "A0A0A0"};
	
	public static final String NCBI_BASE_URL = "https://www.ncbi.nlm.nih.gov/gene/?term=";
	//public static final String SPECIES_TERM = "+polar+bear";
	public static final String REF_SPECIES_FILENAME = "ref_spec.txt";
	
	// used by derBrowser2
	public static final String MATE_INFO_SPLIT_CHAR = "&";
	public static final String MATE_INFO_ENTRIES_SPLIT_CHAR = ":";
	public static final String READ_ID_SPLIT_CHAR = "#";
	
	// used in reads key
	public static final String[] SV_COLORS = {DELETION_COLOR_STRING, 
		DELETION_CIGAR_COLOR_STRING, INSERTION_COLOR_STRING, INVERSION_COLOR_STRING, 
		DUPLICATION_COLOR_STRING, DEFAULT_GRAY_COLOR_STRING};
	public static final String[] SV_NAMES = {"Deletion", "Deletion from CIGAR String",
		"Insertion", "Inversion", "Duplication", "Unknown or Concordant"}; 
	
	// used by GNDP for obtaining color strings
	public static final String DELETION_TYPE = "DEL";
	public static final String INSERTION_TYPE = "INS";
	// these do not occur in vcf files, keep for backward compatibility with tab separated files
	public static final String INVERSION_FORWARD_TYPE = "INV_F";
	public static final String INVERSION_REVERSE_TYPE = "INV_R";
	public static final String INVERSION_TYPE = "INV";
	public static final String DUPLICATION_TYPE = "DUP";
	
	public static final String[] SV_ONLY_NAMES = {DELETION_TYPE, INSERTION_TYPE,
		INVERSION_TYPE, DUPLICATION_TYPE};
	
	public static java.util.List<String> svOnlyNamesList = Arrays.asList(SV_ONLY_NAMES);
	
	// used by Sequence in derBrowser2
	public static final char SEQUENCE_PLACEHOLDER_CHAR = '&';
	public static final char DELETION_PLACEHOLDER_CHAR = ' ';
	public static final char N_PLACEHOLDER_CHAR = '.';
	public static final char P_PLACEHOLDER_CHAR = '*';
	
	public static final String SEQUENCE_PLACEHOLDER = Character.toString(SEQUENCE_PLACEHOLDER_CHAR);
	public static final String DELETION_PLACEHOLDER = Character.toString(DELETION_PLACEHOLDER_CHAR);
	public static final String N_PLACEHOLDER = Character.toString(N_PLACEHOLDER_CHAR);
	public static final String P_PLACEHOLDER = Character.toString(P_PLACEHOLDER_CHAR);
	
	//public static final Color SEQ_N_COLOR = Color.white;
	public static final Color SEQ_N_COLOR = new Color(248, 248, 248);
	public static final Color SEQ_A_MISMATCH_COLOR = Color.green;/* orange */
	public static final Color SEQ_T_MISMATCH_COLOR = Color.red;/* yellow */
	public static final Color SEQ_G_MISMATCH_COLOR = Color.blue;/* magenta */
	public static final Color SEQ_C_MISMATCH_COLOR = Color.yellow;/* cyan */
	public static final Color SEQ_PLUS_COLOR = Color.magenta;
	public static final Color SEQ_DASH_COLOR = Color.white;
	
	// Fixes bug where reference does not reach right end. Index error should not be encountered
	// since sequences end with a large number of "N" entries which are not of interest
	public static final int REFERENCE_SEQUENCE_END_INDEX_CORR = 2;
	
	// if flag information is necessary for display of information or troubleshooting
	public static final boolean FLAG_INFORMATION_NEEDED = false;
	//public static final boolean FLAG_INFORMATION_NEEDED = true;
	
	public static final boolean GRAPH_UNMAPPED_READS = false;
	public static final boolean GRAPH_SECONDARY_ALIGNMENT_READS = false;
	public static final boolean GRAPH_NOT_PASSING_READS = false;
	public static final boolean GRAPH_DUPLICATE_READS = false;
	public static final boolean GRAPH_SUPPLEMENTARY_ALIGNMENT_READS = false;
	
	// length of rectangle indicating mate is offscreen and direction of mate
	public static final int OFFSCREEN_MATE_INDICATOR_LENGTH = 20;
	/**
	 * This padding prevents a piece being placed immediately next to a piece with an offscreen mate
	 * which would then appear as a mated pair. 
	 */
	public static final int OFFSCREEN_MATE_PADDING_LENGTH = 20;
	public static final double PADDING_PERCENT = 0.005;
	
	public static final int MERGED_INTERVALS_START_COLUMN_INDEX = 2;
	public static final int MERGED_INTERVALS_END_COLUMN_INDEX = 3;
	
	public static final double SEQSTEP_CUTOFF = 1;// original 2.
	public static final int SEQ_SHOW_STEP_LIMIT = 8;
	
	// correct for indices in file names not matching indices in merged intervals file
	public static final boolean CORRECT_MERGED_INTERVALS_INDICES = true;
	// true for bear false for single cell
	public static final boolean ADJUST_INDICES = true;
	
	public static final int INTERVALS_SUBFILES_SIZE = 100;
	public static final double MAX_P_VALUE = 1;
	public static final int MAX_INTERVAL_WIDTH = 5000;
	//public static final int MAX_INTERVAL_WIDTH = 20000;
	// use if no maximum
	//public static final int MAX_INTERVAL_WIDTH = Integer.MAX_VALUE;
	
	//public static final int MINIMUM_INTERVAL = 300;
	public static final int MINIMUM_INTERVAL_FIRST_WINDOW = 1000;
	public static final int MINIMUM_INTERVAL = 800;
	public static final int MINIMUM_TWO_WINDOW_INTERVAL = 800;
	public static final int MINIMUM_VIEW_AS_TWO_INTERVAL = 80;
	public static final double VARIANT_MULTIPLIER = 5;
	
	// used by derBrowser1
	public static final int SCALE_POINTS_Y_POS = 472;
	public static final int POINTS_OBJ_PAD = 3;
	public static final int ZERO_P_VALUE_Y_OFFSET = 3;
	// used by derBrowser1 and GNDP
	public static final int POINT_SIZE = 6;
	
	// enables dialog used to highlight a user selected area in reads window
	public static final boolean USE_SET_AREA_DIALOG = true;
	
	public static final boolean EXON_DATA_EXISTS = true;
	public static final boolean DRAW_EXON_LINE_BACK = false;
	public static final String EXON_SAMPLE_NAME = "Exons";
	public static final String EXON_NAME_READS_FILE = "Exon";
	
	public static final String REF_NAME_READS_FILE = "ref";
	public static final String REF_ID_READS_FILE = "Reference";

	public static final String GFF_TYPE = "exon";
	//public static final String GFF_TYPE = "gene";
	//public static final String GFF_TYPE = "mRNA";
	//public static final String GFF_TYPE = "CDS";
	
	// udes by GNDP variant effect prediction
	public static final int NUCL_PROTEIN_SEQ_TRANS_TYPE_1_LETTER = 1;
	public static final int NUCL_PROTEIN_SEQ_TRANS_TYPE_3_LETTER = 3;	
	public static final String STANDARD_CODON_TABLE_TYPE = "Standard";
	public static final String STANDARD_CODON_TABLE_PATH = "etc/Standard_Code-NCBI.txt";
	
	// used for Options -> MATES_DIFF_CHR_MENU_ITEM
	public static final String MATES_DIFF_CHR_MENU_ITEM = "Show Reads with Mates on Different Chr";
	public static final String MATES_SAME_CHR_DESCRIPTION = "-mates_same_chr";
	
	// Menu items - Edit -> Filter menu
	public static final String FILTER_MENU_NAME = "Filter";
	public static final String FILTER_OPTIONS_TITLE = "Filter Options";
	public static final String OVERLAP_ITEM_NAME = "Only Overlap";
	public static final String NEARBY_ITEM_NAME = "Only Nearby";
	public static final String DELETIONS_ITEM_NAME = "DEL";
	public static final String DUPLICATIONS_ITEM_NAME = "DUP";
	public static final String INSERTIONS_ITEM_NAME = "INS";
	public static final String INVERSIONS_ITEM_NAME = "INV";
	
	public static final String VAR_VS_FEATURES_LABEL = "Variants vs Features";
	public static final String VAR_TYPES_LABEL = "Variant Types";
		
	public static final int DEFAULT_FASTA_LINE_LENGTH = 80;
	public static final String VAR_DIRECTORY_NAME = "var";
	public static final String POINTS_DIRECTORY_NAME = "points";
	public static final String READS_DIRECTORY_NAME = "reads";
	public static final String VAR_DIRECTORY = VAR_DIRECTORY_NAME + "/";
	public static final String POINTS_DIRECTORY = POINTS_DIRECTORY_NAME + "/";
	public static final String READS_DIRECTORY = READS_DIRECTORY_NAME + "/";
	public static final String REF_SUBFILES_DIRECTORY = "ref_subfiles/";
	public static final String REF_SUBFILES_DIRECTORY_CDS = "ref_subfiles-cds/";
	public static final String INTERVALS_BASE_FILENAME = "intervals";
	public static final String INTERVALS_FILENAME_EXTENSION = ".txt";
	public static final String INTERVALS_FILENAME = INTERVALS_BASE_FILENAME + INTERVALS_FILENAME_EXTENSION;
	// Used to generate ref, SAM files and reads files for entries from BND ALT entries and possible other reasons 
	public static final String INTERVALS_NOT_IN_TABLE_BASE_FILENAME = "intervals_non_table";
	public static final String INTERVALS_NOT_IN_TABLE_FILENAME = INTERVALS_NOT_IN_TABLE_BASE_FILENAME + INTERVALS_FILENAME_EXTENSION;
	public static final String DATA_TABLE_BASE_FILENAME = "table_data";
	public static final String DATA_TABLE_EXTENSION = ".txt";
	public static final String DATA_TABLE_FILENAME = DATA_TABLE_BASE_FILENAME + DATA_TABLE_EXTENSION;
	public static final String DATA_TABLE_HEADER_FILENAME = "table_header_data.txt";
	public static final String GFF_FILENAME = "gff_data.txt";
	public static final String GFF_CDS_FILENAME = "gff_cds_data.txt";
	public static final String DEFAULT_VCF_FILE_TYPE = ".vcf";
	public static final String DEFAULT_SPECIES_NAME = "None given";
	public static final String FASTA_INDICES_FILE = "fasta_indices.txt";
	public static final String DEFAULT_SPECIES_COLOR_FILE = "species_color.csv";
	public static final String DEFAULT_INCLUDE_SPECIES_IN_SAMPLE_NAME = "0"; // maintain backward compatibility for files from before field was added
	
	public static final String[] REQUIRED_PROJECT_FOLDERS = {VAR_DIRECTORY_NAME, POINTS_DIRECTORY_NAME,
		READS_DIRECTORY_NAME}; 
	public static final String[] REQUIRED_PROJECT_FOLDERS_SINGLE_SAMPLE = {
		READS_DIRECTORY_NAME}; 
	public static final String[] REQUIRED_PROJECT_FILES = {INTERVALS_FILENAME, DATA_TABLE_FILENAME};
	
	/*
	 * Constants used in CheckConfig.java to compare the os name with the system
	 */
	public static final String OSNAME_MAC = "Mac OS X";
	public static final String OSNAME_WINDOWS = "Windows";
	public static final String OSNAME_LINUX = "Linux";
	public static final String HOMEDIR_FOLDER = "GenomeNavigatorData";
	public static final String PATH_PREFIX_WINDOWS = "C:/Users/";
	public static final String PATH_SUFFIX_WINDOWS = "/AppData/Local/";

	public static final String PATH_PREFIX_WINDOWS_XP = "C:/Documents and Settings/";
	public static final String PATH_SUFFIX_WINDOWS_XP = "/Local Settings/Application Data/";
	
	//Points towards to local etc folder in the project directory
	public static final String PATH_TO_ETC = "etc/";
	public static final boolean COPY_DATA = false;
	
	//Font Color of buttons in all derBrowser windows
	public static final Color BUTTON_FONT_COLOR = Color.white;
		
	//Color of slider arrow. Called inside aSlider.java
	public static final Color SLIDER_ARROW_COLOR = Color.white;
		
	public static final Color TABLE_HEADER_BACKGROUND_COLOR = Color.gray;
	public static final Color TABLE_HEADER_FOREGROUND_COLOR = Color.white;
	
	public static final Color UI_BACKGROUND_COLOR = Color.DARK_GRAY;
	public static final Color UI_FOREGROUND_COLOR = Color.lightGray;
	
	//Link to website - this link is no good!
	public static final String LINK_TO_GNWEBSITE = "http://grigoriev.rutgers.edu/~grigoriev/der/gn/about.html";

	// Turns tool tips on or off in DataTableCellRenderer
	// Currently these tool tips are not working correctly. If fixed, they are
	// useful but currently of low priority. 
	public static final boolean SHOW_DATA_TABLE_CELL_TOOL_TIPS = false;		
	
	// Maybe make this true for releases?
	//public static final boolean USE_EXIT_PROMPT = true;
	// Please keep this as false for development purposes. It is a major
	// nuisance to have to click a button to exit when testing.
	public static final boolean USE_EXIT_PROMPT = false;
	
}
