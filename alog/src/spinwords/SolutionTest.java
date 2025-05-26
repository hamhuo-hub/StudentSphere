package spinwords;

import base.BaseTestRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest extends BaseTestRule {
        @Test
        public void test() {
            assertEquals("emocleW", new Solution().spinWords("Welcome"));
            assertEquals("Hey wollef sroirraw", new Solution().spinWords("Hey fellow warriors"));
        }
}
