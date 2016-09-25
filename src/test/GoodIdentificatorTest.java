package test;

import org.junit.Test;

import analyzer.*;
import dataStructures.Good;

import org.junit.Assert.*;

public class GoodIdentificatorTest {
	
	@Test
	public void identify() {
		GoodTypeIdentificator identificator = null;
		Good good = null;//=new Good(merch, titl, desc, price);
		identificator.reconizeType(good, " ");
	}
}
