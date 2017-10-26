/* Loren Milliman
 * loren.milliman@gmail.com
 * Bowling Score
 * May 20, 2016
 */

package Bowling;

import java.util.Scanner;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * Bowling Tests:
 * Some jUnit tests and a console test interface
 * for game simulation
 * 
 * @author l2m
 * @version 1.0
 */
public class BowlingScoreTest {

	/** some test objects. */
	private BowlingScore bs1;
	private BowlingScore bs2;
	private BowlingScore bs3;
	private static final String myScores = "XXX9/72X8/X"; // 8 frames
	
	/** Set up for tests. */
	@Before
	public void setUp() {
		bs1 = new BowlingScore();
		bs2 = new BowlingScore(myScores);
		bs3 = new BowlingScore(myScores,15,3);
	}
	
	/** the tests */
	@Test
	public void testAddRoll() {
		assertEquals(true, bs1.addRoll('X'));
		assertEquals(true, bs1.addRoll('X'));
		assertEquals(true, bs1.addRoll('x'));
		assertEquals(false, bs1.addRoll('/'));
		assertEquals(true, bs1.addRoll('7'));
		assertEquals(false, bs1.addRoll('X'));
		assertEquals(true, bs1.addRoll('/'));
		assertEquals(true, bs1.addRoll('X'));
		assertEquals(true, bs1.addRoll('0'));
		assertEquals(true, bs1.addRoll('/'));
		assertEquals(true, bs1.addRoll('X'));
		assertEquals(true, bs1.addRoll('X'));
		assertEquals(true, bs1.addRoll('X'));
		assertEquals(true, bs1.addRoll('X')); // frame 10
		assertEquals(true, bs1.addRoll('3'));
		assertEquals(true, bs1.addRoll('2'));
	}
	
	@Test
	public void testAddRoles() {
		assertEquals('X', bs2.getFrame(0).roll[0]);
		assertEquals('\0', bs2.getFrame(0).roll[1]);
		assertEquals('X', bs2.getFrame(1).roll[0]);
		assertEquals('X', bs2.getFrame(2).roll[0]);
		assertEquals(0, bs2.getFrameScore(9));
		assertEquals(145,bs2.getCurrentScore());
		assertEquals(234, bs2.addRolls(myScores));
		assertEquals(30, bs2.getFrameScore(1));
	}
	
	@Test
	public void testGetFrameScore() {
		assertEquals(-1, bs1.getFrameScore(-1));
		assertEquals(-1, bs1.getFrameScore(12));
	}
	
	@Test
	public void testScoreFrame() {
		assertEquals(true, bs1.addRoll('X'));
		assertEquals(true, bs1.addRoll('3'));
		assertEquals(true, bs1.addRoll('2'));
	}
	
	@Test
	public void testScoreLastFrame() {
		assertEquals(true, bs2.addRoll('X'));
		assertEquals(true, bs2.addRoll('X'));
		assertEquals(true, bs2.addRoll('X'));
		assertEquals(true, bs2.addRoll('X'));
	}
	
	@Test
	public void testValidInput() {
		assertEquals(true, bs1.addRoll('0'));
		assertEquals(false, bs1.addRoll('f'));
		assertEquals(false, bs1.addRoll('\0'));
	}
	
	@Test
	public void testToString() {
		System.out.println(bs2.toString());
	}
	
	@Test
	public void testPerfectGame() {
		for (int i = 0; i < 12; i++) {
			bs1.addRoll('X');
		}
		assertEquals(300,bs1.getCurrentScore());
	}
	
	@Test
	public void testHasStrike() {
		for (int i = 0; i < 9; i++) {
			bs1.addRoll('X');
		}
		bs1.addRoll('3');
		assertEquals(false,bs1.getFrame(9).hasStrike());
		bs1.addRoll('X'); // 2nd roll
		assertEquals(true,bs1.getFrame(9).hasStrike());
		bs2.addRoll('X');
		bs2.addRoll('3');
		bs2.addRoll('/');
		assertEquals(false,bs2.getFrame(9).hasStrike());
		bs2.addRoll('X'); // 3rd roll
		assertEquals(true,bs2.getFrame(9).hasStrike());
	}
	/**
	 * Console interface for testing game play
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		BowlingScore bs = new BowlingScore();
		String in = "";
		Scanner s = new Scanner (System.in);
		while (!in.contains("e")) {
			if (bs.gameOver()) {
				System.out.print("Game Over ");
			} else {
				System.out.print("Enter a roll ");
			}
			System.out.println("('e' to exit, 'n' new game): ");
			in = s.next();
			if (in.charAt(0) == 'n') {
				bs = new BowlingScore();
			} else if (!bs.gameOver()){
				if (!bs.addRoll(in.charAt(0))) {
					System.out.println("Invalid input (enter [0-9], X, or /");
				};
				System.out.println(bs.toString());
				System.out.println("CurrentScore: " + bs.getCurrentScore());
			}
		}
		s.close();
		System.out.println("Thanks for playing!");

	}

}
