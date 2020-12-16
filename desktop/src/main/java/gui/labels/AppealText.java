package gui.labels;

import gui.picture.Picture;
import gui.settings.Settings;

/**
 * JLabel which shows the Appel to enter the code.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class AppealText extends BasicTextLabel {
    /** The Position for the next element. */
    private static int positionForNextElement;

    /** The Width for the next element. */
    private static int widthForTheNextElement;

    /**
     * Constructor.
     */
    public AppealText() {
        super(Settings.getAppealString());
        initTheLabel();
    }

    /**
     * Constructor for the Second Appeal
     */
    public AppealText(boolean val) {
        super(Settings.getNextAppeal());
        initTheLabel();
    }

    /**
     * Initializes the AppealText.
     */
    private void initTheLabel() {
        setLocation(0, Picture.getPositionForNextElement());

        positionForNextElement = getY() + getHeight();
        widthForTheNextElement = getWidth() / 2;
    }

    /**
     * Getter for the position of the next element.
     * @return position of the next element.
     */
    public static int getPositionForNextElement() {
        return positionForNextElement;
    }

    /**
     * Getter for the width of the next element.
     * @return width of the next element.
     */
    public static int getWidthForTheNextElement() {
        return widthForTheNextElement;
    }
}
