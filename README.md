\# Smart Campus API — 5COSC022W Coursework



\## Overview

A RESTful API built with JAX-RS (Jersey) for managing university campus 

rooms and sensors. The API provides endpoints for room management, sensor 

operations, sensor readings, and includes comprehensive error handling.



\## Technology Stack

\- Java 11

\- JAX-RS with Jersey 3.1.3

\- Grizzly HTTP Server

\- Jackson for JSON

\- Maven for build management

\- In-memory storage using ConcurrentHashMap



\## Project Structure



src/main/java/

├── com.smartcampus

│   ├── Main.java

│   └── SmartCampusApplication.java

├── com.smartcampus.model

│   ├── Room.java

│   ├── Sensor.java

│   └── SensorReading.java

├── com.smartcampus.store

│   └── DataStore.java

├── com.smartcampus.resource

│   ├── DiscoveryResource.java

│   ├── RoomResource.java

│   ├── SensorResource.java

│   └── SensorReadingResource.java

├── com.smartcampus.exception

│   ├── RoomNotEmptyException.java

│   ├── LinkedResourceNotFoundException.java

│   └── SensorUnavailableException.java

├── com.smartcampus.mapper

│   ├── RoomNotEmptyExceptionMapper.java

│   ├── LinkedResourceNotFoundExceptionMapper.java

│   ├── SensorUnavailableExceptionMapper.java

│   └── GlobalExceptionMapper.java

└── com.smartcampus.filter

└── LoggingFilter.java



\## How to Build and Run



\### Prerequisites

\- Java 11 or higher

\- Maven 3.x



\### Build



\### Run



Server starts at: http://localhost:8080/api/v1



\---



\## Sample curl Commands



\### 1. Get API Discovery



\### 2. Create a Room



\### 3. Get All Rooms



\### 4. Create a Sensor



\### 5. Get Sensors filtered by type



\### 6. Post a Sensor Reading



\### 7. Get All Readings for a Sensor



\### 8. Delete a Room



\---



\## Report — Question Answers



\### Part 1.1 — JAX-RS Resource Lifecycle

By default, JAX-RS creates a new instance of a resource class for every 

incoming HTTP request (request-scoped). This means instance variables 

inside resource classes cannot store shared data. To manage shared 

in-memory data safely across requests, I used a Singleton DataStore class 

with ConcurrentHashMap, which is thread-safe and prevents race conditions 

when multiple requests arrive simultaneously on different threads.



\### Part 1.2 — HATEOAS

HATEOAS means the API includes navigation links in responses guiding 

clients to related actions. Unlike static documentation, clients discover 

URLs dynamically from responses, so URL changes do not break clients. 

The API becomes self-documenting and easier to explore without external 

documentation.



\### Part 2.1 — IDs vs Full Objects

Returning only IDs forces clients to make a separate GET request for each 

room, causing the N+1 problem. 100 rooms would require 100 extra requests. 

Returning full objects increases response size but delivers everything in 

one request. My implementation returns full objects since the management 

dashboard needs all details immediately.



\### Part 2.2 — DELETE Idempotency

Yes, DELETE is idempotent. The first call deletes the room and returns 204. 

Subsequent calls return 404 since the room no longer exists. The end state 

is identical each time — the room does not exist — satisfying idempotency 

even though the response code differs.



\### Part 3.1 — @Consumes Mismatch

If a client sends text/plain or application/xml when the method declares 

@Consumes(APPLICATION\_JSON), JAX-RS intercepts the request before the 

method is called and automatically returns HTTP 415 Unsupported Media Type. 

The resource method never executes.



\### Part 3.2 — @QueryParam vs Path Segment

Query parameters are better for filtering because they are optional, 

combinable (?type=CO2\&status=ACTIVE), and semantically correct — they 

modify how a collection is returned rather than identifying a new resource. 

Path segments imply a resource exists at that location, which is misleading 

for filtering operations.



\### Part 4.1 — Sub-Resource Locator Pattern

The pattern delegates nested path handling to dedicated classes. 

SensorReadingResource handles only readings, keeping each class small and 

focused. This improves maintainability, makes testing easier, follows the 

Single Responsibility Principle, and allows the API to scale without 

creating massive controller classes.



\### Part 5.2 — 422 vs 404

404 means the URL does not exist. 422 is more accurate because the URL 

/sensors is valid — the problem is inside the JSON body where roomId 

references a non-existent resource. 422 tells the client their URL is 

correct but their payload contains an invalid reference.



\### Part 5.4 — Stack Trace Security Risk

Stack traces expose framework versions (enabling CVE attacks), internal 

package and class names, file paths, and business logic flow. Attackers 

use this to identify known vulnerabilities in specific library versions 

and craft targeted attacks. My GlobalExceptionMapper returns a generic 

500 message to clients while logging real errors internally only.



\### Part 5.5 — Filters vs Inline Logging

Filters apply logging once for every request automatically, following the 

DRY principle. Inline logging requires adding Logger statements to every 

method — repetitive, error-prone, and if forgotten, endpoints go 

unmonitored. Filters guarantee consistent logging across the entire API 

without touching individual resource methods.

