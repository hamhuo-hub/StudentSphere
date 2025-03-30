package narcissistic;

import base.BaseTestRule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SolutionTest extends BaseTestRule {
    @Test
    public void exampleTests() {
        assertTrue("153 is narcissistic", Solution.isNarcissistic(153));
        assertTrue("1634 is narcissistic", Solution.isNarcissistic(1634));
        assertFalse("112 is not narcissistic", Solution.isNarcissistic(112));
        assertTrue("0 is narcissistic", Solution.isNarcissistic(0));
        assertFalse("-1 is not acceptable", Solution.isNarcissistic(-1));
        assertTrue("7 is narcissistic", Solution.isNarcissistic(7));
        assertTrue("8 is narcissistic", Solution.isNarcissistic(8));
        assertTrue("9 is narcissistic", Solution.isNarcissistic(9));
        assertTrue("1634 is narcissistic", Solution.isNarcissistic(1634));
    }

}
