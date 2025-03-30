package palindromes;

import base.BaseTestRule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SolutionTest extends BaseTestRule {

    @Test
    public void test1() {
        assertTrue(Solution.isPalindrome(121L));
    }

    @Test
    public void test2() {
        assertTrue(Solution.isPalindrome(123454321L));
    }

    @Test
    public void test3() {
        assertTrue(Solution.isPalindrome(98765432123456789L));
    }

    @Test
    public void test4() {
        assertFalse(Solution.isPalindrome(9876543212345678L));
    }

    @Test
    public void test5() {
        assertFalse(Solution.isPalindrome(0));
    }
}
