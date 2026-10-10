package com.credit.simulator.controller;

import com.credit.simulator.model.LoanCalculation;
import com.credit.simulator.repository.SheetRepository;
import com.credit.simulator.service.InstallmentCalculator;
import com.credit.simulator.validator.LoanValidator;
import com.credit.simulator.view.ConsoleView;

public class AppContext {
    public static final String DEFAULT_SHEET = "default";

    private final ConsoleView view;
    private final LoanValidator validator;
    private final InstallmentCalculator calculator;
    private final SheetRepository sheets;

    private String activeSheet = DEFAULT_SHEET;
    private LoanCalculation current;
    private boolean running = true;

    public AppContext(
        ConsoleView view, 
        LoanValidator validator, 
        InstallmentCalculator calculator,
        SheetRepository sheets) 
    {
        this.view = view;
        this.validator = validator;
        this.calculator = calculator;
        this.sheets = sheets;
    }

    public ConsoleView view() {
        return view;
    }

    public LoanValidator validator() {
        return validator;
    }

    public InstallmentCalculator calculator() {
        return calculator;
    }

    public SheetRepository sheets() {
        return sheets;
    }

    public String activeSheet() {
        return activeSheet;
    }

    public void setActiveSheet(String name) {
        this.activeSheet = name;
    }

    public LoanCalculation current() {
        return current;
    }

    public void setCurrent(LoanCalculation result) {
        this.current = result;
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }
}
