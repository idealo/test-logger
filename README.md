# test-logger

[![Build Status](https://travis-ci.org/idealo/logback-redis.svg?branch=master)](https://travis-ci.org/idealo/test-logger)
 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.idealo.test/test-logger/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.idealo.test/test-logger) 
 
 
 **Goal**
 
 This is a test utility to fine tune log settings in tests configurable for each test.
 
 include in your pom
 ```xml
 <dependency>
     <groupId>de.idealo.test</groupId>
     <artifactId>test-logger</artifactId>
     <version>v0.1</version>
     <scope>test</scope>
 </dependency>
 ```
 **Samples**
 
 ***Swallow all logs***
 ```java
 public class SwallowAllLogsTest {
 
     private final Logger log = LoggerFactory.getLogger("test");
 
     @Rule
     public TestRule testLogger = TestLoggerRuleFactory.silent();
 
     @Test
     public void test(){
         // this will swallowed
         log.error("ddd");
     }
 }
 ```

***Log only errors***
 ```java
 public class LogOnlyErrorsTest {
 
     private final Logger log = LoggerFactory.getLogger("test");
 
     @Rule
     public TestRule testLogger = TestLoggerRuleFactory.error();
 
     @Test
     public void test(){
         // this will swallowed
         log.warn("ddd");
         //this will be visible
         log.error("really important log message for test");
     }
 }
 ```
 
 ***Specific level per logger***
 ```java
 public class LogLevelPerLoggerTest {
 
     private final Logger log1 = LoggerFactory.getLogger("test1");
     private final Logger log2 = LoggerFactory.getLogger("test2");
 
     @Rule
     public TestRule testLogger = TestLoggerRuleFactory
             .withLevel("test1", Level.ERROR)
             .silence("test2")
             .build();
 
     @Test
     public void test(){
         //this will be visible
         log1.error("really important log message for test");
         // this will be swallowed
         log2.error("not important log message for test");
     }
 }
 ```
 
 All samples are located in [src/test/java/de/idealo/junit/rules/samples](tree/master/src/test/java/de/idealo/junit/rules/samples). Runnable samples.
