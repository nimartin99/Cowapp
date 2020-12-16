package gui.buttons;

import gui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

public abstract class BasicButton extends JButton {

    /**
     * Constructor.
     * @param string Text for the Button
     */
    public BasicButton(String string) {
        super(string);
        initTheButton();
        addActionListener(new Listener(this));
    }

    /**
     * Initializes the Button.
     */
    private void initTheButton() {
        setFont(MainFrame.getTheFont());
        setBackground(MainFrame.getColor2());
        setForeground(Color.WHITE);
    }

    public abstract void actionToPerform();
}
