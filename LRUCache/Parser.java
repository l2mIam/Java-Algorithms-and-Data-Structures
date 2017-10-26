/* Loren Milliman
 * loren.milliman@gmail.com
 * May 24, 2016
 * LRUCache implementation
 */

package LRUCache;

import java.io.IOException;
import java.util.Scanner;

public class Parser {
	
	/** the LRUCache. */
	private static LRUCache<String,String> myCache;
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		parseInput(in);
	}
	
	/**
	 * Parse the input.
	 * 
	 * @param in
	 */
	public static void parseInput(Scanner in) {
		StringBuilder output = new StringBuilder();
		Scanner item;
		String cmd;
		String key;
		String val;
		boolean done = false;
		boolean error = false;

		// first line should be SIZE command
		if (in.hasNextLine()) {
			item = new Scanner(in.nextLine());
			if (item.hasNext() && item.next().equals("SIZE") && item.hasNextInt() && initCache(item.nextInt())) {
				output.append("SIZE OK").append(System.getProperty("line.separator"));
			} else {
				// CAN'T PROCEED: don't have an int or not able to initialize cache
				output.append("Unable to initialize the cache, the first line of input must contain the word SIZE followed by a positive integer.");
				item.close();
				done = true;
			}
		}
		
		while (!done && in.hasNextLine()) {
			item = new Scanner(in.nextLine());

			if (item.hasNext()) {
				cmd = item.next();
				
				if (cmd.equals("GET")) {
					// should have one and only one token
					if (item.hasNext()) {
						key = item.next();
						if (item.hasNext()) { // has a second token
							error = true;
						} else { // one and only one token
							output.append(getValue(key)).append(System.getProperty("line.separator"));
						}
					} else { // no first token
						error = true;
					}
					
				} else if (cmd.equals("SET")) {
					// should have two and only two tokens
					if (item.hasNext()) {
						key = item.next();
						if (item.hasNext()) {
							val = item.next();
							if (item.hasNext()) { // has a third token
								error = true;
							} else {
								// two and only two tokens
								output.append(setValue(key,val)).append(System.getProperty("line.separator"));
							}
						} else { // no second token
							error = true;
						}
					} else { // no first token
						error = true;
					}
					
				} else if (cmd.equals("EXIT")) {
					// output.append("EXIT");
					item.close();
					done = true;

				} else {
					// Invalid command (not GET/SET/EXIT)
					error = true;
				}					
			}

			if (error) {
				output.append("ERROR").append(System.getProperty("line.separator"));
				error = false;
			}
		}
		in.close();
		System.out.print(output.toString());
	}
	
	/**
	 * Get value from the LRU Cache with the provided key.
	 * 
	 * @param theKey
	 * @return "GET" + the value, or "NOTFOUND"
	 */
	protected static String getValue(String theKey) {
		String retVal = myCache.get(theKey);
		return  retVal == null ? "NOTFOUND" : "GET " + retVal;
	}
	
	/**
	 * Enters the provided key/value pair into the LRU Cache.
	 * 
	 * @param theKey
	 * @param theVal
	 * @return "SET OK"
	 */
	protected static String setValue(String theKey, String theVal) {
		myCache.put(theKey,theVal);
		return "SET OK";
	}
	
	/**
	 * Initialize the LRU Cache with the provided size.
	 * 
	 * @param theMaxSize Size of the Cache.
	 * @return successful initialization
	 */
	protected static boolean initCache(int theMaxSize) {
		// validate size
		if (theMaxSize <= 0) return false;

		// create cache
		try {
			myCache = new LRUCache<String,String>(theMaxSize);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected static LRUCache<String,String> getCache() {
		return myCache;
	}
}
