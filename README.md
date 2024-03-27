# Oriented Graph Generation and Manual Input
## Parameters that are given through commandline:
- **KInMin**, **KInMax** - How many inbound edges each vertex should have (Min/Max).
- **KOutMin**, **KOutMax** - How many outbound edges each vertex should have (Min/Max).
- **V** - Vertex count.
### Graph Generation
To generate a graph open a terminal navigate to the Graph.java folder and write:
##
    javac Graph.java
    java Graph <KInMin> <KinMax> <KOutMin> <KOutMax> <V>
### Graph Manual Input
To input a graph manually open a terminal navigate to Graph.Java folder and write:
##
    javac Graph.java
    java <V>
After specifying the vertex count follow the programs instructions to make edges and after you're finished you will get the graph.
## BFS (Breadth-first search)
Program also includes a BFS algorithm to find the longest path from a source vertex to the furthest vertex.
