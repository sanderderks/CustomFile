package me.sd_master92.customfile.database;

import java.sql.ResultSet;

public record CustomTable(CustomDatabase database, String name)
{
    public boolean exists()
    {
        try
        {
            return database.getConnection().getMetaData().getTables(null, null, name, null).next();
        } catch (Exception e)
        {
            database.error(e);
            return false;
        }
    }

    public boolean create(String column, CustomColumn.DataType dataType)
    {
        return database.execute("CREATE TABLE " + name + " (" + column + " " + dataType.getValue() + ")");
    }

    public boolean createIFNotExists(String column, CustomColumn.DataType dataType)
    {
        if (!exists())
        {
            return create(column, dataType);
        }
        return true;
    }

    public boolean delete(String table)
    {
        return database.execute("DROP TABLE " + table);
    }

    public CustomDatabase getDatabase()
    {
        return database;
    }

    public String getName()
    {
        return name;
    }

    public CustomColumn getColumn(String name)
    {
        return new CustomColumn(database, this, name);
    }

    public boolean insertData(String[] columns, String[] values)
    {
        StringBuilder columnsAsString = new StringBuilder();
        for (String column : columns)
        {
            columnsAsString.append(column).append(",");
        }
        columnsAsString.deleteCharAt(columnsAsString.length() - 1);

        StringBuilder valuesAsString = new StringBuilder();
        for (String value : values)
        {
            valuesAsString.append(value).append(",");
        }
        valuesAsString.deleteCharAt(valuesAsString.length() - 1);

        return database.execute("INSERT INTO " + name + " (" + columnsAsString + ") VALUES (" + valuesAsString + ")");
    }

    public boolean updateData(String where, String changes)
    {
        return database.execute("UPDATE " + name + " SET " + changes + " WHERE " + where);
    }

    public ResultSet getData(String where)
    {
        return database.query("SELECT * FROM " + name + " WHERE " + where);
    }

    public ResultSet getAll()
    {
        return database.query("SELECT * FROM " + name);
    }

    public boolean deleteData(String where)
    {
        return database.execute("DELETE FROM " + name + " WHERE " + where);
    }
}
