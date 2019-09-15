package datamigrationGeneric;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVFileHandler {
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';

	public static String getStringWithoutQuotes(String stringWithQuotes) {
		if(stringWithQuotes.length()==0)
			return  stringWithQuotes;

		if (stringWithQuotes.charAt(0) == '"' && stringWithQuotes.charAt(stringWithQuotes.length() - 1) == '"')
			return stringWithQuotes.substring(1, stringWithQuotes.length() - 1);
		return stringWithQuotes;
	}
	
	public ArrayList<List<String>> getDataFromCSVFile(String csvFilename) {
		
		ArrayList<List<String>> existingData = new ArrayList<List<String>>();

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(csvFilename));
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while (scanner.hasNext()) {
			List<String> line = parseLine(scanner.nextLine());
			existingData.add(line);
		}
		
		scanner.close();
		
		return existingData;
	}

	public  List<String> parseLine(String cvsLine) {
		return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	public  List<String> parseLine(String cvsLine, char separators) {
		return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	}

	public  List<String> parseLine(String cvsLine, char separators, char customQuote) {
		List<String> result = new ArrayList<>();

		if (cvsLine == null || cvsLine.isEmpty()) {
			return result;
		}

		if (customQuote == ' ') {
			customQuote = DEFAULT_QUOTE;
		}

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;

		char[] chars = cvsLine.toCharArray();

		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;
				if (ch == customQuote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
					curVal.append(ch);
				} else {

					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}
				}
			} else {
				if (ch == customQuote) {

					inQuotes = true;

					if (chars[0] != '"' && customQuote == '\"') {
						curVal.append('"');
					}

					if (startCollectChar) {
						curVal.append('"');
					}
				} else if (ch == separators) {

					result.add(getStringWithoutQuotes(curVal.toString()));

					curVal = new StringBuffer();
					startCollectChar = false;
				} else if (ch == '\r') {
					continue;
				} else if (ch == '\n') {
					break;
				} else {
					curVal.append(ch);
				}
			}

		}

		result.add(getStringWithoutQuotes(curVal.toString()));

		return result;
	}

}
