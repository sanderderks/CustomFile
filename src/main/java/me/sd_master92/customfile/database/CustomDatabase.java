package me.sd_master92.customfile.database;

import me.sd_master92.customfile.CustomFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomDatabase
{
    private static final String PREFIX = "[CustomFile] ";
    private final String database, username, password;
    private Connection connection;
    private String host;

    public CustomDatabase(String host, String database, String username, String password)
    {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public CustomDatabase(CustomFile config, String path)
    {
        this.host = config.getString(path + ".host") + ":" + config.getString(path + ".port");
        this.database = config.getString(path + ".database");
        this.username = config.getString(path + ".user");
        this.password = config.getString(path + ".password");
    }

    public boolean connect()
    {
        if (!host.contains(":"))
        {
            host += ":3306";
        }
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?allowMultiQueries=true&useTimezone=true&serverTimezone=UTC", username, password);
            return isConnected();
        } catch (SQLException e)
        {
            error(e);
            return false;
        }
    }

    public boolean disconnect()
    {
        try
        {
            connection.close();
            return true;
        } catch (SQLException e)
        {
            error(e);
            return false;
        }
    }

    public Connection getConnection()
    {
        return connection;
    }

    public boolean isConnected()
    {
        try
        {
            return connection != null && connection.isValid(3);
        } catch (SQLException e)
        {
            error(e);
            return false;
        }
    }

    public boolean execute(String statement)
    {
        statement = statement.endsWith(";") ? statement : statement + ";";
        try
        {
            connection.createStatement().executeUpdate(statement);
            return true;
        } catch (SQLException e)
        {
            print(statement);
            error(e);
            return false;
        }
    }

    public ResultSet query(String statement)
    {
        statement = statement.endsWith(";") ? statement : statement + ";";
        try
        {
            return connection.createStatement().executeQuery(statement);
        } catch (SQLException e)
        {
            print(statement);
            error(e);
            return null;
        }
    }

    public CustomTable getTable(String name)
    {
        return new CustomTable(this, name);
    }

    void print(String text)
    {
        System.out.println(PREFIX + text);
    }

    void error(Exception e)
    {
        System.out.println(PREFIX + e.getMessage());
        e.printStackTrace();
    }
}
