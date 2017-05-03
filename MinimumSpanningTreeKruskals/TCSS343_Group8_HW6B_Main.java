// TCSS 343b Group 8
// Loren Milliman, Huy Ngo, Jacob Peterson
// HW 6b
// Due: 12/3/14

import java.util.Iterator;

public class TCSS343_Group8_HW6B_Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MinSpanningTree myMin = new MinSpanningTree();
		
		// For testing (need to create GUI output for extra credit):
		System.out.println("Edges:");
		Iterator<Edge> itr = myMin.getEdges().iterator();
		while (itr.hasNext()){
			Edge tempEdge = itr.next();
			System.out.println("(" + tempEdge.getFirstEndpoint().getName().toString() + ", " +
		                       tempEdge.getSecondEndpoint().getName().toString() + ")");
			
		}
		System.out.println("TotalCost: " + myMin.getTotalCost());
	}

}
