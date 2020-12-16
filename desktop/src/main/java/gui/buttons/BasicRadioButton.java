package gui.buttons;

import gui.frame.MainFrame;

import javax.swing.*;

/**
 * RadioButton to choose the Test-Result.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class BasicRadioButton extends JRadioButton {

    /**
     * Constructor.
     * @param string
     */
    public BasicRadioButton(String string) {
        super(string);
        initTheRadioButton();
    }

    /**
     * Initializes the Radio Button.
     */
    private void initTheRadioButton() {
        setFont(MainFrame.getTheFont());
        setSize(150, 50);
        setBackground(MainFrame.getColor());
        setHorizontalAlignment(SwingConstants.CENTER);
    }
}
