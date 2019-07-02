/**
 * --------------------------------------------------------
 * Class: LibraryComponents
 *
 * @author Mark O'Reilly
 * Developed: 2016-2017
 *
 * Purpose: To contain a library of utility methods that can be accessed from other Java applications
 *
 * Currently: 
 *  - LocateAJLabel - for positioning a JLabel using the layout manager: SpringLayout 
 *  - LocateAJTextField - for positioning a JTextField using SpringLayout 
 *  - LocateAJButton - for positioning a JButton using SpringLayout 
 *  - LocateAJTextArea - for positioning a JTextArea using SpringLayout 
 *  - LocateAJScrollPane - for positioning a JScrollPane using SpringLayout
 *
 * ----------------------------------------------------------
 */


package quickquiz;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.Dimension;



public class LibraryComponents
{
    
    /** --------------------------------------------------------
    * Purpose: Locate a single JLabel within a JFrame.
    * @param   JFrame, Layout_manager, JLabel_Caption, Width, X_position, Y_Position
    * @returns The JLabel.
    * ----------------------------------------------------------
    */
    public static JLabel LocateAJLabel(JFrame myJFrame, SpringLayout myJLabelLayout, String JLabelCaption, int x, int y)
    {
	// Declare and Instantiate the JLabel
        JLabel myJLabel = new JLabel(JLabelCaption);
	// Add the JLabel to the screen
        myJFrame.add(myJLabel); 
	// Set the position of the JLabel (From left hand side of the JFrame (West), and from top of JFrame (North))
        myJLabelLayout.putConstraint(SpringLayout.WEST, myJLabel, x, SpringLayout.WEST, myJFrame);
        myJLabelLayout.putConstraint(SpringLayout.NORTH, myJLabel, y, SpringLayout.NORTH, myJFrame);
	// Return the label to the calling method
        return myJLabel;
    }
   
        
    /** --------------------------------------------------------
    * Purpose: Locate a single JTextField within a JFrame.
    * @param   JFrame, KeyListener, Layout_manager, Width, X_position, Y_Position
    * @returns The JTextField.
    * ----------------------------------------------------------
    */
    public static JTextField LocateAJTextField(JFrame myJFrame, KeyListener myKeyLstnr, SpringLayout myJTextFieldLayout, int width, int x, int y)
    {
        JTextField myJTextField = new JTextField(width);
        myJFrame.add(myJTextField);  
        myJTextField.addKeyListener(myKeyLstnr);
        myJTextFieldLayout.putConstraint(SpringLayout.WEST, myJTextField, x, SpringLayout.WEST, myJFrame);
        myJTextFieldLayout.putConstraint(SpringLayout.NORTH, myJTextField, y, SpringLayout.NORTH, myJFrame);
        return myJTextField;
    }

        
    /** --------------------------------------------------------
    * Purpose: Locate a single JButton within a JFrame.
    * @param   JFrame, ActionListener, Layout_manager, JButton_name, JButton_caption, X_position, Y_Position, Width, Height
    * @returns The JButton.
    * ----------------------------------------------------------
    */
    public static JButton LocateAJButton(JFrame myJFrame, ActionListener myActnLstnr, SpringLayout myJButtonLayout, String  JButtonCaption, int x, int y, int w, int h)
    {    
        JButton myJButton = new JButton(JButtonCaption);
        myJFrame.add(myJButton);
        myJButton.addActionListener(myActnLstnr);
        myJButtonLayout.putConstraint(SpringLayout.WEST, myJButton, x, SpringLayout.WEST, myJFrame);
        myJButtonLayout.putConstraint(SpringLayout.NORTH, myJButton, y, SpringLayout.NORTH, myJFrame);
        myJButton.setPreferredSize(new Dimension(w,h));
        return myJButton;
    }

    
    /** --------------------------------------------------------
    * Purpose: Locate a single JTextArea within a JFrame.
    * @param   JFrame, Layout_manager, X_position, Y_Position, Width, Height
    * @returns The JTextArea.
    * ----------------------------------------------------------
    */
    public static JTextArea LocateAJTextArea(JFrame myJFrame, SpringLayout myLayout, int x, int y, int w, int h)
    {    
        JTextArea myJTextArea = new JTextArea(w,h);
        myJFrame.add(myJTextArea);
        myLayout.putConstraint(SpringLayout.WEST, myJTextArea, x, SpringLayout.WEST, myJFrame);
        myLayout.putConstraint(SpringLayout.NORTH, myJTextArea, y, SpringLayout.NORTH, myJFrame);
        
        return myJTextArea;
    }
    // Currently Non-Functional
    /** --------------------------------------------------------
    * Purpose: Locate a single JScrollPane within a JFrame.
    * @param   JFrame, Layout_manager, JTextArea, X_position, Y_Position, Width, Height
    * @returns The JScrollPane.
    * ----------------------------------------------------------
    */
    public static JScrollPane LocateAJScrollPane(JFrame myJFrame, SpringLayout myLayout, JTextArea txtArea, int x, int y)
    {    
        JScrollPane myJScrollPane = new JScrollPane(txtArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //setPreferredSize(new Dimension(350, 50));
        myLayout.putConstraint(SpringLayout.WEST, myJScrollPane, x, SpringLayout.WEST, myJFrame);
        myLayout.putConstraint(SpringLayout.NORTH, myJScrollPane, y, SpringLayout.NORTH, myJFrame);
        
        return myJScrollPane;
    }
    
    public int getLargestIndex(int arr[])
    {
        int largestIndex = -1;
        int largestValue = -1;
        for (int i = 0; i<arr.length; i++)
        {
            if(arr[i] > largestValue)
            {
                largestValue = arr[i];
                largestIndex = i;
            }
        }        
        return largestIndex;       
    }

    public int getLargestValue(int arr[])
    {
        int largestValue = -1;
        for (int i = 0; i<arr.length; i++)
        {
            if(largestValue > arr[i])
            {
                largestValue = arr[i];
            }
        }        
        return largestValue;       
    }
    public void ClearData(JTextField[][] JTxtFld, int maxX, int maxY)
    {
        for (int y = 2; y < maxY; y++)
        {
            for (int x = 1; x < maxX; x++)
            {
                JTxtFld[x][y].setText("");
            }
        }
    }
    
}
