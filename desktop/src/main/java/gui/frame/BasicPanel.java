package gui.frame;

import gui.frame.MainFrame;

import javax.swing.*;

/**
 * Panels for the gui.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class BasicPanel extends JPanel {
    public BasicPanel() {
        super();
        init();
    }

    private void init() {
        setBackground(MainFrame.getColor());
    }
}
