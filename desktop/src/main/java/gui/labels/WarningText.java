package gui.labels;

import gui.frame.MainFrame;
import gui.picture.Picture;
import gui.settings.Settings;

import java.awt.*;

/**
 * Text to be shown, when the Code is invalid.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class WarningText extends BasicTextArea {
    /**
     * Constructor
     */
    public WarningText() {
        super(Settings.getConnectionString());
        setTheSize();
        setForeground(Color.RED);
    }

    /**
     * Sets the size of the Label.
     */
    private void setTheSize() {
        setLocation(((MainFrame.getTheWidth() / 2) - (getWidth() / 2)), Picture.getPositionForNextElement()+100);
        setSize(MainFrame.getTheWidth() - 100, 200);
    }
}
