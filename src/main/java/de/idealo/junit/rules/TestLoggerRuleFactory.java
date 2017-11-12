package de.idealo.junit.rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.event.Level;

public class TestLoggerRuleFactory {

    public static TestLoggerRule silent() {
        return silence(Logger.ROOT_LOGGER_NAME).build();
    }

    public static TestLoggerRule error() {
        return withLevel(Level.ERROR).build();
    }

    public static TestLoggerBuilder withLevel(Level level) {
        return new DefaultBuilder().withLevel(Logger.ROOT_LOGGER_NAME, level);
    }

    public static TestLoggerBuilder withLevel(String logger, Level level) {
        return new DefaultBuilder().withLevel(logger, level);
    }

    public static TestLoggerBuilder withLevel(Class clazz, Level level) {
        return TestLoggerRuleFactory.withLevel(clazz.getCanonicalName(), level);
    }

    public static TestLoggerBuilder silence(String logger) {
        return new DefaultBuilder().silence(logger);
    }

    public static TestLoggerBuilder silence(Class clazz) {
        return TestLoggerRuleFactory.silence(clazz.getCanonicalName());
    }

    public interface TestLoggerBuilder {
        TestLoggerBuilder withLevel(String logger, Level level);

        TestLoggerBuilder silence(String logger);

        TestLoggerRule build();
    }

    private static class DefaultBuilder implements TestLoggerBuilder {
        private Map<String, Level> levelMap = new HashMap<>();
        private Set<String> silencedLogger = new HashSet<>();

        @Override
        public TestLoggerBuilder withLevel(String logger, Level level) {
            levelMap.put(logger, level);
            return this;
        }

        @Override
        public TestLoggerBuilder silence(String logger) {
            silencedLogger.add(logger);
            return this;
        }

        @Override
        public TestLoggerRule build() {
            TestLoggerRule testLoggerRule = new TestLoggerRule();
            Runnable rules = () -> {
                levelMap.forEach(testLoggerRule::setLevelOfLoggername);
                silencedLogger.forEach(testLoggerRule::silenceLoggername);
            };
            testLoggerRule.setRules(rules);
            return testLoggerRule;
        }
    }
}
