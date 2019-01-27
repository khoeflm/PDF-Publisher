package util;

import java.util.Locale;
import java.util.ResourceBundle;

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

    public Localization(String lang){
        Locale.setDefault(new Locale(lang));
        ResourceBundle langBundle = ResourceBundle.getBundle("MessagesBundle");

        inhalt = langBundle.getString("inhalt");
        revision = langBundle.getString("revision");
        copyright = langBundle.getString("copyright");
        update = langBundle.getString("update");
        page = langBundle.getString("page");
        chapter = langBundle.getString("chapter");
        date = langBundle.getString("date");
        change = langBundle.getString("change");
        num = langBundle.getString("num");
        part = langBundle.getString("part");
        descr = langBundle.getString("descr");
        qty = langBundle.getString("qty");
        neu = langBundle.getString("neu");
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


