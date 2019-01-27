package model;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class ETL {
    private String etlNo;
    private ArrayList<ETLRow> etl;
    private HashMap<String, String> contentMap;

    public ETL(String file){
        int i = file.lastIndexOf("\\");
        etlNo = file.substring(i+1,i+8).toUpperCase();
        try {
            this.contentMap = new HashMap<>();
            etl = new ArrayList<>();
            Scanner scanner = new Scanner(new File(file));
            scanner.useDelimiter("\r\n");
            while (scanner.hasNext()) {
                String line = scanner.next();
                line = removeNul(line);
                this.etl.add(new ETLRow(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String removeNul(String s) {
        s = s.trim();
        s = s.replaceAll("\uFEFF","");
        s = s.replaceAll("\u0000","");
        s = s.replaceAll(",", ".");
        return s;
    }

    public String getEtlNo() {
        return etlNo;
    }

    public ArrayList<ETLRow> getEtl() {
        return etl;
    }

    public ArrayList<ETLRow> getItems(int index){
        ArrayList<ETLRow> list = new ArrayList<>();
        for (ETLRow row : etl){
            if (row.getNo()== index) list.add(row);
        }
        return list;
    }

    public HashMap getContentMap() {
        return contentMap;
    }

    public ArrayList<ETLRow> getSortedChangeNoList() {
        ArrayList<ETLRow> changeNoList = new ArrayList<>();
        for(ETLRow e : etl) {
            if (!e.getChangeno().isEmpty()) {
                changeNoList.add(e);
            }
        }
        changeNoList.sort((o1, o2) -> {

            try {
                String year1, year2;
                String changeno1 = o1.getChangeno();
                String changeno2 = o2.getChangeno();
                if(Integer.valueOf(changeno1.substring(1,3)) > 70){
                    year1 = "19";
                } else year1 = "20";
                if(Integer.valueOf(changeno2.substring(1,3)) > 70){
                    year2 = "19";
                } else year2 = "20";
                String date1 = changeno1.substring(5, 7) + "." + changeno1.substring(3, 5) + "." + year1 + changeno1.substring(1, 3);
                String date2 = changeno2.substring(5, 7) + "." + changeno2.substring(3, 5) + "." + year2 + changeno2.substring(1, 3);

                Date d1 = new SimpleDateFormat("dd.MM.yyyy").parse(date1);
                Date d2 = new SimpleDateFormat("dd.MM.yyyy").parse(date2);
                return d2.compareTo(d1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
        return changeNoList;
    }
}
