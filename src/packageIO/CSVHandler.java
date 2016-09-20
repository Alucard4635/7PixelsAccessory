package packageIO;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;



/**
 * @author Andrea Marisio<br>
 *this class read,write and parse a CVS
 */
public class CSVHandler implements Iterator<String[]>,Iterable<String[]>,Closeable {
	
	private BufferedReader reader;
	private Iterator<String> iteratorOfReader;
	private String regexDelimiter;
	public CSVHandler(File fileCSV,String regexDelimiter) throws FileNotFoundException {
		this.regexDelimiter = regexDelimiter;
		reader = new BufferedReader(new FileReader(fileCSV));
		iteratorOfReader = reader.lines().iterator();
	}
	public CSVHandler(BufferedReader bufferFileCSV,String regexDelimiter) throws FileNotFoundException {
		this.regexDelimiter = regexDelimiter;
		reader = bufferFileCSV;
		iteratorOfReader = bufferFileCSV.lines().iterator();
	}
	
	@Override
	public Iterator<String[]> iterator() {
		return this;
	}
	
	@Override
	public boolean hasNext() {
		return iteratorOfReader.hasNext();
	}

	@Override
	public String[] next() {
		return iteratorOfReader.next().split(regexDelimiter);
	}
	
	@Override
	public void close() throws IOException {
		if (reader.ready()==true) {
			reader.close();
		}
	}
	
	/**
	 * @param fileCSV to read
	 * @return list of row's values
	 * @throws IOException on reading
	 */
	public static List<String[]> readCSV(File fileCSV) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(fileCSV));
		return readAndCloseCSV(reader);
	}

	/**
	 * @param reader to read
	 * @return list of row's values
	 * @throws IOException on reading
	 */
	public static List<String[]> readAndCloseCSV(BufferedReader reader) throws IOException {
		LinkedList<String[]> listOfValues = new LinkedList<String[]>();
		Stream<String> lines = reader.lines();
		Iterator<String> iterator = lines.iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			listOfValues.add( string.split(","));
		}
		reader.close();
		return listOfValues;
	}
	
	/**
	 * @param fileCSV to read
	 * @return maxDouble value
	 * @throws IOException on reading
	 */
	public static double getMaxDoubleValue(File fileCSV) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileCSV));
		Stream<String> lines = reader.lines();
		Iterator<String> iterator = lines.iterator();
		double maxValueFound=0;
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			String[] split = string.split(",");
			for (int i = 0; i < split.length; i++) {
				double current = Double.parseDouble(split[i]);
				if (current>maxValueFound) {
					maxValueFound=current;
				}
			}
		}
		reader.close();
		return maxValueFound;
	}

	/**
	 * @param fileCSV to read
	 * @return a map of all integer values and strings
	 * @throws IOException on reading
	 */
	public static HashMap<String, Integer> getAllPossibleIntegerValues(File fileCSV) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileCSV));
		Stream<String> lines = reader.lines();
		Iterator<String> iterator = lines.iterator();
		HashMap<String, Integer> integers=new HashMap<String, Integer>();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			String[] split = string.split(",");
			for (int i = 0; i < split.length; i++) {
				Integer integer = integers.get(split[i]);
				if (integer==null) {
					integers.put(split[i], Integer.parseInt(split[i]));
				}
			}
		}
		reader.close();
		return integers;
	}
	
	/**write A Value Set on a buffer, skip any values indicated
	 * @param writer buffer to write
	 * @param valuesLessToSkip values to skip
	 * @param values to write
	 * @param startWithANewline start with a new line if true
	 * @throws IOException on writing
	 */
	public static void writeAValueSet(BufferedWriter writer, HashSet<String> valuesLessToSkip, 
			String[] values,boolean startWithANewline) throws IOException {
		boolean writedAValue = false;
		if (startWithANewline) {
			writer.write("\n");
		}
		for (int i = 0; i < values.length;i++ ) {
			if (valuesLessToSkip==null||!valuesLessToSkip.contains(values[i])) {
				if (writedAValue) {
					writer.write(",");
					writedAValue=false;
				}
				writedAValue = true;
				writer.write(values[i]);
			}
		}
		
	}

	/**return lines with one or more indicated values
	 * @param mainFiles files to read
	 * @param valuesToFind values to report the lines
	 * @return a list of index of lines, it starts from 0
	 * @throws IOException on reading
	 */
	public static ArrayList<Integer> getLinesWithThatValues( File[] mainFiles,HashSet<String>[] valuesToFind) throws IOException {
		ArrayList<Integer> linesToSkip=new ArrayList<Integer>();
		ArrayList<Integer> linesToSkipPartial;
		for (int i = 0; i < mainFiles.length; i++) {
			File fileCSV = mainFiles[i];
			HashSet<String> values = valuesToFind[i];
			linesToSkipPartial = getLinesWithThatValues(fileCSV, values);
			linesToSkip.addAll(linesToSkipPartial);
		}
		return linesToSkip;
	}

	/**return lines with one or more indicated values
	 * @param fileCSV files to read
	 * @param values values to report the lines
	 * @return a list of index of lines, it starts from 0
	 * @throws IOException on reading
	 */
	public static ArrayList<Integer> getLinesWithThatValues(File fileCSV, HashSet<String> values) throws IOException {
		ArrayList<Integer> linesToSkipPartial=new ArrayList<Integer>();
		List<String[]> content=CSVHandler.readCSV(fileCSV);
		Integer currentLine=0;
		for (String[] word : content) {
			for (int j = 0; j < word.length; j++) {
				if (values.contains(word[j])) {
					linesToSkipPartial.add(currentLine);
					break;
				}
			}
			currentLine++;
		}
		return linesToSkipPartial;
	}

	/**create a map with values mapped with integers
	 * @param bufferedReader buffer to map
	 * @return a map with values mapped with integers
	 * @throws IOException on reading
	 */
	public static HashMap<String, String> remapValues(BufferedReader bufferedReader) throws IOException {
		HashMap<String, String> values=new HashMap<String, String>();
		//List<String[]> readedCSV = readAndCloseCSV(bufferedReader);
		CSVHandler csvHandler = new CSVHandler(bufferedReader, ",");
		Integer counter=0;
		for (String[] strings : csvHandler) {
			for (int i = 0; i < strings.length; i++) {
				if (!values.containsKey(strings[i])) {
					String value = counter.toString();
					values.put(strings[i], value);
					counter++;
				}
			}
		}
		csvHandler.close();
		return values;
	}

	/**write a CSV files mapping with the given map , the new file is named {@code currentFile.getName()+"Remapped.csv"}
	 * @param oldToNewValueMap map for map values
	 * @param files files to remapped
	 * @throws IOException on writing
	 */
	public static void createARemappedCSV(HashMap<String, String> oldToNewValueMap, File... files) throws IOException {
		for (int i = 0; i < files.length; i++) {
			File currentFile = files[i];
			if (currentFile!=null) {
				BufferedWriter writer=new BufferedWriter(new FileWriter(new File(currentFile.getParentFile(),currentFile.getName()+"Remapped.csv" )));
				BufferedReader bufferedReader = new BufferedReader(new FileReader(currentFile));
				Stream<String> reader=bufferedReader.lines();
				boolean startWithANewline = false;
				for (Iterator<String> lineIterator = reader.iterator(); lineIterator.hasNext();) {
					String[] values = ( lineIterator.next()).split(",");
					writeARemappedValueSet(writer, oldToNewValueMap, values, ",", startWithANewline);
	
					startWithANewline=true;
				}
				writer.flush();
				writer.close();
				reader.close();
				bufferedReader.close();
			}
		}
	}
	/**write a line of a "csv" with the indicated delimiter
	 * @param writer where to write
	 * @param oldToNewValueMap how to map values, null -&gt; no mapping
	 * @param values what to write,or map
	 * @param startWithANewline true for start whit a newline
	 * @throws IOException on writing
	 */
	public static void writeARemappedValueSet(BufferedWriter writer,HashMap<String, String> oldToNewValueMap, 
			String[] values,String delimiter,boolean startWithANewline) throws IOException {
		boolean writedAValue = false;
		if (startWithANewline) {
			writer.write("\n");
		}
		for (int i = 0; i < values.length;i++ ) {
			String currentValue = values[i];
			String remappedValue = null;
			if (oldToNewValueMap!=null) {
				remappedValue=oldToNewValueMap.get(currentValue);
			}
			if (remappedValue!=null) {
				currentValue=remappedValue;
			}
			if (writedAValue) {
				writer.write(delimiter);
				writedAValue=false;
			}
			writedAValue = true;
			writer.write(currentValue);
		}
		writer.flush();
	}






	
	


}
