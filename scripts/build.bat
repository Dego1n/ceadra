@echo off
title Building Auth Socket Project

echo Cleaning build up folder
rd /s /q build

echo Creating new out directory
md build
md build\AuthServer

echo Building core with gradle
call gradle build

echo Copying built lib to build
ROBOCOPY  build\libs build\AuthServer\libs /E /njh /njs /ndl /nc /ns

echo Copying dist content
ROBOCOPY  dist\ build\AuthServer\ /E /njh /njs /ndl /nc /ns

echo Build done
