package org.laserforce.scaling.io;

import org.junit.Assert;
import org.junit.Test;

public class CsvLineTransformerTest {

	@Test
	public void convertLineFrom() {
		
		final CsvLineTransformer transformer = new CsvLineTransformer(0, 5);
		
        final String[] line1 = new String[] {"Rusty","12","8","6,377","1,175","8002","6522","5202","5962","6842","7402","4361","6721"};
		Assert.assertEquals("Rusty", transformer.getAthleteName(line1));
		Assert.assertArrayEquals(new Integer[] {8002,6522,5202,5962,6842,7402,4361,6721}, transformer.getScores(line1).toArray());
		
		final String[] line2 = new String[] {"Morpheus","4","1","2,782","#DIV/0!","2782","","","","",""};
		Assert.assertEquals("Morpheus", transformer.getAthleteName(line2));
		Assert.assertArrayEquals(new Integer[] {2782}, transformer.getScores(line2).toArray());
	}
}
