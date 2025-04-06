package droot;

import base.BaseTestRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest extends BaseTestRule {
    @Test
    public void Test1() {
        assertEquals( "Nope!" , 7, Solution.digital_root(16));
    }
    @Test
    public void Test2() {
        assertEquals( "Nope!" , 6, Solution.digital_root(456));
        assertEquals( "Nope!" , 1, Solution.digital_root(29386));
    }
}
