/* Loren Milliman
 * loren.milliman@gmail.com
 * May 24, 2016
 * LRUCache implementation
 */

package LRUCache;

import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class ParserTest {

	/** some test objects. */
	LRUCache<String,String> testCache1;
	Scanner testInput;

	/** Set up for tests. */
	@Before
	public void setUp() {
		testInput = new Scanner("SIZE 3\nFOO bar\nGET foo\nSET foo 1\nGET foo\nGET\nSET\nSET foo\nSET foo one two\nSET foo 1.1\nGET foo\nSET spam 2\nGET spam\nSET ham third\nSET parrot four\nGET foo\nGET spam\nGET ham\nSET spam 1\nGET ham parrot\nGET parrot\nGET spam\nGET spam\nEXIT");
		try {
			testCache1 = new LRUCache<String,String>(5);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/** the tests */
	@Test
	public void testCreateCache() {
		assertEquals(false, Parser.initCache(0));
		assertEquals(false, Parser.initCache(-10));
		assertEquals(true, Parser.initCache(3000));
		try {
			new LRUCache<String,String>(0);
			assertTrue(false);
		} catch (IOException e) {
			assertTrue(true);	
		}
		try {
			new LRUCache<Integer,Integer>(-3);
			assertTrue(false);
		} catch (IOException e) {
			assertTrue(true);	
		}
	}
	
	@Test
	public void testParser() {
		Parser.parseInput(testInput);
		assertEquals(true,"CACHE: ham: third, parrot: four, spam: 1".equals(Parser.getCache().toString()));
	}
	
	@Test
	public void invalidInputs() {
		Parser.parseInput(new Scanner("SIZE"));
		assertEquals(true, Parser.getCache() == null);
	}
		
}
