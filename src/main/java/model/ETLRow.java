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
    private double qty;
    private String changeno;
    private String chapter;
    private char itemType;

    ETLRow(String str) {
        String[] tokens = str.split("\\|");
        if (tokens.length >5) {
            // Positionsnummer
            setNo(Integer.parseInt(tokens[0]));
            // Teilenummer(Material)
            setPartno(tokens[1]);
            // Beschreibung
            setDescription(tokens[2]);
            setDescriptionLine2(tokens[3]);
            setDescriptionLine2(getDescriptionLine2().replace("U+0085", "\r"));
            // Menge
            tokens[4] = tokens[4].replace(',','.');
            setQty(Double.valueOf(tokens[4]));
            // Kapitel
            setChapter(tokens[6]);
            // Positionstyp
            setItemType(tokens[7].charAt(0));
            // Ersatzteilkennzeichen
            if(tokens.length > 8 && tokens[8].length() >= 1) {
                setEtkz(tokens[8].charAt(0));
            }
            // Änderungsnummer
            if(tokens.length > 9) {
                setChangeno(tokens[9]);
            } else setChangeno("");
        }
    }

    public String getDescriptionLine2() {
        return descriptionLine2;
    }

    private void setDescriptionLine2(String descriptionLine2) {
        this.descriptionLine2 = descriptionLine2;
    }

    public int getNo() {
        return no;
    }

    private void setNo(int no) {
        this.no = no;
    }

    public char getEtkz() {
        return etkz;
    }

    private void setEtkz(char etkz) {
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

    private void setDescription(String description) {
        this.description = description;
    }

    public double getQty() {
        return qty;
    }

    private void setQty(Double qty) {
        this.qty = qty;
    }

    public String getChangeno() {
        return changeno;
    }

    private void setChangeno(String changeno) {
        this.changeno = changeno;
    }

    public String getChapter() {
        return chapter;
    }

    private void setChapter(String chapter) {
        this.chapter = chapter;
    }

    private void setItemType(char itemType){
        this.itemType = itemType;
    }

    public char getItemType() {
        return itemType;
    }
}
