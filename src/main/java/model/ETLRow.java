package model;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class ETLRow {
    private int no;
    private char etkz;
    private String partno;
    private String description;
    private String descriptionLine2;
    private String descriptionLine3;
    private double qty;
    private String changeno;
    private String chapter;
    private char itemType;

    public ETLRow(String str) {
        String[] tokens = str.split("\\|");
        if (tokens.length >5) {
            // Positionsnummer
            setNo(Integer.parseInt(tokens[1]));
            // Teilenummer(Material)
            setPartno(tokens[2]);
            // Beschreibung
            setDescription(tokens[3]);
            setDescriptionLine2(tokens[4]);
            setDescriptionLine3(tokens[5]);
            // Menge
            tokens[6] = tokens[6].replace(',','.');
            setQty(Double.valueOf(tokens[6]));
            // Kapitel
            setChapter(tokens[8]);
            // Positionstyp
            setItemType(tokens[9].charAt(1));
            // Ersatzteilkennzeichen
            if(tokens.length > 10 && tokens[10].length() >= 1) {
                setEtkz(tokens[10].charAt(0));
            }
            // Änderungsnummer
            if(tokens.length > 11) {
                setChangeno(tokens[11]);
            } else setChangeno("");
        }
    }

    public String getDescriptionLine2() {
        return descriptionLine2;
    }

    public void setDescriptionLine2(String descriptionLine2) {
        this.descriptionLine2 = descriptionLine2;
    }

    public String getDescriptionLine3() {
        return descriptionLine3;
    }

    public void setDescriptionLine3(String descriptionLine3) {
        this.descriptionLine3 = descriptionLine3;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public char getEtkz() {
        return etkz;
    }

    public void setEtkz(char etkz) {
        this.etkz = etkz;
    }

    public String getPartno() {
        return partno;
    }

    public void setPartno(String partno) {
        this.partno = partno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getChangeno() {
        return changeno;
    }

    public void setChangeno(String changeno) {
        this.changeno = changeno;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public void setItemType(char itemType){
        this.itemType = itemType;
    }

    public char getItemType() {
        return itemType;
    }
}
