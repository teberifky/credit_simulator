package com.credit.simulator.exception;

public class InputEndedException extends RuntimeException {
    public InputEndedException() {
        super("Input habis");
    }
}
