import java.util.Arrays;


public class DisjointSet {
	
	/** The array of nodes in the uptrees. */
	private int[] nodes;
	
	/**
	 * Creates a new DisjointSet of a specified size.
	 * @param size how large to make the DisjointSet
	 */
	public DisjointSet(int size) {
		nodes = new int[size];
		Arrays.fill(nodes, -1);
	}
	
	/**
	 * Returns the root of the uptree that the vertex v belongs to.
	 * @param v the vertex to look at to find the root of the uptree it belongs to
	 * @return the root of the uptree that the vertex v comes from
	 */
	public int findRoot(int v) {
		if (nodes[v] < 0) {
			return v;
		} else {
			return findRoot(nodes[v]);
		}
	}
	
	/**
	 * Unions the two uptrees based on size.
	 * @param root1 the root of one tree to union
	 * @param root2 the root of the other tree to union
	 */
	public void unionSets(int root1, int root2) {
		if (Math.abs(nodes[root1]) < Math.abs(nodes[root2])) {
			nodes[root1] = root2;
		} else {			
			nodes[root2] = root1;
		}
	}
}
