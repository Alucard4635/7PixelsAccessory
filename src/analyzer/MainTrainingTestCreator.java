package analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MainTrainingTestCreator {
	private static final double SPLIT_PERCENTAGE = 0.5;

	public static void main(String[] args) {
		File[] files = null;
		JFileChooser f = new JFileChooser();
		f.setMultiSelectionEnabled(true);
		f.setCurrentDirectory(new File(System.getProperty("user.dir")));
		JOptionPane.showMessageDialog(null, "Select files");
		int retVal = f.showOpenDialog(null);
		if (retVal != JFileChooser.APPROVE_OPTION ) {
			System.exit(0);
		} else {
			files = f.getSelectedFiles();
			if (files==null) {
				new Exception("nessun File selezionato").printStackTrace();
				System.exit(-1);
			}
		}
		BufferedReader csv = null;
		BufferedWriter fileTrainig = null;
		BufferedWriter fileTest = null;
		String name="";
		for (int i = 0; i < files.length; i++) {
			name += files[i].getName().split("[.]")[0];
		}
		try {
			fileTrainig = new BufferedWriter(new FileWriter(new File("TrainingSet"+name+SPLIT_PERCENTAGE+".txt")));
			fileTest = new BufferedWriter(new FileWriter(new File("TestSet"+name+SPLIT_PERCENTAGE+".txt")));

		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		for (int i = 0; i < files.length; i++) {
			try {
				csv=new BufferedReader(new FileReader(files[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			Stream<String> lines = csv.lines();
			Iterator<String> lineIterator = lines.iterator();
			double trainingRecordNumber=1;
			double testRecordNumber=1;
			while (lineIterator.hasNext()) {
				String currentRecord = (String) lineIterator.next();
				try {
					if (Math.random()<=testRecordNumber/trainingRecordNumber*SPLIT_PERCENTAGE) {
						fileTrainig.write(currentRecord+"\n");
						trainingRecordNumber++;
					}else {
						fileTest.write(currentRecord+"\n");
						testRecordNumber++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		try {
			fileTrainig.flush();
			fileTrainig.close();
			fileTest.flush();
			fileTest.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("SplitComplited");
	}

}
