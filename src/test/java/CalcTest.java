import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CalcTest {
	private Map<String, List> data = new HashMap<>();
	MetricMath metricMath;
	
	@Before
	public void prepareData() {
		data.put("m1", Arrays.asList(new Double[] {1.0,1.0,1.0}));
		data.put("m2", Arrays.asList(new Double[] {2.0,2.0,2.0}));
		data.put("m3", Arrays.asList(new Double[] {3.0,3.0,3.0}));
		data.put("m4", Arrays.asList(new Double[] {3.1,3.1,3.1}));
		
		metricMath = new MetricMath(data);
	}
	
	@Test
	public void test1() {
		Object res = metricMath.execute("SUM([m1,m2])");
		assertEquals(3.0, (double)((List)res).get(0), 0);
		assertEquals(6.0, (double)((List)res).get(1), 0);
	}
	
	@Test
	public void test2() {
		Object res = metricMath.execute("AVG(m3)");
		assertEquals(3.0, (double)res, 0);
	}
	
	@Test
	public void test3() {
		Object res = metricMath.execute("SUM([m1,m2])/AVG(m3)");
		assertEquals(1.0, (double)((List)res).get(0), 0);
		assertEquals(2.0, (double)((List)res).get(1), 0);
	}
	
	@Test
	public void test4() {
		Object res = metricMath.execute("m1-m2");
		assertEquals(-1.0, (double)((List)res).get(0), 0);
		assertEquals(-1.0, (double)((List)res).get(1), 0);
		assertEquals(-1.0, (double)((List)res).get(2), 0);
	}
	
	@Test
	public void test5() {
		Object res = metricMath.execute("ABS(m1-m2)");
		assertEquals(1.0, (double)((List)res).get(0), 0);
		assertEquals(1.0, (double)((List)res).get(1), 0);
		assertEquals(1.0, (double)((List)res).get(2), 0);
	}
	
	@Test
	public void test6() {
		Object res = metricMath.execute("MIN(ABS(m1-m2))");
		assertEquals(1.0, (double)res, 0);
	}
	
	@Test
	public void test7() {
		Object res = metricMath.execute("CEIL(m4)");
		assertEquals(4.0, (double)((List)res).get(0), 0);
		assertEquals(4.0, (double)((List)res).get(1), 0);
		assertEquals(4.0, (double)((List)res).get(2), 0);
	}
	
	@Test
	public void test8() {
		Object res = metricMath.execute("SUM(CEIL(m4))");
		assertEquals(12.0, (double)res, 0);
	}
}
