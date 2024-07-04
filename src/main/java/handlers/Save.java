package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.Utils;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class Save implements HttpHandler {

    public void handle(HttpExchange h) throws IOException {
        try {
            var request = Utils.getPostMap(h);

            request.forEach(this::saveInput);
            Utils.returnSuccess(h, "");
        } catch (Exception e) {
            Utils.returnFail(h, Arrays.toString(e.getStackTrace()));
        }
    }

    private void saveInput(String field, String input) {
        Jdbi jdbi = Jdbi.create(Utils.DB_URL);

        int index = Integer.parseInt(field.replace("input", ""));

        String sql = """
                select
                1 from
                notes
                where
                id=:id
                limit 1
                """;

        Optional<Integer> exists = jdbi.withHandle(handle ->
                handle.createQuery(sql)
                .bind("id", Integer.toString(index))
                .mapTo(Integer.class)
                .findOne());

        if (exists.isEmpty()) {
            insert(jdbi, index, input);
        } else {
            update(jdbi, index, input);
        }
    }

    private void update(Jdbi jdbi, int index, String input) {
        String sql = """
                update notes
                set
                note=:note
                where
                id=:id
                """;

        jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                .bind("note", input)
                .bind("id", index)
                .execute());
    }

    private void insert(Jdbi jdbi, int index, String input) {
        String sql = """
                insert into notes
                (id, note)
                values
                (:id, :note)
                """;

        jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                .bind("id", index)
                .bind("note", input)
                .execute());
    }
}
