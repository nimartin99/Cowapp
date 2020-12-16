package gui.labels;

import gui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * BasicTextLabels for the Gui.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class BasicTextLabel extends JLabel {

    /**
     * Constructor
     * @param text The text to be shown.
     */
    public BasicTextLabel(String text) {
        super(text, SwingConstants.HORIZONTAL);
        init();
    }

    /**
     * Inits the Label.
     */
    private void init() {
        setFont(MainFrame.getTheFont());
        setSize(MainFrame.getTheWidth(), 50);
        setPreferredSize(new Dimension(MainFrame.getTheWidth(), 50));

    }
}
