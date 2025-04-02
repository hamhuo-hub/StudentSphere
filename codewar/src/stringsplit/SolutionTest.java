package stringsplit;

import base.BaseTestRule;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SolutionTest extends BaseTestRule {
    @Test
    public void testEvenString() {
        String s = "abcdef";
        String s1 = "HelloWorld";
        assertArrayEquals(new String[]{"ab", "cd", "ef"}, Solution.solution(s));
        assertArrayEquals(new String[]{"He", "ll", "oW", "or", "ld"}, Solution.solution(s1));
    }

    @Test
    public void testOddString() {
        String s = "abcde";
        String s1 = "LovePizza";
        assertArrayEquals(new String[]{"ab", "cd", "e_"}, Solution.solution(s));
        assertArrayEquals(new String[]{"Lo", "ve", "Pi", "zz", "a_"}, Solution.solution(s1));
    }
}
