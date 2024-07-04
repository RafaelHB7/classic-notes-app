package handlers;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.Note;
import main.Utils;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;

public class insertNote implements HttpHandler {

    public void handle(HttpExchange h) throws IOException {
        try {
            int noteId = sqlInsertNote();

            Handlebars handlebars = Utils.handleBars();
            Template template = handlebars.compile("note");
            String content = template.apply(new Note(noteId, ""));

            Utils.returnSuccess(h, content);
        } catch (Exception e) {
            Utils.returnFail(h, e.getMessage());
        }
    }

    private int sqlInsertNote() {
        Jdbi jdbi = Jdbi.create(Utils.DB_URL);

        String sql = """
                insert into notes
                (note)
                values
                ("")
                """;

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Integer.class)
                .one());
    }
}
