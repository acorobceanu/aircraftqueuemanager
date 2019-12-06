# aircraftqueuemanager
Simple aircraft airport queue manager


## Purpose

A software subsystem of an air-traffic control system is defined to manage a queue of aircraft (AC) in an airport.  The aircraft queue is managed by a process that responds to three types of requests: 
1.	System boot used to start the system.
2.	Enqueue aircraft used to insert a new AC into the system. 
3.	Dequeue aircraft used to remove an AC from the system.

AC’s have at least (but are not limited to having) the following properties: 
1. AC type:  Emergency, VIP, Passenger or Cargo
2. AC size:  Small or Large

The process that manages the queue of AC’s satisfies the following: 
1.	There is no limit to the number of AC’s it can manage.
2.	Dequeue aircraft requests result in selection of one AC for removal such that:
a.	VIP aircraft has precedence over all other ACs except Emergency. Emergency aircraft has highest priority. 
b.	Passenger AC’s have removal precedence over Cargo AC’s.
c.	Large AC’s of a given type have removal precedence over Small AC’s of the same type.
d.	Earlier enqueued AC’s of a given type and size have precedence over later enqueued AC’s of the same type and size.

System Implementation Requirements
1.	Develop one or more data structures to hold the state of an individual AC. 
2.	Develop one or more data structures to hold the state of the AC queue. 
3.	Define data structures and/or constants needed to represent requests.
4.	Develop the code for three defined requests (queue, dequeue and list) and follows the above guidelines to implement an aircraft queue manager.  
5.	Assume multiple users of your dequeue implementation.  Multiple air traffic controllers will ask for the next plane to be dequeued.  
6.	Expose the implementation as REST endpoints for integration with a 3rd party UI.  

## How to run

<b>NOTE: This application requires running Postgres server. By default it is configured to run on localhost and default port 5432.</b>

1. Using Maven execute following command from the root of the project (Windows):
```
  > mvnw spring-boot:run
```
2. Build boot jar (builds boot jar under target folders, for example \target\controller-0.0.1-SNAPSHOT.jar) and run with java command line (Windows):
```
  - mvnw clean package
  - java -jar target\controller-0.0.1-SNAPSHOT.jar
```
