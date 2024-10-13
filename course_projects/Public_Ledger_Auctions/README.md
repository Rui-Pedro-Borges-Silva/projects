inside ssd_blockchain

check if can build:
./gradlew clean build

to make a Jar so that server and client can run:\
./gradlew clean shadowJar

command to run server:
java -jar build/libs/ssd_blockchain-1.0-SNAPSHOT.jar --server

command to run client:
java -jar build/libs/ssd_blockchain-1.0-SNAPSHOT.jar --client

each client must use a different terminal