package gui.labels;

import gui.buttons.ConfirmButton;
import gui.frame.MainFrame;
import gui.settings.Settings;

import java.awt.*;

public class SyntaxText extends BasicTextArea{
    /**
     * Constructor
     */
    public SyntaxText() {
        super(Settings.getSyntaxString());
        initTheLabel();
    }

    /**
     * Initializes the SyntaxText.
     */
    private void initTheLabel() {
        setLocation(((MainFrame.getTheWidth() / 2) - (getWidth() / 2)), ConfirmButton.getyPositionOfTheNextElement());
        setForeground(Color.RED);
        setVisible(false);
    }

}
