package me.sd_master92.customfile.database;

public record CustomColumn(CustomDatabase database,
                           CustomTable table, String name)
{
    public boolean exists()
    {
        try
        {
            return database.getConnection().getMetaData().getColumns(null, null, table.getName(), name).next();
        } catch (Exception e)
        {
            database.error(e);
            return false;
        }
    }

    public boolean create(DataType dataType)
    {
        return database.execute("ALTER TABLE " + table.getName() + " ADD " + name + " " + dataType.getValue());
    }

    public boolean createIFNotExists(DataType dataType)
    {
        if (!exists())
        {
            return create(dataType);
        }
        return true;
    }

    public boolean delete()
    {
        return database.execute("ALTER TABLE " + table.getName() + " DROP COLUMN " + name);
    }

    public CustomDatabase getDatabase()
    {
        return database;
    }

    public CustomTable getTable()
    {
        return table;
    }

    public String getName()
    {
        return name;
    }

    public boolean insertData(String value)
    {
        return database().execute("INSERT INTO " + table.getName() + " (" + name + ") VALUES (" + value + ")");
    }

    public enum DataType
    {
        INT_PRIMARY("int"),
        VARCHAR_PRIMARY("varchar(255) PRIMARY KEY"),
        INT("int"),
        LONG("bigint"),
        VARCHAR("varchar(255)");

        private final String value;

        DataType(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}