package validatepin;

import base.BaseTestRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest extends BaseTestRule {
    @Test
    public void validPins() {
        assertEquals(true, Solution.validatePin("1234"));
        assertEquals(true, Solution.validatePin("0000"));
        assertEquals(true, Solution.validatePin("1111"));
        assertEquals(true, Solution.validatePin("123456"));
        assertEquals(true, Solution.validatePin("098765"));
        assertEquals(true, Solution.validatePin("000000"));
        assertEquals(true, Solution.validatePin("090909"));
    }

    @Test
    public void nonDigitCharacters() {
        assertEquals(false, Solution.validatePin(" a234"));
        assertEquals(false, Solution.validatePin(".234"));
        assertEquals(false, Solution.validatePin("utp4564utp"));
        assertEquals(false, Solution.validatePin("newline"));
    }

    @Test
    public void invalidLengths() {
        assertEquals(false, Solution.validatePin("1"));
        assertEquals(false, Solution.validatePin("12"));
        assertEquals(false, Solution.validatePin("123"));
        assertEquals(false, Solution.validatePin("12345"));
        assertEquals(false, Solution.validatePin("1234567"));
        assertEquals(false, Solution.validatePin(" 1234"));
        assertEquals(false, Solution.validatePin(" 1234 "));
        assertEquals(false, Solution.validatePin("1234 "));
        assertEquals(false, Solution.validatePin("1.234"));
        assertEquals(false, Solution.validatePin("00000000"));
        assertEquals(false, Solution.validatePin("00000"));
        assertEquals(false, Solution.validatePin("00000a"));
    }

}
