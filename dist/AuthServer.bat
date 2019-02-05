@echo off
title MMO Auth Server

echo Starting Auth Server
echo.
java -Xms64m -Xmx128m -jar libs/authserver-DEVELOPMENT.jar

pause