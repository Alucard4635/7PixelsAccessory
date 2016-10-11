package analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import dataStructures.Good;
import dataStructures.NodeWordOfGoods;
import dataStructures.TypeOfGoods;
import packageIO.GoodsParser;
import preprocessing.GoodsProcesser;
import preprocessing.SpaceReplacer;
import preprocessing.TitleRemover;


public class Main {
private static final int LEARNING_ITERATIONS = 1;
	//	private static final double PERCENTUALE_DI_ADDESTRAMENTO = 0.5;
	private static final String goodDeimiter = " ";

	public static void main(String[] args) {
		//selezione dei file di training e test
		File[] files = new File[2];//accessories and product
		String[] dialogs={"Select Trainings set csv file","Select Test set csv file"};
		JFileChooser f = new JFileChooser();
		f.setCurrentDirectory(new File(System.getProperty("user.dir")));
		for (int i = 0; i < files.length; i++) {
			JOptionPane.showMessageDialog(null, dialogs[i]);
			int retVal = f.showOpenDialog(null);
			if (retVal != JFileChooser.APPROVE_OPTION ) {
				System.exit(0);
			} else {
				files[i] = f.getSelectedFile();
			}
		}
		
		// creazione dei passsi di preprocessing
		GoodsProcesser[] preprocessers=new GoodsProcesser[2];
		preprocessers[0]=new SpaceReplacer();
		preprocessers[1]=new TitleRemover();
		
		
		// parsing dei file con ,
		GoodsParser parserTraining = null;
		GoodsParser parserTest = null;
		try {
			parserTraining = new GoodsParser(files[0], ",");//files[0]=Training 
			parserTest = new GoodsParser(files[1], ",");//files[1]=test
		} catch (FileNotFoundException e) {
			System.out.println("File non trovato");
			System.exit(-1);
		}
		
		// caricamento in memoria dei dati di learning e test
		Collection<Good> learningSet = new LinkedList<Good>();
		Collection<Good> testSet = null;
		
		try {
			learningSet = parserTraining.readAndClose();
			System.out.println("Training "+files[0].getName());
			testSet = parserTest.readAndClose();
			System.out.println("Test "+files[1].getName());

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	
		/*try {
			LinkedList<Good> class0 = parserTest.readAndClose();
			LinkedList<Good> class1 = parserTraining.readAndClose();
			
			int j = (int) (class0.size()*PERCENTUALE_DI_ADDESTRAMENTO);
			for (int i = 0; i <= j; i++) {
				learningSet.add(class0.removeLast());
				if (!class1.isEmpty()) {
					learningSet.add(class1.removeLast());
				}
			}
			testSet = class1;
			testSet.addAll(class0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}*/
		
		GoodsClassificator goodsClassificator = new GoodsClassificator();
		Good[] arrayGoodLearning=new Good[learningSet.size()];
		learningSet.toArray(arrayGoodLearning);
		preprocess(preprocessers, arrayGoodLearning);
		
		Good[] arrayGoodTest=new Good[testSet.size()];
		testSet.toArray(arrayGoodTest);
		preprocess(preprocessers, arrayGoodTest);

		//preparazione file missclassificati
		File misclassified=new File("Misclassified.txt");
		BufferedWriter missWriter = null;
		// scrittura del significato delle features
		try {
			missWriter = new BufferedWriter(new FileWriter(misclassified));
			TypeOfGoods[] values = TypeOfGoods.values();
			String featuresClasses = "[";
			for (int i = 0; i < values.length; i++) {
				featuresClasses+=values[i]+",";
			}
			featuresClasses=featuresClasses.substring(0, featuresClasses.length()-1)+"]";
			missWriter.write(featuresClasses+"\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		double prodHit = 0;
		double accHit = 0;
		double accNum=0;
		
		for (int addest = 0; addest < LEARNING_ITERATIONS; addest++) {
			goodsClassificator.learnType(arrayGoodLearning, goodDeimiter, 1, 1, 1);
		
			double hit = 0;
			for (int i = 0; i < arrayGoodTest.length; i++) {
				Good current = arrayGoodTest[i];
				TypeOfGoods reconizeType = goodsClassificator.reconizeType(current, goodDeimiter);
				TypeOfGoods correctType = current.getType(goodDeimiter);
				if (reconizeType.equals(correctType)) {
					hit++;
					if (correctType.equals(TypeOfGoods.ACCESSORY)) {
						accHit++;
					}
					if (correctType.equals(TypeOfGoods.PRODUCT)) {
						prodHit++;
					}
				}else {
					writeMiss(goodsClassificator, missWriter, current, reconizeType, correctType);
				}
				if (correctType.equals(TypeOfGoods.ACCESSORY)) {
					accNum++;
				}
				
			}
			try {
				missWriter.flush();
				missWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Global "+hit/arrayGoodTest.length);
			System.out.println("Acc "+accHit/accNum);
			System.out.println("Prod "+prodHit/(arrayGoodTest.length-accNum));


		}

	}

	private static void writeMiss(GoodsClassificator goodsClassificator, BufferedWriter missWriter, Good current,
			TypeOfGoods reconizeType, TypeOfGoods correctType) {
		WordRanker ranker = goodsClassificator.getRanker();
		NodeWordOfGoods[] titleKeys = ranker.getAllKeywords(current.getTitle(), goodDeimiter);
		NodeWordOfGoods[] descKeys = ranker.getAllKeywords(current.getDescription(), goodDeimiter);
		try {
			missWriter.write(current.toString()+"\n");
			missWriter.write(reconizeType+" insteadOf "+correctType+"\n");
			missWriter.write("Title Keywords: ");
		for (int j = 0; j < titleKeys.length; j++) {
			missWriter.write(titleKeys[j].toString());
		}
		missWriter.write("\nDescription Keywords: ");
		for (int j = 0; j < descKeys.length; j++) {
			missWriter.write(descKeys[j].toString());
		}
		missWriter.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void preprocess(GoodsProcesser[] preprocessers, Good[] arrayGoodTest) {
		for (int i = 0; i < arrayGoodTest.length; i++) {
			for (int j = 0; j < preprocessers.length; j++) {
				preprocessers[j].processes(arrayGoodTest[i]);
			}
			
		}
	}
}
