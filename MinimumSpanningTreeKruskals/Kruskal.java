import java.util.Iterator;



public class Kruskal {
	
	private static SimpleGraph myGraph;
	private static DisjointSet myDisjointSet;
	private static int myAcceptedEdges;
	private static BinaryHeap myEdgeHeap;
	
	public Kruskal() {
	}
	
	public static void initialize() {
		myGraph = new SimpleGraph();
		GraphInput.LoadSimpleGraph(myGraph);
		
		myEdgeHeap = new BinaryHeap();	
		
		loadEdges();
		
		myDisjointSet = new DisjointSet(myGraph.numVertices());
		
		myAcceptedEdges = 0;
	}
	
	public static void calculateMST() {
		while ((myAcceptedEdges < myGraph.numVertices() - 1) && !myEdgeHeap.isEmpty()) {

			try {
				Edge minEdge = (Edge) myEdgeHeap.deleteMin();
				
				System.out.println("Min Deleted: " + minEdge.getFirstEndpoint().getName() + ", " + minEdge.getSecondEndpoint().getName() + " w: " + minEdge.getData());
				
				int A = Integer.parseInt((String) minEdge.getFirstEndpoint().getName());
			    int B = Integer.parseInt((String) minEdge.getSecondEndpoint().getName());
			    
			    System.out.println("First endpoint: " + A + " Second Endpoint: " + B);
			    
			    int rootA = myDisjointSet.findRoot(A);
			    int rootB = myDisjointSet.findRoot(B);
				
			    if (rootA != rootB) {
			    	myAcceptedEdges++;
			    	myDisjointSet.unionSets(A, B);
			    }
			} catch (EmptyHeapException e) {
				System.out.println("Tried to delete from an empty heap!");
			}				
		}
	}
	
	public static void loadEdges() {
		Iterator<Edge> edgeIt = myGraph.edges();
		while (edgeIt.hasNext()) {
			ComparableEdge cEdge = new ComparableEdge(edgeIt.next());
			myEdgeHeap.insert(cEdge);
			System.out.println("Inserted edge " + cEdge.getFirstEndpoint().getName() + ", " + cEdge.getSecondEndpoint().getName() + " w: " + cEdge.getData());
		}
	}
	
	public static void printResults() {
		
	}
	
	public static void main(String[] args) {
		initialize();
		calculateMST();
		printResults();
	}
	
	
}