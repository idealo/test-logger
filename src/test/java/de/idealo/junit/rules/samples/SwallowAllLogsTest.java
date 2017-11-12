package de.idealo.junit.rules.samples;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.idealo.junit.rules.TestLoggerRuleFactory;

public class SwallowAllLogsTest {

    private final Logger log = LoggerFactory.getLogger("test");

    @Rule
    public TestRule testLogger = TestLoggerRuleFactory.silent();

    @Test
    public void test(){
        // this will swallowed
        log.error("ddd");
    }
}