echo "Removing Existing Builds!!!"
rm build/libs/*.jar

echo "Building New Build!!!"
./gradlew shadowJar

echo "Starting Server!!!"
cd build/libs
java -jar CrewMate-*.jar "$@"