package me.yarinlevi.qprivatevoices.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.yarinlevi.qprivatevoices.QPrivateVoices;
import me.yarinlevi.qprivatevoices.configuration.Configuration;
import me.yarinlevi.qprivatevoices.utilities.Logger;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author YarinQuapi
 **/
public class QDatabase {
    private Connection connection;
    @Getter private static QDatabase instance;

    public QDatabase() {
        instance = this;

        //MYSQL 8.x CONNECTOR - com.mysql.cj.jdbc.MysqlDataSource
        //MYSQL 5.x CONNECTOR - com.mysql.jdbc.jdbc2.optional.MysqlDataSource
        //SQLITE - org.sqlite.JDBC

        HikariConfig hc = new HikariConfig();

        hc.setDriverClassName("org.sqlite.JDBC");
        hc.setJdbcUrl("jdbc:sqlite:" + QPrivateVoices.getInstance().getDataFolder() + "/database.db");

        HikariDataSource dataSource = new HikariDataSource(hc);

        String guildTable = "CREATE TABLE IF NOT EXISTS `guildTable`(" +
                "`guildId` VARCHAR(18) NOT NULL," +
                "`adminRole` VARCHAR(18) NOT NULL," +
                "`categoryId` VARCHAR (18) NOT NULL," +
                "`joinToCreateId` VARCHAR(18) NOT NULL" +
                ");";

        Logger.warning("Please await database connection..");

        try {
            connection = dataSource.getConnection();

            Statement statement = connection.createStatement();
            {
                statement.executeUpdate(guildTable);
                statement.closeOnCompletion();
                Logger.info("Successfully established connection to database.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Logger.error("Something went horribly wrong! please check your credentials and settings.");
        }
    }

    @Nullable
    public ResultSet get(String query) {

        query = query.replaceAll("( id )", " rowid ");

        try {
            return connection.createStatement().executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public int update(String query) {
        query = query.replaceAll("( id )", " rowid ");

        try {
            return connection.createStatement().executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public boolean insert(String query) {
        query = query.replaceAll("( id )", " rowid ");


        try {
            connection.createStatement().execute(query);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean insertLarge(List<String> list) {
        try {
            Statement statement = connection.createStatement();

            for (String s : list) {
                statement.addBatch(s.replaceAll("( id )", " rowid "));
            }

            statement.executeBatch();
            statement.closeOnCompletion();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
