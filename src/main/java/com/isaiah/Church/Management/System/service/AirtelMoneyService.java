package com.isaiah.Church.Management.System.service;

import org.springframework.stereotype.Service;

@Service
public class AirtelMoneyService {

    /*
     * This service will handle communication
     * with the Airtel Money API.
     *
     * For now, it is only a placeholder because
     * the Airtel Client ID and Client Secret are
     * still pending approval.
     */

    public String initiatePayment(
            String phoneNumber,
            Double amount,
            String transactionReference) {

        /*
         * Airtel API integration will be implemented here
         * after the Airtel application is approved.
         */

        return "TEST_PAYMENT_REQUEST_CREATED";
    }
}