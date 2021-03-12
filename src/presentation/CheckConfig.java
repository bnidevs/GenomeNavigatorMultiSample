package presentation;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.FileUtils;

public class CheckConfig {

	//Constant strings required to check if the directory exists and create a new one
	public static String osname = System.getProperty("os.name");
	public static String userdir = System.getProperty("user.home");
	public static String filesep = System.getProperty("file.separator");
	public static String userName = System.getProperty("user.name");
		
	public static String pathToHomeDir;
	//Function to check for directory
	public static void checkAndCreateDir() {
		
		if (osname.equals(Constants.OSNAME_MAC)) {
			pathToHomeDir = (userdir + filesep + Constants.HOMEDIR_FOLDER);
			if (Files.isDirectory(Paths.get(pathToHomeDir))) {
				//System.out.println(Constants.HOMEDIR_FOLDER + " folder exists");
			} else {
				try {
					File thedir = new File(pathToHomeDir);
					thedir.mkdir();
					//System.out.println(Constants.HOMEDIR_FOLDER + " is created");
				} catch (SecurityException se) {
					System.out.println("Error creating a directory");
				}
			}
		}
		else if (osname.equals(Constants.OSNAME_LINUX)) {
			pathToHomeDir = (userdir + filesep + Constants.HOMEDIR_FOLDER);
			if (Files.isDirectory(Paths.get(pathToHomeDir))) {
				//System.out.println(Constants.HOMEDIR_FOLDER + " folder exists");
			} else {
				try {
					File thedir = new File(pathToHomeDir);
					thedir.mkdir();
					//System.out.println(Constants.HOMEDIR_FOLDER + " is created");
				} catch (SecurityException se) {
					System.out.println("Error creating a directory");
				}
			}
		}
		else if (osname.contains(Constants.OSNAME_WINDOWS)) {
			pathToHomeDir = Constants.PATH_PREFIX_WINDOWS + userName + Constants.PATH_SUFFIX_WINDOWS + Constants.HOMEDIR_FOLDER;
			if (osname.contains("XP")) {
				pathToHomeDir = Constants.PATH_PREFIX_WINDOWS_XP + userName + Constants.PATH_SUFFIX_WINDOWS_XP + Constants.HOMEDIR_FOLDER;
			} else {
				
			}
			if (Files.isDirectory(Paths.get(pathToHomeDir))) {
				//System.out.println(Constants.HOMEDIR_FOLDER + " folder exists");
			} else {
				try {
					File thedir = new File(pathToHomeDir);
					thedir.mkdir();
					//System.out.println(Constants.HOMEDIR_FOLDER + " is created");
				} catch (SecurityException se) {
					System.out.println("Error creating a directory");
				}
			}
		} else {
			System.out.println(osname + " error");
		}
		
	}
	
	public static void macMenuBar() {
		if(osname.equals(Constants.OSNAME_MAC)) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Genome Navigator");
			
			try{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}catch(ClassNotFoundException e ) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void copyDirectory(String source, String destination) {
		
		File srcDir = new File(source);
		
		File destDir = new File(destination);
		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to copy etc folder");
			e.printStackTrace();
		}
	}
		
}
