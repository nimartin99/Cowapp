package gui.picture;

import gui.frame.MainFrame;
import gui.settings.XML_Translator;

import javax.swing.*;
import java.awt.*;

/**
 * The CoW-App emblem.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class Picture extends JLabel {
    /** The Image of this picture. */
    private ImageIcon image;

    /** The Position for the next element. */
    private static int positionForNextElement;

    /**
     * Constructor
     */
    public Picture() {
        super();
        init();
    }

    /**
     * Initializes the Object.
     */
    private void init() {
        setTheImage();
        setTheSize();
    }

    /**
     * Setter for the Image.
     */
    private void setTheImage() {
        XML_Translator xml_translator = new XML_Translator("xml/Settings.xml");
        image = new ImageIcon(xml_translator.getString("image"));
        setIcon(image);
    }

    /**
     * Setter for the Size.
     */
    private void setTheSize() {
        int x = (MainFrame.getTheWidth() / 2) - (image.getIconWidth() / 2);
        int y = 25;
        setBounds(x, y, image.getIconWidth(), image.getIconHeight());
        setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
        positionForNextElement = (y + image.getIconHeight() + 25);
    }

    /**
     * Getter for the position of the next element.
     * @return position of the next element.
     */
    public static int getPositionForNextElement() {
        return positionForNextElement;
    }
}
