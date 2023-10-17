package com.hanielfialho.lobby.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseOperations {
    void execute(Connection connection) throws SQLException;
}
