package model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class ETL {
    ArrayList<ETLRow> etl;
    HashMap<String, String> contentMap;


    public ETL(String file) throws FileNotFoundException {
        this.contentMap = new HashMap<>();
        etl = new ArrayList<>();
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter("\r\n");
        while (scanner.hasNext()) {
            String line = scanner.next();
            this.etl.add(new ETLRow(line));
        }
/*       try(BufferedReader in = new BufferedReader(new FileReader(file))) {
            String str;
            while((str = in.readLine()) != null) {
                str = removeNul(str);
                if (!str.isEmpty()) {
                    this.etl.add(new ETLRow(str));
                }
            }
        }
        catch (IOException e) {
            System.out.println("File Read Error");
        }*/
    }

    private String removeNul(String s) {
        s = s.trim();
        s = s.replaceAll("\u0000","");
        s = s.replaceAll(",", ".");
        return s;
    }

    public ArrayList<ETLRow> getEtl() {
        return etl;
    }

    public void setEtl(ArrayList<ETLRow> etl) {
        this.etl = etl;
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
}
