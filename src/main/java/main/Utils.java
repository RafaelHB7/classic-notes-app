package main;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Utils {
    public static final String WEB_PATH = "./src/main/resources/html/";
    public static final String TEMPLATE_PATH = "./src/main/resources/handlebars/";
    public static final String DB_URL = "jdbc:sqlite:data.db";
    public static String readFile(String name) throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        File file = new File(STR."\{WEB_PATH}\{name}.html");
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            content.append(reader.nextLine());
        }
        reader.close();
        return content.toString();
    }

    public static void returnFail(HttpExchange h, String data) throws IOException {
        returnRequest(h, data, 500);
    }
    public static void returnSuccess(HttpExchange h, String data) throws IOException {
        returnRequest(h, data, 200);
    }

    private static void returnRequest(HttpExchange h, String data, int responseCode) throws IOException {
        OutputStream outputStream = h.getResponseBody();
        h.sendResponseHeaders(responseCode, data.getBytes().length);
        outputStream.write(data.getBytes());
        outputStream.close();
    }

    public static HashMap<String, String> getGetMap(HttpExchange h) {
        return mapInput(h.getRequestURI().getQuery());
    }

    public static HashMap<String, String> getPostMap(HttpExchange h) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream ioStream = h.getRequestBody();
        int chr;
        while ((chr = ioStream.read()) != -1) {
            stringBuilder.append((char) chr);
        }
        return mapInput(stringBuilder.toString());
    }

    private static HashMap<String, String> mapInput(String query) {
        if (!query.contains("?")) {
            int index = query.indexOf("=");
            HashMap<String, String> map = new HashMap<>(1);
            map.put(query.substring(0, index), query.substring(index+1));
            return map;
        }
        HashMap<String, String> map = new HashMap<>();
        Arrays.stream(query.split("\\?")).forEach(param -> {
            int index = param.indexOf("=");
            map.put(param.substring(0, index), param.substring(index+1));
        });
        return map;
    }

    public static Handlebars handleBars() {
        FileTemplateLoader loader = new FileTemplateLoader(Utils.TEMPLATE_PATH);
        return new Handlebars(loader);
    }
}
