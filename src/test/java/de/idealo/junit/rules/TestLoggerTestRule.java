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
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().mute().enableLog();

    private LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    private CyclicBufferAppender<ILoggingEvent> appender;

    @Before
    public void setup() {
        appender = new CyclicBufferAppender<>();
        appender.setContext(loggerContext);
        appender.start();

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
        Logger         logger         = LoggerFactory.getLogger("test");
        TestLoggerRule testLoggerRule = new TestLoggerRule("test");

        testLoggerRule.silenceLog();
        logger.info("xxx");

        assertThat(appender.getLength()).isZero();
    }

    @Test
    public void shouldRestore() {
        Logger         logger         = LoggerFactory.getLogger("test");
        TestLoggerRule testLoggerRule = new TestLoggerRule("test");

        testLoggerRule.silenceLog();
        logger.error("xxx");
        testLoggerRule.after();
        logger.error("yyy");

        assertThat(appender.getLength()).isEqualTo(1);
        assertThat(appender.get(0).getMessage()).isEqualTo("yyy");
    }

    @Test
    public void shouldSetSpecificLevel() {
        Logger         logger         = LoggerFactory.getLogger("test");
        TestLoggerRule testLoggerRule = new TestLoggerRule("test");

        testLoggerRule.setLevel(Level.ERROR);
        logger.info("xxx");
        logger.error("yyy");

        assertThat(appender.getLength()).isEqualTo(1);
        assertThat(appender.get(0).getMessage()).isEqualTo("yyy");
    }
}