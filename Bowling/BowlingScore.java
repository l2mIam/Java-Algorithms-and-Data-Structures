/* Loren Milliman
 * loren.milliman@gmail.com
 * Bowling Score
 * May 20, 2016
 */

package Bowling;

/**
 * Bowling Score: tallies up the rolls and keeps track of the score.
 * Can set the number of frames and number of rolls per frame.
 * Can add rolls one at a time or a sequence of rolls.
 * 
 * @author l2m
 * @version 1.0
 */
public class BowlingScore {

	/** An array of Frames */
	private Frame[] frame;
	
	/** Default number of frames in game. */
	private int numFrames = 10;
	
	/** Default number of rolls per frame. */
	private int numRolls = 2;

	/** Current frame the bowler is on. */
	private int currFrame = 1;
	
	/** Current roll within currFrame. */
	private int currRoll = 1;
	
	/**
	 * Inner class Frame
	 * @author l2m
	 */
	class Frame {

		/** Frame score*/
		int score;
		
		/** Rolls for the frame. */
		char[] roll;
		
		/** True after final roll (not spare/strike), or after spare/strike re-tallied. */
		boolean scored;
		
		Frame() {
			this(numRolls);
		}
		
		Frame(int theNumRolls) {
			score = 0;
			roll = new char[theNumRolls];
			scored = false;
		}
		
		public boolean hasStrike() {
			if (this.roll.length > numRolls) {
				// last frame
				return (roll[0] == 'X' || roll[1] == 'X' || roll[2] == 'X');
			}
			return roll[0] == 'X';
		}
		
		public boolean hasSpare() {
			return roll[1] == '/';
		}
		
		public boolean needsScoreUpdate() {
			return !scored;
		}
		
		/**
		 * Update the score of the frame.
		 * NOTE: Previous frame MUST be scored (!needsScoreUpdate()) before scoring this frame.
		 * Score the frame if not spare/strike.
		 * Mark Scored, if:
		 * a) not spare/strike and last roll
		 * b) spare & rollOne not null
		 * c) strike & rollTwo not null
		 * 
		 * @param rollOne the score value of the next roll (-1 doesn't exist).
		 * @param rollTwo the score value of the second roll (-1 doesn't exist).
		 *
		 */
		public void scoreFrame(int rollOne, int rollTwo) {
			score = 0;
			if (this.hasSpare()) {
				if (rollOne < 0) {
					score += getRollScore(roll[0]);
				} else {
					score += 10 + rollOne;
					scored = true;
				}
			} else if (this.hasStrike()) {
				if (rollTwo >= 0) {
					score += 10 + rollOne + rollTwo;
					// if spare subtract the first roll back out (already included)
					if (rollOne != 10 && rollTwo == 10) {
						score -= rollOne;
					}
					scored = true;
				}
			} else {
				score += (getRollScore(roll[0]) + getRollScore(roll[1]));
				if (roll[1] != '\0') {
					scored = true;
				}
			}
		}
		
		public void scoreLastFrame() {
			score = 0;
			score += getRollScore(roll[0]) + getRollScore(roll[1]) + getRollScore(roll[2]);
			if ( (!hasSpare() && !hasStrike() && roll[1] != '\0') || ((hasSpare() || hasStrike()) && roll[2] != '\0') ) {
				scored = true;
			}
		}
		
	}

	///////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////
	BowlingScore() {
		frame = initFrames();
	}
	
	/**
	 * Constructor takes a sequence of rolls as String.
	 * 
	 * @param theRolls representation of rolls.
	 */
	BowlingScore(String theRolls) {
		this();
		addRolls(theRolls);
	}
	
	/**
	 * Constructor takes a sequence of roll values,
	 * number of frames, and number of rolls per frame.
	 * 
	 * @param theRolls representation of rolls.
	 * @param theNumFrames number of frames in the game.
	 * @param theNumRolls number of rolls per frame.
	 */
	BowlingScore(String theRolls, int theNumFrames, int theNumRolls) {
		numFrames = theNumFrames;
		numRolls = theNumRolls;
		frame = initFrames();
		addRolls(theRolls);
	}

	///////////////////////////////////////////
	// Public methods
	///////////////////////////////////////////

	/**
	 * Input a new roll and update scores.
	 * 
	 * @param theRoll A char ([0-9]|-|X|x|/)
	 * @return false = invalid input.
	 */
	public boolean addRoll(char theRoll) {
		if(validInput(theRoll) && !gameOver()) {
			// add roll to frame
			frame[currFrame-1].roll[currRoll-1] = modInput(theRoll);

			// update scores
			updateScore();

			// increment current values
			incrementValues();

			return true;
		}
		return false;
	}
	
	/**
	 * Add a series of rolls from user input.
	 * Will add as many rolls as there is room to add them
	 * starting with currentFrame.
	 * 
	 * @param theRolls a sequence of roll values.
	 * @return the current game score.
	 */
	public int addRolls(String theRolls) {
		// populate all the frames with rolls
		int i = 0;
		while(i < theRolls.length() && currFrame <= numFrames) {
			// add roll to frame
			frame[currFrame-1].roll[currRoll-1] = modInput(theRolls.charAt(i));
			incrementValues();
			i++;
		}
		
		// Score the frames
		for (int j = 0; j < numFrames-1; j++) {
			frame[j].scoreFrame(getNextRoll(j,1), getNextRoll(j,2));
		}
		frame[numFrames-1].scoreLastFrame();

		return getCurrentScore();
	}
	
	/**
	 * Return the score for the given frame number.
	 * Will return zero if the frame has not yet been scored.
	 * 
	 * @param theFrame A frame number between 1 and numFrames(default 10).
	 * @return score for the given frame number, or -1 if invalid frame number.
	 */
	public int getFrameScore(int theFrame) {
		if (theFrame > 0 && theFrame <= numFrames) {
			return frame[theFrame-1].score;
		}
		return -1;
	}
	
	/**
	 * Return the current game score.
	 * 
	 * @return the score of the game.
	 */
	public int getCurrentScore() {
		int retVal = 0;
		for (int i = 0; i < numFrames; i++) {
			retVal += frame[i].score;
		}
		return retVal;
	}
	
	/**
	 * Indicates the game status.
	 * 
	 * @return true if game is over (you can't have another roll).
	 */
	public boolean gameOver() {
		return (currFrame == numFrames+1);
	}
	
	/**
	 * Output a string representation of the Bowling Game Status.
	 * 
	 * @return table showing frames, rolls, and scores.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Frame:");
		for (int i = 1; i <= numFrames; i++) {
			sb.append("\t").append(i);
		}
		sb.append("\n");
		
		sb.append("Rolls:");
		int j;
		for (j = 0; j < numFrames-1; j++) {
			sb.append("\t").append(frame[j].roll[0]).append(frame[j].roll[1]);
		}
		sb.append("\t").append(frame[j].roll[0]).append(frame[j].roll[1]).
						append(frame[j].roll[2]).append("\n");
		
		sb.append("Scores:");
		int currScore = 0;
		for (int k = 0; k < numFrames; k++) {
			if (frame[k].score != '\0') {
				currScore = frame[k].score;
				sb.append("\t").append(currScore);
			}
		}
		sb.append("\n");
		
		return sb.toString();
	}

	///////////////////////////////////////////
	// Private methods
	///////////////////////////////////////////
	protected Frame getFrame(int index) {
		return frame[index];
	}
	
	private void incrementValues() {
		if (currFrame != numFrames) {
			if (currRoll == numRolls || frame[currFrame-1].hasStrike()) {
				currRoll = 1;
				currFrame++;
			} else {
				currRoll++;
			}
		// on last frame
		} else {
			if (currRoll < numRolls || (currRoll == numRolls &&
					(frame[currFrame-1].hasSpare() || frame[currFrame-1].hasStrike())) ) {
				currRoll++;
			} else {
				currFrame++;
			}
		}
	}

	/**
	 * Check that the input is a valid character.
	 * @param theRoll the value of the roll [0-9]|-|X|x|/
	 * @return true if valid character.
	 */
	private boolean validInput(char theRoll) {
		return ("0-123456789Xx/".indexOf(theRoll) >= 0
				&& !(Character.toUpperCase(theRoll) == 'X' && currRoll == 2 && currFrame < numFrames)
				&& !(theRoll == '/' && currRoll == 1 && currFrame < numFrames) );
	}
	
	/**
	 * Modify the input before storing in Frame.
	 * Store 0 as '-': this is how it should be displayed and
	 * avoids conflict with tests for null.
	 * 
	 * @param theRoll
	 * @return
	 */
	private char modInput(char theRoll) {
		if (theRoll == '0') {
			return '-';
		} else if (theRoll == 'x') {
			return 'X';
		}
		return theRoll;
	}


	
	/**
	 * Initialize the Frames at the start.
	 * 
	 * @return the list of frames
	 */
	private Frame[] initFrames() {
		Frame[] theFrames = new Frame[numFrames];
		for (int i = 0; i < numFrames-1; i++) {
			theFrames[i] = new Frame();
		}
		
		// last frame potentially has extra roll
		theFrames[numFrames-1] = new Frame(numRolls+1);
		
		return theFrames;
	}
	
	
	/**
	 * Update the Frame scores.
	 */
	private void updateScore() {
		// update previous frame scores
		if (currFrame > 2 && frame[currFrame-3].needsScoreUpdate()) {
			frame[currFrame-3].scoreFrame(getNextRoll(currFrame-3,1), getNextRoll(currFrame-3,2));
		}
		if ( (currFrame == 2 || (currFrame > 2 && !frame[currFrame-3].needsScoreUpdate())) && frame[currFrame-2].needsScoreUpdate() ) {
			frame[currFrame-2].scoreFrame(getNextRoll(currFrame-2,1), getNextRoll(currFrame-2,2));
		}

		// can't score current frame if previous frame not scored
		if (currFrame == 1 || !frame[currFrame-2].needsScoreUpdate()) {
			// on last frame
			if (currFrame == numFrames) {
				frame[currFrame-1].scoreLastFrame();
			} else {
				// score current frame
				frame[currFrame-1].scoreFrame(getNextRoll(currFrame-1,1), getNextRoll(currFrame-1,2));
			}
		}
	}

	/**
	 * Returns the next roll values for calculating spare/strikes.
	 * 
	 * @param frameNum Index of frame to get the next rolls
	 * @param rollNum 1 = next roll, 2 = roll after next
	 * @return Numeric value of roll, -1 if doesn't exist.
	 */
	private int getNextRoll(int frameNum, int rollNum) {
		if (frameNum < numFrames-1) {
			if (rollNum == 1 && frame[frameNum+1].roll[0] != '\0') {
				return getRollScore(frame[frameNum+1].roll[0]);
			} else if (rollNum == 2) {
				if (frame[frameNum+1].roll[1] != '\0') {
					return getRollScore(frame[frameNum+1].roll[1]);
				} else if (frameNum < numFrames-2 && frame[frameNum+2].roll[0] != '\0') {
					return getRollScore(frame[frameNum+2].roll[0]);
				}
			}
		}
		return -1;
	}
	
	/**
	 * Convert the character representation of the roll to an numeric value.
	 * the '-' character represents a roll of zero.
	 * 
	 * @param theRoll
	 * @return the numeric value of the roll.
	 */
	private int getRollScore(char theRoll) {
		int retVal = 0;
		if (theRoll == 'X' || theRoll == '/') {
			retVal +=10;
		} else if (theRoll != '-' && theRoll != '\0') {
			retVal += Character.getNumericValue(theRoll);
		}
		return retVal;
	}
	
}
