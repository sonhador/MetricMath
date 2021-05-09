import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CalcTest {
	private Map<String, Object> data = new HashMap<>();
	MetricMath metricMath;
	
	@Before
	public void prepareData() {
		data.put("m1", Arrays.asList(new Double[] {1.0,1.0,1.0}));
		data.put("m2", Arrays.asList(new Double[] {2.0,2.0,2.0}));
		data.put("m3", Arrays.asList(new Double[] {3.0,3.0,3.0}));
		data.put("m4", Arrays.asList(new Double[] {3.1,3.1,3.1}));
		data.put("m5", Arrays.asList(new Integer[] {1,1,0,0}));
		data.put("m6", Arrays.asList(new Integer[] {30,0,0,30}));
		data.put("m7", Arrays.asList(new Integer[] {0,0,20,20}));
		data.put("m8", Arrays.asList(new Integer[] {1,2,3,4}));
		data.put("m9", Arrays.asList(new Integer[] {3,3,2,1}));
		data.put("m10", Arrays.asList(new Integer[] {5,7,6,8}));
		
		Object[] tsArray = new Object[] {data.get("m8"), data.get("m9"), data.get("m10")};
		data.put("m8_9_10", tsArray);
		
		metricMath = new MetricMath(data);
	}
	
	@Test
	public void test01() {
		Object res = metricMath.execute("SUM([m1,m2])");
		assertEquals(3.0, (double)((List)res).get(0), 0);
		assertEquals(6.0, (double)((List)res).get(1), 0);
	}
	
	@Test
	public void test02() {
		Object res = metricMath.execute("AVG(m3)");
		assertEquals(3.0, (double)res, 0);
	}
	
	@Test
	public void test03() {
		Object res = metricMath.execute("SUM([m1,m2])/AVG(m3)");
		assertEquals(1.0, (double)((List)res).get(0), 0);
		assertEquals(2.0, (double)((List)res).get(1), 0);
	}
	
	@Test
	public void test04() {
		Object res = metricMath.execute("m1-m2");
		assertEquals(-1.0, (double)((List)res).get(0), 0);
		assertEquals(-1.0, (double)((List)res).get(1), 0);
		assertEquals(-1.0, (double)((List)res).get(2), 0);
	}
	
	@Test
	public void test05() {
		Object res = metricMath.execute("ABS(m1-m2)");
		assertEquals(1.0, (double)((List)res).get(0), 0);
		assertEquals(1.0, (double)((List)res).get(1), 0);
		assertEquals(1.0, (double)((List)res).get(2), 0);
	}
	
	@Test
	public void test06() {
		Object res = metricMath.execute("MIN(ABS(m1-m2))");
		assertEquals(1.0, (double)res, 0);
	}
	
	@Test
	public void test07() {
		Object res = metricMath.execute("CEIL(m4)");
		assertEquals(4.0, (double)((List)res).get(0), 0);
		assertEquals(4.0, (double)((List)res).get(1), 0);
		assertEquals(4.0, (double)((List)res).get(2), 0);
	}
	
	@Test
	public void test08() {
		Object res = metricMath.execute("SUM(CEIL(m4))");
		assertEquals(12.0, (double)res, 0);
	}
	
	@Test
	public void test09() {
		Object res = metricMath.execute("IF(m5,m6,m7)");
		assertEquals(30, (int)((List)res).get(0));
		assertEquals(0, (int)((List)res).get(1));
		assertEquals(20, (int)((List)res).get(2));
		assertEquals(20, (int)((List)res).get(3));
	}
	
	@Test
	public void test10() {
		Object res = metricMath.execute("IF(m5 <= 0,m6,m7)");
		assertEquals(0, (int)((List)res).get(0));
		assertEquals(0, (int)((List)res).get(1));
		assertEquals(0, (int)((List)res).get(2));
		assertEquals(30, (int)((List)res).get(3));
	}
	
	@Test
	public void test11() {
		Object res = metricMath.execute("SORT(m8_9_10, AVG, DESC)");
		assertEquals(5, ((List)((Object[])res)[0]).get(0));
		assertEquals(1, ((List)((Object[])res)[1]).get(0));
		assertEquals(3, ((List)((Object[])res)[2]).get(0));
	}
}
