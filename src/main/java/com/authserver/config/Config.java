package com.authserver.config;

import com.authserver.util.PropertiesParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private static final String configDir = "./config";

    private static String authSocketProperties = configDir + "/network/authsocket.ini";
    public static String AUTH_SOCKET_LISTEN_ADDRESS;
    public static short AUTH_SOCKET_LISTEN_PORT;

    private static String databaseProperties = configDir + "/database/database.ini";
    public static String DATABASE_DRIVER;
    public static String DATABASE_DIALECT;
    public static String DATABASE_URL;
    public static String DATABASE_USER;
    public static String DATABASE_PASSWORD;
    public static String DATABASE_SHOW_SQL;
    public static String DATABASE_HBM2DLL;

    public static void Load()
    {
        log.info("Loading configuration files");

        //Если запускаем из IDE
        if(Files.isDirectory(Paths.get("./dist/config")))
        {
            log.info("Seems like auth server is running from IDE. Using path: ./dist/config");
            authSocketProperties = "./dist/config/network/authsocket.ini";
            databaseProperties = "./dist/config/database/database.ini";
        }

        PropertiesParser configParser = new PropertiesParser(authSocketProperties);
        AUTH_SOCKET_LISTEN_ADDRESS = configParser.getString("ListenAddress", "192.168.1.83");
        AUTH_SOCKET_LISTEN_PORT = configParser.getShort("ListenPort", (short)4784);

        configParser = new PropertiesParser(databaseProperties);
        DATABASE_DRIVER = configParser.getString("DatabaseDriver","org.postgresql.Driver");
        DATABASE_DIALECT = configParser.getString("DatabaseDialect","org.hibernate.dialect.PostgreSQL95Dialect");
        DATABASE_URL = configParser.getString("DatabaseURL","jdbc:postgresql://localhost:5432/postgres?useSSL=false");
        DATABASE_USER = configParser.getString("DatabaseUser","postgres");
        DATABASE_PASSWORD = configParser.getString("DatabasePassword","root");
        DATABASE_SHOW_SQL = configParser.getString("DatabaseShowSql","true");
        DATABASE_HBM2DLL = configParser.getString("DatabaseHBM2DLL","update");
    }
}
