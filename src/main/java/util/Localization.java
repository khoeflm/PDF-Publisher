package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * SemNOTAM Project (User Interface)
 * Created by khoef on 27.01.2019.
 */
public class Localization {

    private String inhalt;
    private String revision;
    private String copyright;
    private String update;
    private String page;
    private String chapter;
    private String date;
    private String change;
    private String num;
    private String part;
    private String descr;
    private String qty;
    private String neu;

    public Localization(String lang) throws IOException {
        Locale.setDefault(new Locale(lang));
  //      ResourceBundle langBundle = ResourceBundle.getBundle("internationalization/MessagesBundle");

        //to load application's properties, we use this class
        Properties mainProperties = new Properties();
        FileInputStream file;

        //the base folder is ./, the root of the main.properties file
        String filename = "/internationalization/MessagesBundle_"+lang+".properties";
        String path = "./"+filename;

        //load the file handle for main.properties
        file = new FileInputStream(path);
        mainProperties.load(file);
        file.close();

        inhalt = mainProperties.getProperty("inhalt");
        copyright = mainProperties.getProperty("copyright");
        update = mainProperties.getProperty("update");
        page = mainProperties.getProperty("page");
        chapter = mainProperties.getProperty("chapter");
        date = mainProperties.getProperty("date");
        change = mainProperties.getProperty("change");
        num = mainProperties.getProperty("num");
        part = mainProperties.getProperty("part");
        descr = mainProperties.getProperty("descr");
        qty = mainProperties.getProperty("qty");
        neu = mainProperties.getProperty("neu");
        revision = mainProperties.getProperty("revision");
    }

    public String getInhalt() {
        return inhalt;
    }

    public String getRevision() {
        return revision;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getUpdate() {
        return update;
    }

    public String getPage() {
        return page;
    }

    public String getChapter() {
        return chapter;
    }

    public String getDate() {
        return date;
    }

    public String getChange() {
        return change;
    }

    public String getNum() {
        return num;
    }

    public String getPart() {
        return part;
    }

    public String getDescr() {
        return descr;
    }

    public String getQty() {
        return qty;
    }

    public String getNeu() {
        return neu;
    }
}


