package org.parad0x.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AopAnnotatedAspectTest {

    @Autowired
    AopAnnotatedInterface aopAnnotated;

    @Test
    public void testRetryAmount() {
        // Given
        Exception actual = null;
        // When
        try {
            aopAnnotated.retryMethod("max attempts");
        } catch (Exception e) {
            actual = e;
        }
        // Then
        assertEquals("Expected 2nd exception message", "Runtime Exception 2", actual.getMessage());
    }

    @Test
    public void testExcludedRetry() {
        // Given
        Exception actual = null;
        // When
        try {
            aopAnnotated.retryMethod("illegal arg");
        } catch (Exception e) {
            actual = e;
        }
        // Then
        assertEquals("Expected no retries", "Exception 1", actual.getMessage());
    }

    @Configuration
    @EnableRetry
    public static class RetryConfig {

        @Bean
        public AopAnnotatedInterface aopAnnotated() {
            AopAnnotated mockBean = Mockito.mock(AopAnnotated.class);
            when(mockBean.retryMethod(eq("max attempts"))).thenThrow(new RuntimeException("Runtime Exception 1"))
                    .thenThrow(new RuntimeException("Runtime Exception 2"))
                    .thenThrow(new RuntimeException("Runtime Exception 3"));
            when(mockBean.retryMethod(eq("illegal arg"))).thenThrow(new IllegalArgumentException("Exception 1"))
                    .thenThrow(new RuntimeException("Runtime Exception 2"));
            return mockBean;
        }
    }
}
