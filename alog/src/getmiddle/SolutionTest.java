package getmiddle;

import base.BaseTestRule;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class SolutionTest extends BaseTestRule {
    @Test
    public void basicTests() {
        assertEquals("es", Solution.getMiddle("test"));
        assertEquals("dd", Solution.getMiddle("middle"));
        assertEquals("t", Solution.getMiddle("testing"));
        assertEquals("A", Solution.getMiddle("A"));
    }

    private String middle(String word) {
        int mid = word.length() / 2;
        return (word.length() % 2 == 1 ? word.substring(mid, mid+1) : word.substring(mid-1, mid+1));
    }

    @Test
    public void randTests() {
        Random randGen = new Random();
        String alph = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < 50; i++) {
            int randLength = randGen.nextInt(1000)+1;
            String testWord = "";
            for (int e = 0; e < randLength; e++) {
                int alphI = randGen.nextInt(alph.length());
                testWord += alph.substring(alphI, alphI+1);
            }
            String expected = middle(testWord);
            String actual = Solution.getMiddle(testWord);
            assertEquals("Middle of " + testWord + " is " + expected + " not " + actual, expected, actual);
        }
    }
}
