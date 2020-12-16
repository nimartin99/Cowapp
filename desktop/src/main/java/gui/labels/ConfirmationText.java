package gui.labels;

import gui.frame.MainFrame;
import gui.picture.Picture;
import gui.settings.Settings;

/**
 * Text to be shown when the Code is transmitted.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class ConfirmationText extends BasicTextArea {

    /**
     * Constructor
     */
    public ConfirmationText() {
        super(Settings.getSuccessfulString());
        setLocation(((MainFrame.getTheWidth() / 2) - (getWidth() / 2)), Picture.getPositionForNextElement()+100);
        setSize(MainFrame.getTheWidth() - 100, 200);
    }
}
