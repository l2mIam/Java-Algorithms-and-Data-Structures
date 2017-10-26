/* Loren Milliman
 * loren.milliman@gmail.com
 * May 24, 2016
 * LRUCache implementation
 */

package LRUCache;
import java.io.IOException;
import java.util.HashMap;

/**
 * Least Recently Used Cache
 * @author l2m
 * 
 * @param <K>
 * @param <V>
 */
public class LRUCache<K,V> {
	
	/** Maximum size of the cache. */
	private int maxSize;
	
	/** The LRU Cache */
	private HashMap<K,Node<K,V>> myLRUCache;
	
	/** Node that contains least recently used key (prev points to MRUNode). */
	private Node<K,V> LRUNode;
	
	/** Node that contains most recently used key (next points to LRUNode). */
	private Node<K,V> MRUNode;
	
	/**
	 * Node contains key/value pair and pointers to prev/next.
	 * @author l2m
	 *
	 * @param <L> The key
	 * @param <M> The value
	 */
	private class Node<L,M> {
		private L key;
		private M val;
		private Node<L,M> prev;
		private Node<L,M> next;
		
		public Node(L theKey, M theVal, Node<L,M> thePrev, Node<L,M> theNext) {
			this.key = theKey;
			this.val = theVal;
			this.prev = thePrev;
			this.next = theNext;
		}
		
	}
	
	/**
	 * Least Recently Used Cache Constructor
	 * 
	 * @param theMaxSize must be greater than zero.
	 */
	public LRUCache(int theMaxSize) throws IOException {
		if (theMaxSize <= 0) {
			throw new IOException();
		}
		this.maxSize = theMaxSize;
		this.myLRUCache = new HashMap<K,Node<K,V>>();
		this.LRUNode = null;
		this.MRUNode = null;
	}
	
	/**
	 * Get the value corresponding to the provided key.
	 * 
	 * @param theKey
	 * @return Returns the value to which the specified key is mapped,
	 * or null if this map contains no mapping for the key.
	 */
	public V get(K theKey) {
		Node<K,V> tempNode = this.myLRUCache.get(theKey);
		// If doesn't exist return null
		if (tempNode == null) {
			return null;
		}
		
		// Else move Node to MRUNode and return the value
		makeMRUNode(tempNode);
		return tempNode.val;
	}
	
	/**
	 * Put the value into the cache
	 * 
	 * @param theKey
	 * @param theVal
	 * 
	 * @return the previous value associated with key, or null
	 */
	public V put(K theKey, V theVal) {
		V retVal = null;
		
		// key exists > update value
		if (this.myLRUCache.containsKey(theKey)) {
			Node<K,V> tempNode = this.myLRUCache.get(theKey);
			retVal = tempNode.val;
			tempNode.val = theVal;
			makeMRUNode(tempNode);
		} else {
			// Cache is full: remove LRUNode
			if (this.myLRUCache.size() == this.maxSize) {
				// disconnect LRUNode
				this.MRUNode.next = this.LRUNode.next;
				this.LRUNode.next = this.MRUNode;
				// remove from cache
				this.myLRUCache.remove(this.LRUNode.key);
				// repoint to new LRUNode
				this.LRUNode = this.MRUNode.next;
			}
			// create new Node (always MRUNode)
			Node<K,V> tempNode = new Node<K,V>(theKey, theVal, this.MRUNode, this.LRUNode);
						
			if (this.LRUNode == null) {
				// first Node inserted
				tempNode.next = tempNode;
				tempNode.prev = tempNode;
				this.LRUNode = tempNode;
				this.MRUNode = tempNode;
			} else {
				makeMRUNode(tempNode);
			}
			// insert into HashMap
			this.myLRUCache.put(theKey, tempNode);
		}
		return retVal;
	}
	
	/**
	 * Move the given node to MRU Node and update links.
	 * 
	 * @param theNode
	 */
	protected void makeMRUNode(Node<K,V> theNode) {
		// case: theNode == LRUNode
		if (theNode.equals(LRUNode)) {
			this.MRUNode = this.LRUNode;
			this.LRUNode = this.LRUNode.next;

		// case: theNode in middle
		} else if (!theNode.equals(MRUNode)) {
			// disconnect node
			theNode.prev.next = theNode.next;
			theNode.next.prev = theNode.prev;
			// reconnect node
			this.LRUNode.prev = theNode;
			theNode.next = this.LRUNode;
			this.MRUNode.next = theNode;
			theNode.prev = this.MRUNode;
			this.MRUNode = theNode;
		}
		// case: theNode == MRUNode (don't move any nodes)
	}
	
	/**
	 * return a list of the key/val pairs in order from LRU to MRU
	 * does not alter the order.  Used to verify cache.
	 * 
	 * @return Representation of the key/val pairs in LRUCache from LRU to MRU
	 */
	public String toString() {
		 StringBuilder sb = new StringBuilder("CACHE:");
		 // iterate the list and output the pairs
		 Node<K,V> currNode = this.LRUNode;
		 for (int i = 0; i < this.myLRUCache.size(); i++) {
			 if (currNode != null) {
				 sb.append(" ").append(currNode.key).append(": ").append(currNode.val).append(",");
			 }
			 currNode = currNode.next;
		 }
		 sb.deleteCharAt(sb.length()-1);
		 return sb.toString();
	}

}
