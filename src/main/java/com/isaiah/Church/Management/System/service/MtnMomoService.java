
package com.isaiah.Church.Management.System.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaiah.Church.Management.System.config.MtnMomoConfig;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class MtnMomoService {

    private final MtnMomoConfig config;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MtnMomoService(
            MtnMomoConfig config,
            ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    public String generateAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(
                config.getApiUser(),
                config.getApiKey());
        headers.set(
                "Ocp-Apim-Subscription-Key",
                config.getSubscriptionKey());

        HttpEntity<Void> request =
                new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response =
                restTemplate.exchange(
                        config.getBaseUrl()
                                + "/collection/token/",
                        HttpMethod.POST,
                        request,
                        JsonNode.class);

        JsonNode body = response.getBody();

        if (body == null ||
                body.get("access_token") == null) {
            throw new IllegalStateException(
                    "MTN did not return an access token.");
        }

        return body.get("access_token").asText();
    }

    public PaymentRequestResult requestPayment(
            String amount,
            String phoneNumber,
            String externalId,
            String referenceId) {

        String token = generateAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set(
                "Ocp-Apim-Subscription-Key",
                config.getSubscriptionKey());
        headers.set(
                "X-Target-Environment",
                config.getTargetEnvironment());
        headers.set("X-Reference-Id", referenceId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payer = Map.of(
                "partyIdType", "MSISDN",
                "partyId", phoneNumber);

        Map<String, Object> body = Map.of(
                "amount", amount,
                "currency", "EUR",
                "externalId", externalId,
                "payer", payer,
                "payerMessage", "Church contribution test",
                "payeeNote", "Church contribution test");

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Void> response =
                restTemplate.exchange(
                        config.getBaseUrl()
                                + "/collection/v1_0/requesttopay",
                        HttpMethod.POST,
                        request,
                        Void.class);

        return new PaymentRequestResult(
                response.getStatusCode().value(),
                referenceId);
    }

    public JsonNode getPaymentStatus(String referenceId) {
        String token = generateAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set(
                "Ocp-Apim-Subscription-Key",
                config.getSubscriptionKey());
        headers.set(
                "X-Target-Environment",
                config.getTargetEnvironment());

        HttpEntity<Void> request =
                new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response =
                restTemplate.exchange(
                        config.getBaseUrl()
                                + "/collection/v1_0/requesttopay/"
                                + referenceId,
                        HttpMethod.GET,
                        request,
                        JsonNode.class);

        return response.getBody();
    }

    public String generateReferenceId() {
        return UUID.randomUUID().toString();
    }

    public static class PaymentRequestResult {
        private final int httpStatus;
        private final String referenceId;

        public PaymentRequestResult(
                int httpStatus,
                String referenceId) {
            this.httpStatus = httpStatus;
            this.referenceId = referenceId;
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        public String getReferenceId() {
            return referenceId;
        }
    }
}
