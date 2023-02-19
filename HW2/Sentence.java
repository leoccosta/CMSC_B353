/** 
 * This class represents a single sentence from the input file.
 * Assignment details: https://docs.google.com/document/d/1prm8GxSvuraXgHlyWnFG1nyi0kM62bwc2jZWIHY_IuU/
 *
 * Added to by: lcosta
 */

public class Sentence  {
	
	/**
	 * The sentiment score for the sentence. Should be in the range [-2, 2]
	 */
	private int score;
	
	/**
	 * The text contained in the sentence. 
	 */
	private String text;
	
	public Sentence(int score, String text) {
		this.score = score;
		this.text = text;
	}
	
	public int getScore() {
		return score;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj instanceof Sentence == false) return false;
		Sentence other = (Sentence) obj;
		return text.equals(other.text) && score == other.score;
	}

	@Override
	public int hashCode() {
		return text.hashCode() + score;
	}

	public String toString() {
		return "\n(" + this.score + ", " + this.text + ")";
	}
	
}
