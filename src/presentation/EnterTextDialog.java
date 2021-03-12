package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 * @author James Kelley
 * Generic dialog with text entry and buttons - was extended for FindDialog and SetAreaDialog
 *
 */
public class EnterTextDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField textField = new JTextField();

	public JButton selectButton = new JButton("  SELECT  ");
	public JButton cancelButton = new JButton("  CANCEL  ");
	
	public JLabel textLabel = new JLabel();
	
	private JMenuItem pasteItem = new JMenuItem("Paste");

	public EnterTextDialog() {

		getRootPane().setDefaultButton(selectButton);

		setTitle("Select");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		textField.setEditable(true);		

		textField.setPreferredSize(new Dimension(350, 25));
		textField.setMaximumSize(new Dimension(350, 25));
		textField.setMinimumSize(new Dimension(350, 25));
		
		textLabel.setForeground(Color.lightGray);
		textLabel.setFont(new Font("Courier", Font.PLAIN, 16));
		
		//box layout
		Box vb = Box.createVerticalBox();

		Box hbLabels = Box.createHorizontalBox();
		Box hbTop = Box.createHorizontalBox();	    	    
		Box hbTextLabel = Box.createHorizontalBox();	    
		Box hbText = Box.createHorizontalBox();
		
		Box vbLabels = Box.createVerticalBox();
		Box vbCombos = Box.createVerticalBox();

		Box hbLabeledCombos = Box.createHorizontalBox();
		Box hbButton = Box.createHorizontalBox();

		//top label
		JLabel topLabel = new JLabel();
		topLabel.setText("");
		topLabel.setSize(new Dimension(300, 30));
		topLabel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
		
		hbTop.add(topLabel);	
		hbTop.setAlignmentX(LEFT_ALIGNMENT);

		hbLabels.add(hbTop);

		//text Label and combo
		textLabel.setText("Enter Text:");
		textLabel.setPreferredSize(new Dimension(100, 25));
		textLabel.setMaximumSize(new Dimension(100, 25));
		textLabel.setMinimumSize(new Dimension(100, 25));
		textLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		textLabel.setAlignmentX(LEFT_ALIGNMENT);
		textLabel.setAlignmentY(TOP_ALIGNMENT);	    	    

		JPanel panelTextLabel = new JPanel();
		panelTextLabel.setLayout(new BoxLayout(panelTextLabel, BoxLayout.X_AXIS));
		panelTextLabel.add(textLabel);
		panelTextLabel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

		hbTextLabel.add(panelTextLabel);
		hbTextLabel.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panelTextField = new JPanel();
		panelTextField.setLayout(new BoxLayout(panelTextField, BoxLayout.X_AXIS));
		panelTextField.add(textField);
		panelTextField.setBorder(BorderFactory.createEmptyBorder(0,0,10,10));
		panelTextField.setAlignmentX(RIGHT_ALIGNMENT);

		hbText.add(panelTextField);
		hbText.setAlignmentX(RIGHT_ALIGNMENT);

		vbLabels.add(hbTextLabel);
		JLabel blankLabel1 = new JLabel("");
		vbLabels.add(blankLabel1);
		vbCombos.add(hbText);
		
		selectButton.setMnemonic(KeyEvent.VK_S);
		selectButton.setEnabled(false);
		JLabel blank = new JLabel("    "); 
		cancelButton.setMnemonic(KeyEvent.VK_C);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(selectButton);
		buttonPanel.add(blank);
		buttonPanel.add(cancelButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));

		hbButton.add(buttonPanel);

		vb.add(hbLabels);
		hbLabeledCombos.add(vbLabels);
		hbLabeledCombos.add(vbCombos);
		vb.add(hbLabeledCombos);
		vb.add(hbButton);

		add(vb);
		
		ActionListener selectButtonActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent prodActionEvent) {
					
			}
		};

		selectButton.addActionListener(selectButtonActionListener);

		ActionListener cancelButtonActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent prodActionEvent) {
				setVisible(false);
				dispose();
			}
		};

		cancelButton.addActionListener(cancelButtonActionListener);
		
		pasteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) { 
				textField.setText("");
	            try{
	                String clip_string = getClipboardContents(EnterTextDialog.this);
	                if (clip_string != null) {
	                	textField.replaceSelection(clip_string.trim());
	                }
	            }catch(Exception excpt){
	                 //excpt.printStackTrace();
	            	System.out.println("Clipboard error.");
	            }
			}
		});
		
		final JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(pasteItem);
		textField.add(popupMenu);
		
		textField.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e)  {check(e);}
			public void mouseReleased(MouseEvent e) {check(e);}

			public void check(MouseEvent e) {
				if (e.isPopupTrigger()) { //if the event shows the menu
					popupMenu.show(textField, e.getX(), e.getY()); 
				}
			}
		});  
		
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				enableButtons();
				fieldChangeAction();
			}
			public void removeUpdate(DocumentEvent e) {
				enableButtons();
				fieldChangeAction();
			}
			public void insertUpdate(DocumentEvent e) {
				enableButtons();
				fieldChangeAction();
			}

			public void enableButtons() {
				if (textField.getText() != null && textField.getText().trim().length() > 0) {
					selectButton.setEnabled(true);
				} else {
					selectButton.setEnabled(false);
				}
			}
			public void fieldChangeAction() {
				if (textField.getText().trim().length() > 0) {
					selectButton.setEnabled(true);
				} else {
					selectButton.setEnabled(false);
				}
			}
		});

	} 
	
	//from http://www.javakb.com/Uwe/Forum.aspx/java-programmer/21291/popupmenu-for-a-cell-in-a-JXTable
	private static String getClipboardContents(Object requestor) {
		Transferable t = Toolkit.getDefaultToolkit()
				.getSystemClipboard().getContents(requestor);
		if (t != null) {
			DataFlavor df = DataFlavor.stringFlavor;
			if (df != null) {
				try {
					Reader r = df.getReaderForText(t);
					char[] charBuf = new char[512];
					StringBuffer buf = new StringBuffer();
					int n;
					while ((n = r.read(charBuf, 0, charBuf.length)) > 0) {
						buf.append(charBuf, 0, n);
					}
					r.close();
					return (buf.toString());
				} catch (IOException ex) {
//					JOptionPane.showMessageDialog(null,                
//							"Clipboard Error.",                
//							"Error",                                
//							JOptionPane.ERROR_MESSAGE);
					//ex.printStackTrace();
					System.out.println("Clipboard Error.");
				} catch (UnsupportedFlavorException ex) {
//					JOptionPane.showMessageDialog(null,                
//							"Clipboard Error. Unsupported Flavor",                
//							"Error",                                
//							JOptionPane.ERROR_MESSAGE);
					//ex.printStackTrace();
					System.out.println("Clipboard Error.");
				}
			}
		}
		return null;
	}

	private static void setClipboardContents(String s) {
		StringSelection selection = new StringSelection(s);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				selection, selection);
	}
	
	public static void main(String[] args) throws Exception {
		//based on code from http://stackoverflow.com/questions/6403821/how-to-add-an-image-to-a-jframe-title-bar
		final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_16).getImage()); 
		icons.add(new ImageIcon(Constants.MAIN_IMAGE_PATH_32).getImage());

		EnterTextDialog frame = new EnterTextDialog();

		frame.setIconImages(icons);
		//frame.setSize(550, 270);
		frame.pack();
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
