package domain;

import java.sql.SQLException;

public final class SQL {
    public interface SQLMapper<I, O> {
        O map(I param) throws SQLException;
    }

    public interface SQLScope<T> {
        void call(T param) throws SQLException;
    }
}
