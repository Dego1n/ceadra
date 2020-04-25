echo Building Auth Socket Project

echo Cleaning build up folder
rm -rf ./build

echo Creating new out directory
mkdir build
mkdir build/AuthServer

echo Building core with gradle
gradle build

echo Copying built lib to build
mkdir build/AuthServer/libs
cp -r build/libs/* build/AuthServer/libs/

echo Copying dist content
cp -r dist/* build/AuthServer/

echo Build done

