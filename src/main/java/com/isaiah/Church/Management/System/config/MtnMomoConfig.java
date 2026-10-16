
package com.isaiah.Church.Management.System.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MtnMomoConfig {

    @Value("${mtn.momo.base-url}")
    private String baseUrl;

    @Value("${mtn.momo.subscription-key}")
    private String subscriptionKey;

    @Value("${mtn.momo.api-user}")
    private String apiUser;

    @Value("${mtn.momo.api-key}")
    private String apiKey;

    @Value("${mtn.momo.target-environment:sandbox}")
    private String targetEnvironment;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getSubscriptionKey() {
        return subscriptionKey;
    }

    public String getApiUser() {
        return apiUser;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getTargetEnvironment() {
        return targetEnvironment;
    }
}
