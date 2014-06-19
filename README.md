Installation
----------------------------
The only prerequisite is ```sbt```, the Scala's [ Simple Build Tool](www.scala-sbt.org). 
Sbt will take care of all the dependencies, will compile and allow running the code and  tests easily. 
  
When you run any ```sbt``` command, it  will automatically recompile the modified code if needed.

P.S. Recomended sbt version: 0.12.2+ or 0.13.x (and Scala 2.10.x is needed).

Running the tests and code-checks
----------------------------
```bash
# run the main (covering items from the assignment and printing the results)
sbt run

# run the tests (sample output from assignment and randomized scalacheck tests)
sbt test
```

Code Structure
----------------------------
The Main code is in [src/main/scala/kwl/](src/main/scala/kwl/) containing the files described below. 
The design decisions are briefed next to each class/file name, with more details in the code.
Generally, practicality & simplicity is preferred over performance (unless the three are easy to combine).

 * ```Kiwiland``` - the entry point (which mixes in the other functionality)
 * ```Graph``` - a simple graph that, in addition to flat adjacency-list, provides a couple of other
  lazily calculated representations each suited for different use cases, e.g. 
  dict of adjacency lists by out-vertex, adjacency matrix, renaming of vertices into numbered ones etc 
 * ```RouteDist``` - simply calculates the distance of a given route
 * ```shortestpath``` package:
    - ```FloydWarshall``` - super simple all-pairs-shortest path
    - ```Dijkstra``` - based on a simple min-queue (if need be, performance can be improved by using a 
      better queue, e.g. a Heap queue with a decreaseKey)
 * ```tripscounter``` package:
    - ```TripsCounter``` - a recursive counter of distinct routes of dynamic-programing nature. 
      It is decorated with result caching/memo that guarantees polynomic running time. 
    - ```TripsCounterRecursive``` an alternative purely recursive implementation useful in 
      testing/validating the earlier
 * ```utils/``` package:
   - ```utils/SimpleUpdatableMinQueue``` - a simple updatable min-queue (if needbe, it could be 
   replaced by e.g. a MinHeap with decreaseKey)
   - ```utils/Memo``` - a basic generic Memento "decorator" (7 lines of beautiful external code)     

**The coding style** is mostly based on the following [Scala Style Guide](http://www.codecommit.com/scala-style-guide.pdf). 
Max Line Length of 100 chars is used.

Tests
=============================
The tests are contained in [src/test/scala/](src/test/scala/):
 
 * ```TestSuite``` contains the unit tests based on:
     - sample output from the assignment
     - running a large number of randomized tests based on arbitrary inputs generated with help of 
     the [ScalaCheck](www.scalacheck.org) lib (details below)
 * ```CheckShortestPath``` - for a number of arbitrary graphs, ensures that results by the two 
   Shortest-Path algorithms are the same for all pairs of nodes  
 * ```GraphGenerator``` - generates random arbitrary graphs
 * ```CheckUpdatableMinQueue``` - generates random priority queues and checks that findMin and 
   updatePriority methods work correctly
 * ```CheckTripsCounter``` - for a number of random graphs check that the results of the two different implementations match

Test results can be seen here: [![Build Status](https://travis-ci.org/vidma/scala-kiwiland.svg?branch=master)](https://travis-ci.org/vidma/scala-kiwiland)
