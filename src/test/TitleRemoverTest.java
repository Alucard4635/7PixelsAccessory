package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import preprocessing.TitleRemover;

public class TitleRemoverTest {
	
	private static TitleRemover tR;
	
	@BeforeClass
	public static void intialize(){
		tR=new TitleRemover();
	}
	
	@Test
	public void process() {
		dataStructures.Good good = new dataStructures.Good(null, "a b c d", "a b c d e f", 0f);
		tR.processes(good);
		assertFalse(good.getDescription().contains(good.getTitle()));
		
		good = new dataStructures.Good(null, "a b c d", "a bc d e f", 0f);
		tR.processes(good);
		assertFalse(good.getDescription().contains(good.getTitle()));
	}
}
