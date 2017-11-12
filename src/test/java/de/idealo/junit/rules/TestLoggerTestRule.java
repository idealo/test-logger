package de.idealo.junit.rules;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.CyclicBufferAppender;

public class TestLoggerTestRule {
    private static final String LOGGER_NAME = "test";
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().mute().enableLog();

    private LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    private CyclicBufferAppender<ILoggingEvent> appender;

    private Logger testLogger;

    @Before
    public void setup() {
        appender = new CyclicBufferAppender<>();
        appender.setContext(loggerContext);
        appender.start();

        testLogger = LoggerFactory.getLogger(LOGGER_NAME);
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger("ROOT");
        logger.addAppender(appender);
        logger.setLevel(Level.INFO);
    }

    @After
    public void cleanup() {
        appender.stop();
    }

    @Test
    public void shouldSilence() {
        TestLoggerRule testLoggerRule = new TestLoggerRule(LOGGER_NAME);

        testLoggerRule.silenceLog();
        testLogger.info("xxx");

        assertThat(appender.getLength()).isZero();
    }

    @Test
    public void shouldRestore() {
        TestLoggerRule testLoggerRule = new TestLoggerRule(LOGGER_NAME);

        testLoggerRule.silenceLog();
        testLogger.error("xxx");
        testLoggerRule.after();
        testLogger.error("yyy");

        assertThat(appender.getLength()).isEqualTo(1);
        assertThat(appender.get(0).getMessage()).isEqualTo("yyy");
    }

    @Test
    public void shouldSetSpecificLevel() {
        TestLoggerRule testLoggerRule = new TestLoggerRule(LOGGER_NAME);

        testLoggerRule.setLevel(Level.ERROR);
        testLogger.info("xxx");
        testLogger.error("yyy");

        assertThat(appender.getLength()).isEqualTo(1);
        assertThat(appender.get(0).getMessage()).isEqualTo("yyy");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotSetLevelTwice() {
        TestLoggerRule testLoggerRule = new TestLoggerRule(LOGGER_NAME);
        testLoggerRule.setLevel(Level.ERROR);
        testLoggerRule.setLevel(Level.WARN);
    }
}