Installation
----------------------------
The only prerequisite is ```sbt```, the scala build tool
 ( http://www.scala-sbt.org/ ). Sbt will take care of all the other dependencies
  needed for compiling and running the tests and the code. 
  
When running any of Sbt commands, it  would also recompile the modified code automatically if needed.

Running the tests and code-checks
----------------------------
```bash
# run the basic tests (from assignment) and randomized scalacheck tests
sbt test

# or for verbose:
sbt test -v

# stylecheck
sbt scalastyle
```

Code Structure
----------------------------
Main code is is src/main/scala which contains the following files:
    * Kiwiland - the entry point (which mixes in the functionality below)
    * Graph - simple adjacency-list based graph structure
    * RouteDist - simply calculates the distance of a given route
    * ShortestPath - implements Floyd-Warshall and Dijkstra based on min-queue
    * TripsCounter - a basic recursive counter of different routes available
    * SimpleUpdatableMinQueue - basic updatable min-queue on top of scala's PriorityQueue

Tests implemented
----------------------------
All the tests are contained in src/test/scala:
    * TestSuite 
        - contains the unit tests (from samples in assignment)
        - it also calls the tests based on large number of randomized inputs generated with help of the ScalaCheck lib.
    * CheckShortestPath - for a number of arbitrary graphs, ensures that results by the two Shortest-Path algorithms are the same for all pairs of nodes  
        - GraphGenerator - generates the random arbitrary graphs
    * CheckUpdatableMinQueue - generates random priority queues and checks that findMin and updatePriority methods work correctly 
