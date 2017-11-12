package de.idealo.junit.rules;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.CyclicBufferAppender;

public class TestLoggerRuleTest {
    private static final String LOGGER_NAME = "test";
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().mute()
                                                                  .enableLog();

    private LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    private CyclicBufferAppender<ILoggingEvent> appender;

    private Logger testLogger;


    TestLoggerRule testLoggerRule = new TestLoggerRule();

    @Before
    public void setup() throws Throwable {
        appender = new CyclicBufferAppender<>();
        appender.setContext(loggerContext);
        appender.start();

        testLogger = LoggerFactory.getLogger(LOGGER_NAME);
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger("ROOT");
        logger.addAppender(appender);
        logger.setLevel(ch.qos.logback.classic.Level.INFO);

        testLoggerRule.before();
    }

    @After
    public void cleanup() {
        appender.stop();
    }

    @Test
    public void shouldSilence() {
        testLoggerRule.silenceLoggername(LOGGER_NAME);

        testLogger.info("xxx");

        assertThat(appender.getLength()).isZero();
    }

    @Test
    public void shouldRestore() {
        testLoggerRule.silenceLoggername(LOGGER_NAME);

        testLogger.error("xxx");
        testLoggerRule.after();
        testLogger.error("yyy");

        assertThat(appender.getLength()).isEqualTo(1);
        assertThat(appender.get(0)
                           .getMessage()).isEqualTo("yyy");
    }

    @Test
    public void shouldSetSpecificLevel() {
        testLoggerRule.setLevelOfLoggername(LOGGER_NAME, Level.ERROR);

        testLogger.info("xxx");
        testLogger.error("yyy");

        assertThat(appender.getLength()).isEqualTo(1);
        assertThat(appender.get(0)
                           .getMessage()).isEqualTo("yyy");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRaiseErrorWhenInvoke_After_Directly() {
        new TestLoggerRule().after();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRaiseErrorWhenInvoke_silenceLoggerName_Directly() {
        new TestLoggerRule().silenceLoggername("test");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRaiseErrorWhenInvoke_setLevelOfLoggername_Directly() {
        new TestLoggerRule().setLevelOfLoggername("test", Level.ERROR);
    }
}