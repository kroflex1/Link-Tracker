package edu.java.scrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MigrationTest extends IntegrationTest {

    @Test
    public void chatsTableTest() throws SQLException {
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement sqlQuery = connection.prepareStatement("SELECT * FROM chats");
            ResultSetMetaData metaData = sqlQuery.executeQuery().getMetaData();
            String idColumnName = metaData.getColumnName(1);
            String timeColumnName  = metaData.getColumnName(2);
            assertEquals("chat_id", idColumnName);
            assertEquals("created_at", timeColumnName);
        }
    }

    @Test
    public void linksTableTest() throws SQLException{
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement sqlQuery = connection.prepareStatement("SELECT * FROM links");
            ResultSetMetaData metaData = sqlQuery.executeQuery().getMetaData();
            assertEquals("link", metaData.getColumnName(1));
            assertEquals("created_at", metaData.getColumnName(2));
            assertEquals("last_check_time", metaData.getColumnName(3));
            assertEquals("last_activity_time", metaData.getColumnName(4));
        }
    }
}
