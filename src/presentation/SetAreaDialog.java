package presentation;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * 
 * @author James Kelley
 * Allows user to highlight a selected area by coordinates. Right clicking in a derBrowser window
 * only allows user to highlight area of an existing item.
 *
 */
public class SetAreaDialog extends EnterTextDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SetAreaDialog() {
		super();
		setTitle("Highlight Area");
		textLabel.setText("Enter Coordinates:");
		selectButton.setText("  OK  ");
	}
	
	public static void main(String[] args) throws Exception {
		//based on code from http://stackoverflow.com/questions/6403821/how-to-add-an-image-to-a-jframe-title-bar
		final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_16).getImage()); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_32).getImage());

		SetAreaDialog frame = new SetAreaDialog();

		frame.setIconImages(icons);
		//frame.setSize(550, 270);
		frame.pack();
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
