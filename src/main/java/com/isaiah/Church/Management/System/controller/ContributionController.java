package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Contribution;
import com.isaiah.Church.Management.System.service.ContributionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contributions")
public class ContributionController {

    private final ContributionService service;

    public ContributionController(ContributionService service) {
        this.service = service;
    }

    @PostMapping
    public Contribution addContribution(@RequestBody Contribution contribution) {
        return service.saveContribution(contribution);
    }

    @GetMapping
    public List<Contribution> getAllContributions() {
        return service.getAllContributions();
    }
}