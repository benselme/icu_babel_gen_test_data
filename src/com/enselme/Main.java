package com.enselme;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import org.json.simple.JSONObject;
import com.ibm.icu.text.NumberingSystem;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

public class Main {

    static private final String BIG_DEC = "bigdec";
    static private final String DOUBLE = "double";
    static private final String INT = "int";

    public static void main(String[] args) throws IOException{
        JSONObject test_data = new JSONObject();
        test_data.put("NumberFormat", generateNumberFormatData());
        try (FileWriter file = new FileWriter(args[0])) {
            file.write(test_data.toJSONString());
        }
    }

    private static JSONObject generateNumberFormatData() {
        ULocale[] locales = ULocale.getAvailableLocales();
        int[] styles = new int[]{NumberFormat.SCIENTIFICSTYLE, NumberFormat.CURRENCYSTYLE, NumberFormat.PERCENTSTYLE,
                NumberFormat.NUMBERSTYLE};
        System.out.println(locales.length);
        BigDecimal bd = new BigDecimal("1234.45678");
        double db = 7432653.4321d;
        int in = 1000000;
        HashMap<String, Object> values = new HashMap<>();
        values.put(BIG_DEC, bd);
        values.put(DOUBLE, db);
        values.put(INT, in);
        JSONObject test_data = new JSONObject();
        JSONObject locales_data = new JSONObject();
        test_data.put("values", new JSONObject(values));
        test_data.put("locales", locales_data);
        for (ULocale loc : locales) {
            NumberingSystem numberingSystem = NumberingSystem.getInstance(loc);
            String ns_name = numberingSystem.getName();
            if (numberingSystem.getName().equals("latn")) {
                JSONObject locale_data = new JSONObject();
                locales_data.put(loc.getName(), locale_data);
                for (int style : styles) {
                    NumberFormat nf = NumberFormat.getInstance(loc, style);
                    JSONObject style_data = new JSONObject();
                    style_data.put(BIG_DEC, nf.format(bd));
                    style_data.put(DOUBLE, nf.format(db));
                    style_data.put(INT, nf.format(in));
                    locale_data.put(style, style_data);
                }
            }
        }
        return test_data;

    }
}
