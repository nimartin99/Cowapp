package gui.buttons;

import gui.frame.MainFrame;
import gui.labels.AppealText;
import gui.settings.Settings;

/**
 * The positive RadioButton.
 */
public class RadioButtonPositive extends BasicRadioButton {
    /**
     * Constructor.
     *
     */
    public RadioButtonPositive() {
        super(Settings.getPositiveString());
        setPosition();
        setSelected(true);
    }

    /**
     * Setter for the position.
     */
    private void setPosition() {
        int x = ((MainFrame.getTheWidth() / 2) - getWidth());
        int y = AppealText.getPositionForNextElement();
        setLocation(x, y);
    }
}
