package com.credit.simulator.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.credit.simulator.model.LoanRequest;

public class InMemorySheetRepository implements SheetRepository {
    private final Map<String, LoanRequest> sheets = new LinkedHashMap<>();

    @Override
    public void save(String name, LoanRequest request) {
        sheets.put(name, request);
    }

    @Override
    public Optional<LoanRequest> find(String name) {
        return Optional.ofNullable(sheets.get(name));
    }

    @Override
    public List<String> names() {
        return new ArrayList<>(sheets.keySet());
    }
}
