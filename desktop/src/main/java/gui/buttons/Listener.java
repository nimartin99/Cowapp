package gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action-Listener for the Buttons.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class Listener implements ActionListener {
    /** The basic Button. */
    private BasicButton basicButton;

    /**
     * Constructor.
     * @param basicButton the basicButton.
     */
    public Listener(BasicButton basicButton) {
        this.basicButton = basicButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        basicButton.actionToPerform();
    }
}
