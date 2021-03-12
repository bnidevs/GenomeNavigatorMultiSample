package presentation;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FilterOptionsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// need all variants item
	public JCheckBox overlapGenesCheckBox = new JCheckBox(Constants.OVERLAP_ITEM_NAME);
	public JCheckBox nearbyGenesCheckBox = new JCheckBox(Constants.NEARBY_ITEM_NAME);
	public JCheckBox deletionCheckBox = new JCheckBox(Constants.DELETIONS_ITEM_NAME);	
	public JCheckBox duplicationCheckBox = new JCheckBox(Constants.DUPLICATIONS_ITEM_NAME);
	public JCheckBox insertionCheckBox = new JCheckBox(Constants.INSERTIONS_ITEM_NAME);	
	public JCheckBox inversionCheckBox = new JCheckBox(Constants.INVERSIONS_ITEM_NAME);
	
	JLabel varVsFeatTitleLabel = new JLabel(Constants.VAR_VS_FEATURES_LABEL);
	JLabel varTypesTitleLabel = new JLabel(Constants.VAR_TYPES_LABEL);
	
	public JButton okButton = new JButton("    OK    ");
	public JButton cancelButton = new JButton("  Cancel  ");

	public FilterOptionsDialog() {
		
		getRootPane().setDefaultButton(okButton);
		
		setTitle(Constants.FILTER_OPTIONS_TITLE);
		
		// set check box defaults
		deletionCheckBox.setSelected(true);	
		duplicationCheckBox.setSelected(true);	
		insertionCheckBox.setSelected(true);	
		inversionCheckBox.setSelected(true);	
		
		//box layout
		Box vb = Box.createVerticalBox();

		Box hbVarVsFeatTitle = Box.createHorizontalBox();
		Box hbOverlapGenes = Box.createHorizontalBox();
		
		Box hbNearbyGenes = Box.createHorizontalBox();
		Box hbVarTypesTitle = Box.createHorizontalBox();
		Box hbDeletion = Box.createHorizontalBox();
		Box hbDuplication = Box.createHorizontalBox();
		Box hbInsertion = Box.createHorizontalBox();
		Box hbInversion = Box.createHorizontalBox();
		Box hbButton = Box.createHorizontalBox();
		
		JPanel hbvarVsFeatTitlePanel = new JPanel();
		hbvarVsFeatTitlePanel.setLayout(new BoxLayout(hbvarVsFeatTitlePanel, BoxLayout.X_AXIS));
		varVsFeatTitleLabel.setToolTipText("Deselect both items to show all SVs");
		hbvarVsFeatTitlePanel.add(varVsFeatTitleLabel);
		hbvarVsFeatTitlePanel.setBorder(BorderFactory.createEmptyBorder(
				FilterOptionsConstants.TOP_BORDER, 
				FilterOptionsConstants.LEFT_BORDER, 
				FilterOptionsConstants.SUB_TOPIC_GAP, 
				FilterOptionsConstants.RIGHT_BORDER));

		hbVarVsFeatTitle.add(leftJustify(hbvarVsFeatTitlePanel));
		
		JPanel hbOverlapGenesPanel = new JPanel();
		hbOverlapGenesPanel.setLayout(new BoxLayout(hbOverlapGenesPanel, BoxLayout.X_AXIS));
		hbOverlapGenesPanel.add(overlapGenesCheckBox);
		hbOverlapGenesPanel.setBorder(BorderFactory.createEmptyBorder(
				0, 
				FilterOptionsConstants.LEFT_BORDER, 
				FilterOptionsConstants.SUB_TOPIC_GAP, 
				FilterOptionsConstants.RIGHT_BORDER));

		hbOverlapGenes.add(leftJustify(hbOverlapGenesPanel));
		
		JPanel hbNearbyGenesPanel = new JPanel();
		hbNearbyGenesPanel.setLayout(new BoxLayout(hbNearbyGenesPanel, BoxLayout.X_AXIS));
		hbNearbyGenesPanel.add(nearbyGenesCheckBox);
		hbNearbyGenesPanel.setBorder(BorderFactory.createEmptyBorder(
				0, 
				FilterOptionsConstants.LEFT_BORDER, 
				FilterOptionsConstants.MAIN_TOPIC_GAP, 
				FilterOptionsConstants.RIGHT_BORDER));

		hbNearbyGenes.add(leftJustify(hbNearbyGenesPanel));
		
		JPanel hbVarTypesTitlePanel = new JPanel();
		hbVarTypesTitlePanel.setLayout(new BoxLayout(hbVarTypesTitlePanel, BoxLayout.X_AXIS));
		varTypesTitleLabel.setToolTipText("Select one or more SV type");
		hbVarTypesTitlePanel.add(varTypesTitleLabel);
		hbVarTypesTitlePanel.setBorder(BorderFactory.createEmptyBorder(
				FilterOptionsConstants.TOP_BORDER, 
				FilterOptionsConstants.LEFT_BORDER, 
				FilterOptionsConstants.SUB_TOPIC_GAP, 
				FilterOptionsConstants.RIGHT_BORDER));

		hbVarTypesTitle.add(leftJustify(hbVarTypesTitlePanel));
		
		JPanel hbDeletionPanel = new JPanel();
		hbDeletionPanel.setLayout(new BoxLayout(hbDeletionPanel, BoxLayout.X_AXIS));
		hbDeletionPanel.add(deletionCheckBox);
		hbDeletionPanel.setBorder(BorderFactory.createEmptyBorder(
				0, 
				FilterOptionsConstants.LEFT_BORDER, 
				FilterOptionsConstants.SUB_TOPIC_GAP, 
				FilterOptionsConstants.RIGHT_BORDER));

		hbDeletion.add(leftJustify(hbDeletionPanel));
		
		JPanel hbDuplicationPanel = new JPanel();
		hbDuplicationPanel.setLayout(new BoxLayout(hbDuplicationPanel, BoxLayout.X_AXIS));
		hbDuplicationPanel.add(duplicationCheckBox);
		hbDuplicationPanel.setBorder(BorderFactory.createEmptyBorder(0, 
				FilterOptionsConstants.LEFT_BORDER, 
				FilterOptionsConstants.SUB_TOPIC_GAP, 
				FilterOptionsConstants.RIGHT_BORDER));

		hbDuplication.add(leftJustify(hbDuplicationPanel));
		
		JPanel hbInsertionPanel = new JPanel();
		hbInsertionPanel.setLayout(new BoxLayout(hbInsertionPanel, BoxLayout.X_AXIS));
		hbInsertionPanel.add(insertionCheckBox);
		hbInsertionPanel.setBorder(BorderFactory.createEmptyBorder(0, 
				FilterOptionsConstants.LEFT_BORDER, 
				FilterOptionsConstants.SUB_TOPIC_GAP, 
				FilterOptionsConstants.RIGHT_BORDER));

		hbInsertion.add(leftJustify(hbInsertionPanel));
		
		JPanel hbInversionPanel = new JPanel();
		hbInversionPanel.setLayout(new BoxLayout(hbInversionPanel, BoxLayout.X_AXIS));
		hbInversionPanel.add(inversionCheckBox);
		hbInversionPanel.setBorder(BorderFactory.createEmptyBorder(0, 
				FilterOptionsConstants.LEFT_BORDER, 
				FilterOptionsConstants.MAIN_TOPIC_GAP, 
				FilterOptionsConstants.RIGHT_BORDER));

		hbInversion.add(leftJustify(hbInversionPanel));
		
		okButton.setMnemonic(KeyEvent.VK_O);
		JLabel blank = new JLabel("    "); 
		cancelButton.setMnemonic(KeyEvent.VK_C);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(okButton);
		buttonPanel.add(blank);
		buttonPanel.add(cancelButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,20,FilterOptionsConstants.BOTTOM_BORDER,20));

		hbButton.add(buttonPanel);

		vb.add(hbVarVsFeatTitle);
		vb.add(hbOverlapGenes);
		vb.add(hbNearbyGenes);
		vb.add(hbVarTypesTitle);
		vb.add(hbDeletion);
		vb.add(hbDuplication);
		vb.add(hbInsertion);
		vb.add(hbInversion);
		vb.add(hbButton);
		add(vb);
    	
    	ActionListener okButtonActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent prodActionEvent) {	
				boolean exit = true;
//				System.out.println(overlapGenesCheckBox.isSelected());
//				System.out.println(nearbyGenesCheckBox.isSelected());
//				System.out.println(deletionCheckBox.isSelected());
//				System.out.println(duplicationCheckBox.isSelected());
//				System.out.println(insertionCheckBox.isSelected());
//				System.out.println(inversionCheckBox.isSelected());
				if (!deletionCheckBox.isSelected() && !duplicationCheckBox.isSelected() &&
						!insertionCheckBox.isSelected() && !inversionCheckBox.isSelected()) {
					setAlwaysOnTop(false);
					JOptionPane.showMessageDialog(null,                
							"No SV type is selected.",                
							"SV Type Selection Error.",                                
							JOptionPane.ERROR_MESSAGE);
					setAlwaysOnTop(true);
					exit = false;
				}
				if (exit) {
					setVisible(false);
				}
			}
		}; 
		
		okButton.addActionListener(okButtonActionListener);
    	
    	ActionListener cancelButtonActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent prodActionEvent) {
				setVisible(false);
				dispose();
			}
		}; 
		
		cancelButton.addActionListener(cancelButtonActionListener);
		
	}
	
	/**
	 * Left Justifies component in a panel
	 * @param panel
	 * @return
	 */
	// from http://stackoverflow.com/questions/9212155/java-boxlayout-panels-alignment
	private Component leftJustify(JPanel panel)  {
	    Box  b = Box.createHorizontalBox();
	    b.add( panel );
	    b.add( Box.createHorizontalGlue() );
	    // (Note that you could throw a lot more components
	    // and struts and glue in here.)
	    return b;
	}
	
	public static void main(String[] args) throws Exception {
		//based on code from http:stackoverflow.com/questions/6403821/how-to-add-an-image-to-a-jframe-title-bar
		final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon("etc/most16.jpg").getImage()); 
		icons.add(new ImageIcon("etc/most32.jpg").getImage());
		
		FilterOptionsDialog d = new FilterOptionsDialog();
		
		d.setIconImages(icons);
    	d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	//d.setSize(400, 300);
    	d.pack();
    	d.setLocationRelativeTo(null);
    	d.setVisible(true);

	}
	
}
