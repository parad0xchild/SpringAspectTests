package org.parad0x.examples;

import org.springframework.retry.annotation.Retryable;

/**
 * Hello world!
 *
 */
public class AopAnnotated implements AopAnnotatedInterface {

    @Retryable(maxAttempts = 2)
    public String retryMethod() {
        return "";
    }
}
