package gui.textfield;

import gui.frame.MainFrame;
import gui.labels.AppealText;

import javax.swing.*;
import java.awt.*;

/**
 * Field to enter the ID-Code.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class IdTextField extends JTextField {
    /** The x-size for the next elements. */
    private static int sizeForNextElements;

    /** the y-position for the next element. */
    private static int positionOfNextElements;

    /**
     * Constructor.
     */
    public IdTextField() {
        super();
        init();
    }

    /**
     * Initializes the TextField.
     */
    private void init() {
        setFont(MainFrame.getTheFont());
        setTheSize();
    }

    /**
     * Setter for the Size.
     */
    private void setTheSize() {
        int x = ((MainFrame.getTheWidth() - AppealText.getWidthForTheNextElement()) / 2);
        setBounds(x, AppealText.getPositionForNextElement(), AppealText.getWidthForTheNextElement(), 50);
        setPreferredSize(new Dimension(AppealText.getWidthForTheNextElement(), 50));

        positionOfNextElements = getY() + getHeight() + 25;
        sizeForNextElements = (AppealText.getWidthForTheNextElement() / 2);
    }

    /**
     * Getter for the position of the next elements.
     * @return y-position of the next elements.
     */
    public static int getPositionOfNextElements() {
        return positionOfNextElements;
    }

    /**
     * Getter the size for the next elements.
     * @return x-size for the next elements.
     */
    public static int getSizeForNextElements() {
        return sizeForNextElements;
    }
}
