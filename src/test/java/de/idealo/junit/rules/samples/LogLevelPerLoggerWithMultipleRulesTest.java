package de.idealo.junit.rules.samples;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.idealo.junit.rules.TestLoggerRuleFactory;

import ch.qos.logback.classic.Level;

public class LogLevelPerLoggerWithMultipleRulesTest {

    private final Logger importantLog = LoggerFactory.getLogger("test1");
    private final Logger irrelevantLog = LoggerFactory.getLogger("test2");

    @Rule
    public TestRule testLoggerRule1 = TestLoggerRuleFactory.withLevel("test1", Level.ERROR).build();
    @Rule
    public TestRule testLoggerRule2 = TestLoggerRuleFactory.withLevel("test2", Level.OFF).build();

    @Test
    public void test(){
        //this will be visible
        importantLog.error("really important log message for test");
        // this will be swallowed
        irrelevantLog.error("not important log message for test");
    }
}