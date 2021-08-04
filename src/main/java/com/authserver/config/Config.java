package com.authserver.config;

import com.authserver.util.PropertiesParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private static final String CONFIG_DIR = "./dist/config";

    private static String authSocketProperties = CONFIG_DIR + "/network/authsocket.ini";
    public static String AUTH_SOCKET_LISTEN_ADDRESS;
    public static short AUTH_SOCKET_LISTEN_PORT;

    private static String databaseProperties = CONFIG_DIR + "/database/database.ini";
    public static String DATABASE_DRIVER;
    public static String DATABASE_DIALECT;
    public static String DATABASE_URL;
    public static String DATABASE_USER;
    public static String DATABASE_PASSWORD;
    public static String DATABASE_SHOW_SQL;
    public static String DATABASE_HBM2DLL;

    private Config() {

    }

    public static void Load()
    {
        log.info("Parsing config files");
        PropertiesParser configParser = new PropertiesParser(authSocketProperties);
        AUTH_SOCKET_LISTEN_ADDRESS = System.getenv("LISTEN_CLIENTS_ADDR") != null ? System.getenv("LISTEN_CLIENTS_ADDR") : configParser.getString("ListenAddress", "127.0.0.1");
        AUTH_SOCKET_LISTEN_PORT = configParser.getShort("ListenPort", (short)4784);

        configParser = new PropertiesParser(databaseProperties);
        DATABASE_DRIVER = configParser.getString("DatabaseDriver","org.postgresql.Driver");
        DATABASE_DIALECT = configParser.getString("DatabaseDialect","org.hibernate.dialect.PostgreSQL95Dialect");
        DATABASE_URL = System.getenv("DATABASE_URL") != null ? System.getenv("DATABASE_URL") :  configParser.getString("DatabaseURL","jdbc:postgresql://localhost:5432/postgres?useSSL=false");
        DATABASE_USER = System.getenv("DATABASE_USER") != null ? System.getenv("DATABASE_USER") : configParser.getString("DatabaseUser","postgres");
        DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD") != null ? System.getenv("DATABASE_PASSWORD") : configParser.getString("DatabasePassword","root");
        DATABASE_SHOW_SQL = configParser.getString("DatabaseShowSql","true");
        DATABASE_HBM2DLL = configParser.getString("DatabaseHBM2DLL","update");
    }
}
