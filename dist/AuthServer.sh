echo Starting Auth Server
echo.
java -Xms64m -Xmx128m -cp "libs/authserver-DEVELOPMENT.jar:config/log/" com.authserver.AuthServer
