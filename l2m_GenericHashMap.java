/*
 * Loren Milliman
 * TCSS 342 Sp14
 * HW4
 * MyHashMap.java
 * Due: 5/20/14
 */

/**
 * MyHashMap specifies both a generic Key and Value and implements separate chaining.
 * The default size is 16, but any size can be specified.
 * MyHashMap has a put(key, value) and get(key) method
 * The map rehashes when the number of elements exceeds 2 times the size.
 * 
 * @author l2m
 * @version TCSS342SP14
 * @param <AnyKey> A key of any type.
 * @param <AnyVal> A value of any type.
 */
public class MyHashMap<AnyKey, AnyVal> {

	/** Track the number of elements and the current size of the Map. */
	public int myCount, mySize;

	/** This provides a means to quickly traverse all the Nodes when rehashing. */
	private Node<AnyKey, AnyVal> traversalFront;

	/** The Main storage array for the Hash Map. */
	private Node<AnyKey, AnyVal>[] lists;

	/** Default Constructor.  Constructs a HashMap of size 16. */
	public MyHashMap() {
		this(16);
	}

	/**
	 * Constructs a HashMap of the provided size.
	 * @param theMapSize Size of the new HashMap.
	 */
	@SuppressWarnings("unchecked")
	public MyHashMap(final int theMapSize) {
		lists = new Node[theMapSize];
		traversalFront = null;
		myCount = 0;
		mySize = theMapSize;
	}

	/**
	 * Put a new Key, Value pair in the HashMap.
	 * If the Key exists, update the value.
	 * @param theKey A key to place in the map.
	 * @param theVal a value to place in the map.
	 * @return true, indicating successful insertion (there is no failure!)
	 */
	public boolean put(final AnyKey theKey, final AnyVal theVal) {
		// if key exists, update value:
		int hashIndex = myHashCode(theKey);
		for(Node<AnyKey, AnyVal> p = lists[hashIndex]; p != null; p = p.next) {
			if (p.myKey.equals(theKey)) {
				p.myVal = theVal;
				return true;
			}
		}
		if (++myCount > mySize * 2) {
			rehash();
		}
		// create new Node, add to chain at list[index] and to traversal "list"
		Node<AnyKey, AnyVal> newNode = new Node<AnyKey, AnyVal>(theKey, theVal);
		newNode.traversal = traversalFront;
		newNode.next = lists[hashIndex];
		lists[hashIndex] = traversalFront = newNode;
		return true;
	}

	/**
	 * Get a value from myHashMap corresponding to the provided key.
	 * @param theKey the given key used to retrieve the value.
	 * @return the value at the given key.
	 */
	public AnyVal get(final AnyKey theKey) {
		int hashIndex = myHashCode(theKey);
		for(Node<AnyKey, AnyVal> p = lists[hashIndex]; p != null; p = p.next) {
			if (p.myKey.equals(theKey)) {
				return p.myVal;
			}
		}
		return null;
	}

	/**
	 * Create a new array twice the old size.  Copy all Nodes to new list
	 * at their new hash location.
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {
		mySize *= 2;
		Node<AnyKey, AnyVal>[] newLists = new Node[mySize];
		for(Node<AnyKey, AnyVal> p = traversalFront; p != null; p = p.traversal) {
			int hashIndex = myHashCode(p.myKey);
			p.next = newLists[hashIndex];
			newLists[hashIndex] = p;
		}
		lists = newLists;
	}

	/**
	 * Take the provided key and return an index.
	 * @param theKey a key to be hashed
	 * @return an index in the list array
	 */
	private int myHashCode(final AnyKey theKey) {
		return Math.abs(theKey.hashCode() % mySize);
	}

	/** Node used in myHashMap. */
	public static class Node<AnyKey, AnyVal> {

		private AnyKey myKey;
		private AnyVal myVal;

		/** The next Node in the separate chain at the specific list index. */
		private Node<AnyKey, AnyVal> next;

		/** All Nodes are connected when added to allow traversing all Nodes. */
		private Node<AnyKey, AnyVal> traversal;

		/** Construct a new Node. */
		Node(final AnyKey theKey, final AnyVal theVal) {
			myKey = theKey;
			myVal = theVal;
			next = null;
			traversal = null;
		}
	}
}
