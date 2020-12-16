package gui.buttons;

import gui.frame.MainFrame;
import gui.labels.AppealText;
import gui.settings.Settings;

/**
 * The Negative Radio-Button.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class RadioButtonNegative extends BasicRadioButton {
    /**
     * Constructor.
     */
    public RadioButtonNegative() {
        super(Settings.getNegativeString());
        setPosition();
    }

    /**
     * Setter for the position.
     */
    private void setPosition() {
        int x = (MainFrame.getTheWidth() / 2);
        int y = AppealText.getPositionForNextElement();
        setLocation(x, y);
    }
}
