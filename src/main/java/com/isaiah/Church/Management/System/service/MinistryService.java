package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.Ministry;
import com.isaiah.Church.Management.System.repository.MinistryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MinistryService {

    private final MinistryRepository repository;

    public MinistryService(MinistryRepository repository) {
        this.repository = repository;
    }

    // CREATE / UPDATE
    public Ministry saveMinistry(Ministry ministry) {
        return repository.save(ministry);
    }

    // READ ALL
    public List<Ministry> getAllMinistries() {
        return repository.findAll();
    }

    // READ ONE
    public Ministry getMinistryById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // DELETE
    public void deleteMinistry(Integer id) {
        repository.deleteById(id);
    }
}