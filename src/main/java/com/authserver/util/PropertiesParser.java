package com.authserver.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

public final class PropertiesParser
{
    private final Properties _properties = new Properties();
    private final File _file;

    public PropertiesParser(String name)
    {
        this(new File(name));
    }

    public PropertiesParser(File file)
    {
        _file = file;
        try (FileInputStream fileInputStream = new FileInputStream(file))
        {
            try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.defaultCharset()))
            {
                _properties.load(inputStreamReader);
            }
        }
        catch (Exception e)
        {
            System.out.println("[" + _file.getName() + "] There was an error loading config reason: " + e.getMessage());
        }
    }

    public boolean containskey(String key)
    {
        return _properties.containsKey(key);
    }

    private String getValue(String key)
    {
        String value = _properties.getProperty(key);
        return value != null ? value.trim() : null;
    }

    public boolean getBoolean(String key, boolean defaultValue)
    {
        String value = getValue(key);
        if (value == null)
        {
            System.out.println("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        if (value.equalsIgnoreCase("true"))
        {
            return true;
        }
        else if (value.equalsIgnoreCase("false"))
        {
            return false;
        }
        else
        {
            System.out.println("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"boolean\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public byte getByte(String key, byte defaultValue)
    {
        String value = getValue(key);
        if (value == null)
        {
            System.out.println("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try
        {
            return Byte.parseByte(value);
        }
        catch (NumberFormatException e)
        {
            System.out.println("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"byte\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public short getShort(String key, short defaultValue)
    {
        String value = getValue(key);
        if (value == null)
        {
            System.out.println("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try
        {
            return Short.parseShort(value);
        }
        catch (NumberFormatException e)
        {
            System.out.println("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"short\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue)
    {
        String value = getValue(key);
        if (value == null)
        {
            System.out.println("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            System.out.println("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"int\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue)
    {
        String value = getValue(key);
        if (value == null)
        {
            System.out.println("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try
        {
            return Long.parseLong(value);
        }
        catch (NumberFormatException e)
        {
            System.out.println("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"long\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public float getFloat(String key, float defaultValue)
    {
        String value = getValue(key);
        if (value == null)
        {
            System.out.println("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try
        {
            return Float.parseFloat(value);
        }
        catch (NumberFormatException e)
        {
            System.out.println("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"float\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public double getDouble(String key, double defaultValue)
    {
        String value = getValue(key);
        if (value == null)
        {
            System.out.println("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try
        {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException e)
        {
            System.out.println("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"double\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public String getString(String key, String defaultValue)
    {
        String value = getValue(key);
        if (value == null)
        {
            System.out.println("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }
        return value;
    }

    public <T extends Enum<T>> T getEnum(String key, Class<T> clazz, T defaultValue)
    {
        String value = getValue(key);
        if (value == null)
        {
            System.out.println("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try
        {
            return Enum.valueOf(clazz, value);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be enum value of \"" + clazz.getSimpleName() + "\" using default value: " + defaultValue);
            return defaultValue;
        }
    }
}