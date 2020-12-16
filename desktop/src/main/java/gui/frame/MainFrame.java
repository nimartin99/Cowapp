package gui.frame;

import gui.buttons.RadioButtonNegative;
import gui.buttons.RadioButtonPositive;
import gui.labels.*;
import gui.textfield.IdTextField;
import gui.buttons.ConfirmButton;
import gui.buttons.LanguageButton;
import gui.picture.Picture;
import gui.settings.LanguageE;
import gui.settings.Settings;
import gui.settings.XML_Translator;
import network.RetrofitController;

import javax.swing.*;
import java.awt.*;

/**
 * This is the MainFrame of the CoW-Desktop-App-Gui.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class MainFrame extends JFrame {

    /** The only instance of this class. */
    private static MainFrame mainFrame;

    /** The contentPane of the MainFrame. */
    private static Container contentPane;

    /** The current shown Panel. */
    private static BasicPanel currentPanel;

    /** The font for the GUI. */
    private static Font font;

    /** The Background-Color. */
    private static Color color;

    /** The Button-Color. */
    private static Color color2;

    /** The current language of this Frame. */
    private static LanguageE language;

    /** The width of the Frame. */
    private static int width;

    /** The height of the Frame. */
    private static int height;

    /** The Input from the Gui. */
    private static String input;

    /** Elements of this Frame. */
    private static Picture picture;
    private static AppealText appealText;
    private static AppealText nextAppealText;
    private static IdTextField idTextField;
    private static ConfirmButton confirmButton;
    private static LanguageButton languageButton;
    private static SyntaxText syntaxText;
    private static WaitText waitText;
    private static ConfirmationText confirmText;
    private static WarningText warningText;
    private static RadioButtonPositive radioButtonPositive;
    private static RadioButtonNegative radioButtonNegative;

    /**
     * Constructor.
     */
    private MainFrame() {
        super();
        prepare();
        init();
    }

    /**
     * Starts and returns the MainFrame IF there is not already one.
     * @return a MainFrame instance.
     */
    public static MainFrame startTheMainFrame() {
        if (mainFrame == null) {
            return new MainFrame();
        }
        return mainFrame;
    }

    /**
     * Goes to the next step
     * @param value a String with length 8
     */
    public static void nextStep(String value) {
        input = value;
        currentPanel.remove(idTextField);
        currentPanel.remove(syntaxText);
        currentPanel.remove(appealText);

        nextAppealText = new AppealText(true);
        radioButtonPositive = new RadioButtonPositive();
        radioButtonNegative = new RadioButtonNegative();

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonNegative);
        buttonGroup.add(radioButtonPositive);

        currentPanel.add(nextAppealText);
        currentPanel.add(radioButtonNegative);
        currentPanel.add(radioButtonPositive);

        currentPanel.repaint();
    }

    /**
     * This Method starts the key transmission.
     */
    public static void outPutText() {
        transmit(input, radioButtonPositive.isSelected());
        currentPanel.remove(confirmButton);
        currentPanel.remove(languageButton);
        currentPanel.remove(nextAppealText);
        currentPanel.remove(radioButtonNegative);
        currentPanel.remove(radioButtonPositive);

        waitText = new WaitText();
        currentPanel.add(waitText);

        currentPanel.repaint();
    }

    public static void testPositive() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                    MainFrame.transmittingSuccessful();
                } catch (Exception e) {
                    return;
                }
            }
        }.start();
    }

    public static void testNegative() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                    MainFrame.transmittingNotSuccessful();
                } catch (Exception e) {
                    return;
                }
            }
        }.start();
    }

    /**
     * This Method puts the key from the gui-component to the network-component.
     * @param input a String with length 8
     * @param result true = infection, false = non infection
     */
    public static void transmit(String input, boolean result) {
        RetrofitController.sendId(input, result);
    }

    /**
     * This Method prepares the MainFrame.
     */
    private void prepare() {
        font = new Font ("Arial", Font.BOLD , 20);
        color = new Color(233, 246, 248);
        color2 = new Color(20, 40, 80);
        setTheSize();
        mainFrame = this;
        contentPane = this.getContentPane();
        currentPanel = new BasicPanel();
        Settings.init();
    }

    /**
     * Setter for the Size.
     */
    private void setTheSize() {
        XML_Translator xml_translator = new XML_Translator("xml/Settings.xml");
        width = Integer.valueOf(xml_translator.getString("width"));
        height = Integer.valueOf(xml_translator.getString("height"));
        setSize(width, height);
    }

    /**
     * This Method initializes the MainFrame.
     */
    private void init() {
        setTitle("CoW-Desktop-App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTheLayOut();
        addElements();
        contentPane.add(currentPanel);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Sets the Layout.
     */
    private void setTheLayOut() {
        currentPanel.setLayout(null);
    }

    /**
     * Add the Elements to the frame.
     */
    private void addElements() {
        picture = new Picture();
        appealText = new AppealText();
        idTextField = new IdTextField();
        confirmButton = new ConfirmButton();
        languageButton = new LanguageButton();
        syntaxText = new SyntaxText();

        currentPanel.add(picture);
        currentPanel.add(appealText);
        currentPanel.add(idTextField);
        currentPanel.add(confirmButton);
        currentPanel.add(languageButton);
        currentPanel.add(syntaxText);
    }

    /**
     * This Method should be called, when the Transmitting was successful.
     */
    public static void transmittingSuccessful() {
        currentPanel.remove(waitText);

        confirmText = new ConfirmationText();
        currentPanel.add(confirmText);

        currentPanel.repaint();
    }

    /**
     * This Method should be called, when the Transmitting is not successful.
     */
    public static void transmittingNotSuccessful() {
        currentPanel.remove(waitText);

        warningText = new WarningText();
        currentPanel.add(warningText);

        currentPanel.repaint();
    }

    /**
     * This Method should be called, when the confirm-button is pressed, but the input is wrong.
     */
    public static void wrongInput() {
        syntaxText.setVisible(true);
    }

    /**
     * Setter for the language.
     * @param language the language to set.
     */
    public static void setLanguage(LanguageE language) {
        MainFrame.language = language;
    }

    /**
     * Getter for the language.
     * @return the language.
     */
    public static LanguageE getLanguage() {
        return language;
    }

    /**
     * Getter for the color of the Background.
     * @return the color.
     */
    public static Color getColor() {
        return color;
    }

    /**
     * Getter for the color of the Buttons.
     * @return the color.
     */
    public static Color getColor2() {
        return color2;
    }

    /**
     * Getter for the font.
     * @return the font.
     */
    public static Font getTheFont() {
        return font;
    }

    /**
     * Getter for the height.
     * @return the height.
     */
    public static int getTheHeight() {
        return height;
    }

    /**
     * Getter for the width.
     * @return the Width.
     */
    public static int getTheWidth() {
        return width;
    }

    /**
     * Getter for the Input.
     * @return the Input of the textfield.
     */
    public static String getInputText() {
        return idTextField.getText();
    }

    public static void changeLanguage(LanguageE language) {
        Settings.changeLanguage(language);

        appealText.setText(Settings.getAppealString());
        confirmButton.setText(Settings.getConfirmString());
        languageButton.setText(Settings.getLanguageString());
        syntaxText.setText(Settings.getSyntaxString());

        if (nextAppealText != null) {
            nextAppealText.setText(Settings.getNextAppeal());
            radioButtonPositive.setText(Settings.getPositiveString());
            radioButtonNegative.setText(Settings.getNegativeString());
        }

        currentPanel.repaint();
    }
}
