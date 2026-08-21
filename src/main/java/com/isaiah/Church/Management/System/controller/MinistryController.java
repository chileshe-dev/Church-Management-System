package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Ministry;
import com.isaiah.Church.Management.System.service.MinistryService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ministries")
@PreAuthorize("hasRole('ADMIN')")
public class MinistryController {

    private final MinistryService service;

    public MinistryController(MinistryService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public Ministry addMinistry(@RequestBody Ministry ministry) {
        return service.saveMinistry(ministry);
    }

    // READ ALL
    @GetMapping
    public List<Ministry> getAllMinistries() {
        return service.getAllMinistries();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Ministry getMinistry(@PathVariable Integer id) {
        return service.getMinistryById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Ministry updateMinistry(
            @PathVariable Integer id,
            @RequestBody Ministry updatedMinistry) {

        Ministry ministry = service.getMinistryById(id);

        if (ministry != null) {
            ministry.setMinistryName(updatedMinistry.getMinistryName());
            ministry.setDescription(updatedMinistry.getDescription());
            ministry.setLeaderName(updatedMinistry.getLeaderName());

            return service.saveMinistry(ministry);
        }

        return null;
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteMinistry(@PathVariable Integer id) {
        service.deleteMinistry(id);
    }
}