package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.Utils;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;

public class deleteNote implements HttpHandler {

    public void handle(HttpExchange h) throws IOException {
        try {
            var request = Utils.getPostMap(h);
            sqlDeleteNote(request.get("id"));

            Utils.returnSuccess(h, "");
        } catch (Exception e) {
            Utils.returnFail(h, e.getMessage());
        }
    }

    private void sqlDeleteNote(String id) {
        Jdbi jdbi = Jdbi.create(Utils.DB_URL);

        String sql = """
                delete from notes
                where
                id=:id
                """;

        jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                .bind("id", id)
                .execute());
    }
}
