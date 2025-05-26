package pangramchecker;

import base.BaseTestRule;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SolutionTest extends BaseTestRule {
    Solution pc = null;

    @Before
    public void setUp() {
        pc = new Solution();
    }

    private void doTest(String sentence, boolean expected) {
        boolean actual = pc.check(sentence);
        String msg = "Incorrect answer for sentence = \"" + sentence + "\"\n";
        assertEquals(msg, expected, actual);
    }

    @Test
    public void test1() {
        String pangram1 = "The quick brown fox jumps over the lazy dog.";
        doTest(pangram1, true);
    }
    @Test
    public void test2() {
        String pangram2 = "You shall not pass!";
        doTest(pangram2, false);
    }
    @Test
    public void test3() {
        String pangram1 = "Cwm fjord bank glyphs vext quiz";
        doTest(pangram1, true);
    }
    @Test
    public void test4() {
        String pangram2 = "Pack my box with five dozen liquor jugs.";
        doTest(pangram2, true);
    }
    @Test
    public void test5() {
        String pangram3 = "How quickly daft jumping zebras vex.";
        doTest(pangram3, true);
    }
    @Test
    public void test6() {
        String pangram4 = "ABCD45EFGH,IJK,LMNOPQR56STUVW3XYZ";
        doTest(pangram4, true);
    }
    @Test
    public void test7() {
        String pangram5 = "abcdefghijklmopqrstuvwxyz";
        doTest(pangram5, false);
    }
    @Test
    public void test8() {
        String pangram6 = "A pangram is a sentence that contains every single letter of the alphabet at least once.";
        doTest(pangram6, false);
    }

    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private char[] shuffle(char[] a) {
        for (int i = 0; i < a.length; i++) {
            int j = (int)(Math.random() * a.length);
            char t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
        return a;
    }
    @Test
    public void randomizedTests() {
        for (int trial = 1; trial <= 50; trial++) {
            int wordBreaks = (int)(Math.random() * 8 + 1);
            char[] arr = new char[wordBreaks];
            Arrays.fill(arr, ' ');
            String spaces = new String(arr), sentence = "", missing = "";
            switch ( (int)(Math.random() * 3) ) {
                case 0: // non-pangram
                    //System.out.println("non-pangram");
                    arr = Arrays.copyOf(shuffle(alphabet.toCharArray()), alphabet.length());
                    int removals = (int)(Math.random() * 15 + 1);
                    sentence = new String(arr, 0, 26 - removals);
                    missing = new String(arr, sentence.length(), removals);
                    break;
                case 1: // exact pangram
                    //System.out.println("exact pangram");
                    sentence = alphabet;
                    break;
                case 2: // pangram w/extras
                    //System.out.println("pangram w/extras");
                    char[] extras = new char[(int)(Math.random() * 15) + 1];
                    for (int i = 0; i < extras.length; i++)
                        extras[i] = alphabet.charAt((int)(Math.random() * 26));
                    sentence = alphabet.concat(new String(extras));
                    break;
            }
            sentence = (new String(shuffle(sentence.concat(spaces).toCharArray()))).replaceAll("\\s\\s*"," ").trim();
            doTest(sentence, 0 == missing.length());
        }
    }
}
