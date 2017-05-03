// TCSS 343b Group 8
// Loren Milliman, Huy Ngo, Jacob Peterson
// HW 6b
// Due: 12/3/14

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Creates a Minimum Spanning Tree from a simple graph using Kruskal's algorithm
 * 
 * @author Loren Milliman, Huy Ngo, & Jacob Peterson
 */
public class MinSpanningTree {

	/** A simple graph for wich to create the minimum spanning tree. */
	public SimpleGraph myGraph;
	
	/** The Output: Edges that define the minimum spanning tree. */
	private List<Edge> myEdges;
	
	/**
	 * Construct the Minimum spanning tree
	 * Calls to LoadSimpleGraph for input data and creation of myGraph.
	 */
	MinSpanningTree(){
		myGraph = new SimpleGraph();
		Hashtable<String,Vertex> myUptree = new Hashtable<String,Vertex>();
		myUptree = GraphInput.LoadSimpleGraph(myGraph);
		createOutputEdges(myUptree);
	}
	
	/**
	 * Returns the list of output edges.
	 * @return the output edges of the minimum spanning tree
	 */
	public List<Edge> getEdges() {
		return myEdges;
	}
	
	/**
	 * Total Cost of the edges of the minimum spanning tree.
	 * @return total cost of the edges of the minimum spanning tree.
	 */
	public double getTotalCost(){
		double retVal = 0;
		Iterator<Edge> itr = myEdges.iterator();
		while(itr.hasNext()){
			retVal += (double)(itr.next().getData());
		}
		return retVal;
	}
	
	/**
	 * A heap of the edges of the simple graph.
	 * @return A heap of all the edges in myGraph.
	 */
	public BinaryHeap createEdgeHeap() {
		BinaryHeap myEdgeHeap = new BinaryHeap();
		Iterator<Edge> itr = myGraph.edges();
		while(itr.hasNext()){
			myEdgeHeap.insert(new ComparableEdge(itr.next()));
	    }
		return new BinaryHeap();
		
	}
	
	/**
	 * Create the list of output edges.
	 * 
	 * @param myUptree the list of vertices returned from LoadSimpleGraph
	 * @throws EmptyHeapException 
	 */
	public void createOutputEdges(Hashtable<String,Vertex> myUptree) throws EmptyHeapException {
		// IMPLEMENT
		BinaryHeap myEdgeHeap = createEdgeHeap();
		while (!myEdgeHeap.isEmpty()){
			Comparable<ComparableEdge> tmpEdge = myEdgeHeap.deleteMin();
			// Do Stuff
		}
		
	}
	
	
}
