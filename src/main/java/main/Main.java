package main;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Main {
    public static void main() throws IOException {
        var handlers = new HashMap<String, HttpHandler>();
        handlers.put("/", new Index());
        handlers.put("/save", new Save());
        handlers.put("/insertNote", new insertNote());
        handlers.put("/deleteNote", new deleteNote());

        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 8000), 0);
        httpServer.setExecutor(null);
        handlers.forEach(httpServer::createContext);
        httpServer.start();
    }
}