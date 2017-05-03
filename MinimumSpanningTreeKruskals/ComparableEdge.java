// TCSS 343b Group 8
// Loren Milliman, Huy Ngo, Jacob Peterson
// HW 6b
// Due: 12/3/14

/**
 * A wrapper for the Edge class.
 * Comparable Edge implements comparable and can be used with BinaryHeap.
 */
public class ComparableEdge implements Comparable<ComparableEdge> {

	private Edge myEdge;
	
	public ComparableEdge(Edge theEdge){
		myEdge = theEdge;
	}

	public Edge getEdge(){
		return myEdge;
	}
	
	@Override
	public int compareTo(ComparableEdge theOtherEdge) {
		
		if ((double)(myEdge.getData()) > (double)(theOtherEdge.getEdge().getData())){
			return 1;
		} else if ((double)(myEdge.getData()) < (double)(theOtherEdge.getEdge().getData())){
			return -1;
		} else {
			return 0;
		}
	}

}
