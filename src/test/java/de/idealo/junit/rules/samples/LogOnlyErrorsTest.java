package de.idealo.junit.rules.samples;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.idealo.junit.rules.TestLoggerRuleFactory;

public class LogOnlyErrorsTest {

    private final Logger log = LoggerFactory.getLogger("test");

    @Rule
    public TestRule testLogger = TestLoggerRuleFactory.error();

    @Test
    public void test(){
        // this will swallowed
        log.warn("ddd");
        //this will be visible
        log.error("really important log message for test");
    }
}