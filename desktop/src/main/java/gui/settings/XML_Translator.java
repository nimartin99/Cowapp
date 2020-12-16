package gui.settings;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.util.List;

/**
 * This class helps to work with XML-Datafiles.
 *
 * The constructor need the xml-datafile-name. All methods then work with this datafile, until changed with setXmlDataFile(..);
 *
 * you can get Strings via getString(..)
 * you can get integer via getInt(..)
 * you can get doubles via getDouble(..)
 * you can get Elements via getElement(..)
 * you can get lists via getList(..)
 *
 * you can add new childes to root via addNewChildToRoot(..)
 * you can add new childes to Elements via addNewChild(..)
 * you can safe persistent safePersistent(..)
 *
 * @author Mergim Miftari
 * @version Jan 2020
 */
public class XML_Translator {
    /** The Path to the wished XML-Data. */
    private String xmlDataPath;

    /** The Document. */
    private Document document;

    /** The root of the XML-Data-Document. */
    private Element root;

    /**
     * Constructor initializes the xmlDataPath and takes the root out.
     *
     * @param xmlDataPath The Data-Path of the xml-Data (for example: "xml/settings.xml")
     */
    public XML_Translator(String xmlDataPath) {
        setXmlData(xmlDataPath);
    }

    /**
     * Constructor
     */
    public XML_Translator() {
        createNewXmlDocument();
    }

    /**
     * Initializes the xmlDataPath and takes the root out.
     *
     * @param xmlDataPath The Data-Path of the xml-Data (for example: "xml/playboy.xml")
     */
    public void setXmlData(String xmlDataPath) {
        try {
            document = new SAXBuilder().build(xmlDataPath);
            this.xmlDataPath = xmlDataPath;
            this.root = document.getRootElement();
        } catch (Exception e) {
            System.out.println("The XML-Data was not found or has no root.");
        }
    }

    /**
     * Gets the Child-text of the wished child from the root.
     *
     * @param child the child from the root.
     * @return the Data as String.
     */
    public String getString(String child) {
        return root.getChildText(child);
    }

    /**
     * Gets the Child-text of the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @return the Data as String.
     */
    public String getString(String child1, String child2) {
        return root.getChild(child1).getChildText(child2);
    }

    /**
     * Gets the Child-text of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @return the Data as String.
     */
    public String getString(String child1, String child2, String child3) {
        return root.getChild(child1).getChild(child2).getChildText(child3);
    }

    /**
     * Gets the Child-text of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @return the Data as String.
     */
    public String getString(String child1, String child2, String child3, String child4) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChildText(child4);
    }

    /**
     * Gets the Child-text of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param child5 the child from the child3.
     * @return the Data as String.
     */
    public String getString(String child1, String child2, String child3, String child4, String child5) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChildText(child5);
    }

    /**
     * Und so weiter...
     */
    public String getString(String child1, String child2, String child3, String child4, String child5, String child6) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChildText(child6);
    }

    /**
     * Und so weiter...
     */
    public String getString(String child1, String child2, String child3, String child4, String child5, String child6, String child7) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChildText(child7);
    }

    /**
     * Und so weiter...
     */
    public String getString(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChildText(child8);
    }

    /**
     * Und so weiter...
     */
    public String getString(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8).getChildText(child9);
    }

    /**
     * Und so weiter...
     */
    public String getString(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9, String child10) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8).getChild(child9).getChildText(child10);
    }

    /**
     * Gets the Child-text of the wished child from the root.
     *
     * @param child the child from the root.
     * @return the Data as int.
     */
    public int getInt(String child) {
        return Integer.parseInt(getString(child));
    }

    /**
     * Gets the Child-text of the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @return the Data as int.
     */
    public int getInt(String child1, String child2) {
        return Integer.parseInt(getString(child1, child2));
    }

    /**
     * Gets the Child-text of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @return the Data as int.
     */
    public int getInt(String child1, String child2, String child3) {
        return Integer.parseInt(getString(child1, child2, child3));
    }

    /**
     * Gets the Child-text of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @return the Data as int.
     */
    public int getInt(String child1, String child2, String child3, String child4) {
        return Integer.parseInt(getString(child1, child2, child3, child4));
    }

    /**
     * Gets the Child-text of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param child5 the child from the child3.
     * @return the Data as int.
     */
    public int getInt(String child1, String child2, String child3, String child4, String child5) {
        return Integer.parseInt(getString(child1, child2, child3, child4, child5));
    }

    /**
     * und so weiter...
     */
    public int getInt(String child1, String child2, String child3, String child4, String child5, String child6) {
        return Integer.parseInt(getString(child1, child2, child3, child4, child5, child6));
    }

    /**
     * und so weiter...
     */
    public int getInt(String child1, String child2, String child3, String child4, String child5, String child6, String child7) {
        return Integer.parseInt(getString(child1, child2, child3, child4, child5, child6, child7));
    }

    /**
     * und so weiter...
     */
    public int getInt(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8) {
        return Integer.parseInt(getString(child1, child2, child3, child4, child5, child6, child7, child8));
    }

    /**
     * und so weiter...
     */
    public int getInt(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9) {
        return Integer.parseInt(getString(child1, child2, child3, child4, child5, child6, child7, child8, child9));
    }

    /**
     * und so weiter...
     */
    public int getInt(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9, String child10) {
        return Integer.parseInt(getString(child1, child2, child3, child4, child5, child6, child7, child8, child9, child10));
    }

    /**
     * Gets the Child-text of the wished child from the root.
     *
     * @param child the child from the root.
     * @return the Data as double.
     */
    public double getdouble(String child) {
        return Double.parseDouble(getString(child));
    }

    /**
     * Gets the Child-text of the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @return the Data as double.
     */
    public double getdouble(String child1, String child2) {
        return Double.parseDouble(getString(child1, child2));
    }

    /**
     * Gets the Child-text of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @return the Data as double.
     */
    public double getdouble(String child1, String child2, String child3) {
        return Double.parseDouble(getString(child1, child2, child3));
    }

    /**
     * Gets the Child-text of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @return the Data as double.
     */
    public double getdouble(String child1, String child2, String child3, String child4) {
        return Double.parseDouble(getString(child1, child2, child3, child4));
    }

    /**
     * Gets the Child-text of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param child5 the child from the child3.
     * @return the Data as double.
     */
    public double getdouble(String child1, String child2, String child3, String child4, String child5) {
        return Double.parseDouble(getString(child1, child2, child3, child4, child5));
    }

    /**
     * und so weiter...
     */
    public double getdouble(String child1, String child2, String child3, String child4, String child5, String child6) {
        return Double.parseDouble(getString(child1, child2, child3, child4, child5, child6));
    }

    /**
     * und so weiter...
     */
    public double getdouble(String child1, String child2, String child3, String child4, String child5, String child6, String child7) {
        return Double.parseDouble(getString(child1, child2, child3, child4, child5, child6, child7));
    }

    /**
     * und so weiter...
     */
    public double getdouble(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8) {
        return Double.parseDouble(getString(child1, child2, child3, child4, child5, child6, child7, child8));
    }

    /**
     * und so weiter...
     */
    public double getdouble(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9) {
        return Double.parseDouble(getString(child1, child2, child3, child4, child5, child6, child7, child8, child9));
    }

    /**
     * und so weiter...
     */
    public double getdouble(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9, String child10) {
        return Double.parseDouble(getString(child1, child2, child3, child4, child5, child6, child7, child8, child9, child10));
    }

    /**
     * Gets the Child-text of the wished child from the element.
     *
     * @param child the child from the root.
     * @return the Data as String.
     */
    public String getString(Element element, String child) {
        return element.getChildText(child);
    }

    /**
     * Gets the Child-text of the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @return the Data as String.
     */
    public String getString(Element element, String child1, String child2) {
        return element.getChild(child1).getChildText(child2);
    }

    /**
     * Gets the Child-text of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @return the Data as String.
     */
    public String getString(Element element, String child1, String child2, String child3) {
        return element.getChild(child1).getChild(child2).getChildText(child3);
    }

    /**
     * Gets the Child-text of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @return the Data as String.
     */
    public String getString(Element element, String child1, String child2, String child3, String child4) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChildText(child4);
    }

    /**
     * Gets the Child-text of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param child5 the child from the child3.
     * @return the Data as String.
     */
    public String getString(Element element, String child1, String child2, String child3, String child4, String child5) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChildText(child5);
    }

    /**
     * Und so weiter...
     */
    public String getString(Element element, String child1, String child2, String child3, String child4, String child5, String child6) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChildText(child6);
    }

    /**
     * Und so weiter...
     */
    public String getString(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChildText(child7);
    }

    /**
     * Und so weiter...
     */
    public String getString(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChildText(child8);
    }

    /**
     * Und so weiter...
     */
    public String getString(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8).getChildText(child9);
    }

    /**
     * Und so weiter...
     */
    public String getString(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9, String child10) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8).getChild(child9).getChildText(child10);
    }

    /**
     * Gets the Child-text of the wished child from the element.
     *
     * @param child the child from the root.
     * @return the Data as int.
     */
    public int getInt(Element element, String child) {
        return Integer.parseInt(getString(element, child));
    }

    /**
     * Gets the Child-text of the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @return the Data as int.
     */
    public int getInt(Element element, String child1, String child2) {
        return Integer.parseInt(getString(element, child1, child2));
    }

    /**
     * Gets the Child-text of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @return the Data as int.
     */
    public int getInt(Element element, String child1, String child2, String child3) {
        return Integer.parseInt(getString(element, child1, child2, child3));
    }

    /**
     * Gets the Child-text of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @return the Data as int.
     */
    public int getInt(Element element, String child1, String child2, String child3, String child4) {
        return Integer.parseInt(getString(element, child1, child2, child3, child4));
    }

    /**
     * Gets the Child-text of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from theelement.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param child5 the child from the child3.
     * @return the Data as int.
     */
    public int getInt(Element element, String child1, String child2, String child3, String child4, String child5) {
        return Integer.parseInt(getString(element, child1, child2, child3, child4, child5));
    }

    /**
     * und so weiter...
     */
    public int getInt(Element element, String child1, String child2, String child3, String child4, String child5, String child6) {
        return Integer.parseInt(getString(element, child1, child2, child3, child4, child5, child6));
    }

    /**
     * und so weiter...
     */
    public int getInt(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7) {
        return Integer.parseInt(getString(element, child1, child2, child3, child4, child5, child6, child7));
    }

    /**
     * und so weiter...
     */
    public int getInt(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8) {
        return Integer.parseInt(getString(element, child1, child2, child3, child4, child5, child6, child7, child8));
    }

    /**
     * und so weiter...
     */
    public int getInt(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9) {
        return Integer.parseInt(getString(element, child1, child2, child3, child4, child5, child6, child7, child8, child9));
    }

    /**
     * und so weiter...
     */
    public int getInt(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9, String child10) {
        return Integer.parseInt(getString(element, child1, child2, child3, child4, child5, child6, child7, child8, child9, child10));
    }

    /**
     * Gets the Child-text of the wished child from the element.
     *
     * @param child the child from the root.
     * @return the Data as double.
     */
    public double getdouble(Element element, String child) {
        return Double.parseDouble(getString(element, child));
    }

    /**
     * Gets the Child-text of the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @return the Data as double.
     */
    public double getdouble(Element element, String child1, String child2) {
        return Double.parseDouble(getString(element, child1, child2));
    }

    /**
     * Gets the Child-text of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @return the Data as double.
     */
    public double getdouble(Element element, String child1, String child2, String child3) {
        return Double.parseDouble(getString(element, child1, child2, child3));
    }

    /**
     * Gets the Child-text of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @return the Data as double.
     */
    public double getdouble(Element element, String child1, String child2, String child3, String child4) {
        return Double.parseDouble(getString(element, child1, child2, child3, child4));
    }

    /**
     * Gets the Child-text of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param child5 the child from the child3.
     * @return the Data as double.
     */
    public double getdouble(Element element, String child1, String child2, String child3, String child4, String child5) {
        return Double.parseDouble(getString(element, child1, child2, child3, child4, child5));
    }

    /**
     * und so weiter...
     */
    public double getdouble(Element element, String child1, String child2, String child3, String child4, String child5, String child6) {
        return Double.parseDouble(getString(element, child1, child2, child3, child4, child5, child6));
    }

    /**
     * und so weiter...
     */
    public double getdouble(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7) {
        return Double.parseDouble(getString(element, child1, child2, child3, child4, child5, child6, child7));
    }

    /**
     * und so weiter...
     */
    public double getdouble(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8) {
        return Double.parseDouble(getString(element, child1, child2, child3, child4, child5, child6, child7, child8));
    }

    /**
     * und so weiter...
     */
    public double getdouble(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9) {
        return Double.parseDouble(getString(element, child1, child2, child3, child4, child5, child6, child7, child8, child9));
    }

    /**
     * und so weiter...
     */
    public double getdouble(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9, String child10) {
        return Double.parseDouble(getString(element, child1, child2, child3, child4, child5, child6, child7, child8, child9, child10));
    }

    /**
     * Gets the Element of the wished child from the root.
     *
     * @return the Element.
     */
    public Element getElement() {
        return root;
    }

    /**
     * Gets the Element of the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @return the Element.
     */
    public Element getElement(String child1, String child2) {
        return root.getChild(child1).getChild(child2);
    }

    /**
     * Gets the Element of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @return the Element.
     */
    public Element getElement(String child1, String child2, String child3) {
        return root.getChild(child1).getChild(child2).getChild(child3);
    }

    /**
     * Gets the Element of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @return the Element.
     */
    public Element getElement(String child1, String child2, String child3, String child4) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4);
    }

    /**
     * Gets the Element of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param child5 the child from the child3.
     * @return the Element.
     */
    public Element getElement(String child1, String child2, String child3, String child4, String child5) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5);
    }

    /**
     * Und so weiter...
     */
    public Element getElement(String child1, String child2, String child3, String child4, String child5, String child6) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6);
    }

    /**
     * Und so weiter...
     */
    public Element getElement(String child1, String child2, String child3, String child4, String child5, String child6, String child7) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7);
    }

    /**
     * Und so weiter...
     */
    public Element getElement(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8);
    }

    /**
     * Und so weiter...
     */
    public Element getElement(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8).getChild(child9);
    }

    /**
     * Und so weiter...
     */
    public Element getElement(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9, String child10) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8).getChild(child9).getChild(child10);
    }

    /**
     * Gets the Element-List of the wished child from the root.
     *
     * @param childs the children titel.
     * @return the List of Elements.
     */
    public List<Element> getList(String childs) {
        return root.getChildren(childs);
    }

    /**
     * Gets the Element-List of the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param childs the children titel.
     * @return the List of Elements.
     */
    public List<Element> getList(String child1, String childs) {
        return root.getChild(child1).getChildren(childs);
    }

    /**
     * Gets the Element-List of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param childs the children titel.
     * @return the List of Elements.
     */
    public List<Element> getList(String child1, String child2, String childs) {
        return root.getChild(child1).getChild(child2).getChildren(childs);
    }

    /**
     * Gets the Element-List of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param childs the children titel.
     * @return the List of Elements.
     */
    public List<Element> getList(String child1, String child2, String child3, String childs) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChildren(childs);
    }

    /**
     * Gets the Element-List of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param childs the children titel.
     * @return the List of Elements.
     */
    public List<Element> getList(String child1, String child2, String child3, String child4, String childs) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChildren(childs);
    }

    /**
     * Und so weiter...
     */
    public List<Element> getList(String child1, String child2, String child3, String child4, String child5, String childs) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChildren(childs);
    }

    /**
     * Und so weiter...
     */
    public List<Element> getList(String child1, String child2, String child3, String child4, String child5, String child6, String childs) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChildren(childs);
    }

    /**
     * Und so weiter...
     */
    public List<Element> getList(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String childs) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChildren(childs);
    }

    /**
     * Und so weiter...
     */
    public List<Element> getList(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String childs) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8).getChildren(childs);
    }

    /**
     * Und so weiter...
     */
    public List<Element> getList(String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9, String childs) {
        return root.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8).getChild(child9).getChildren(childs);
    }

    /**
     * Gets the Child-text of the wished child from the element.
     *
     * @param child the child from the root.
     * @return the Data as boolean.
     */
    public boolean getBoolean(Element element, String child) {
        return Boolean.parseBoolean(getString(element, child));
    }

    /**
     * Gets the Child-text of the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @return the Data as boolean.
     */
    public boolean getBoolean(Element element, String child1, String child2) {
        return Boolean.parseBoolean(getString(element, child1, child2));
    }

    /**
     * Gets the Child-text of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @return the Data as boolean.
     */
    public boolean getBoolean(Element element, String child1, String child2, String child3) {
        return Boolean.parseBoolean(getString(element, child1, child2, child3));
    }

    /**
     * Gets the Child-text of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @return the Data as boolean.
     */
    public boolean getBoolean(Element element, String child1, String child2, String child3, String child4) {
        return Boolean.parseBoolean(getString(element, child1, child2, child3, child4));
    }

    /**
     * Gets the Child-text of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from theelement.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param child5 the child from the child3.
     * @return the Data as boolean.
     */
    public boolean getBoolean(Element element, String child1, String child2, String child3, String child4, String child5) {
        return Boolean.parseBoolean(getString(element, child1, child2, child3, child4, child5));
    }

    /**
     * und so weiter...
     */
    public boolean getBoolean(Element element, String child1, String child2, String child3, String child4, String child5, String child6) {
        return Boolean.parseBoolean(getString(element, child1, child2, child3, child4, child5, child6));
    }

    /**
     * und so weiter...
     */
    public boolean getBoolean(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7) {
        return Boolean.parseBoolean(getString(element, child1, child2, child3, child4, child5, child6, child7));
    }

    /**
     * und so weiter...
     */
    public boolean getBoolean(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8) {
        return Boolean.parseBoolean(getString(element, child1, child2, child3, child4, child5, child6, child7, child8));
    }

    /**
     * und so weiter...
     */
    public boolean getBoolean(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9) {
        return Boolean.parseBoolean(getString(element, child1, child2, child3, child4, child5, child6, child7, child8, child9));
    }

    /**
     * und so weiter...
     */
    public boolean getBoolean(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9, String child10) {
        return Boolean.parseBoolean(getString(element, child1, child2, child3, child4, child5, child6, child7, child8, child9, child10));
    }

    /**
     * Gets the Child-text of the wished child from the element.
     *
     * @param child the child from the root.
     * @return the Data as boolean.
     */
    public boolean getBoolean(String child) {
        return Boolean.parseBoolean(getString(child));
    }

    /**
     * Gets the Child-text of the wished child from the element.
     *
     * @param child the child from the root.
     * @return the Data as boolean.
     */
    public boolean getBoolean(String child0, String child) {
        return Boolean.parseBoolean(getString(child0, child));
    }

    /**
     * Gets the Child-text of the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @return the Data as boolean.
     */
    public boolean getBoolean(String child0, String child1, String child2) {
        return Boolean.parseBoolean(getString(child0, child1, child2));
    }

    /**
     * Gets the Child-text of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @return the Data as boolean.
     */
    public boolean getBoolean(String child0, String child1, String child2, String child3) {
        return Boolean.parseBoolean(getString(child0, child1, child2, child3));
    }

    /**
     * Gets the Child-text of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the element.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @return the Data as boolean.
     */
    public boolean getBoolean(String child0, String child1, String child2, String child3, String child4) {
        return Boolean.parseBoolean(getString(child0, child1, child2, child3, child4));
    }

    /**
     * Gets the Child-text of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from theelement.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param child5 the child from the child3.
     * @return the Data as boolean.
     */
    public boolean getBoolean(String child0, String child1, String child2, String child3, String child4, String child5) {
        return Boolean.parseBoolean(getString(child0, child1, child2, child3, child4, child5));
    }

    /**
     * und so weiter...
     */
    public boolean getBoolean(String child0, String child1, String child2, String child3, String child4, String child5, String child6) {
        return Boolean.parseBoolean(getString(child0, child1, child2, child3, child4, child5, child6));
    }

    /**
     * und so weiter...
     */
    public boolean getBoolean(String child0, String child1, String child2, String child3, String child4, String child5, String child6, String child7) {
        return Boolean.parseBoolean(getString(child0, child1, child2, child3, child4, child5, child6, child7));
    }

    /**
     * und so weiter...
     */
    public boolean getBoolean(String child0, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8) {
        return Boolean.parseBoolean(getString(child0, child1, child2, child3, child4, child5, child6, child7, child8));
    }

    /**
     * und so weiter...
     */
    public boolean getBoolean(String child0, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9) {
        return Boolean.parseBoolean(getString(child0, child1, child2, child3, child4, child5, child6, child7, child8, child9));
    }

    /**
     * Gets the Element-List of the wished child from the root.
     *
     * @param childs the children titel.
     * @return the List of Elements.
     */
    public List<Element> getList(Element element, String childs) {
        return element.getChildren(childs);
    }

    /**
     * Gets the Element-List of the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param childs the children titel.
     * @return the List of Elements.
     */
    public List<Element> getList(Element element, String child1, String childs) {
        return element.getChild(child1).getChildren(childs);
    }

    /**
     * Gets the Element-List of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param childs the children titel.
     * @return the List of Elements.
     */
    public List<Element> getList(Element element, String child1, String child2, String childs) {
        return element.getChild(child1).getChild(child2).getChildren(childs);
    }

    /**
     * Gets the Element-List of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param childs the children titel.
     * @return the List of Elements.
     */
    public List<Element> getList(Element element, String child1, String child2, String child3, String childs) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChildren(childs);
    }

    /**
     * Gets the Element-List of the wished child5 of the wished child4 of the wished child3 from the wished child2 from the wished child1 from the root.
     *
     * @param child1 the child from the root.
     * @param child2 the child from the child1.
     * @param child3 the child from the child2.
     * @param child4 the child from the child3.
     * @param childs the children titel.
     * @return the List of Elements.
     */
    public List<Element> getList(Element element, String child1, String child2, String child3, String child4, String childs) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChildren(childs);
    }

    /**
     * Und so weiter...
     */
    public List<Element> getList(Element element, String child1, String child2, String child3, String child4, String child5, String childs) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChildren(childs);
    }

    /**
     * Und so weiter...
     */
    public List<Element> getList(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String childs) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChildren(childs);
    }

    /**
     * Und so weiter...
     */
    public List<Element> getList(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String childs) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChildren(childs);
    }

    /**
     * Und so weiter...
     */
    public List<Element> getList(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String childs) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8).getChildren(childs);
    }

    /**
     * Und so weiter...
     */
    public List<Element> getList(Element element, String child1, String child2, String child3, String child4, String child5, String child6, String child7, String child8, String child9, String childs) {
        return element.getChild(child1).getChild(child2).getChild(child3).getChild(child4).getChild(child5).getChild(child6).getChild(child7).getChild(child8).getChild(child9).getChildren(childs);
    }

    /**
     * Safes the current Document persistent at the path.
     * @param path
     */
    public void safePersistant(String path) {
        try {
            // new XMLOutputter().output(doc, System.out);
            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, new FileWriter(path));
        } catch (Exception e) {
            System.out.println("Speichern der XML-Datei fehlgeschlagen.");
        }
    }

    public void createNewXmlDocument() {
        root = new Element("root");
        document = new Document();
        document.setRootElement(root);
    }

    /**
     * Adds a new empty Child-Element to the Element
     * @param element
     * @param child
     * @return
     */
    public Element addNewChild(Element element, String child) {
        Element element1 = new Element(child);
        element.addContent(element1);
        return element1;
    }

    /**
     * Adds a new empty Child-Element to the Element
     * @param element
     * @param child
     * @return
     */
    public Element addNewChild(Element element, String child, String text) {
        Element element1 = new Element(child).setText(text);
        element.addContent(element1);
        return element1;
    }

    /**
     * Adds a new empty Child-Element to the Root
     * @param child
     * @return
     */
    public Element addNewChildToRoot(String child) {
        Element element1 = new Element(child);
        root.addContent(element1);
        return element1;
    }

    /**
     * Adds a new empty Child-Element to the Root
     * @param child
     * @return
     */
    public Element addNewChildToRoot(String child, String text) {
        Element element1 = new Element(child).setText(text);
        root.addContent(element1);
        return element1;
    }
}
