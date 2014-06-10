Installation
----------------------------
The only prerequisite is ```sbt```, the [Scala Build Tool](www.scala-sbt.org). 
Sbt will take care of all the dependencies, will compile and allow running the code and  tests easily. 
  
When you run any ```sbt``` command, it  will automatically recompile the modified code if needed.

Running the tests and code-checks
----------------------------
```bash
# run the main (covering items from the assignment and printing the results)
sbt run

# run the tests (sample output from assignment and randomized scalacheck tests)
sbt test

# stylecheck
sbt scalastyle
```

Code Structure
----------------------------
The Main code is is src/main/scala which contains the following files:

 * Kiwiland - the entry point (which mixes in the functionality below)
 * Graph - a simple adjacency-list based graph
 * RouteDist - simply calculates the distance of a given route
 * ShortestPath - implements 1) Floyd-Warshall and 2) Dijkstra based on min-queue
 * TripsCounter - a basic recursive counter of different routes available
 * SimpleUpdatableMinQueue - basic updatable min-queue on top of scala's Scala's HashSet
  - *Note: for better performance an adapted minHeap shall be used, which would keep track of the physical location of each entry in the heap*

Tests
----------------------------
The tests are contained in src/test/scala:
 
 * TestSuite contains the unit tests based on:
     - sample output from the assignment
     - running a large number of randomized tests based on arbitrary inputs generated with help of the [ScalaCheck](www.scalacheck.org) lib (details below)
 * CheckShortestPath - for a number of arbitrary graphs, ensures that results by the two Shortest-Path algorithms are the same for all pairs of nodes  
 * GraphGenerator - generates random arbitrary graphs
 * CheckUpdatableMinQueue - generates random priority queues and checks that findMin and updatePriority methods work correctly 
