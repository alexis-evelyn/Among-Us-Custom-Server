#!/bin/zsh

# Check For Existing Builds and Remove Them
if ls -U build/libs/*.jar 1> /dev/null 2>&1; then
  echo "Removing Existing Builds!!!"
  rm build/libs/*.jar
fi

# Build New Build
echo "Building New Build!!!"
./gradlew shadowJar

# Change Directory To Server Directory
echo "Starting Server!!!"
cd build/libs || (echo "Server File Missing!!!" && exit)

# Start Server
java -jar *.jar "$@"