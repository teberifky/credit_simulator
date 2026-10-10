package com.credit.simulator.repository;

import java.util.List;
import java.util.Optional;

import com.credit.simulator.model.LoanRequest;

public interface SheetRepository {
    void save(String name, LoanRequest request);

    Optional<LoanRequest> find(String name);

    List<String> names();
}
