import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Part of Project 3 for CS 241 Winter 2018
 */

/**
 * This class will represent a directed, weighted graph. Adding vertices and edges are supported within
 * this class, however only removing edges are currently supported. The main function this class focuses
 * on is obtaining the shortest path from a given vertex index to another, using Dijkstra's algorithm. Thus,
 * this graph strictly only supports non-negative weights.
 * @author Joel Tengco
 * @param <T> The type of objects this graph will contain as its vertices.
 *
 */
class Digraph<T extends Comparable<T>> {
	private ArrayList<LinkedList<EdgeListNode>> edges;
	private T[] vertices;
	
	/**
	 * Constructs a new <code>Digraph</code> object containing all the <code>T</code> objects
	 * within the given array.
	 * @param initialNodes An array containing all the desired objects that are to be represented
	 * in this graph.
	 */
	public Digraph(T[] initialNodes) {
		vertices = initialNodes;
		edges = new ArrayList<LinkedList<EdgeListNode>>(vertices.length);
		// set a new LinkedList for each vertex
		for(int i = 0; i < vertices.length; i++)
			edges.add(new LinkedList<EdgeListNode>());
	}
	
	/**
	 * Adds a weighted edge to this graph from the given source index to the given target index. Note that indices
	 * representing this graph's vertices starts at zero. For example, the first indexed vertex of this graph
	 * will have an index of 0.
	 * @param sourceIndex The index of the source vertex for the new edge.
	 * @param targetIndex The index of the target vertex for the new edge.
	 * @param weight The weight for the new edge. Must be non-negative.
	 * @throws IllegalArgumentException If a negative integer is given for the weight of the edge
	 */
	public void addEdge(int sourceIndex, int targetIndex, int weight) throws IllegalArgumentException {
		if(weight < 0)
			throw new IllegalArgumentException("This graph does not support negative weights.");
		else
			edges.get(sourceIndex).add(new EdgeListNode(targetIndex, weight));
	}
	
	/**
	 * Inserts a new vertex into this graph.
	 * @param newVertex The new vertex to be represented in this graph.
	 */
	public void addVertex(T newVertex) {
		// extend the array length by one
		vertices = Arrays.copyOf(vertices, vertices.length + 1);
		vertices[vertices.length - 1] = newVertex;
		
		// set a new LinkedList for the new vertex
		edges.add(new LinkedList<EdgeListNode>());
	}
	
	/**
     * Checks if this graph contains the given element. Specifically, this method
     * returns true if and only if <code>element.compareTo(searchElement) == 0</code>
     * for some element in this graph, otherwise false.
     * @param searchElement An element to be checked if an equivalent exists in this graph.
     * @return True if and only if there exists an equivalent to the search element in this graph,
     * false otherwise.
     */
	public boolean contains(T searchElement) {
		// check every vertex if they match the search element, return true if there is a match
		for(T element : vertices)
			if(element.compareTo(searchElement) == 0)
				return true;
			
		return false;
	}
	
	public int getVertexIndex(T searchElement) {
		for(int i = 0; i < vertices.length; i++)
			if(vertices[i].compareTo(searchElement) == 0)
				return i;
			
		return -1;
	}
	
	public void removeEdge(int sourceIndex, int targetIndex) {
		/*
		 * for every node in the source vertex's edge list,
		 * 	if the target index is found in that node, remove it
		 */
		for(EdgeListNode node : edges.get(sourceIndex))
			if(node.vertexIndex == targetIndex) {
				edges.get(sourceIndex).remove(node);
				return;
			}
	}
	
	public T getVertex(int index) {
		return vertices[index];
	}
	
	public int getEdgeWeight(int sourceIndex, int targetIndex) {
		// return the weight of the edge, if found
		for(EdgeListNode node : edges.get(sourceIndex))
			if(node.vertexIndex == targetIndex)
				return node.weight;
		
		return 0;
	}
	
	public int[] getShortestPath(int sourceIndex, int targetIndex) throws IllegalArgumentException {
		// variables to find shortest path
		boolean[] visited;
		int[] pathLengths;
		int currentIndex;
		PriorityQueue<PQElement> queue;
		// variables to store shortest paths
		Stack<PQElement> shortestPaths;
		Stack<PQElement> pathToTarget;
		// variables to build resulting array
		boolean pathFound;
		int targetPathLength;
		int[] resultArray;
		
		// initialize necessary variables
		visited = new boolean[vertices.length];
		pathLengths = new int[vertices.length];
		queue = new PriorityQueue<PQElement>();
		shortestPaths = new Stack<PQElement>();
		pathToTarget = new Stack<PQElement>();
		pathFound = false;
		
		// set each vertex's distance estimate to max integer value
		for(int i = 0; i < pathLengths.length; i++)
			pathLengths[i] = Integer.MAX_VALUE;
		
		// set source index path length to zero, set source index as current index
		pathLengths[sourceIndex] = 0;
		currentIndex = sourceIndex;
		// add new PQElement to priority queue
		queue.add(new PQElement(currentIndex, 0, -1));
		
		while(!pathFound && !queue.isEmpty()) {
			if(!visited[queue.peek().vertexIndex]) {
				// store queue head element in shortestPaths stack
				shortestPaths.push(queue.remove());
				// set current index as aforementioned element
				currentIndex = shortestPaths.peek().vertexIndex;
				// if moved element contained the target index, set pathFound to true
				if(currentIndex == targetIndex) {
					pathFound = true;
					continue;
				}
			} else { // next vertex index is already visited, thus remove it
				queue.remove();
				continue;
			}
			
			// for every edge in current vertex's edge list, relax its neighbors
			for(EdgeListNode node : edges.get(currentIndex))
				if(!visited[node.vertexIndex]) {
					int newPathLength = pathLengths[currentIndex] + node.weight;
					
					if(newPathLength < pathLengths[node.vertexIndex]) {
						pathLengths[node.vertexIndex] = newPathLength;
						queue.add(new PQElement(node.vertexIndex, newPathLength, currentIndex));
					}
				}
			
			// set the current index as visited
			visited[currentIndex] = true;
		}
		
		if(!pathFound)
			throw new IllegalArgumentException("Vertex with target index is unreachable.");
		
		// obtain the path length from source to target vertex
		targetPathLength = shortestPaths.peek().pathLength;
		// push the top element of shortestPaths to pathToTarget
		pathToTarget.push(shortestPaths.peek());
		// obtain the adjacent vertex in the path
		int targetAdjVertexIndex = shortestPaths.pop().adjacentIndex;
		while(!shortestPaths.isEmpty()) {
			// continue to push top element to pathToTarget if and only if it contains the target adjacent index
			if(shortestPaths.peek().vertexIndex == targetAdjVertexIndex) {
				pathToTarget.push(shortestPaths.peek());
				targetAdjVertexIndex = shortestPaths.pop().adjacentIndex;
			} else	// the top element pertains to a different shortest path
				shortestPaths.pop();
		}
		
		// build the resulting array
		resultArray = new int[pathToTarget.size() + 1];
		resultArray[0] = targetPathLength;
		for(int i = 1; i < resultArray.length; i++)
			resultArray[i] = pathToTarget.pop().vertexIndex;
		
		return resultArray;
	}
	
	private class EdgeListNode {
		private int vertexIndex;
		private int weight;
		
		public EdgeListNode(int vertexIndex, int weight) {
			this.vertexIndex = vertexIndex;
			this.weight = weight;
		}
	}
	
	private class PQElement implements Comparable<PQElement>{
		private int vertexIndex;
		private int pathLength;
		private int adjacentIndex;
		
		public PQElement(int vertexIndex, int distanceData, int adjacentIndex) {
			this.vertexIndex = vertexIndex;
			this.pathLength = distanceData;
			this.adjacentIndex = adjacentIndex;
		}
		
		@Override
		public int compareTo(PQElement o) {
			return pathLength - o.pathLength;
		}
		
	}
}
