# HARabbitMQ

- The set of nodes is also called a cluster. These nodes are identified within the cluster by their name, consisting of 
a prefix and a hostname, which must be therefore unique.
- This program performs the function of maintaining connection if 1 node is disconnected
## Purpose
- When you declare a queue by connecting to a node in the cluster, based on the attribute you pass, a queue will be 
created on any one of the nodes in the cluster. The node where the queue resides is called the Master Node for that 
particular queue. Later, when you set up RabbitMQ High Availability for that particular queue, 
a Mirrored Queue will be created in the other nodes of the cluster.
- The messages in the master queue will be synced with the mirror queue in other nodes. Although mirror queues exist, 
all to publish and consumed requests will be directed only to the master queue. Even if the publisher sends the request
directly to the mirror queue, the request will be directed to the node which has the master queue. RabbitMQ does this 
process to maintain message integrity between the master queue and mirrored queues.
## Run app:
- From ha folder run bash:
```bash
docker-compose build
docker-compose up
```
- From main folder run bash:
```bash
mvn clean install
cd target/
java -jar demoHARabbitMQ-0.0.1-SNAPSHOT.jar
```
- Call API with
  + url : http://localhost:8080/api/v1/ha
  + data : {
    "token" : "123",
    "message" : "Show a message"
    }