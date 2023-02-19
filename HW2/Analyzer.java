/**
 * Assignment #2: Data Structures
 * Assignment details: https://docs.google.com/document/d/1prm8GxSvuraXgHlyWnFG1nyi0kM62bwc2jZWIHY_IuU/
 * 
 * This class contains the methods used for conducting a simple sentiment analysis.
 * @author lcosta
 * Due Feb 09, 2023
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Analyzer {
	
	// Map for memoization of calculateSentenceScore
	private static Map<String, Double> results = new HashMap<>(); // key: sentence, value: score

	/**
	 * This method reads sentences from the input file, creates a Sentence object
	 * for each, and returns a Set of the Sentences.
	 * 
	 * @param filename Name of the input file to be read
	 * @return Set containing one Sentence object per sentence in the input file
	 */
	public static Set<Sentence> readFile(String filename) {
		if (filename == null)
			throw new IllegalArgumentException();

		Set<Sentence> set = new HashSet<>();
		Path file = Paths.get(filename);

		try (BufferedReader reader = Files.newBufferedReader(file)) {
			String line = null;

			while ((line = reader.readLine()) != null) {
				String[] splitLine = line.split(" ", 2);
				try {
					if (splitLine.length < 2 || splitLine[1].length() == 0)
						continue;
					int score = Integer.parseInt(splitLine[0]);
					if (score > 2 || score < -2)
						continue;
					String text = splitLine[1];
					Sentence newSentence = new Sentence(score, text);
					set.add(newSentence);
				} catch (NumberFormatException x) {
				} 
			}        
		} catch (IOException x) {
			throw new IllegalArgumentException();
		}
		return set;
	}

	/**
	 * This method calculates the weighted average for each word in all the Sentences.
	 * This method is case-insensitive and all words should be stored in the Map using
	 * only lowercase letters.
	 * 
	 * @param sentences Set containing Sentence objects with words to score
	 * @return Map of each word to its weighted average
	 */
	public static Map<String, Double> calculateWordScores(Set<Sentence> sentences) {
		Map<String, Double> map = new HashMap<String, Double>();

		if (sentences == null || sentences.size() == 0)
			return map;

		Map<String, Double> count = new HashMap<String, Double>();

		// iterate through the sentences in the set
		for (Sentence s : sentences) {
			if (s == null || s.getText().length() == 0)
				continue;

			String[] words = s.getText().toLowerCase().split(" ");
			Double score = s.getScore() * 1.0;

			// for each of the words in each sentence
			for (String word : words) {
				// exclude "words" that start with non-letters:
				if (!Character.isLetter(word.charAt(0)))
					continue;
				
				Double x = map.get(word);
				if (x == null) { // if a new word
					map.put(word, score);
					count.put(word, 1.0);
				}
				else { // if word is already in the map
					map.put(word, score + x);
					count.put(word, count.get(word) + 1);
				}
			}
		}

		// Divide each sum by each count to get an average:
		map.forEach((word, sum) -> map.put(word, sum / count.get(word)));
		return map;
	}
	
	/**
	 * This method determines the sentiment of the input sentence using the average of the
	 * scores of the individual words, as stored in the Map.
	 * This method is case-insensitive and all words in the input sentence should be
	 * converted to lowercase before searching for them in the Map.
	 * 
	 * @param wordScores Map of words to their weighted averages
	 * @param sentence Text for which the method calculates the sentiment
	 * @return Weighted average scores of all words in input sentence
	 */
	public static double calculateSentenceScore(Map<String, Double> wordScores, String sentence) {
		Double cached = results.get(sentence);
		if (cached == null) {
			double score = calculate(wordScores, sentence);
			results.put(sentence, score);
			return score;
		}
		else {			
			return cached;
		}
			
	}

	private static double calculate(Map<String, Double> wordScores, String sentence) {
		if (wordScores == null || wordScores.isEmpty() || sentence == null || sentence.length() == 0)
		return 0;
		
		String[] words = sentence.toLowerCase().split(" ");
		
		double count = 0;
		double sum = 0;

		for (String word : words) {
			if (!Character.isLetter(word.charAt(0)))
				continue;
			
			count++;
			Double score = wordScores.get(word);
			if (score != null)
				sum += score;
		}
		
		if (count == 0)
			return 0;

		return sum / count;
	}
	
	public static void main(String[] args) {
		//System.out.println(readFile(".txt"));
		//System.out.println(readFile(null));
		// System.out.println(calculateWordScores(null));

		if (args.length < 1) {
            System.out.println("no input file");
            return;
        }

		try {
			// Read name of input file as a runtime argument convert to Set of Sentences
			Set<Sentence> file = readFile(args[0]);

			Map<String, Double> wordScores = calculateWordScores(file);
			Scanner input = new Scanner(System.in);

			while (true) {
				// Prompt the user to enter a sentence
				System.out.print("Please input a sentence (or \"quit\"): ");
				String sentence = input.nextLine();
				
				// End loop if user wants to quit
				if (sentence.equals("quit"))
					break;
				
				// Pass the Map & sentence to calculateSentenceScore method, and print out the result
				System.out.println(calculateSentenceScore(wordScores, sentence));
			}
			input.close();

		}
		catch (IllegalArgumentException x) {
			//throw x;
			System.out.println("bad input file");
		}
		
	}

}
