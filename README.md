Installation
----------------------------
The only prerequisite is ```sbt```, the scala build tool
 ( http://www.scala-sbt.org/ ). Sbt will take care of all the dependencies, will compile and allow running the code and  tests easily. 
  
When running any of Sbt commands, it  would also recompile the modified code automatically if needed.

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
  - TODO: for better performance an adapted minHeap shall be used, which would keep track of the physical location of each entry in the heap

Tests
----------------------------
The tests are contained in src/test/scala:
 
 * TestSuite contains the unit tests 
     - sample output in the assignment
     - calls the large number of randomized tests based on arbitrary inputs generated with help of the ScalaCheck lib (see below)
 * CheckShortestPath - for a number of arbitrary graphs, ensures that results by the two Shortest-Path algorithms are the same for all pairs of nodes  
 * GraphGenerator - generates random arbitrary graphs
 * CheckUpdatableMinQueue - generates random priority queues and checks that findMin and updatePriority methods work correctly 
