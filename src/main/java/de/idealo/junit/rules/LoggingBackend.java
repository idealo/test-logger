package de.idealo.junit.rules;

interface LoggingBackend {
    default void setLevelOfLoggername(String loggerName, org.slf4j.event.Level level) {
        throw new IllegalStateException("not implemented");
    }

    default void restoreLog() {
        throw new IllegalStateException("not implemented");
    }

    default void silenceLoggername(String loggerName) {
        throw new IllegalStateException("silencing a logger is not implemented");
    }
}
