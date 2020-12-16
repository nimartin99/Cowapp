package gui.labels;

import com.sun.tools.javac.Main;
import gui.frame.MainFrame;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class BasicTextArea extends JTextPane {

    /**
     * Constructor
     * @param text The text to be shown.
     */
    public BasicTextArea(String text) {
        super();
        init(text);
    }

    /**
     * Inits the Label.
     */
    private void init(String text) {
        setText(text);
        StyledDocument doc = getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        setEditable(false);
        setFont(MainFrame.getTheFont());
        setBackground(MainFrame.getColor());
        setSize(MainFrame.getTheWidth() - 100, 50);
        setPreferredSize(new Dimension(MainFrame.getTheWidth(), 50));

    }
}
