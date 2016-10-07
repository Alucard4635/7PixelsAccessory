package packageIO;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import dataStructures.Good;
import dataStructures.TypeOfGoods;

public class GoodsParser {
	private CSVHandler goodsReader;
	
	public GoodsParser(File fileToRead, String csvDelimiter) throws FileNotFoundException {
		goodsReader=new CSVHandler(fileToRead, csvDelimiter);
	}
	
	public Good read() throws IOException{
		String[] line = null;
		if (goodsReader.hasNext()) {
			line = goodsReader.next();
		}
		return parseGood(line);
	}
	
	public LinkedList<Good> readAndClose() throws IOException{
		Good toAdd;
		LinkedList<Good> result=new LinkedList<Good>();
		while (goodsReader.hasNext()) {
			toAdd = parseGood(goodsReader.next());
			result.add(toAdd);
		}
		return result;
	}

	public Good parseGood(String[] line) throws IOException {
		String price = line[3];
		Good good = new Good(line[0], line[1], line[2], Float.parseFloat(price));
		if (line.length>4) {
			if (line[4].contains("accessories")) {
				good.setType(TypeOfGoods.ACCESSORY);
			}else {
				good.setType(TypeOfGoods.PRODUCT);
			}
		}
		return good;
	
	}
	
}
