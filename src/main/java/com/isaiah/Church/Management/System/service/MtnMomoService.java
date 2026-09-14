package com.isaiah.Church.Management.System.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class MtnMomoService {

    private final RestTemplate restTemplate;

    @Value("${mtn.momo.base-url}")
    private String baseUrl;

    @Value("${mtn.momo.subscription-key}")
    private String subscriptionKey;

    @Value("${mtn.momo.api-user}")
    private String apiUser;

    @Value("${mtn.momo.api-key}")
    private String apiKey;

    public MtnMomoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    /**
     * Get an OAuth access token from MTN.
     */
    public String getAccessToken() {

        String url = baseUrl + "/collection/token/";

       
        // Create Basic Authentication value
        String credentials =
                apiUser + ":" + apiKey;

        String encodedCredentials =
                Base64.getEncoder()
                        .encodeToString(
                                credentials.getBytes()
                        );

        // Headers
        HttpHeaders headers =
                new HttpHeaders();

        headers.set(
                "Authorization",
                "Basic " + encodedCredentials
        );

        headers.set(
                "Ocp-Apim-Subscription-Key",
                subscriptionKey
        );

        headers.setContentType(
                MediaType.APPLICATION_FORM_URLENCODED
        );

        // Request body
        MultiValueMap<String, String> body =
                new LinkedMultiValueMap<>();

        body.add(
                "grant_type",
                "client_credentials"
        );

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(
                        body,
                        headers
                );

        // Send request
        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        Map.class
                );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    "Failed to obtain MTN access token."
            );
        }

        Map<String, Object> responseBody =
                response.getBody();

        if (responseBody == null ||
                responseBody.get("access_token") == null) {

            throw new RuntimeException(
                    "MTN did not return an access token."
            );
        }

        return responseBody
                .get("access_token")
                .toString();
    }
}