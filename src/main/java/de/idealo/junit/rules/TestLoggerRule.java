package de.idealo.junit.rules;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.Setter;

public class TestLoggerRule extends ExternalResource {
    private LoggingBackend loggingBackend = new LoggingBackend() {
    };

    @Setter(AccessLevel.PACKAGE)
    private Runnable rules = () -> {
    };

    @Override
    protected void before() throws Throwable {
        Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        if (logger instanceof ch.qos.logback.classic.Logger) {
            loggingBackend = new LogbackBackend();
        } else {
            throw new IllegalStateException("could not handle logger '" + logger + "'");
        }

        rules.run();
    }

    @Override
    protected void after() {
        loggingBackend.restoreLog();
    }

    public void setLevelOfLoggername(String loggerName, org.slf4j.event.Level level) {
        loggingBackend.setLevelOfLoggername(loggerName, level);
    }

    public void silenceLoggername(String loggerName) {
        loggingBackend.silenceLoggername(loggerName);
    }
}
