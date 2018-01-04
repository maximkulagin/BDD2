package model;


import javax.faces.application.FacesMessage;
import javax.faces.convert.ConverterException;
import java.util.ArrayList;

import java.util.Deque;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TruthTable {

    private ArrayList<Boolean[]> rows;
    private Deque<String> args;
    private String st_func;

    public TruthTable() {
        args = new LinkedList<>();
        rows = new ArrayList<>();
    }

    public String getSt_func() {
        return st_func;
    }

    public void setSt_func(String st_func) {
        this.st_func = st_func;
    }

    public Deque<String> getArgs() {
        return args;
    }

    public void setArgs(Deque<String> args) {
        this.args = args;
    }

    public ArrayList<Boolean[]> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Boolean[]> rows) {
        this.rows = rows;
    }


    public int fillUp() {

        char[] str = st_func.toCharArray();
        int n = BinParser.countX(str);

        args.clear();
        for (int i = 1; i <= n; i++) {
            args.add("x" + i + " ");
        }
        args.add("F()");
        rows.clear();

        BinFunc binFunc = BinParser.parse(str);
        String bin_st;
        for (int i = 0; i < Math.pow(2, n); i++) {
            Boolean[] row = new Boolean[n + 1];
            bin_st = Integer.toBinaryString(i);
            if (bin_st.length() < n) {
                int count0 = n - bin_st.length();
                for (int j = 0; j < count0; j++) {
                    bin_st = "0" + bin_st;
                }
            }
            for (int j = 0; j < n; j++) {
                row[j] = bin_st.charAt(j) == '1' ? true : false;
            }

            try {

                row = binFunc.execute(row);
            } catch (PillowException e) {
                Logger.getAnonymousLogger().log(Level.WARNING,  e.getMessage());
                rows.clear();
                return 0;
            }

            rows.add(row);
        }
        return n;
    }

}
