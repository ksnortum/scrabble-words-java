package net.snortum.scrabblewords.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This immutable class retrieves a list of words from a Scrabble Dictionary
 * text file. The text file must be in the ClassPath with one word per line.
 * Words will be down-cased.
 * 
 * @author Knute Snortum
 * @version 2.1.0
 */
public class ScrabbleDictionary {
	private static final Logger LOG = LogManager.getLogger(ScrabbleDictionary.class);
	static final String INPUTDATA_NULL = "Input Data cannot be null";
	static final String DICTIONARY_NULL = "Dictionary name cannot be null";

	private static Map<DictionaryName, List<String>> words = new HashMap<>();

	private final InputData data;

	/**
	 * Create a new ScrabbleDictionary object. Uses data to get the
	 * {@link DictionaryName}.
	 * 
	 * @param data
	 *            the input data to use
	 * @throws IllegalArgumentException
	 *             if data or data.getDictionaryName() are null
	 */
	public ScrabbleDictionary(InputData data) {
		if (data == null) {
			throw new IllegalArgumentException(INPUTDATA_NULL);
		}

		this.data = new InputData(data);

		if (this.data.getDictionaryName() == null) {
			throw new IllegalArgumentException(DICTIONARY_NULL);
		}
	}

	/**
	 * Get a list of words from a text dictionary file. Cache words for multiple
	 * calls. ScrabbleDictionary file name comes from the {@link InputData}
	 * field entered at object creation. ScrabbleDictionary name must be in the
	 * class path.
	 * 
	 * @return list of words from a text dictionary (uses Map for faster access)
	 */
	public List<String> getValidWords() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Getting dictionary words");
		}

		// Was this dictionary loaded already?
		if (words.containsKey(data.getDictionaryName())) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Using pre-loaded dictionary");
			}
			return words.get(data.getDictionaryName());
		}

		String dictionaryFile = "/dicts/" + data.getDictionaryName().toString().toLowerCase() + ".txt";
		InputStream in = getClass().getResourceAsStream(dictionaryFile);
		List<String> validWords = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			validWords = br.lines()
					.map(word -> word.toLowerCase())
					.collect(Collectors.toList());
		} catch (IOException e) {
			LOG.error(e.toString());
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Size of valid word list: " + validWords.size());
		}
		
		words.put(data.getDictionaryName(), validWords);

		return validWords;
	}
}
