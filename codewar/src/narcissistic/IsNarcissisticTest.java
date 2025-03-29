package narcissistic;

import base.BaseTestRule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsNarcissisticTest extends BaseTestRule {
    @Test
    public void exampleTests() {
        assertTrue("153 is narcissistic", IsNarcissistic.isNarcissistic(153));
        assertTrue("1634 is narcissistic", IsNarcissistic.isNarcissistic(1634));
        assertFalse("112 is not narcissistic", IsNarcissistic.isNarcissistic(112));
        assertTrue("0 is narcissistic", IsNarcissistic.isNarcissistic(0));
        assertFalse("-1 is not acceptable", IsNarcissistic.isNarcissistic(-1));
        assertTrue("7 is narcissistic", IsNarcissistic.isNarcissistic(7));
        assertTrue("8 is narcissistic", IsNarcissistic.isNarcissistic(8));
        assertTrue("9 is narcissistic", IsNarcissistic.isNarcissistic(9));
        assertTrue("1634 is narcissistic", IsNarcissistic.isNarcissistic(1634));
    }

}
