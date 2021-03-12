package presentation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class FindDialog extends EnterTextDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FindDialog() {
		super();
		setTitle("Find");
//		textLabel.setForeground(Color.lightGray);
//		textLabel.setFont(new Font("Courier", Font.PLAIN, 14));
		textLabel.setText("Find:");
		selectButton.setText("  FIND  ");
	}
	
	public static void main(String[] args) throws Exception {
		//based on code from http://stackoverflow.com/questions/6403821/how-to-add-an-image-to-a-jframe-title-bar
		final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_16).getImage()); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_32).getImage());

		FindDialog frame = new FindDialog();

		frame.setIconImages(icons);
		//frame.setSize(550, 270);
		frame.pack();
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
