package presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import com.apple.eawt.Application;

// loosely based on http://www.cs.cf.ac.uk/Dave/HCI/HCI_Handout_CALLER/node167.html
// based on http://www.coderanch.com/t/345311/GUI/java/Adding-rows-Jtable
class DataTableFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean silent = false;
	
	// This will load an empty table, data directory can be loaded in main for testing
	private String dataDirectory = "etc/sample/";
	public String refSpec = "";
	
	// Instance attributes used in this example
	private JPanel topPanel;
	private JScrollPane scrollPane;
	private JXTable table = new JXTable() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {
			return false;

		}
	};
	private derBrowser browser = new derBrowser();
	private JFrame frame = null;
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	private derBrowser1 browser1 = new derBrowser1();
	private JFrame browser1Frame = null;
	
	public JFrame getBrowser1Frame() {
		return browser1Frame;
	}

	public void setBrowser1Frame(JFrame browser1Frame) {
		this.browser1Frame = browser1Frame;
	}
	
	public JFrame keyFrameVar = null;
	public JFrame keyFrameP_Values = null;
	public JFrame keyFrameReads = null;
	
	public boolean rowSelectionChanged = false;
	
	private DefaultTableModel model = new DefaultTableModel();
	private JPanel bottomLeftPanel;
	private JPanel bottomRightPanel;
	private JPanel bottomPanel;
	public static JButton okButton = new JButton("  OK  ");
	//public static JButton cancelButton = new JButton("CANCEL");
	private JMenuItem loadFileItem = new JMenuItem("Load Genome Navigator Project");
	//private JMenuItem selectFileItem = new JMenuItem("Select");
	
	String file_name = "";
	
	private Vector fileStripeVector = new Vector();
	
	private TableRowSorter<DefaultTableModel> sorter;
	//private SortOrder chromosomeColumnSortOrder;
	private SortOrder fromColumnSortOrder;
	private SortOrder toColumnSortOrder;
	
	private boolean exonDataExists = Constants.EXON_DATA_EXISTS;
	
	private boolean useIntervalHighlighter = false;
	private ArrayList<String> intervalNumberList = new ArrayList<String>();
	
	public ArrayList<String> samplesList = new ArrayList<String>();
	public LinkedHashMap<String, String> speciesColorMap = null;
	
	public LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMap = new LinkedHashMap<String, ArrayList<Interval>>();
	public LinkedHashMap<String, ArrayList<Interval>> chrIntervalsMatesDiffChrMap = new LinkedHashMap<String, ArrayList<Interval>>();
	
	private AboutDialog aboutDialog;
	private JMenuItem openPWindowItem = new JMenuItem("Open P-Values Window");
	
	public static Color uiBackgroundColor = Constants.UI_BACKGROUND_COLOR;
	public static Color uiForegroundColor = Constants.UI_FOREGROUND_COLOR;
	
	public static JMenu optionsMenu = new JMenu("Options");
	public static JMenu optionsMenu1 = new JMenu("Options");
	
	public static JMenu helpMenuItem = new JMenu();
	public static JMenu helpMenuItem1 = new JMenu();
	
	public static JMenu editMenu = new JMenu("Edit");
	public static JMenu editMenu1 = new JMenu("Edit");
	
	// Constructor of main frame
	public DataTableFrame() {
		// Set the frame characteristics
		setTitle(Constants.TITLE);

		//okButton.setMnemonic(KeyEvent.VK_O);
		okButton.setEnabled(false);
		//cancelButton.setMnemonic(KeyEvent.VK_C);

		getRootPane().setDefaultButton(okButton);
		
		//chromosomeColumnSortOrder = SortOrder.DESCENDING;
		fromColumnSortOrder = SortOrder.DESCENDING;
		toColumnSortOrder = SortOrder.DESCENDING;
		
		UIManager.put("OptionPane.background", uiBackgroundColor);
		UIManager.put("OptionPane.foreground", uiForegroundColor);
		UIManager.put("OptionPane.messageForeground", uiForegroundColor);
		UIManager.put("Panel.background", uiBackgroundColor);
		UIManager.put("Panel.foreground", uiForegroundColor);
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(  
		          "Courier", Font.PLAIN, 14))); 
		UIManager.put("Button.background", uiForegroundColor);
		UIManager.put("Button.foreground", Constants.BUTTON_FONT_COLOR);
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(  
		          "Courier", Font.PLAIN, 16))); 
		UIManager.put("Label.foreground", uiForegroundColor);
		UIManager.put("CheckBox.background", uiBackgroundColor);
		UIManager.put("CheckBox.foreground", uiForegroundColor);
		UIManager.put("MenuBar.background", uiBackgroundColor);
		UIManager.put("MenuBar.foreground", uiForegroundColor);
		UIManager.put("Menu.background", uiBackgroundColor);
		UIManager.put("Menu.foreground", uiForegroundColor);
		UIManager.put("MenuItem.background", uiBackgroundColor);
		UIManager.put("MenuItem.foreground", uiForegroundColor);
		UIManager.put("CheckBoxMenuItem.background", uiBackgroundColor);
		UIManager.put("CheckBoxMenuItem.foreground", uiForegroundColor);
		
		aboutDialog = new AboutDialog();
		
		ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_16).getImage()); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_32).getImage());
		aboutDialog.setIconImages(icons);					
		aboutDialog.setSize(400, 180);
		aboutDialog.setResizable(false);
		aboutDialog.setLocationRelativeTo(null);		
		aboutDialog.setVisible(false);	
		aboutDialog.setModal(true);
		aboutDialog.licenseButton.addActionListener(UtilityMethods.licenseButtonActionListener);
		aboutDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				aboutDialog.setVisible(false);	
			}
		});
		
		aboutDialog.setForeground(Constants.UI_FOREGROUND_COLOR);
		aboutDialog.setBackground(Constants.UI_BACKGROUND_COLOR);
		aboutDialog.okButton.setForeground(Constants.BUTTON_FONT_COLOR);
		aboutDialog.okButton.setBackground(Constants.UI_FOREGROUND_COLOR);

		JMenuBar menuBar = new JMenuBar();

		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		//fileMenu.setMnemonic(KeyEvent.VK_F);
		loadFileItem.setBackground(uiBackgroundColor);
		loadFileItem.setForeground(uiForegroundColor);
		fileMenu.add(loadFileItem);
		//loadFileItem.setMnemonic(KeyEvent.VK_G);
		loadFileItem.addActionListener(new FileAction());
		
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		//exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(new ExitAction());
		
		menuBar.add(fileMenu);
		
		JMenu helpMenuItemTemp = UtilityMethods.helpMenuFunc(aboutDialog);
		helpMenuItem = helpMenuItemTemp;
        menuBar.add(helpMenuItem);
		
        table.setRowHeight(20);
		table.setColumnSelectionAllowed(false);
		// If desired to have single cell selection, change
		// setRowSelectionAllowed
		// to false, and uncomment the line table.setCellSelectionEnabled(true);
		// Since selection determines which file to load, it may be confusing
		// to only have a single cell selected
		table.setRowSelectionAllowed(true);
		// table.setCellSelectionEnabled(true);
		table.getSelectionModel().addListSelectionListener(new RowListener());
		//table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
//		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
//	        public void valueChanged(ListSelectionEvent event) {
//	            // do some actions here, for example
//	            // print first column value from selected row
//	            //System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
//	        	rowSelectionChanged = true;
//	        	System.out.println(rowSelectionChanged);
//	        }
//	    });

		// Create a panel to hold all other components
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		getContentPane().add(topPanel);
				
		// Add the table to a scrolling pane
		scrollPane = new JScrollPane(table);
		
		// change color of corner to match color scheme of remaining components
		JLabel blankLabel = new JLabel("");
		// from https://stackoverflow.com/questions/2380314/how-do-i-set-a-jlabels-background-color
		blankLabel.setOpaque(true);
		blankLabel.setBackground(Color.LIGHT_GRAY);
		scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, blankLabel);

		// based on https://stackoverflow.com/questions/23037433/changing-the-thumb-color-and-background-color-of-a-jscrollpane
		// and https://stackoverflow.com/questions/44432037/basicscrollbarui-how-to-use-it
		scrollPane.getVerticalScrollBar().setUI(new ArrowScrollbarUI()
	    {   

	    });
		topPanel.add(scrollPane, BorderLayout.CENTER);
		bottomLeftPanel = new JPanel();
		bottomLeftPanel.setLayout(new BorderLayout());
		bottomRightPanel = new JPanel();
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.black);
		bottomRightPanel.setLayout(new BorderLayout());
		okButton.setBackground(Color.gray);
		okButton.setForeground(Constants.BUTTON_FONT_COLOR);
		bottomLeftPanel.add(okButton, BorderLayout.WEST);
//		cancelButton.setBackground(Color.gray);
//		cancelButton.setForeground(Constants.BUTTON_FONT_COLOR);
//		bottomRightPanel.add(cancelButton, BorderLayout.EAST);
		bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);
		bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
		topPanel.add(bottomPanel, BorderLayout.SOUTH);

		// from
		// http://www.java2s.com/Tutorial/Java/0240__Swing/thelastcolumnismovedtothefirstposition.htm
		// columns cannot be rearranged by dragging
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setBackground(Constants.TABLE_HEADER_BACKGROUND_COLOR);
		table.getTableHeader().setForeground(Constants.TABLE_HEADER_FOREGROUND_COLOR);
		
		addSampleColumns();
		table.setModel(model);
		setSampleColumnWidths();
		
		ActionListener okButtonActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				boolean valid = true;
				samplesList = new ArrayList<String>();
				int minFrom = Integer.MAX_VALUE;
				int maxTo = 0;

				ArrayList<String> chrList = new ArrayList<String>();
				ArrayList<String> intervalNumList = new ArrayList<String>();
				for (int j = 0; j < table.getSelectedRows().length; j++) {
					int viewRow = table.convertRowIndexToModel(table.getSelectedRows()[j]);
					String chr = (String) table.getModel().getValueAt(viewRow, 
							DataTableConstants.CHROMOSOME_COLUMN_INDEX);
					if (!chrList.contains(chr)) {
						chrList.add(chr);
					}
					String intervalNum = (String) table.getModel().getValueAt(viewRow, 
							DataTableConstants.INTERVAL_COLUMN_INDEX);
					if (!intervalNumList.contains(intervalNum)) {
						intervalNumList.add(intervalNum);
					}
				}
				if (intervalNumList.size() > 1) {
					JOptionPane.showMessageDialog(null,                
							"Only one interval can be viewed at a time",                
							"Multiple Interval Selection Error.",                                
							JOptionPane.ERROR_MESSAGE);
					valid = false;
				}
				if (valid && rowSelectionChanged) {
					String fileName = "";
					rowSelectionChanged = false;
					//based on code from http://stackoverflow.com/questions/6403821/how-to-add-an-image-to-a-jframe-title-bar
					final ArrayList<Image> icons = new ArrayList<Image>(); 
					icons.add(new ImageIcon(Constants.VARIANTS_WINDOW_IMAGE_PATH_16).getImage()); 
					icons.add(new ImageIcon(Constants.VARIANTS_WINDOW_IMAGE_PATH_32).getImage());
					
					final ArrayList<Image> pointsIcons = new ArrayList<Image>(); 
					pointsIcons.add(new ImageIcon(Constants.P_VALUE_WINDOW_IMAGE_PATH_16).getImage()); 
					pointsIcons.add(new ImageIcon(Constants.P_VALUE_WINDOW_IMAGE_PATH_32).getImage());
					
					for (int k = 0; k < table.getSelectedRows().length; k++) {
						int viewRow = table.convertRowIndexToModel(table.getSelectedRows()[k]);
						String sampleID = (String) table.getModel().getValueAt(viewRow, 
								DataTableConstants.SAMPLE_COLUMN_INDEX);
						if (!samplesList.contains(sampleID)) {
							samplesList.add(sampleID);
						}
						//System.out.println(samplesList);						
						int from = Integer.valueOf((String) table.getModel().getValueAt(viewRow, 
								DataTableConstants.COORDINATES_FROM_COLUMN_INDEX));
						if (from < minFrom) {
							minFrom = from;
						}
						int to = Integer.valueOf((String) table.getModel().getValueAt(viewRow, 
								DataTableConstants.COORDINATES_TO_COLUMN_INDEX));
						if (to > maxTo) {
							maxTo = to;
						}
					}
					
					// Finds interval that is in range of selected rows
					Interval windowRange = UtilityMethods.windowRange(minFrom, maxTo);
					//System.out.println("window range " + windowRange);
					// System.out.println("chr intervals map " + chrIntervalsMap);
					for (int c = 0; c < chrIntervalsMap.get(chrList.get(0)).size(); c++) {
						if (UtilityMethods.isIntervalPortionVisible(new Interval(minFrom, maxTo), chrIntervalsMap.get(chrList.get(0)).get(c))) {
							windowRange = chrIntervalsMap.get(chrList.get(0)).get(c);
							//System.out.println("interval from file " + chrIntervalsMap.get(chrList.get(0)).get(c));
						}
					}

					// open files for variants (derBrowser) and p-values (derBrowser1) windows if exist
					fileName = dataDirectory + Constants.VAR_DIRECTORY + chrList.get(0) + "_" + windowRange.start + "-" + windowRange.end + ".txt";
					File test = new File(fileName);
					if (test.exists()) {
						// maintain backward compatibility for derBrowser type files with 1 chromosome
						browser.clearData();
						browser.fillStripeVector(fileName);
						// copy to save original vector from file
						fileStripeVector = vectorCopy(browser.stripeVector);

						// copy to save original vector from file
						// if OK button clicked multiple times after loading file, need
						// to restore original vector before processing
						Vector fileVector = vectorCopy(fileStripeVector);

						browser = createBrowser(chrList, fileVector, windowRange.start, windowRange.end);
						browser.silent = silent;
						browser.refSpec = refSpec;
						browser.keyFrame = keyFrameVar;
						browser.keyFrameReads = keyFrameReads;
						browser.chrIntervalsMatesDiffChrMap = chrIntervalsMatesDiffChrMap;
						
						final JFrame frame = createBrowserFrame(browser, icons, windowRange.start, windowRange.end, test.getName());
						setFrame(frame);
						derBrowser.frame = frame;
						String fileName2 = "";
						fileName2 = dataDirectory + Constants.POINTS_DIRECTORY + chrList.get(0) + "_" + windowRange.start + "-" + windowRange.end + ".txt";
						
						File test2 = new File(fileName2);
						if (test2.exists()) {
							browser1 = new derBrowser1();
							browser1.silent = silent;
							browser1.refSpec = refSpec;
							browser1.chrIntervalsMap = chrIntervalsMap;
							browser1.chromosomeDisplayed = chrList.get(0);
							browser1.samplesList = samplesList;
							browser1.readsIntervals = chrIntervalsMap.get(chrList.get(0));
							browser1.speciesColorMap = speciesColorMap;
							browser1.mergedIntervalStart = windowRange.start;
							browser1.mergedIntervalEnd = windowRange.end;
							browser1.chrIntervalsMatesDiffChrMap = chrIntervalsMatesDiffChrMap;
							
							browser1.dataDirectory = dataDirectory;
							browser1.keyFrame = keyFrameP_Values;
							browser1.keyFrameReads = keyFrameReads;

							final JFrame browser1Frame = new JFrame();
							browser1Frame.setResizable(false);
							browser.setBrowser1Frame(browser1Frame);
							browser.browser1 = browser1;
							frame.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent evt) {
									for (String s : browser1.openReadsBrowsersMap.keySet()) {
										browser1.openReadsBrowsersMap.get(s).clearData();
									}
									for (String s : browser1.openFramesMap.keySet()) {
										browser1.openFramesMap.get(s).dispose();
									}
									browser1Frame.dispose();
									frame.dispose();
									rowSelectionChanged = true;
								}
							});
							//setBrowser1Frame(browser1Frame);
							Container c = browser1Frame.getContentPane();
							c.setBackground(Color.black);
							String title = Constants.TITLE + " - " + Constants.P_VALUE_WINDOW_TITLE + " - " + test2.getName();
							browser1.frameFileName = test2.getName();
							browser1Frame.addWindowFocusListener(new WindowAdapter() {
							    //To check window gained focus - used for listening to Esc key for closing menus
							    public void windowGainedFocus(WindowEvent e) {
							        //set flag
							        browser1.isWindowActive = true;
							    }

							    //To check window lost focus
							    public void windowLostFocus(WindowEvent e) {
							        //set flag
							    	browser1.isWindowActive = false;							   
							    }
							});
							browser1Frame.setTitle(title);
							browser1Frame.setIconImages(pointsIcons);
							
							JMenuBar menuBar = new JMenuBar();
							browser1Frame.setJMenuBar(menuBar);
							
							//editMenu1.setMnemonic(KeyEvent.VK_E);
							editMenu1.setOpaque(true);
							// This is necessary for Windows
							editMenu1.setBackground(uiBackgroundColor);
							editMenu1.setForeground(uiForegroundColor);
							
							JMenuItem findItem1 = new JMenuItem("Find");
							//findItem1.setMnemonic(KeyEvent.VK_F);
							editMenu1.add(findItem1);

							findItem1.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent a) {
									browser1.findInput.visible = true;
									browser1.repaint();
								}    	     
							});
							
							final JCheckBoxMenuItem showMatesDiffChrItem = new JCheckBoxMenuItem(Constants.MATES_DIFF_CHR_MENU_ITEM);
							optionsMenu1.setBackground(uiBackgroundColor);
							optionsMenu1.setForeground(uiForegroundColor);
							optionsMenu1.setOpaque(true);
//							if(derBrowser1.optionsMenuValue == false) {
//								System.out.println("derBrowser OptionsMenueValue is false");
//							}
							
							JMenuItem scaleUnitsMenu1 = new JMenuItem("Scale Units");
							scaleUnitsMenu1.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub
									browser1.scaleInput.visible = true;
									browser1.repaint();
								}
							});
							
//							miscOptionsMenu.addActionListener(new ActionListener() {
//								
//								@Override
//								public void actionPerformed(ActionEvent e) {
//									// TODO Auto-generated method stub
//									browser1.miscMenu.visible = true;
//									browser1.repaint();
//								}
//							});
							
							optionsMenu1.add(UtilityMethods.selectFontsMenuFuncBrowser1());
							optionsMenu1.add(scaleUnitsMenu1);
							optionsMenu1.add(UtilityMethods.miscOptionsMenuFuncBrowser1(browser1));
							
							//optionsMenu1.setMnemonic(KeyEvent.VK_O);

							optionsMenu1.add(showMatesDiffChrItem);
					        //showMatesDiffChrItem.setMnemonic(KeyEvent.VK_S);

					        showMatesDiffChrItem.setSelected(true);
					        
					        showMatesDiffChrItem.addActionListener(new ActionListener() {
					        	public void actionPerformed(ActionEvent ae) {
					        		boolean state = showMatesDiffChrItem.getState();
					        		if (state == true) {
					        			browser1.showReadsMatesDiffChr = true;
					        		} else {
					        			browser1.showReadsMatesDiffChr = false;
					        		}
					        	}
					        });
							
							menuBar.add(editMenu1);
						    menuBar.add(optionsMenu1);
					        
						    JMenu helpMenuItemTemp = UtilityMethods.helpMenuFunc(aboutDialog);
						    helpMenuItem1 = helpMenuItemTemp;
					        menuBar.add(helpMenuItem1);
						    
							browser1Frame.add(browser1);
							browser1Frame.pack();
							browser1.enableNewMap = false;

							browser1.fileName = fileName2;
							browser1.init();
							
							browser1Frame.setSize(Constants.P_VALUE_WINDOW_FRAME_WIDTH, Constants.P_VALUE_WINDOW_FRAME_HEIGHT);
							//browser1Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
							//browser1Frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
							browser1Frame.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent evt) {
									browser1.keyFrame.setVisible(false);
									browser1Frame.setVisible(false);
									openPWindowItem.setEnabled(true);
								}
							});
							
							Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

							int y = (screenSize.height - browser1Frame.getSize().height)/2;
							if (screenSize.width < Constants.VARIANTS_WINDOW_FRAME_WIDTH + Constants.P_VALUE_WINDOW_FRAME_WIDTH) {
								browser1Frame.setLocation((screenSize.width - browser1Frame.getSize().width) - 10, y);
							} else {
								browser1Frame.setLocation(screenSize.width/2, y);
							}

							//browserFrame.setLocationRelativeTo(null);
							browser1Frame.setVisible(true);
						} else {
							if (!silent) {
								System.out.println("No data message 1");
							}
							JOptionPane.showMessageDialog(null,                
									"No data exists for selected range.",                
									"No Data Warning.",                                
									JOptionPane.WARNING_MESSAGE);
						}
					} else {
						if (!silent) {
							System.out.println("No data message 2");
						}
						JOptionPane.showMessageDialog(null,                
								"No data exists for selected range.",                
								"No Data Warning.",                                
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		};

		okButton.addActionListener(okButtonActionListener);

//		ActionListener cancelButtonActionListener = new ActionListener() {
//			public void actionPerformed(ActionEvent ae) {
//				setVisible(false);
//				//dispose() function wont quit the application completely. It would still be running in the backround on Mac. 
//				System.exit(0);
//			}
//		};

		//cancelButton.addActionListener(cancelButtonActionListener);

	}
	
	/*****************************************************************************************/
	// end constructor
	/*****************************************************************************************/
	
	class OpenAboutUrlAction implements ActionListener {
		@Override public void actionPerformed(ActionEvent e) {
			System.out.println("test");
			// add open url in future
		}
	}
	
	class ExitAction implements ActionListener {
		public void actionPerformed(ActionEvent cae) {
			setVisible(false);
			System.exit(0);			
		}
	}
	
	class FileAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Load Directory"); 
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setBackground(Color.gray);
			//... Open a file dialog.
			int retval = fileChooser.showOpenDialog(null);
			if (retval == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getSelectedFile();				
				loadGNProject(f);		
			}		
		}
	}
	
	public void loadGNProject(File f) {
		String dir = f.toString();
		dataDirectory = dir + "/";
		speciesColorMap = UtilityMethods.speciesColorMap(dataDirectory + Constants.DEFAULT_SPECIES_COLOR_FILE);
		createKeyFrames(this, speciesColorMap);
		File refSpecFile = new File(dataDirectory + "/" + Constants.REF_SPECIES_FILENAME);
		if (refSpecFile.exists()) {
			refSpec = UtilityMethods.refSpec(dataDirectory + "/" + Constants.REF_SPECIES_FILENAME);
		}
		if (isValidGNProject(f)) {
			loadDataTable(new File(dataDirectory + "/" + Constants.DATA_TABLE_FILENAME));
		}	
	}
	
	public boolean isValidGNProject(File f) {
		File[] fList = f.listFiles();
		java.util.List<File> fileList = Arrays.asList(fList);
		ArrayList<String> dirNames = new ArrayList<String>();	
		ArrayList<String> fileNames = new ArrayList<String>();
		for (int j = 0; j < fileList.size(); j++) {
			if (fileList.get(j).isDirectory()) {
				dirNames.add(fileList.get(j).getName());
			} else {
				fileNames.add(fileList.get(j).getName());
			}
		}
		boolean valid = true;
		String missingItems = "";
		for (int i = 0; i < Constants.REQUIRED_PROJECT_FOLDERS.length; i++) {
			if (!dirNames.contains(Constants.REQUIRED_PROJECT_FOLDERS[i])) {
				missingItems += Constants.REQUIRED_PROJECT_FOLDERS[i] + ", ";
				valid = false;
			}
		}
		for (int i = 0; i < Constants.REQUIRED_PROJECT_FILES.length; i++) {
			if (!fileNames.contains(Constants.REQUIRED_PROJECT_FILES[i])) {
				missingItems += Constants.REQUIRED_PROJECT_FILES[i] + ", ";
				valid = false;
			}
		}
		if (!valid) {
			JOptionPane.showMessageDialog(null,                
					"<html>The Selected Directory is Not a Valid Genome Navigator Project. The Items <br>"
							+ missingItems.substring(0, missingItems.length() - 2) + " are not present",                
							"Invalid GN Project Error",                                
							JOptionPane.ERROR_MESSAGE);
		}	
		return valid;
	}
	
	public void loadDataTable(File file) {
		clearTable();
		//... The user selected a file, get it, use it.
		processFile(file.getAbsolutePath());
		if (table.getRowCount() > 0) {
			ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
	    	
	    	int columnIndexToSort1 = DataTableConstants.CHROMOSOME_COLUMN_INDEX;
	    	int columnIndexToSort2 = DataTableConstants.COORDINATES_FROM_COLUMN_INDEX;
	    	sortKeys.add(new RowSorter.SortKey(columnIndexToSort1, SortOrder.ASCENDING));
	    	sortKeys.add(new RowSorter.SortKey(columnIndexToSort2, SortOrder.ASCENDING));
			fromColumnSortOrder = SortOrder.ASCENDING;
			sorter.setSortKeys(sortKeys);
	    	sorter.sort();
	    	useIntervalHighlighter = true;
		}		
    	
		file_name = file.getName();
		setTitle(Constants.TITLE + " - " + Constants.VARIANTS_WINDOW_TITLE + " - " + dataDirectory + file_name);
		//selectFileItem.setEnabled(true);
	}
	
	public void loadFile(File file) {
		if (file.exists()) {
			clearTable();
			processFile(file.getAbsolutePath());
		} else {
			JOptionPane.showMessageDialog(null,                
					"File " + file.getAbsolutePath() + " Not Found Error.",                
					"Error",                                
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void processFile(String file)
	{ 
		DataTableFileReader dataTableReader = new DataTableFileReader();
		dataTableReader.readFile(file);
		if (dataTableReader.content.length() > 0) {
			String[] lineData = dataTableReader.content.split("\n");
			String[] exonLineData = exonData(lineData).split("\n");
			for (int i = 0; i < lineData.length; i++) {
				String[] data = lineData[i].split("\t");
				Vector <String> row = new Vector<String>();	
				row.add(data[0]);
				row.add(data[1]);
				row.add(data[2]);
				row.add(data[3]);
				row.add(data[4]);
				if (data.length > 5) {
					row.add(data[5]);
				} else {
					row.add("");
				}
				if (data.length > 6) {
					row.add(data[6]);
				} else {
					row.add("");
				}
				if (data.length > 7) {
					row.add(data[7]);
				} else {
					row.add("");
				}
				if (exonDataExists) {
					String[] exonData = exonLineData[i].split("\t");
					row.add(exonData[0]);
					row.add(exonData[1]);
				}
				if (data.length > DataTableConstants.INTERVAL_COLUMN_INDEX) {
					String intervalNum = data[DataTableConstants.INTERVAL_COLUMN_INDEX];
					if (intervalNum.length() > 0 && UtilityMethods.isInteger(intervalNum)) {
						if (!intervalNumberList.contains(intervalNum)) {
							intervalNumberList.add(intervalNum);
						}
					}
				}
				model.addRow(row);
			}
			setUpTable();
			Collections.sort(intervalNumberList, new IntComparator());
		}		
	}
	
	private derBrowser createBrowser(ArrayList<String> chrList, Vector fileVector, int start, int end) {
		browser = new derBrowser();
		browser.chrIntervalsMap = chrIntervalsMap;
		browser.chromosomeDisplayed = chrList.get(0);
		browser.dataDirectory = dataDirectory;

		browser.speciesColorMap = speciesColorMap;
		browser.stripeVector = fileVector;
		for (int i = browser.stripeVector.size() - 1; i >= 0; i--) {
			if (browser.stripeVector.elementAt(i) instanceof Clones) {
				Clones clones = (Clones)browser.stripeVector.elementAt(i);
				String[] nameField = clones.name.split("\\|");
				if (!samplesList.contains(nameField[0])) {
					browser.stripeVector.remove(i);

				}
			} 
		}
		if (Constants.EXON_DATA_EXISTS) {
			browser.CLONES = samplesList.size() + 1;
		} else {
			browser.CLONES = samplesList.size();
		}
		//browser.LEFTEND = left;
		browser.LEFTEND = start;
		//if (right < browser.RIGHTEND) {
		//browser.RIGHTEND = right;
		browser.RIGHTEND = end;
		//}

		browser.samplesList = samplesList;
		
		return browser;
	}
	
	private JFrame createBrowserFrame(final derBrowser browser, ArrayList<Image> icons, int start, int end, String fileName) {
		final JFrame frame = new JFrame();
		
		frame.addWindowFocusListener(new WindowAdapter() {
		    //To check window gained focus - used for listening to Esc key for closing menus
		    public void windowGainedFocus(WindowEvent e) {
		        //set flag
		        browser.isWindowActive = true;
		    }

		    //To check window lost focus
		    public void windowLostFocus(WindowEvent e) {
		        //set flag
		    	browser.isWindowActive = false;							   
		    }
		});
		
		Container c = frame.getContentPane();
		c.setBackground(Color.black);
		frame.setTitle(Constants.TITLE + " - " + Constants.VARIANTS_WINDOW_TITLE + " - " + fileName);
		frame.setIconImages(icons);
		frame.setResizable(false);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				browser.keyFrame.setVisible(false);
				frame.dispose();
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		//editMenu.setMnemonic(KeyEvent.VK_E);
		editMenu.setOpaque(true);
		// This is necessary for Windows
		editMenu.setBackground(uiBackgroundColor);
		editMenu.setForeground(uiForegroundColor);
		
		JMenuItem findItem = new JMenuItem("Find");
		//findItem.setMnemonic(KeyEvent.VK_F);
		editMenu.add(findItem);

		findItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				browser.findInput.visible = true;
				browser.repaint();
			}    	     
		});
		
		menuBar.add(editMenu);
		
		final JCheckBoxMenuItem showMatesDiffChrItem = new JCheckBoxMenuItem(Constants.MATES_DIFF_CHR_MENU_ITEM);
		//optionsMenu.setMnemonic(KeyEvent.VK_O);
		optionsMenu.setOpaque(true);
		optionsMenu.setForeground(uiForegroundColor);
		optionsMenu.setBackground(uiBackgroundColor);
	
		optionsMenu.add(UtilityMethods.selectFontsMenuFuncBrowser());
		
//		JMenuItem scaleUnitsMenu = new JMenuItem("Scale Units");
//		scaleUnitsMenu.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				browser.scaleInput.visible = true;
//				browser.repaint();
//			}
//		});
		
		//optionsMenu.add(scaleUnitsMenu);
		optionsMenu.add(UtilityMethods.miscOptionsMenuFuncBrowser(browser));
		optionsMenu.add(showMatesDiffChrItem);
        //showMatesDiffChrItem.setMnemonic(KeyEvent.VK_S);
        
        showMatesDiffChrItem.setSelected(true);

        showMatesDiffChrItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
        		boolean state = showMatesDiffChrItem.getState();
        		if (state == true) {
        			browser.showReadsMatesDiffChr = true;
        		} else {
        			browser.showReadsMatesDiffChr = false;
        		}
        	}
        });
        
        openPWindowItem.setForeground(uiForegroundColor);
        openPWindowItem.setBackground(uiBackgroundColor);
        openPWindowItem.setEnabled(false);
        optionsMenu.add(openPWindowItem);
        //openPWindowItem.setMnemonic(KeyEvent.VK_P);

        openPWindowItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				if (browser.getBrowser1Frame() != null) {
					browser.getBrowser1Frame().setVisible(true);
					openPWindowItem.setEnabled(false);
				}
			}    	     
		});        

        menuBar.add(optionsMenu);
        helpMenuItem = UtilityMethods.helpMenuFunc(aboutDialog);
        menuBar.add(helpMenuItem);
		
		frame.add(browser);
		frame.pack();
		browser.fileName = null;
		browser.enableNewMap = false;
		browser.init();
		frame.setSize(Constants.VARIANTS_WINDOW_FRAME_WIDTH, Constants.VARIANTS_WINDOW_FRAME_HEIGHT);
		//frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
			
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int y = (screenSize.height - frame.getSize().height)/2;
		if (screenSize.width < Constants.VARIANTS_WINDOW_FRAME_WIDTH + Constants.P_VALUE_WINDOW_FRAME_WIDTH) {
			frame.setLocation(10, y);
		} else {
			frame.setLocation((screenSize.width/2) - Constants.VARIANTS_WINDOW_FRAME_WIDTH, y);
		}

		//frame.setLocationRelativeTo(null);
		frame.setVisible(true); 
		
		return frame;
		
	}
	
	private void clearTable() {
		model = new DefaultTableModel();
		addSampleColumns();
		table.setModel(model);
		setSampleColumnWidths();
	}
	
	HighlightPredicate intervalHighlightPredicate = new HighlightPredicate() {
		public boolean isHighlighted(Component renderer ,ComponentAdapter adapter) {
			int viewRow = table.convertRowIndexToModel(adapter.row);
			if (table.getModel().getValueAt(viewRow, DataTableConstants.INTERVAL_COLUMN_INDEX) != null) {
				String entry = table.getModel().getValueAt(viewRow, DataTableConstants.INTERVAL_COLUMN_INDEX).toString();
				if (entry.length() > 0 && UtilityMethods.isInteger(entry)) {
					String id = table.getModel().getValueAt(viewRow, DataTableConstants.INTERVAL_COLUMN_INDEX).toString();					
					//int id = Integer.valueOf(table.getModel().getValueAt(viewRow, DataTableConstants.INTERVAL_COLUMN_INDEX).toString());					
					if (useIntervalHighlighter) {
						if (intervalNumberList.contains(id)) {
							if (intervalNumberList.indexOf(id)%2 != 0) {
								return true;
							}
						} 
					}
				}	
			}								
			return false;
		}
	};
	
	ColorHighlighter intervalHighlight = new ColorHighlighter(intervalHighlightPredicate, DataTableConstants.INTERVAL_HIGHLIGHT_COLOR, null);
	
	private void setUpTable() {
		table.setModel(model);
		setSampleColumnWidths();
		sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel) table.getModel());
		sorter.setComparator(DataTableConstants.COORDINATES_FROM_COLUMN_INDEX, new IntComparator());
		sorter.setComparator(DataTableConstants.COORDINATES_TO_COLUMN_INDEX, new IntComparator());
        table.setRowSorter(sorter);
        
        table.addHighlighter(intervalHighlight);
        
        scrollToVisible(0, 0);
        
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                if (col == DataTableConstants.COORDINATES_FROM_COLUMN_INDEX) {
                	useIntervalHighlighter = true;
                	ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
                	
                	int columnIndexToSort1 = DataTableConstants.CHROMOSOME_COLUMN_INDEX;
                	sortKeys.add(new RowSorter.SortKey(columnIndexToSort1, SortOrder.ASCENDING));
                	
                	int columnIndexToSort2 = DataTableConstants.COORDINATES_FROM_COLUMN_INDEX;
                	if (fromColumnSortOrder == SortOrder.DESCENDING) {
                		sortKeys.add(new RowSorter.SortKey(columnIndexToSort2, SortOrder.ASCENDING));
                		fromColumnSortOrder = SortOrder.ASCENDING;
                	} else if (fromColumnSortOrder == SortOrder.ASCENDING) {
                		sortKeys.add(new RowSorter.SortKey(columnIndexToSort2, SortOrder.DESCENDING));
                		fromColumnSortOrder = SortOrder.DESCENDING;
                	}
                	 
                	sorter.setSortKeys(sortKeys);
                	sorter.sort();
                } else if (col == DataTableConstants.COORDINATES_TO_COLUMN_INDEX) {
                	useIntervalHighlighter = true;
                	ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
                	
                	int columnIndexToSort1 = DataTableConstants.CHROMOSOME_COLUMN_INDEX;
                	sortKeys.add(new RowSorter.SortKey(columnIndexToSort1, SortOrder.ASCENDING));
                	
                	int columnIndexToSort2 = DataTableConstants.COORDINATES_TO_COLUMN_INDEX;
                	if (toColumnSortOrder == SortOrder.DESCENDING) {
                		sortKeys.add(new RowSorter.SortKey(columnIndexToSort2, SortOrder.ASCENDING));
                		toColumnSortOrder = SortOrder.ASCENDING;
                	} else if (toColumnSortOrder == SortOrder.ASCENDING) {
                		sortKeys.add(new RowSorter.SortKey(columnIndexToSort2, SortOrder.DESCENDING));
                		toColumnSortOrder = SortOrder.DESCENDING;
                	}
                	 
                	sorter.setSortKeys(sortKeys);
                	sorter.sort();
                } else {
                	useIntervalHighlighter = false;
                }
            }
        });
	}

	private class RowListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (table.getSelectedRow() > -1) {
				okButton.setEnabled(true);
				rowSelectionChanged = true;
			}
		}
	}
	
	private void addSampleColumns() {
		if (Constants.EXON_DATA_EXISTS) {
			for (int i = 0; i < DataTableConstants.columnsListWithGenes.size(); i++) {
				model.addColumn(DataTableConstants.columnsListWithGenes.get(i));
			}
		} else {
			for (int i = 0; i < DataTableConstants.columnsList.size(); i++) {
				model.addColumn(DataTableConstants.columnsList.get(i));
			}
		}
	}
	
	private void setSampleColumnWidths() {
		int r = table.getModel().getColumnCount();
		for (int i = 0; i < r; i++) {
			DataTableCellRenderer renderer = new DataTableCellRenderer();
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setCellRenderer(renderer);
			// Column widths can be changed here
			if (i == DataTableConstants.SAMPLE_COLUMN_INDEX) {
				column.setPreferredWidth(DataTableConstants.SAMPLE_COLUMN_WIDTH);
			} 
			if (i == DataTableConstants.CHROMOSOME_COLUMN_INDEX) {
				column.setPreferredWidth(DataTableConstants.CHROMOSOME_COLUMN_WIDTH);
			} 
			if (i == DataTableConstants.SV_TYPE_COLUMN_INDEX) {
				column.setPreferredWidth(DataTableConstants.SV_TYPE_COLUMN_WIDTH);
			}
			if (i == DataTableConstants.COORDINATES_FROM_COLUMN_INDEX) {
				column.setPreferredWidth(DataTableConstants.COORDINATES_FROM_COLUMN_WIDTH);
				renderer.setHorizontalAlignment(JLabel.RIGHT);
			} 
			if (i == DataTableConstants.COORDINATES_TO_COLUMN_INDEX) {
				column.setPreferredWidth(DataTableConstants.COORDINATES_TO_COLUMN_WIDTH);
				renderer.setHorizontalAlignment(JLabel.RIGHT);
			} 
			if (i == DataTableConstants.NUM_SAMPLES_COLUMN_INDEX) {
				column.setPreferredWidth(DataTableConstants.NUM_SAMPLES_COLUMN_WIDTH);
				renderer.setHorizontalAlignment(JLabel.RIGHT);
			} 
			if (i == DataTableConstants.INTERVAL_COLUMN_INDEX) {
				column.setPreferredWidth(DataTableConstants.INTERVAL_COLUMN_WIDTH);
			}
			if (i == DataTableConstants.INFO_COLUMN_INDEX) {
				column.setPreferredWidth(DataTableConstants.INFO_COLUMN_WIDTH);
			} 			
			if (Constants.EXON_DATA_EXISTS) {
				if (i == DataTableConstants.OVERLAP_FEATURES_COLUMN_INDEX) {
					column.setPreferredWidth(DataTableConstants.OVERLAP_FEATURES_COLUMN_WIDTH);
				}
				if (i == DataTableConstants.NEARBY_FEATURES_COLUMN_INDEX) {
					column.setPreferredWidth(DataTableConstants.NEARBY_FEATURES_COLUMN_WIDTH);
				}
			}
		}
	}
	
	private Vector vectorCopy(Vector vector) {
		Vector copy = new Vector();
		
		for (int i = 0; i < vector.size(); i++) {
			copy.add(vector.get(i));
		}
		return copy;
	}

	
	/**
	 * from https://stackoverflow.com/questions/853020/jtable-scrolling-to-a-specified-row-index
	 * scrolls selected row to top if possible
	 * @param rowIndex
	 * @param vColIndex
	 */
	private void scrollToVisible(int rowIndex, int vColIndex) {
        if (!(table.getParent() instanceof JViewport)) {
            return;
        }
        if (table.getRowCount()<1){
            return;
        }
        JViewport viewport = (JViewport)table.getParent();
        // view dimension
        Dimension dim = viewport.getExtentSize();
        // cell dimension
        Dimension dimOne = new Dimension(0,0);

        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
        // fixes bug where if rectangle was above viewport, did no scroll into view
        table.scrollRectToVisible(rect);
        Rectangle rectOne;
        if (rowIndex+1<table.getRowCount()) {
            if (vColIndex+1<table.getColumnCount())
                vColIndex++;
            rectOne = table.getCellRect(rowIndex+1, vColIndex, true);
            dimOne.width=rectOne.x-rect.x;
            dimOne.height=rectOne.y-rect.y;
        }
        
        // '+ veiw dimension - cell dimension' to set first selected row on the top

        rect.setLocation(rect.x+dim.width-dimOne.width, rect.y+dim.height-dimOne.height);
        
        table.scrollRectToVisible(rect);
	}
	
	private String exonData(String[] lineData) {
		//String directory = "etc/bear/";
		String intervalsPath = dataDirectory + Constants.INTERVALS_FILENAME;
		String gffPath = dataDirectory + Constants.GFF_FILENAME;
		
		String content = "";

		LinkedHashMap<String, HashMap<String, ArrayList<GeneralFeatureData>>> chrIntervalGeneDataMap = null;
		
		IntervalFileChrFileProcessor proc = new IntervalFileChrFileProcessor();
		File f = new File(intervalsPath);
		proc.readFile(f.getAbsolutePath(), 1, 2);

		GFFFileReader gffReader = new GFFFileReader();

		chrIntervalsMap = proc.chrIntervalsMap;
		File file = new File(gffPath);
		if (file.exists()) {
			gffReader.readFile(file.getAbsolutePath(), proc.chrIntervalsMap);
			chrIntervalGeneDataMap = gffReader.createChrIntervalGeneDataMap();
			exonDataExists = true;
		} else {
			exonDataExists = false;
		}
		
		if (chrIntervalGeneDataMap != null) {
			for (int i = 0; i < lineData.length; i++) {
				ArrayList<String> affectedGenesList = new ArrayList<String>();
				ArrayList<String> nearbyGenesList = new ArrayList<String>();
				String[] data = lineData[i].split("\t");
				String chr = data[1];
				Interval sampleInterval = new Interval(Integer.parseInt(data[3]), Integer.parseInt(data[4]));
				if (proc.chrIntervalsMap.containsKey(chr)) {
					for (int j = 0; j < proc.chrIntervalsMap.get(chr).size(); j++) {
						Interval mergedInterval = proc.chrIntervalsMap.get(chr).get(j);
						if (sampleInterval.start >= mergedInterval.start && 
								sampleInterval.end <= mergedInterval.end) {
							if (chrIntervalGeneDataMap.containsKey(chr)) {
								for (String gene : chrIntervalGeneDataMap.get(chr).keySet()) {
									ArrayList<GeneralFeatureData> gfdList = chrIntervalGeneDataMap.get(chr).get(gene);
									for (int g = 0; g < gfdList.size(); g++) {
										Interval exonInterval = new Interval(gfdList.get(g).start, gfdList.get(g).end);
										if (UtilityMethods.isIntervalPortionVisible(exonInterval, sampleInterval)) {
											if (!affectedGenesList.contains(gene)) {
												affectedGenesList.add(gene);
											}
										} else if (UtilityMethods.isIntervalPortionVisible(exonInterval, mergedInterval)) {
											if (!nearbyGenesList.contains(gene)) {
												nearbyGenesList.add(gene);
											}
										}
									}
								}
							}
						}
					}
				} else {

				}
				
				String affectedGenes = " ";
				String nearbyGenes = " ";
				for (int j = 0; j < affectedGenesList.size(); j++) {
					if (nearbyGenesList.contains(affectedGenesList.get(j))) {
						nearbyGenesList.remove(nearbyGenesList.indexOf(affectedGenesList.get(j)));
					}
				}
				if (affectedGenesList.size() > 0) {
					affectedGenes = commaSeparatedEntry(affectedGenesList);
				}
				if (nearbyGenesList.size() > 0) {
					nearbyGenes = commaSeparatedEntry(nearbyGenesList);
				}
				content += affectedGenes + "\t" + nearbyGenes + "\n";
			}
		}
		
		return content;
	}
	
	private String commaSeparatedEntry(ArrayList<String> list) {
		String entry = "";
		
		for (int i = 0; i < list.size(); i++) {
			entry += list.get(i);
			if (list.size() > 1 && i < list.size() - 1) {
				entry += ", ";
			}
		}
		
		return entry;
	}
	
	public void createKeyFrames(DataTableFrame frame, LinkedHashMap<String, String> speciesColorMap) {
		final JFrame keyFrameVar = UtilityMethods.createKeyBrowser(Constants.VARIANTS_WINDOW_TITLE, speciesColorMap);
		keyFrameVar.addWindowListener(new WindowAdapter() {

		    @Override
		    public void windowClosing(WindowEvent e) {
		        // handle closing the window
		    	keyFrameVar.setVisible(false);
		    }
		});
		frame.keyFrameVar = keyFrameVar;
		
		final JFrame keyFrameP_Values = UtilityMethods.createKeyBrowser(Constants.P_VALUE_WINDOW_TITLE, speciesColorMap);
		keyFrameP_Values.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// handle closing the window
				keyFrameP_Values.setVisible(false);
			}
		});
		frame.keyFrameP_Values = keyFrameP_Values;
		
		final JFrame keyFrameReads = UtilityMethods.createKeyBrowser(Constants.READS_WINDOW_TITLE, null);
		keyFrameReads.addWindowListener(new WindowAdapter() {

		    @Override
		    public void windowClosing(WindowEvent e) {
		        // handle closing the window
		    	keyFrameReads.setVisible(false);
		    }
		});
		frame.keyFrameReads = keyFrameReads;
	}
	
	// based on https://stackoverflow.com/questions/19577893/custom-scrollbar-arrows
	class ArrowScrollbarUI extends BasicScrollBarUI {

		private ImageIcon downArrow, upArrow, leftArrow, rightArrow;

		public ArrowScrollbarUI(){
			upArrow = new ImageIcon("etc/top_arrow21.jpg"); 
			downArrow = new ImageIcon("etc/bottom_arrow21.jpg"); 
			rightArrow = new ImageIcon("etc/right_arrow21.jpg"); 
			leftArrow = new ImageIcon("etc/left_arrow21.jpg");       
		}

		@Override
		protected JButton createDecreaseButton(int orientation) {
			JButton decreaseButton = new JButton(getAppropriateIcon(orientation)){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Dimension getPreferredSize() {
					return new Dimension(22, 22);
				}
			};
			return decreaseButton;
		}
		@Override
		protected JButton createIncreaseButton(int orientation) {
			JButton increaseButton = new JButton(getAppropriateIcon(orientation)){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Dimension getPreferredSize() {
					return new Dimension(22, 22);
				}
			};
			return increaseButton;
		}

		private ImageIcon getAppropriateIcon(int orientation){
			switch(orientation){
			case SwingConstants.SOUTH: return downArrow;
			case SwingConstants.NORTH: return upArrow;
			case SwingConstants.EAST: return rightArrow;
			default: return leftArrow;
			}
		}
		@Override 
		protected void configureScrollBarColors(){
			this.thumbColor = Color.gray;	
			this.thumbHighlightColor = Color.darkGray;
			this.thumbDarkShadowColor = Color.black;	        	
			//this.thumbLightShadowColor = Color.lightGray;
			this.trackColor = Color.gray;
			this.trackHighlightColor = Color.gray;
			//this.trackHighlight = 2;
		}
	}

	public static void main(String args[]) {
		//Check and Create directory in users system
		CheckConfig.checkAndCreateDir();
		//CheckConfig.macMenuBar();
		
		//Copy etc to home directory
		if (Constants.COPY_DATA) {
			CheckConfig.copyDirectory(Constants.PATH_TO_ETC, CheckConfig.pathToHomeDir);
		}
		
		if (CheckConfig.osname.equals(Constants.OSNAME_MAC)) {
			//To display icon in MAC OS dock 
			Application application = Application.getApplication();
			Image image = Toolkit.getDefaultToolkit().getImage(Constants.MAIN_IMAGE_PATH_32);
			application.setDockIconImage(image);
		}
		
		//based on code from http://stackoverflow.com/questions/6403821/how-to-add-an-image-to-a-jframe-title-bar
		final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_16).getImage()); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_32).getImage());
		
		final DataTableFrame frame = new DataTableFrame();
		frame.setIconImages(icons);
		frame.setSize(DataTableConstants.TABLE_WIDTH, DataTableConstants.TABLE_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setLocationRelativeTo(null);
			
		if (args.length > 0) {
			String filename = args[0];
			boolean validURL = false;
			try {
			    URI uri = new URI(filename);
			    URL url = new URL(filename);
			    // perform checks for scheme, authority, host, etc., based on your requirements

//			    if ("mailto".equals(uri.getScheme()) {/*Code*/}
//			    if (uri.getHost() == null) {/*Code*/}
			    validURL = true;
			} catch (Exception e) {
				validURL = false;
			}
			
			if (validURL) {
				URL url;
				try {
					String name = "file.txt";
					// based on https://stackoverflow.com/questions/8324862/how-to-create-file-object-from-url-object
					url = new URL(filename);
					URLConnection connection = url.openConnection();
					InputStream in = connection.getInputStream();
					FileOutputStream fos = new FileOutputStream(new File(name));
					byte[] buf = new byte[512];
					while (true) {
					    int len = in.read(buf);
					    if (len == -1) {
					        break;
					    }
					    fos.write(buf, 0, len);
					}
					in.close();
					fos.flush();
					fos.close();
					frame.loadFile(new File(name));
					if (args.length == 1) {
						frame.setVisible(true);
					} else {

					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					System.out.println("URL not valid.");
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error.");
					e.printStackTrace();
				}
			} else {
				File f = new File(filename);
				if (f.exists()) {
					frame.loadGNProject(f);
					//frame.loadFile(new File(filename));
					if (args.length == 1) {
						frame.setVisible(true);
					} else if (args.length > 1) {
						for (int i = 1; i < args.length; i++) {
							if (args[i].equals("-s")) {
								frame.silent = true;
							}
						}
						frame.setVisible(true);
					}
				} else {
					System.out.println("File does not exist.");
				}
			}
		} else {
			// load data directory for testing here
			//frame.dataDirectory = "polar_ref-sv032819/";
			frame.dataDirectory = "polar_ref-pigh_xrcc-data/";
			frame.speciesColorMap = UtilityMethods.speciesColorMap(frame.dataDirectory + Constants.DEFAULT_SPECIES_COLOR_FILE);
			frame.createKeyFrames(frame, frame.speciesColorMap);
			File refSpecFile = new File(frame.dataDirectory + Constants.REF_SPECIES_FILENAME);
			System.out.println(refSpecFile.getAbsolutePath());
			if (refSpecFile.exists()) {
				frame.refSpec = UtilityMethods.refSpec(frame.dataDirectory + Constants.REF_SPECIES_FILENAME);
				System.out.println(frame.refSpec);
			}
			frame.loadDataTable(new File(frame.dataDirectory + Constants.DATA_TABLE_FILENAME));
			frame.setVisible(true);
		}
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent we) {
		    	  if (Constants.USE_EXIT_PROMPT) {
		    		//Toolkit.getDefaultToolkit().beep();
				        int result = JOptionPane.showConfirmDialog(frame,
				            "Do you want to Exit ?", "Exit Genome Navigator ",
				            JOptionPane.YES_NO_OPTION);
				        if (result == JOptionPane.YES_OPTION) {
				        	System.exit(0);
				        } else if (result == JOptionPane.NO_OPTION) {
				        	
				        }
		    	  } else {
		    		  System.exit(0);
		    	  }	    	
		      }
		    });
	}
	
	 
}

