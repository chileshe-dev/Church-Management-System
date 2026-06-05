package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.Contribution;
import com.isaiah.Church.Management.System.repository.ContributionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContributionService {

    private final ContributionRepository repository;

    public ContributionService(ContributionRepository repository) {
        this.repository = repository;
    }

    public Contribution saveContribution(Contribution contribution) {
        return repository.save(contribution);
    }

    public List<Contribution> getAllContributions() {
        return repository.findAll();
    }

    public Contribution getContributionById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteContribution(Integer id) {
        repository.deleteById(id);
    }
}