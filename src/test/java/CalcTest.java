import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sun.tools.javac.code.Attribute.Array;

public class CalcTest {
	@Test
	public void test() {
		System.out.println(Array.class.getSimpleName());
		ArrayList<Integer> ts = new ArrayList<>();
		ArrayList []tsArray = new ArrayList[] {ts};
		List<List<Integer>> tsList = new ArrayList<>();
		
		System.out.println(tsArray.getClass().getSimpleName());
		System.out.println(tsList.getClass().getSimpleName());
	}
}
