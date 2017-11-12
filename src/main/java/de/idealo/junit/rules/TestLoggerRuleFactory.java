package de.idealo.junit.rules;

import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.classic.Level;

public class TestLoggerRuleFactory {

    public static TestLoggerRule silent() {
        TestLoggerRule testLoggerRule = new TestLoggerRule();
        testLoggerRule.silenceLog();
        return testLoggerRule;
    }

    public static TestLoggerRule error() {
        return withLevel(Level.ERROR);
    }

    public static TestLoggerRule withLevel(Level level) {
        TestLoggerRule testLoggerRule = new TestLoggerRule();
        testLoggerRule.setLevel(level);
        return testLoggerRule;
    }

    public static TestLoggerBuilder withLevel(String logger, Level level) {
        return new DefaultBuilder().withLevel(logger, level);
    }

    public interface TestLoggerBuilder {
        TestLoggerBuilder withLevel(String logger, Level level);
        TestLoggerRule build();
    }

    private static class DefaultBuilder implements TestLoggerBuilder {

        private Map<String, Level> levelMap = new HashMap<>();

        @Override
        public TestLoggerBuilder withLevel(String logger, Level level) {
            levelMap.put(logger, level);
            return this;
        }

        @Override
        public TestLoggerRule build() {
            TestLoggerRule testLoggerRule = new TestLoggerRule();
            levelMap.forEach(testLoggerRule::setLevel);
            return testLoggerRule;
        }
    }
}
