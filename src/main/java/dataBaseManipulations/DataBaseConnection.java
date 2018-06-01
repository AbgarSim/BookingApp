package dataBaseManipulations;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public final class DataBaseConnection {

    private final String dataBaseURL;
    private final String dataBaseUsername;
    private final String dataBasePassword;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public DataBaseConnection(String dataBaseURL, String dataBaseUsername, String dataBasePassword) {
        this.dataBaseURL = dataBaseURL;
        this.dataBaseUsername = dataBaseUsername;
        this.dataBasePassword = dataBasePassword;
    }

    public String connectToDataBase() {
        try {
            Class.forName("org.postgresql.Driver");
            if (dataBaseURL != null && dataBaseUsername != null && dataBasePassword != null) {
                connection = DriverManager.getConnection(dataBaseURL, dataBaseUsername, dataBasePassword);
                createTableIfNeeded();
                return "Connected";
            }
            return "Bad values";
        } catch (SQLException | ClassNotFoundException e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    private void createTableIfNeeded() {
        String tableSql = "CREATE TABLE SEAT(\n" +
                "  ID INT UNIQUE NOT NULL PRIMARY KEY,\n" +
                "  NAME VARCHAR(30) NOT NULL,\n" +
                "  SURNAME VARCHAR(30) NOT NULL,\n" +
                "  EMAIL VARCHAR(40) UNIQUE NOT NULL\n" +
                ");";
        String setSearchPath = "SET search_path TO train;";
        String checkIfTableExistQuery = "SELECT to_regclass('train.SEAT');";
        try {
            statement = connection.createStatement();
            statement.execute(setSearchPath);
            resultSet = statement.executeQuery(checkIfTableExistQuery);
            if (resultSet.next() && resultSet.getString(1) == null) {
                statement.execute(tableSql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertSql(int ID, String name, String surname, String email) {
        try {
            statement = connection.createStatement();
            String insertQuery =
                    "INSERT INTO seat (id, name, surname, email)" +
                            " VALUES (" + ID + ", '" + name + "','" + surname + "','" + email + "');";
            statement.execute(insertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] selectSql(int ID) {
        String bookedSeat[] = new String[4];
        String selectByIDQuery = "SELECT * from seat where id=" + ID + ";";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectByIDQuery);
            if (resultSet.next()) {
                bookedSeat[0] = resultSet.getString(1);
                bookedSeat[1] = resultSet.getString(2);
                bookedSeat[2] = resultSet.getString(3);
                bookedSeat[3] = resultSet.getString(4);
                return bookedSeat;
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        DataBaseConnection dataBaseConnection
                = new DataBaseConnection("jdbc:postgresql://localhost:8000/bookingapp",
                "postgres",
                "darkknight123");
    }
}
