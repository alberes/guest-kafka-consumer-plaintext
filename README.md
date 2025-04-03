First make downloads of two projects:

[Producer messager](https://github.com/alberes/guest-kafka-producer-plaintext)
[Consumer messager](https://github.com/alberes/guest-kafka-consumer-plaintext)

Execute this commands in those projects:
1 - Create container guest-kafka-producer-plaintext
```
docker build --tag guest-kafka-producer-plaintext .
```
2 - Create container guest-kafka-consumer-plaintext
```
docker build --tag guest-kafka-consumer-plaintext .
```
3 - Run all containeres
```
docker-compose up -d
```
or
```
docker-compose up -d --build
```

4 - Access URL to create table [PGAdmin](http://localhost:15432/browser/)
Click on Register
On General write a name
Select Connection

|Field|Value|
|-----:|-----------|
|Host name/address:| postgresdb|
|Port:| 5432|
|Maintenance database:| guests|
|Username:| postgres|
|Password:| postgres|

It is necesary only in first execution or if you execute docker-compose with --build
```
create table guest(
legal_entity_number char(11) not null primary key,
name char(100) not null,
birthday date not null,
last_update_date timestamp not null,
creation_date timestamp not null
)
```

5 - Some script to use in guest-broker (kafka)

Maybe you need to access container and verify topics like create, list, consumer etc
Firt into in container

- interactive mode
```
docker exec -it guest-broker sh
```

- Into this directory to execute kafka scripts
```
cd /opt/kafka/bin
```

- List topics created in docker-compose
```
./kafka-topics.sh --bootstrap-server guest-broker:9092 --list
```

- Consume topic guest-topic
```
./kafka-console-consumer.sh --bootstrap-server guest-broker:9092 --topic guest-topic --from-beginning
```

- Consume topic guest-topic-failure
```
./kafka-console-consumer.sh --bootstrap-server guest-broker:9092 --topic guest-topic-failure  --from-beginning
```

- Create new topic
```
./kafka-topics.sh --bootstrap-server guest-broker:9092 --create --topic my-new-topic
```

- Produce message
```
./kafka-console-producer.sh --bootstrap-server guest-broker:9092 --topic my-new-topic
```
Type some messages and press enter

Use command Consume topic and change topic name to your topic to see your messages

6 - Down all containeres
```
docker-compose down
```

|           Container | name  |
|--------------------:|-------|
|  zookeeper to kafka |zookeeper|
|               Kafka |guest-broker|
|           Postegres |postgresdb|
| IDE Client Postgres |pgadmin|
|     Spring Producer |guest-kafka-producer-plaintext|
|     Spring Consumer |guest-kafka-consumer-plaintext|

- Create new Guest
```
curl --location 'http://localhost:8081/guests' \
--header 'Content-Type: application/json' \
--data '{
"legalEntityNumber": "96852389014",
"name": "Guest 96852389014",
"birthday": "2026-04-05"
}'
```

- Find all Guest in cache
```
curl --location 'http://localhost:8082/guests'
```

- Find guest in cache or database
```
curl --location 'http://localhost:8082/guests/96852389014'
```