package de.idealo.junit.rules;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class LogbackBackend implements LoggingBackend {
    private static final Map<org.slf4j.event.Level, Level> MAPPING = new HashMap<>();

    static {
        MAPPING.put(org.slf4j.event.Level.ERROR, Level.ERROR);
        MAPPING.put(org.slf4j.event.Level.WARN, Level.WARN);
        MAPPING.put(org.slf4j.event.Level.INFO, Level.INFO);
        MAPPING.put(org.slf4j.event.Level.DEBUG, Level.DEBUG);
        MAPPING.put(org.slf4j.event.Level.TRACE, Level.TRACE);
    }

    private final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    private final Map<String, Level> logger2LoglevelMap = new HashMap<>();

    @Override
    public void setLevelOfLoggername(String loggerName, org.slf4j.event.Level level) {
        Level intendedLevel = MAPPING.get(level);
        setLevelOfLoggerName(loggerName, intendedLevel);
    }

    private void setLevelOfLoggerName(String loggerName, Level intendedLevel) {
        loggerContext.getLoggerList()
                     .forEach(logger -> {
                         String name = logger.getName();
                         if (name.equals(loggerName) || loggerName.equals(Logger.ROOT_LOGGER_NAME)) {
                             preserveLogLevel(logger, name);
                             logger.setLevel(intendedLevel);
                         }
                     });
    }

    private void preserveLogLevel(ch.qos.logback.classic.Logger log, String name) {
        logger2LoglevelMap.put(name, log.getEffectiveLevel());
    }

    @Override
    public void restoreLog() {
        logger2LoglevelMap.forEach((loggerName, preservedLogLevel) -> {
            loggerContext.getLogger(loggerName)
                         .setLevel(preservedLogLevel);
        });
        logger2LoglevelMap.clear();
    }

    @Override
    public void silenceLoggername(String loggerName) {
        setLevelOfLoggerName(loggerName, Level.OFF);
    }
}
