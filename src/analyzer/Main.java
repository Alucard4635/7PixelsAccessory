package analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import dataStructures.Good;
import dataStructures.TypeOfGoods;
import packageIO.GoodsParser;
import preprocessing.GoodsProcesser;
import preprocessing.SpaceReplacer;
import preprocessing.TitleRemover;


public class Main {
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
		try {
			goods = goods0.readAndClose();
			goods.addAll(goods1.readAndClose());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		GoodsClassificator goodsClassificator = new GoodsClassificator();
		Good[] arrayGood=new Good[goods.size()];
		goods.toArray(arrayGood);
		for (int i = 0; i < arrayGood.length; i++) {
			for (int j = 0; j < preprocessers.length; j++) {
				preprocessers[j].processes(arrayGood[i]);
			}
			
		}
		
		String goodDeimiter = " ";
		goodsClassificator.learnType(arrayGood, goodDeimiter, 1, 1, 1);

		
		double hit = 0;
		for (int i = 0; i < arrayGood.length; i++) {
			Good current = arrayGood[i];
			TypeOfGoods reconizeType = goodsClassificator.reconizeType(current, goodDeimiter);
			if (reconizeType.equals(current.getType(goodDeimiter))) {
				hit++;
			}
		}
		System.out.println(hit/arrayGood.length);

	}
}
