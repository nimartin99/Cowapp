package gui.labels;

import gui.frame.MainFrame;
import gui.picture.Picture;
import gui.settings.Settings;

/**
 * The text that user should wait.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class WaitText extends BasicTextArea {
    /**
     * Constructor
     */
    public WaitText() {
        super(Settings.getWaitString());
        setLocation(((MainFrame.getTheWidth() / 2) - (getWidth() / 2)), Picture.getPositionForNextElement()+100);
        setSize(MainFrame.getTheWidth() - 100, 200);
    }
}
