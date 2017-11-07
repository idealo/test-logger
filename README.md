# test-logger

[![Build Status](https://travis-ci.org/idealo/logback-redis.svg?branch=master)](https://travis-ci.org/idealo/test-logger)
 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.idealo.test/test-logger/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.idealo.test/test-logger) 
 
 
 *Goal*
 This is a test utility to swallow logs in tests configurable for each test.
 
 include in your pom
 ```xml
 <dependency>
     <groupId>de.idealo.test</groupId>
     <artifactId>test-logger</artifactId>
     <version>v0.1</version>
     <scope>test</scope>
 </dependency>
 ```
 
 simple test
 ```java
 public class SimpleTest {

    @Rule
    public TestRule testLogger = TestLoggerRule.silent(); 
    
    private final Logger log = LoggerFactory.getLogger("test");
 
    @Test
    public void test(){
       // this will swallowed
       log.error("ddd")
    }
 }
 ```
