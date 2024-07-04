package main;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

public class CreateDataBase {
    public static void main(String[] args) {
        Jdbi jdbi = Jdbi.create(Utils.DB_URL);

        createTable(jdbi);
    }
    private static void createTable(Jdbi jdbi) {
        String sql = """
                create table if not exists notes (
                    id integer primary key autoincrement,
                    note text not null
                );
                """;

        try (Handle handle = jdbi.open()){
            handle.execute(sql);
        }
    }
}
