import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class FindNextSquareTest {
    private static final Logger logger = Logger.getLogger("");

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        logger.info(String.format("Test %s %s, spent %d microseconds",
                testName, status, TimeUnit.NANOSECONDS.toMicros(nanos)));
    }

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void succeeded(long nanos, Description description) {
            logInfo(description, "succeeded", nanos);
        }

        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            logInfo(description, "failed", nanos);
        }

        @Override
        protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
            logInfo(description, "skipped", nanos);
        }

        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, "finished", nanos);
        }
    };



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
