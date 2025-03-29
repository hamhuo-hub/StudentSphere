package numberfun;

import base.BaseTestRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberFunTest extends BaseTestRule {

    @Test
    public void test1() {
        assertEquals(144, NumberFun.findNextSquare(121));
    }


    @Test
    public void test2() {
        assertEquals(-1, NumberFun.findNextSquare(155));
    }

    @Test
    public void test3() {
        assertEquals(320356, NumberFun.findNextSquare(319225));
    }

    @Test
    public void test4() {
        assertEquals(15241630849L, NumberFun.findNextSquare(15241383936L));
    }

    @Test
    public void test5() {
        assertEquals(-1, NumberFun.findNextSquare(342786627));
    }


    @Test
    public void randomTest1() {
        long input = (long)(Math.random()*100000L)+1;
        long square = input*input;

        assertEquals(square+(input*2+1), NumberFun.findNextSquare(square));
    }


}
