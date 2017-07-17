package de.idealo.junit.rules;

import java.util.HashMap;
import java.util.Map;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class TestLoggerRule extends ExternalResource {
    private static final String ROOT = "ROOT";

    private final String loggerName;
    private final Map<String, Integer> logger2LoglevelMap = new HashMap<>();

    public TestLoggerRule() {
        this(ROOT);
    }

    public TestLoggerRule(String loggername) {
        this.loggerName = loggername;
    }

    public TestLoggerRule(Class clazz) {
        this(clazz.getCanonicalName());
    }

    @Override
    protected void after() {
        restoreLog();
    }

    public static TestLoggerRule silent() {
        TestLoggerRule testLoggerRule = new TestLoggerRule();
        testLoggerRule.silenceLog();
        return testLoggerRule;
    }

    public void setLevel(Level level) {

        if (!logger2LoglevelMap.isEmpty()) {
            throw new IllegalStateException("this is an usage error, this method is called twice");
        }

        setLevelOfLoggername(level, loggerName);
    }

    private void setLevelOfLoggername(Level level, String loggerName) {
        Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        if (logger instanceof ch.qos.logback.classic.Logger) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

            loggerContext.getLoggerList()
                    .forEach(log -> {
                        String name = log.getName();
                        if (name.equals(loggerName) || loggerName.equals(Logger.ROOT_LOGGER_NAME)) {
                            logger2LoglevelMap.put(name, log.getEffectiveLevel().levelInt);
                            log.setLevel(level);
                        }
                    });

        } else {
            throw new IllegalStateException("could not handle logger '" + logger + "'");
        }
    }

    public void silenceLog() {
        setLevel(Level.OFF);
    }

    private void restoreLog() {

        Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        if (logger instanceof ch.qos.logback.classic.Logger) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

            logger2LoglevelMap.forEach((name, value) -> {
                Level oldLevel = Level.toLevel(value);
                loggerContext.getLogger(name).setLevel(oldLevel);
            });
            logger2LoglevelMap.clear();
        } else {
            throw new IllegalStateException("could not handle logger '" + logger + "'");
        }
    }

    public void silenceLog(Class clazz) {
        setLevelOfLoggername(Level.OFF, clazz.getCanonicalName());
    }
}
