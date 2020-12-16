package gui.buttons;

import gui.settings.LanguageE;
import gui.textfield.IdTextField;
import gui.frame.MainFrame;
import gui.settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Button to change the language.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class LanguageButton extends BasicButton {

    /**
     * Constructor
     */
    public LanguageButton() {
        super(Settings.getLanguageString());
        setTheSize();
    }

    /**
     * Sets the size of the Button.
     */
    private void setTheSize() {
        int width = IdTextField.getSizeForNextElements();
        int height = 50;
        int y = IdTextField.getPositionOfNextElements();
        int x = (MainFrame.getTheWidth() / 2) + 25;

        setBounds(x, y, width, height);
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void actionToPerform() {
        if (Settings.getCurrentLanguage() == LanguageE.English) {
            MainFrame.changeLanguage(LanguageE.German);
        } else {
            MainFrame.changeLanguage(LanguageE.English);
        }
    }
}
