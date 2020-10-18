./gradlew shadowJar
cd build/libs
java -jar CrewMate-*.jar "$@"