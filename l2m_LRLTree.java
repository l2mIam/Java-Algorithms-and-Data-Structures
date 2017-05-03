/*
 * Christopher Helmer
 * Loren Milliman
 * TCSS 342 Sp14
 * HW3
 * LRLTree.java
 * Due: 5/6/14
 */
import java.io.*;
import java.util.*;


/**
 * @author Christopher Helmer
 * @author Loren Milliman
 * @version TCSS342sp14
 *
 */
public class LRLTree {
	
	/** The String representation of the indent for the toJavaString method. */
	final private static String INDENT = "    ";

	/** Node for the LRL Tree. */
	class TreeNode {
		private String data;
		private TreeNode left;
		private TreeNode right;
		
		/** Construct an empty TreeNode. */
		private TreeNode() {
			data = "";
			left = right = null;
		}

		/** Constructs a TreeNode with the given data. */
		private TreeNode(final String theData) {
			data = theData;
			left = right = null;
		}
	} // end of TreeNode

	/** A reference to the root of the LRL Tree. */
	final private TreeNode root;
	
	/** Maintains a list of all variables and their current values. */
	final private HashMap<String, Integer> env;
	
	/**
	 * Constructs an LRL Tree with the given LRL code.
	 * @param anInputFile the raw LRL code
	 * @throws FileNotFoundException If the referenced file cannot be found.
	 */
	public LRLTree(final String anInputFile) throws FileNotFoundException {
		
		final Scanner input = new Scanner(new File(anInputFile));
		env = new HashMap<String, Integer>();
		root = buildTree(input);
		populateEnv(root);
	}
	
	/**
	 * Recursively build LRL Tree from LRL input file.
	 * @param s A Scanner to an LRL File.
	 * @return A TreeNode
	 */
	private TreeNode buildTree(Scanner s) {
		TreeNode tempNode = new TreeNode();
		String str = s.next();
		while ((str.equals(")") && s.hasNext()) || str.equals("(")) {
			str = s.next();
		}
		tempNode.data = str; // base case
		if(isOperator(str)) { // recursive case
			tempNode.left = buildTree(s);
			if (!str.equals("print")) {
				tempNode.right = buildTree(s);
			}
		}
		return tempNode;
	}
	
	/**
	 * Determines if the given string is an LRL operator.
	 * @param theString the provided token.
	 * @return TRUE if the given string is an operator.
	 */
	private boolean isOperator(String str) {
		return str.equals("+") || str.equals("-") || str.equals("*") 
				|| str.equals("/") || str.equals("==") || str.equals("=") 
				|| str.equals("<") || str.equals(">") || str.equals("if") 
				|| str.equals("while") || str.equals("block") || str.equals("print");
	}
	
	/**
	 * Recursively trace the LRL tree and assign variables to keys in the env hashMap.
	 * @param aNode the current Node called upon.
	 */
	private void populateEnv(TreeNode aNode) {
		if(aNode.data.equals("=") && isInteger(aNode.right.data)) { //base case
			env.put(aNode.left.data, Integer.parseInt(aNode.right.data));
		} 
		if(aNode.right != null) {	//if you can go right, then you must be able to go left
			populateEnv(aNode.left);	//go left first
			populateEnv(aNode.right);
		} 
	}
	
	/**
	 * Determines if the given String is an integer.
	 * @param str A given String.
	 * @return True if the String is an integer.
	 */
	private boolean isInteger(String str) {
		if(str == null) {
			return false;
		} else {
			try {
					Integer.parseInt(str);
			} catch(NumberFormatException e) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Produce console output for the LRL Tree.
	 */
	public void print() {
		print(root);
	}

	/**
	 * Recursively trace the LRL Tree and output to console.
	 * @param aNode is the input node to print.
	 */
	private void print(TreeNode aNode) {
		if(aNode == null) {
			System.out.print(")");
		} else if(!isOperator(aNode.data)) {
			System.out.print(aNode.data + " ");
		} else if(aNode.data.equals("print")) {
			System.out.print("(" + aNode.data + " ");
			print(aNode.left);
			System.out.print(")");
		} else {
			System.out.print("(" + aNode.data + " ");
			print(aNode.left);
			print(aNode.right);
			System.out.print(")");
		}
	}
	
	/**
	 * Evaluate (execute) the LRL code.
	 * @return 0
	 */
	public int eval() {
		return eval(root);
	}
	
	/**
	 * Recursively evaluates the LRL code in the LRL tree.
	 * @param aNode is the node from which to evaluate the LRL code.
	 * @return an integer representation of the leaves of the LRL tree.
	 */
	private int eval(TreeNode aNode) {
		if(aNode.data.equals("print")) {
			System.out.println(eval(aNode.left));
		} 
		if(aNode.data.equals("block")) {
			eval(aNode.left);
			eval(aNode.right);
		}
		if(aNode.data.equals("while")) {
			handleWhile(aNode.left, aNode.right);
		} 
		if(aNode.data.equals("=")) {
			env.put(aNode.left.data, eval(aNode.right));
		} 
		if(isInteger(aNode.data)) {
			return Integer.parseInt(aNode.data);
		} 
		if(!isOperator(aNode.data) && !isInteger(aNode.data)) {
			return env.get(aNode.data);
		} 
		if(aNode.data.equals("if")) {
			handleIf(aNode.left, aNode.right); 
		} 
		if(aNode.data.equals("+")) {
			return eval(aNode.left) + eval(aNode.right);
		}
		if(aNode.data.equals("/")) {
			return eval(aNode.left) / eval(aNode.right);
		}
		if(aNode.data.equals("-")) {
			return eval(aNode.left) - eval(aNode.right);
		}
		if(aNode.data.equals("*")) {
			return eval(aNode.left) * eval(aNode.right);
		}
		return 0;
	}
	
	/**
	 * Handles the "while" case when evaluating the LRL code.
	 * @param left is the node to the left of "while" containing the conditional.
	 * @param right is the node to the right of "while" containing the code to 
	 * execute while the condition is yet to terminate.
	 */
	private void handleWhile(TreeNode left, TreeNode right) {
		if(left.data.equals("<")) {
			while(eval(left.left) < eval(left.right)) {
				eval(right);
			}
		}
		if(left.data.equals(">")) {
			while(eval(left.left) > eval(left.right)) {
				eval(right);
			}
		}
		if(left.data.equals("==")) {
			while(eval(left.left) == eval(left.right)) {
				eval(right);
			}
		}
	}
	
	/** 
	 * Handles the "if" case when evaluating the LRL code.
	 * @param left is the node to the left of the "if" statement containing
	 * the conditions.
	 * @param right is the node to the right of the "if" statement containing 
	 * the code to evaluate if the conditions are met.
	 */
	private void handleIf(TreeNode left, TreeNode right) {
		if(left.data.equals("<")) {
			if(eval(left.left) < eval(left.right)) {
				eval(right);
			} else {
				return;
			}
		}
		if(left.data.equals(">")) {
			if(eval(left.left) > eval(left.right)) {
				eval(right);
			} else {
				return;
			}
		}
		if(left.data.equals("==")) {
			if(eval(left.left) == eval(left.right)) {
				eval(right);
			} else {
				return;
			}
		}
	}
		
	/**
	 * Format the LRL tree into Java formatted code.
	 * @return A representation of the LRL Tree in Java formatted code.
	 */
	public String toJavaString() {
		int currIndent = 0;
		// Setup preliminary output
		StringBuilder javaString = new StringBuilder(500);
		javaString.append("public class JavaCode {\n");
		javaString.append(indentString(++currIndent)).
			append("public static void main(String[] args) {\n");
		currIndent++;
		for (String key : env.keySet()) {
			javaString.append(indentString(currIndent)).append("int ").append(key).append(";\n");
		}
		// Recurse Tree
		javaString.append(toJavaString(currIndent, root));

		// close out blocks
		javaString.append((javaString.charAt(javaString.length() - 1) == '}') ? "\n" : ";\n");
		while (currIndent > 0) {
			javaString.append(indentString(--currIndent)).append("}\n");
		}
		return javaString.toString();
	}
	
	/** Given indent multiplier return a String with the correct number of space chars. */
	private String indentString(final int currIndent) {
		String retVal = "";
		for (int i = 0; i < currIndent; i++) {
			retVal += INDENT;
		}
		return retVal;
	}
	
	/** The recursive portion of formatting the LRL tree into Java formatted code. */
	private String toJavaString(int currIndent, TreeNode aNode) {
//		String retVal = "";
		StringBuilder retVal = new StringBuilder(400);
		if(aNode == null) {
			return null;
		} else if (aNode.data.equals("block")) {
			retVal.append(toJavaString(currIndent, aNode.left)).
				append((retVal.charAt(retVal.length() - 1) == '}') ? "\n" : ";\n").
				append(toJavaString(currIndent, aNode.right));
		} else if(aNode.data.equals("print")) {
			retVal.append(indentString(currIndent)).append("System.out.println(").
				append(toJavaString(0, aNode.left)).append(")");
		} else if (aNode.data.equals("while") || aNode.data.equals("if")) {
			retVal.append(indentString(currIndent)).append(aNode.data).
				append("(").append(toJavaString(0, aNode.left)).append(") {\n");
			currIndent++;
			retVal.append(toJavaString(currIndent, aNode.right)).
				append((retVal.charAt(retVal.length() - 1) == '}') ? "\n" : ";\n").
				append(indentString(--currIndent) + "}");
		
		// operator (excluding Block While or If which have already been checked for above)
		} else if(isOperator(aNode.data)) { 
			retVal.append(indentString(currIndent)).append((currIndent == 0) ? "(" : "").
				append(toJavaString(0, aNode.left) + " ").append(aNode.data).append(" ").
				append(toJavaString(0, aNode.right)).append((currIndent == 0) ? ")" : "");
		
		// var or int: return the data
		} else { 
			retVal.append(aNode.data);
		}
		return retVal.toString();
	}
	
}
