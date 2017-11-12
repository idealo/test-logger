package de.idealo.junit.rules;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.slf4j.event.Level;

import lombok.SneakyThrows;

public class TestLoggerRuleFactoryTest {

    @Test
    public void configureWithClassAndLevel() {
        TestLoggerRuleFactory.TestLoggerBuilder builder = TestLoggerRuleFactory.withLevel(getClass(), Level.ERROR);

        assertThat(levelMap(builder)).containsEntry(getClass().getCanonicalName(), Level.ERROR);
    }

    @Test
    public void silenceByClass() {
        TestLoggerRuleFactory.TestLoggerBuilder builder = TestLoggerRuleFactory.silence(getClass());

        assertThat(silenced(builder)).contains(getClass().getCanonicalName());
    }

    @Test
    public void silenceByLogger() {
        TestLoggerRuleFactory.TestLoggerBuilder builder = TestLoggerRuleFactory.silence("test");

        assertThat(silenced(builder)).contains("test");
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private Map<String, Level> levelMap(TestLoggerRuleFactory.TestLoggerBuilder builder) {
        Field levelMap = builder.getClass()
                                .getDeclaredField("levelMap");
        levelMap.setAccessible(true);
        return (Map<String, Level>) levelMap.get(builder);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private Set<String> silenced(TestLoggerRuleFactory.TestLoggerBuilder builder) {
        Field silencedLogger = builder.getClass()
                                      .getDeclaredField("silencedLogger");
        silencedLogger.setAccessible(true);
        return (Set<String>) silencedLogger.get(builder);
    }
}