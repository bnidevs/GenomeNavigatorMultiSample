package presentation;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class UtilityMethods {
	
	/*
	 * Reads two window functions
	 */
	// used to calculate two intervals if range > max or one interval if in range
	public static ArrayList<Interval> readsWindowRanges(int minFrom, int maxTo) {
//		System.out.println("min from " + minFrom);
//		System.out.println("max to " + maxTo);
		ArrayList<Interval> intervals = new ArrayList<Interval>();
		Interval initialInterval = new Interval(minFrom, maxTo);
		if (initialInterval.size() > Constants.MAX_INTERVAL_WIDTH) {
//			System.out.println("start " + initialInterval.start);
//			System.out.println("end " + initialInterval.end);
//			System.out.println("size " + initialInterval.size());
			intervals = calculateReadsTwoWindowRanges(minFrom, maxTo);
		} else {
			Interval interval = readsWindowRangeForIntervalsFile(minFrom, maxTo);
//			System.out.println("right int left " + interval.start);
//			System.out.println("right int right " + interval.end);
			intervals.add(interval);
		}
		
		return intervals;
	}
	
	// used to calculate left and right intervals for two window display
	public static ArrayList<Interval> calculateReadsTwoWindowRanges(int minFrom, int maxTo) {
		ArrayList<Interval> intervals = new ArrayList<Interval>();
		int width = Constants.MINIMUM_TWO_WINDOW_INTERVAL/2;
		Interval intervalLeft = new Interval(minFrom - width, minFrom + width);
		Interval intervalRight = new Interval(maxTo - width, maxTo + width);
//		System.out.println("left " + intervalLeft.start);
//		System.out.println("right " + intervalLeft.end);
		intervals.add(intervalLeft);
//		System.out.println("left int left " + intervalRight.start);
//		System.out.println("left int right " + intervalRight.end);
		intervals.add(intervalRight);
		
		return intervals;
	}
	
	/*
	 * End reads two window functions
	 */
	
	public static boolean isIntervalWithinMergedIntervals(ArrayList<Interval> mergedIntervals, Interval interval) {
		int startIndex = interval.start;
		int endIndex = interval.end;
//		if (Constants.CORRECT_MERGED_INTERVALS_INDICES) {
//			startIndex -= 1;
//			endIndex -= 1;
//		}
		boolean inRange = false;
		for (int i = 0; i < mergedIntervals.size(); i++) {
			if (startIndex >= mergedIntervals.get(i).start &&
					endIndex <= mergedIntervals.get(i).end) {
				inRange = true;
			}
		}
//		if (!inRange) {
//			System.out.println(interval);
//		}
		
		return inRange;
	}

	
	/**
	 * Tests if given interval is inside of test interval or equal to test interval
	 * Used by DataTableFrame and derBrowser
	 * @param testInterval
	 * @param interval
	 * @return
	 */
	public static boolean isIntervalInRange(Interval interval, Interval testInterval) {
		if (interval.start >= testInterval.start && interval.end <= testInterval.end) {
			return true;
		}
		
		return false;
		
	}
	
	/**
	 * Tests if end given interval is > start of test interval or start of
	 * given interval < end of test interval
	 * Used by DataTableFrame 
	 * @param testInterval
	 * @param interval
	 * @return
	 */
	public static boolean isIntervalVisible(Interval interval, Interval testInterval) {
		if (interval.start >= testInterval.start && interval.end <= testInterval.end) {
			return true;
		}
		
		return false;
		
	}
	
	/*
	 * Method used by DataTableFrame, derBrowser and derBrowser1
	 */
	public static boolean isIntervalPortionVisible(Interval interval, Interval testInterval) {
		// start or end inside
		if ((interval.start >= testInterval.start && interval.start <= testInterval.end) || 
				(interval.end >= testInterval.start && interval.end <= testInterval.end)) {
			return true;
			// interval larger than test interval
		} else if (interval.start < testInterval.start && interval.end > testInterval.end) {
			return true;
			// interval entirely inside test interval
		} else if (interval.start > testInterval.start && interval.end < testInterval.end) {
			return true;
		}
		
		return false;
		
	}
	
	/*
	 * Method used in DataTableFrame
	 */
	public static Interval windowRange(int minFrom, int maxTo) {
		int avgValue = (minFrom + maxTo)/2;
		int left = (int) (avgValue - 1.5*(avgValue - minFrom));
		if (left < 0) {
			left = 0;
		}
		//System.out.println(left);
		int right = (int) (avgValue + 1.5*(maxTo - avgValue));
		//System.out.println(right);
		if (right - left < Constants.MINIMUM_INTERVAL_FIRST_WINDOW) {
			left = avgValue - Constants.MINIMUM_INTERVAL_FIRST_WINDOW/2;
			right = avgValue + Constants.MINIMUM_INTERVAL_FIRST_WINDOW/2;
		}
		if (left < 0) {
			right += -left;
			left = 0;
		}
		return new Interval(left, right);
	}
	
	public static Interval readsWindowRange(int minFrom, int maxTo) {
//		System.out.println("min from " + minFrom);
//		System.out.println("max to " + maxTo);
		Interval interval = readsWindowRangeForIntervalsFile(minFrom, maxTo);
//		System.out.println("left " + interval.start);
//		System.out.println("right " + interval.end);
		return interval;
	}
	
	// method used to write intervals file for all variants when no intervals of interest are provided
	public static Interval readsWindowRangeForIntervalsFile(int minFrom, int maxTo) {
		int avgValue = (minFrom + maxTo)/2;
		int left = (int) (avgValue - Constants.VARIANT_MULTIPLIER*(avgValue - minFrom));
		if (left < 0) {
			left = 0;
		}
		//System.out.println(left);
		int right = (int) (avgValue + Constants.VARIANT_MULTIPLIER*(maxTo - avgValue));
		//System.out.println(right);
		if (right - left < Constants.MINIMUM_INTERVAL) {
			left = avgValue - Constants.MINIMUM_INTERVAL/2;
			right = avgValue + Constants.MINIMUM_INTERVAL/2;
		} else {
			
		}
		if (left < 0) {
			right += -left;
			left = 0;
		}
		return new Interval(left, right);
	}
	
	/*
	 * Method used for colors in key frames and GNDP for variant and p-value window colors
	 */
	public static LinkedHashMap<String, String> speciesColorMap(String file) {
		CSVSpeciesColorFileReader reader = new CSVSpeciesColorFileReader();
		LinkedHashMap<String, String> speciesColorMap = new LinkedHashMap<String, String>();
		File f = new File(file);
		if (f.exists()) {
			reader.readFile(f.getAbsolutePath(), speciesColorMap);
		}
		
		return speciesColorMap;
	}
	
	/*
	 * Method reads file to get species name used in generating URL for NCBI if species is given
	 */
	public static String refSpec(String file) {
		String entry = "";
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));
			entry = reader.readLine();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entry;
	}
	
	/*
	 * Method used to generate URL for NCBI, replaces spaces with +
	 */
	public static String queryStringFromRefSpec(String refSpec) {
		String entry = "";
		if (refSpec != null && refSpec.length() > 0) {
			entry = "+" + refSpec.replaceAll(" ", "+");
		}
		
		return entry;
		
	}
	
	public static LinkedHashMap<String, String> fileSampleNameMap(String file) {
		CSVFileSampleFileReader reader = new CSVFileSampleFileReader();
		LinkedHashMap<String, String> fileSampleNameMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> fileAliasMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> sampleAliasMap = new LinkedHashMap<String, String>();
		File f = new File(file);
		reader.readFile(f.getAbsolutePath(), fileSampleNameMap, fileAliasMap, sampleAliasMap);
		
		return fileSampleNameMap;
	}
	
	/**
	 * Creates key frames that are opened by KEY button in each of 3 derBrowser classes
	 * @param type
	 * @param speciesColorMap
	 * @return
	 */
	public static JFrame createKeyBrowser(String type, LinkedHashMap<String, String> speciesColorMap) {
		final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_16).getImage()); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_32).getImage());
		
		derBrowserKey browser = new derBrowserKey();
		derBrowserKey.frame = new JFrame(); 
		JFrame frame = derBrowserKey.frame;
		frame.setTitle(Constants.TITLE + " - " + derBrowserKey.TITLE + " - " + type);
		frame.setIconImages(icons);
		Container c = derBrowserKey.frame.getContentPane();
		c.setBackground(Color.white);
		frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, Constants.KEY_FRAME_BLACK_BORDER_THICKNESS));
		frame.add(browser);
		frame.pack();
		
		browser.speciesColorMap = speciesColorMap;
		browser.type = type;
		browser.init();
		int heightAdjustment = 0;
		if (speciesColorMap != null) {
			heightAdjustment = speciesColorMap.size() * 20;
		}
		int height = Constants.KEY_FRAME_HEIGHT + heightAdjustment;
		int width = Constants.KEY_FRAME_WIDTH;
		if (type.equals(Constants.READS_WINDOW_TITLE)) {
			height = Constants.KEY_FRAME_READS_HEIGHT;
			width = Constants.KEY_FRAME_READS_WIDTH;
		} else if (type.equals(Constants.P_VALUE_WINDOW_TITLE)) {
			height += Constants.P_VALUE_KEY_FRAME_TEXT_HEIGHT_CORRECTION;			
		} else if (type.equals(Constants.VARIANTS_WINDOW_TITLE)) {
			height += Constants.VARIANTS_KEY_FRAME_TEXT_HEIGHT_CORRECTION;			
		}
		frame.setSize(width, height);
		if (type.equals(Constants.P_VALUE_WINDOW_TITLE)) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			
			//int y = (screenSize.height - frame.getSize().height)/2;
			if (screenSize.width < Constants.VARIANTS_WINDOW_FRAME_WIDTH + Constants.P_VALUE_WINDOW_FRAME_WIDTH) {
				frame.setLocation((screenSize.width - frame.getSize().width) - 10, 0);
			} else {
				frame.setLocation(screenSize.width/2, 0);
			}
		}
		frame.setResizable(false);
		derBrowserKey.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		//frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setVisible(false);
		return frame;   
	}
	
	/*
	 * from original derBrowser class
	 */
	public static Color hexColor(String number)
	{
		try
		{
			int z = Integer.parseInt(number, 16);
			return( new Color(
					(z >>> 16) & 0xff,
					(z >>> 8) & 0xff,
					(z & 0xff)));
		} 

		catch (NumberFormatException e)
		{
			if(number.equals("DER BROWSER")) return null;
			return Color.green;
		}
	}
	
	public static String createSampleName(String sampleName, String type) {
		return sampleName + Constants.SAMPLE_NAME_TYPE_CONNECTOR + type;
	}
	
	/**
	 * Determine if value is integer
	 * @param value
	 * @return
	 */
	public static boolean isInteger(String value) {
		try
		{
			Integer.parseInt(value); 
		}
		catch (NumberFormatException nfe) {
			return false;
		} 
		return true;
		
	}
	
	/**
	 * Determine if value is numeric
	 * @param value
	 * @return
	 */
	public static boolean isNumber(String value) {
		try
		{
			Double.parseDouble(value); 
		}
		catch (NumberFormatException nfe) {
			return false;
		} 
		return true;
		
	}
	
	public static void showMessage(JDialog dialog, String message, 
			String messageTitle, int messageType) {
		dialog.setAlwaysOnTop(false);
		// running from gui
		JOptionPane.showMessageDialog(null,                
				message,                
				messageTitle,                                
				messageType);
		dialog.setAlwaysOnTop(true);
	}
	
	// from https://stackoverflow.com/questions/16433915/how-to-copy-file-from-one-location-to-another-location
	public static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
	// based on https://stackoverflow.com/questions/16433915/how-to-copy-file-from-one-location-to-another-location
	public static void copyFile(File source, File dest) {
		try {
			copyFileUsingStream(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * based on https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
	 * Type can be an extension or an ending such as ".SV_INDEL.vcf"
	 */
	public static ArrayList<String> fileList(String directory, String type) {
		ArrayList<String> fileList = new ArrayList<String>();
		File[] files = new File(directory).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 

		if (files != null) {
			for (File file : files) {
			    if (file.isFile() && file.getName().endsWith(type)) {
			    	fileList.add(file.getName());
			    }
			}
		} else {
			System.out.println("Files not found");
		}
		
		return fileList;
	}
	
	public static boolean isFeature(One selObj) {
		if (selObj.type != null) {
			System.out.println(selObj.type);
			if (selObj.type.equals(Constants.GFF_DATA_TYPE)) {
				System.out.println(selObj.type);
				return true;
			}
			// maintain backward compatibility for old data
		} else if (selObj.color.equals(UtilityMethods.hexColor(Constants.EXON_COLOR_STRING))) {
			return true;
		}
		return false;
		
	}
	
	public static void openURL(String url) {
		if(Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(url));
			}catch(IOException | URISyntaxException u) {
				u.printStackTrace();
			}
		}
		//Creates a separate process to run the application using xdg-open
		//xdg-open will open a file in user preferred application according to the file type
		else {	
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("xdg-open" + url);
			}catch(IOException u) {
				u.getStackTrace();
			}
		}
	}
	
	public static String getSampleNameFromVarName(String varName) {
		if (varName.endsWith("-DEL")) {
			return varName.replace("-DEL", "");
		} else if (varName.endsWith("-INS")) {
			return varName.replace("-INS", "");
		} else if (varName.endsWith("-INDEL_DEL")) {
			return varName.replace("-INDEL_DEL", "");
		} else if (varName.endsWith("-INDEL_INS")) {
			return varName.replace("-INDEL_INS", "");
		} else if (varName.endsWith("-INV")) {
			return varName.replace("-INV", "");	
		} else if (varName.endsWith("-DUP")) {
			return varName.replace("-DUP", "");	
		}
		
		return varName;
	}
	
	/**
	 * false if ProjectConstants DRAW_DATA_AVAILABLE_READ_BORDERS = false
	 * else check object
	 * @param one
	 * @return
	 */
	public static boolean isDataAvailableForObject(One one) {
//		if (one.color.equals(UtilityMethods.hexColor(Constants.READ_MATE_DIFF_CHR_LINK_COLOR_STRING))) {
//			return true;
		if (!ProjectConstants.DRAW_DATA_AVAILABLE_READ_BORDERS) {
			return false;
		} else {
			if (one.type.equals(Constants.READ_MATE_DIFF_CHR_DATA_TYPE)) {
				return true;
			} else if (one.color.equals(UtilityMethods.hexColor(Constants.DELETION_COLOR_STRING)) &&
					isMateDrawnOffscreen(one)) {
				return true;
			} else if (one.color.equals(UtilityMethods.hexColor(Constants.INSERTION_COLOR_STRING)) &&
					isMateDrawnOffscreen(one)) {
				return true;
			} else if (one.color.equals(UtilityMethods.hexColor(Constants.INVERSION_COLOR_STRING)) &&
					isMateDrawnOffscreen(one)) {
				return true;
			} else if (one.color.equals(UtilityMethods.hexColor(Constants.DUPLICATION_COLOR_STRING)) &&
					isMateDrawnOffscreen(one)) {
				return true;
			}
		}

		return false;

	}
	
	public static boolean isMateDrawnOffscreen(One one) {
		//System.out.println("type " + one.type);
		if (one.type.endsWith("pl_off")) {
			return true;
		} else if (one.type.endsWith("pr_off")) {
			return true;
		}
			
		return false;
	}
	
	public static JMenu testJmenu() {
		JMenu help = new JMenu();
		
		return help;
		
	}
	
	public static JMenu helpMenuFunc(final AboutDialog aboutDialog) {
		
		JMenu helpMenu = new JMenu("Help");
		//helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.setOpaque(true);

		JMenuItem helpTopics = new JMenuItem("Help Topics");
		helpMenu.add(helpTopics);
		//helpTopics.setMnemonic(KeyEvent.VK_H);

		helpTopics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				//Opens URL on browser using two methods
				//Check if default desktop API is supported on the system and then open the browser
				UtilityMethods.openURL(Constants.LINK_TO_GNWEBSITE);
			}    	     
		});

		JMenuItem aboutMenu = new JMenuItem("About Genome Navigator");
		helpMenu.add(aboutMenu);
		//aboutMenu.setMnemonic(KeyEvent.VK_A);

		aboutMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				aboutDialog.setVisible(true);			
			}    	     
		});
		
		return helpMenu;
	}
	
	public static ActionListener licenseButtonActionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.out.println("test");
		}
	};
	
	public static JMenu miscOptionsMenuFuncBrowser(final derBrowser browser) {
		JMenu miscOptionsMenu = new JMenu("Misc Options");
		miscOptionsMenu.setOpaque(true);
		
		final JCheckBoxMenuItem hiStripesCheckBox = new JCheckBoxMenuItem("Highlight Stripes when Selecting");
		//final JCheckBoxMenuItem drBorderCheckBox = new JCheckBoxMenuItem("Draw Borders around Rectangles");
		final JCheckBoxMenuItem showCursorCheckBox = new JCheckBoxMenuItem("Show Cursor X Coordinate");
//		JCheckBoxMenuItem zoomCheckBox = new JCheckBoxMenuItem("Set Zoom Increment to X2");
//		JCheckBoxMenuItem shiftTextCheckBox = new JCheckBoxMenuItem("Shift Text Down");
//		
		miscOptionsMenu.add(hiStripesCheckBox);
		//miscOptionsMenu.add(drBorderCheckBox);
		miscOptionsMenu.add(showCursorCheckBox);
		showCursorCheckBox.setSelected(true);
		
		hiStripesCheckBox.setSelected(true);
		//drBorderCheckBox.setSelected(true);
		showCursorCheckBox.setSelected(true);
		
		hiStripesCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(hiStripesCheckBox.isSelected() == false) {
					
				}
			}
		});
		
//		drBorderCheckBox.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				if(drBorderCheckBox.isSelected() == false) {
//					derBrowser.drawBorder = false;
//				}else {
//					derBrowser.drawBorder = true;
//				}
//			}
//		});
		
		showCursorCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(showCursorCheckBox.isSelected() == false) {
					derBrowser.showCoord = false;
				}else { 
					derBrowser.showCoord = true;
				}
			}
		});
		
		return miscOptionsMenu;
	}
	
	public static JMenu miscOptionsMenuFuncBrowser1(derBrowser1 browserFont1) {
		JMenu miscOptionsMenu = new JMenu("Misc Options");
		miscOptionsMenu.setOpaque(true);
		JCheckBoxMenuItem hiStripesCheckBox = new JCheckBoxMenuItem("Highlight Stripes when Selecting");
		//final JCheckBoxMenuItem drBorderCheckBox = new JCheckBoxMenuItem("Draw Borders around Rectangles");
		final JCheckBoxMenuItem showCursorCheckBox = new JCheckBoxMenuItem("Show Cursor X Coordinate");
//		JCheckBoxMenuItem zoomCheckBox = new JCheckBoxMenuItem("Set Zoom Increment to X2");
//		JCheckBoxMenuItem shiftTextCheckBox = new JCheckBoxMenuItem("Shift Text Down");
//		
		miscOptionsMenu.add(hiStripesCheckBox);
		//miscOptionsMenu.add(drBorderCheckBox);
		miscOptionsMenu.add(showCursorCheckBox);
		
		hiStripesCheckBox.setSelected(true);
		//drBorderCheckBox.setSelected(true);
		showCursorCheckBox.setSelected(true);
		
//		drBorderCheckBox.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				if(drBorderCheckBox.isSelected() == false) {
//					derBrowser1.drawBorder = false;
//				}else {
//					derBrowser1.drawBorder = true;
//				}
//			}
//		});
		
		showCursorCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(showCursorCheckBox.isSelected() == false) {
					derBrowser1.showCoord = false;
				}else {
					derBrowser1.showCoord = true;
				}
			}
		});
		
		return miscOptionsMenu;
	}
	
	public static JMenu miscOptionsMenuFuncBrowser2(derBrowser2 browserFont2) {
		JMenu miscOptionsMenu = new JMenu("Misc Options");
		miscOptionsMenu.setOpaque(true);
		
		final JCheckBoxMenuItem hiStripesCheckBox = new JCheckBoxMenuItem("Highlight Stripes when Selecting");
//		final JCheckBoxMenuItem drBorderCheckBox = new JCheckBoxMenuItem("Draw Borders around Rectangles");
		final JCheckBoxMenuItem showCursorCheckBox = new JCheckBoxMenuItem("Show Cursor X Coordinate");
//		JCheckBoxMenuItem zoomCheckBox = new JCheckBoxMenuItem("Set Zoom Increment to X2");
//		JCheckBoxMenuItem shiftTextCheckBox = new JCheckBoxMenuItem("Shift Text Down");
//		
		miscOptionsMenu.add(hiStripesCheckBox);
		//miscOptionsMenu.add(drBorderCheckBox);
		miscOptionsMenu.add(showCursorCheckBox);
		
		hiStripesCheckBox.setSelected(true);
		//drBorderCheckBox.setSelected(true);
		showCursorCheckBox.setSelected(true);
		
//		drBorderCheckBox.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				if(drBorderCheckBox.isSelected() == false) {
//					derBrowser2.drawBorder = false;
//				}else {
//					derBrowser2.drawBorder = true;
//				}
//			}
//		});
		
		hiStripesCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(hiStripesCheckBox.isSelected() == false) {
					derBrowser2.hiliteOn = false;
				}else {
					derBrowser2.hiliteOn = true;
				}
			}
		});
		
		showCursorCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(showCursorCheckBox.isSelected() == false) {
					derBrowser2.showCoord = false;
				}else {
					derBrowser2.showCoord = true;
				}
			}
		});
		
		return miscOptionsMenu;
	}
	
	public static JMenu selectFontsMenuFuncBrowser(){
		JMenu selectFontsMenu = new JMenu("Select Fonts");
		JMenuItem dialogMenuScaleItem = new JMenuItem("Dialog");
		JMenuItem sansSerifMenuScaleItem = new JMenuItem("SansSerif");
		JMenuItem serifMenuScaleItem = new JMenuItem("Serif");
		JMenuItem monospacedMenuScaleItem = new JMenuItem("Monospaced");
		JMenuItem dialogInputMenuScaleItem = new JMenuItem("DialogInput");
		
		JMenuItem dialogMenuObjItem = new JMenuItem("Dialog");
		JMenuItem sansSerifMenuObjItem = new JMenuItem("SansSerif");
		JMenuItem serifMenuObjItem = new JMenuItem("Serif");
		JMenuItem monospacedMenuObjItem = new JMenuItem("Monospaced");
		JMenuItem dialogInputMenuObjItem = new JMenuItem("DialogInput");
		
		selectFontsMenu.setOpaque(true);
		selectFontsMenu.setForeground(Constants.UI_FOREGROUND_COLOR);
		selectFontsMenu.setBackground(Constants.UI_BACKGROUND_COLOR);
		
		JMenu scaleFontItem = new JMenu("Scale/Button Font (" + Constants.SCALE_FONT_TYPE_STRING + " " + Constants.SCALE_FONT_SIZE + "pt)");
		scaleFontItem.setOpaque(true);
		
//		for(int i=0; i<fontListArray.length; i++) {
//			JMenuItem item = new JMenuItem(fontListArray[i]);
//			scaleFontItem.add(item);
//		}
		
		JMenu objectFontItem = new JMenu("Object Font (" + Constants.OBJ_FONT_TYPE_STRING + " " + Constants.OBJ_FONT_SIZE + "pt)");
		objectFontItem.setOpaque(true);
//		for(int i=0; i<fontListArray.length; i++) {
//			JMenuItem item = new JMenuItem(fontListArray[i]);
//			objectFontItem.add(item);
//		}
		dialogMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser.scaleF = new Font("Dialog", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		sansSerifMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser.scaleF = new Font("SansSerif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		serifMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser.scaleF = new Font("Serif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		monospacedMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser.scaleF = new Font("Monospaced", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		dialogInputMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser.scaleF = new Font("DialogInput", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		dialogMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser.objF = new Font("Dialog", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		sansSerifMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser.objF = new Font("SansSerif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		serifMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser.objF = new Font("Serif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		monospacedMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser.objF = new Font("Monospaced", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		dialogInputMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser.objF = new Font("DialogInput", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		scaleFontItem.add(dialogMenuScaleItem);
		scaleFontItem.add(sansSerifMenuScaleItem);
		scaleFontItem.add(serifMenuScaleItem);
		scaleFontItem.add(monospacedMenuScaleItem);
		scaleFontItem.add(dialogInputMenuScaleItem);
		
		objectFontItem.add(dialogMenuObjItem);
		objectFontItem.add(sansSerifMenuObjItem);
		objectFontItem.add(serifMenuObjItem);
		objectFontItem.add(monospacedMenuObjItem);
		objectFontItem.add(dialogInputMenuObjItem);
		
		selectFontsMenu.add(scaleFontItem);
		selectFontsMenu.add(objectFontItem);
		
		return selectFontsMenu;
	}
	
	public static JMenu selectFontsMenuFuncBrowser1(){
		JMenu selectFontsMenu = new JMenu("Select Fonts");
		JMenuItem dialogMenuScaleItem = new JMenuItem("Dialog");
		JMenuItem sansSerifMenuScaleItem = new JMenuItem("SansSerif");
		JMenuItem serifMenuScaleItem = new JMenuItem("Serif");
		JMenuItem monospacedMenuScaleItem = new JMenuItem("Monospaced");
		JMenuItem dialogInputMenuScaleItem = new JMenuItem("DialogInput");
		
		JMenuItem dialogMenuObjItem = new JMenuItem("Dialog");
		JMenuItem sansSerifMenuObjItem = new JMenuItem("SansSerif");
		JMenuItem serifMenuObjItem = new JMenuItem("Serif");
		JMenuItem monospacedMenuObjItem = new JMenuItem("Monospaced");
		JMenuItem dialogInputMenuObjItem = new JMenuItem("DialogInput");
		
		selectFontsMenu.setOpaque(true);
		selectFontsMenu.setForeground(Constants.UI_FOREGROUND_COLOR);
		selectFontsMenu.setBackground(Constants.UI_BACKGROUND_COLOR);
		
		JMenu scaleFontItem = new JMenu("Scale/Button Font (" + Constants.SCALE_FONT_TYPE_STRING + " " + Constants.SCALE_FONT_SIZE + "pt)");
		scaleFontItem.setOpaque(true);
		
//		for(int i=0; i<fontListArray.length; i++) {
//			JMenuItem item = new JMenuItem(fontListArray[i]);
//			scaleFontItem.add(item);
//		}
		
		JMenu objectFontItem = new JMenu("Object Font (" + Constants.OBJ_FONT_TYPE_STRING + " " + Constants.OBJ_FONT_SIZE + "pt)");
		objectFontItem.setOpaque(true);
//		for(int i=0; i<fontListArray.length; i++) {
//			JMenuItem item = new JMenuItem(fontListArray[i]);
//			objectFontItem.add(item);
//		}
		dialogMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser1.scaleF = new Font("Dialog", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		sansSerifMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser1.scaleF = new Font("SansSerif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		serifMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser1.scaleF = new Font("Serif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		monospacedMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser1.scaleF = new Font("Monospaced", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		dialogInputMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser1.scaleF = new Font("DialogInput", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		dialogMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser1.objF = new Font("Dialog", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		sansSerifMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser1.objF = new Font("SansSerif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		serifMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser1.objF = new Font("Serif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		monospacedMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser1.objF = new Font("Monospaced", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		dialogInputMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser1.objF = new Font("DialogInput", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		scaleFontItem.add(dialogMenuScaleItem);
		scaleFontItem.add(sansSerifMenuScaleItem);
		scaleFontItem.add(serifMenuScaleItem);
		scaleFontItem.add(monospacedMenuScaleItem);
		scaleFontItem.add(dialogInputMenuScaleItem);
		
		objectFontItem.add(dialogMenuObjItem);
		objectFontItem.add(sansSerifMenuObjItem);
		objectFontItem.add(serifMenuObjItem);
		objectFontItem.add(monospacedMenuObjItem);
		objectFontItem.add(dialogInputMenuObjItem);
		
		selectFontsMenu.add(scaleFontItem);
		selectFontsMenu.add(objectFontItem);
		
		return selectFontsMenu;
	}
	
	public static JMenu selectFontsMenuFuncBrowser2(){
		JMenu selectFontsMenu = new JMenu("Select Fonts");
		JMenuItem dialogMenuScaleItem = new JMenuItem("Dialog");
		JMenuItem sansSerifMenuScaleItem = new JMenuItem("SansSerif");
		JMenuItem serifMenuScaleItem = new JMenuItem("Serif");
		JMenuItem monospacedMenuScaleItem = new JMenuItem("Monospaced");
		JMenuItem dialogInputMenuScaleItem = new JMenuItem("DialogInput");
		
		JMenuItem dialogMenuObjItem = new JMenuItem("Dialog");
		JMenuItem sansSerifMenuObjItem = new JMenuItem("SansSerif");
		JMenuItem serifMenuObjItem = new JMenuItem("Serif");
		JMenuItem monospacedMenuObjItem = new JMenuItem("Monospaced");
		JMenuItem dialogInputMenuObjItem = new JMenuItem("DialogInput");
		
		selectFontsMenu.setOpaque(true);
		selectFontsMenu.setForeground(Constants.UI_FOREGROUND_COLOR);
		selectFontsMenu.setBackground(Constants.UI_BACKGROUND_COLOR);
		
		JMenu scaleFontItem = new JMenu("Scale/Button Font (" + Constants.SCALE_FONT_TYPE_STRING + " " + Constants.SCALE_FONT_SIZE + "pt)");
		scaleFontItem.setOpaque(true);
		
//		for(int i=0; i<fontListArray.length; i++) {
//			JMenuItem item = new JMenuItem(fontListArray[i]);
//			scaleFontItem.add(item);
//		}
		
		JMenu objectFontItem = new JMenu("Object Font (" + Constants.OBJ_FONT_TYPE_STRING + " " + Constants.OBJ_FONT_SIZE + "pt)");
		objectFontItem.setOpaque(true);
//		for(int i=0; i<fontListArray.length; i++) {
//			JMenuItem item = new JMenuItem(fontListArray[i]);
//			objectFontItem.add(item);
//		}
		dialogMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser2.scaleF = new Font("Dialog", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		sansSerifMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser2.scaleF = new Font("SansSerif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		serifMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser2.scaleF = new Font("Serif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		monospacedMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser2.scaleF = new Font("Monospaced", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		dialogInputMenuScaleItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser2.scaleF = new Font("DialogInput", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		dialogMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser2.objF = new Font("Dialog", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		sansSerifMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser2.objF = new Font("SansSerif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		serifMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser2.objF = new Font("Serif", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		monospacedMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser2.objF = new Font("Monospaced", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		dialogInputMenuObjItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				derBrowser2.objF = new Font("DialogInput", Constants.SCALE_FONT_TYPE, Constants.SCALE_FONT_SIZE);
			}
		});
		
		scaleFontItem.add(dialogMenuScaleItem);
		scaleFontItem.add(sansSerifMenuScaleItem);
		scaleFontItem.add(serifMenuScaleItem);
		scaleFontItem.add(monospacedMenuScaleItem);
		scaleFontItem.add(dialogInputMenuScaleItem);
		
		objectFontItem.add(dialogMenuObjItem);
		objectFontItem.add(sansSerifMenuObjItem);
		objectFontItem.add(serifMenuObjItem);
		objectFontItem.add(monospacedMenuObjItem);
		objectFontItem.add(dialogInputMenuObjItem);
		
		selectFontsMenu.add(scaleFontItem);
		selectFontsMenu.add(objectFontItem);
		
		return selectFontsMenu;
	}
	
}