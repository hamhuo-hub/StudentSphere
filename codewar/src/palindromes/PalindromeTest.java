package palindromes;

import base.BaseTestRule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PalindromeTest extends BaseTestRule {

    @Test
    public void test1() {
        assertTrue(Palindromes.isPalindrome(121L));
    }

    @Test
    public void test2() {
        assertTrue(Palindromes.isPalindrome(123454321L));
    }

    @Test
    public void test3() {
        assertTrue(Palindromes.isPalindrome(98765432123456789L));
    }

    @Test
    public void test4() {
        assertFalse(Palindromes.isPalindrome(9876543212345678L));
    }

    @Test
    public void test5() {
        assertFalse(Palindromes.isPalindrome(0));
    }
}
