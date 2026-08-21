package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Contribution;
import com.isaiah.Church.Management.System.service.ContributionService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contributions")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN','TREASURER')")
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

    @GetMapping("/{id}")
    public Contribution getContribution(@PathVariable Integer id) {
        return service.getContributionById(id);
    }

    @PutMapping("/{id}")
    public Contribution updateContribution(
            @PathVariable Integer id,
            @RequestBody Contribution contribution) {

        return service.updateContribution(id, contribution);
    }

    @DeleteMapping("/{id}")
    public void deleteContribution(@PathVariable Integer id) {
        service.deleteContribution(id);
    }

}