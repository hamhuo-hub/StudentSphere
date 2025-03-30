package numberfun;

import base.BaseTestRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest extends BaseTestRule {

    @Test
    public void test1() {
        assertEquals(144, Solution.findNextSquare(121));
    }


    @Test
    public void test2() {
        assertEquals(-1, Solution.findNextSquare(155));
    }

    @Test
    public void test3() {
        assertEquals(320356, Solution.findNextSquare(319225));
    }

    @Test
    public void test4() {
        assertEquals(15241630849L, Solution.findNextSquare(15241383936L));
    }

    @Test
    public void test5() {
        assertEquals(-1, Solution.findNextSquare(342786627));
    }


    @Test
    public void randomTest1() {
        long input = (long)(Math.random()*100000L)+1;
        long square = input*input;

        assertEquals(square+(input*2+1), Solution.findNextSquare(square));
    }


}
