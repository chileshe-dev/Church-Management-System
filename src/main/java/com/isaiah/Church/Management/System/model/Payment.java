package com.isaiah.Church.Management.System.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    private Double amount;

    private String method;

    private String status;

    @OneToOne
    @JoinColumn(name = "contribution_id", unique = true)
    private Contribution contribution;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "transaction_reference", unique = true)
private String transactionReference;

@Column(name = "provider")
private String provider;

@Column(name = "provider_reference")
private String providerReference;

@Column(name = "phone_number")
private String phoneNumber;

@Column(name = "updated_at")
private LocalDateTime updatedAt;


    public Payment() {
    }


    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Contribution getContribution() {
        return contribution;
    }

    public void setContribution(Contribution contribution) {
        this.contribution = contribution;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

        public String getTransactionReference() {
        return transactionReference;
    }

        public void setTransactionReference(String transactionReference) {
            this.transactionReference = transactionReference;
        }

            public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getProviderReference() {
            return providerReference;
        }

        public void setProviderReference(String providerReference) {
            this.providerReference = providerReference;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }   
  }