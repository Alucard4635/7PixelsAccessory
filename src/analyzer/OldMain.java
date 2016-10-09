package analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import dataStructures.Good;
import dataStructures.TypeOfGoods;
import packageIO.GoodsParser;
import preprocessing.GoodsProcesser;
import preprocessing.SpaceReplacer;
import preprocessing.TitleRemover;


public class OldMain {
	private static final double PERCENTUALE_DI_ADDESTRAMENTO = 0.5;
	private static final String goodDeimiter = " ";

	public static void main(String[] args) {
		File[] files = new File[2];//accessories and product
		JFileChooser f = new JFileChooser();
		f.setCurrentDirectory(new File(System.getProperty("user.dir")));
		for (int i = 0; i < files.length; i++) {
			JOptionPane.showMessageDialog(null, "Select csv file");
			int retVal = f.showOpenDialog(null);
			if (retVal != JFileChooser.APPROVE_OPTION ) {
				System.exit(0);
			} else {
				files[i] = f.getSelectedFile();
			}
		}
		
		GoodsProcesser[] preprocessers=new GoodsProcesser[2];
		preprocessers[0]=new SpaceReplacer();
		preprocessers[1]=new TitleRemover();
		
		GoodsParser goods0 = null;
		GoodsParser goods1 = null;
		try {
			goods0 = new GoodsParser(files[0], ",");
			goods1 = new GoodsParser(files[1], ",");
		} catch (FileNotFoundException e) {
			System.out.println("File non trovato");
			System.exit(0);
		}
		Collection<Good> goods = null;
		Collection<Good> learningSet = new LinkedList<Good>();

		try {
			LinkedList<Good> class0 = goods0.readAndClose();
			LinkedList<Good> class1 = goods1.readAndClose();
			int j = (int) (class0.size()*PERCENTUALE_DI_ADDESTRAMENTO);
			for (int i = 0; i < j; i++) {
				learningSet.add(class0.removeLast());
				if (!class1.isEmpty()) {
					learningSet.add(class1.removeLast());
				}
			}
			goods = class1;
			goods.addAll(class0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		GoodsClassificator goodsClassificator = new GoodsClassificator();
		
		Good[] arrayGoodLearning=new Good[learningSet.size()];
		learningSet.toArray(arrayGoodLearning);
		preprocess(preprocessers, arrayGoodLearning);
		
		Good[] arrayGoodTest=new Good[goods.size()];
		goods.toArray(arrayGoodTest);
		preprocess(preprocessers, arrayGoodTest);
		
//		int descEmpty = 0;
//		for (int i = 0; i < arrayGoodLearning.length; i++) {
//			if (arrayGoodLearning[i].getDescription().length()<1) {
//				descEmpty++;
//			}
//		}
//		System.out.println(descEmpty);
		double prodHit = 0;
		double accHit = 0;
		int accNum=0;
		for (int addest = 0; addest < 1; addest++) {
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
				}
				if (correctType.equals(TypeOfGoods.ACCESSORY)) {
					accNum++;
				}
				
			}
			System.out.println("Global "+hit/arrayGoodTest.length);
			System.out.println("Acc "+accHit/accNum);
			System.out.println("Prod "+prodHit/(arrayGoodTest.length-accNum));


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
