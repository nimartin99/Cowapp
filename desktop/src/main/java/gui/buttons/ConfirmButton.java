package gui.buttons;

import gui.textfield.IdTextField;
import gui.frame.MainFrame;
import gui.settings.Settings;

import java.awt.*;

/**
 * Button to confirm and start the transmitting.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class ConfirmButton extends BasicButton {
    /** The y-position of the next element. */
    private static int yPositionOfTheNextElement;

    /** First step over? */
    private static boolean isNextStep;

    /**
     * Constructor
     */
    public ConfirmButton() {
        super(Settings.getConfirmString());
        setTheSize();
    }

    /**
     * Sets the size of the Button.
     */
    private void setTheSize() {
        int width = IdTextField.getSizeForNextElements();
        int height = 50;
        int y = IdTextField.getPositionOfNextElements();
        int x = (MainFrame.getTheWidth() / 2 - width) - 25;

        setBounds(x, y, width, height);
        setPreferredSize(new Dimension(width, height));

        yPositionOfTheNextElement = y + height + 25;
    }

    /**
     * Getter for the y position of the next Element
     * @return the y position of the next Element.
     */
    public static int getyPositionOfTheNextElement() {
        return yPositionOfTheNextElement;
    }

    @Override
    public void actionToPerform() {
        if (!isNextStep) {
            String input = MainFrame.getInputText();
            if (wrongSyntax(input)) {
                MainFrame.wrongInput();
            } else {
                input.toLowerCase();
                MainFrame.nextStep(input);
                isNextStep = true;
            }
        } else {
            MainFrame.outPutText();
        }
    }

    /**
     * Method to check if the Input is right.
     * @param input The input.
     * @return does the input have the right syntax?
     */
    private boolean wrongSyntax(String input) {
        if (input == null) {
            return true;
        }
        if (input.length() != 8) {
            return true;
        }
        if (!input.matches("[a-zA-Z0-9]*")) {
            return true;
        }
        return false;
    }
}
