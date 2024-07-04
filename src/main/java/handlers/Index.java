package handlers;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.Note;
import main.Utils;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Index implements HttpHandler {

    public void handle(HttpExchange h) throws IOException {
        try {
            Handlebars handlebars = Utils.handleBars();
            Template template = handlebars.compile("index");
            String content = template.apply(selectNotes());

            Utils.returnSuccess(h, content);
        } catch (Exception e) {
            Utils.returnFail(h, Arrays.toString(e.getStackTrace()));
        }
    }

    private List<Note> selectNotes() {
        Jdbi jdbi = Jdbi.create(Utils.DB_URL);

        String sql = """
                select id
                , note
                from notes
                order by
                id
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                .mapToBean(Note.class)
                .list());
    }
}
