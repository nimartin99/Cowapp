package gui.settings;

import gui.frame.MainFrame;
import org.jdom2.Element;

/**
 * This class has Getter for the different values.
 *
 * @author Mergim Miftari
 * @version Dec 2020
 */
public class Settings {
    /** The XML_Translator. */
    private static XML_Translator xml_translator;

    /** The current Language. */
    private static LanguageE currentLanguage;

    /**
     * Initializes the Settings.
     */
    public static void init() {
        xml_translator = new XML_Translator("xml/Settings.xml");
        currentLanguage = LanguageE.valueOf(xml_translator.getString("language"));
        MainFrame.setLanguage(currentLanguage);

        if (MainFrame.getLanguage() == LanguageE.English) {
            xml_translator.setXmlData("xml/StringsEnglish.xml");
        } else if (MainFrame.getLanguage() == LanguageE.German) {
            xml_translator.setXmlData("xml/StringsGerman.xml");
        }
    }

    /**
     * Changes the language.
     * @param language the new language.
     */
    public static void changeLanguage(LanguageE language) {
        currentLanguage = language;

        safeNewLanguage(language);

        if (language == LanguageE.English) {
            xml_translator.setXmlData("xml/StringsEnglish.xml");
        } else if (language == LanguageE.German) {
            xml_translator.setXmlData("xml/StringsGerman.xml");
        }
    }

    /**
     * Safes the new Language
     * @param language the new language.
     */
    private static void safeNewLanguage(LanguageE language) {
        XML_Translator safer = new XML_Translator("xml/Settings.xml");
        Element e = safer.getElement();
        e.removeChild("language");

        if (language == LanguageE.English) {
            Element i = safer.addNewChild(e, "language", "English");
        } else if (language == LanguageE.German) {
            Element i = safer.addNewChild(e, "language", "German");
        }

        safer.safePersistant("xml/Settings.xml");
    }

    public static String getAppealString() {
        return xml_translator.getString("appeal");
    }

    public static String getConnectionString() {
        return xml_translator.getString("connection");
    }

    public static String getSuccessfulString() {
        return xml_translator.getString("succesful");
    }

    public static String getConfirmString() {
        return xml_translator.getString("confirm");
    }

    public static String getLanguageString() {
        return xml_translator.getString("language");
    }

    public static String getSyntaxString() {
        return xml_translator.getString("syntax");
    }

    public static String getWaitString() {
        return xml_translator.getString("wait");
    }

    public static String getNextAppeal() {
        return xml_translator.getString("choose");
    }

    public static String getPositiveString() {
        return xml_translator.getString("positive");
    }

    public static String getNegativeString() {
        return xml_translator.getString("negative");
    }

    /**
     * Getter for the current language.
     * @return
     */
    public static LanguageE getCurrentLanguage() {
        return currentLanguage;
    }
}
