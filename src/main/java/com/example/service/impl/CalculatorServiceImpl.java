package com.example.service.impl;

import com.example.service.CalculatorService;
import org.springframework.stereotype.Service;

@Service
public class CalculatorServiceImpl implements CalculatorService {

    @Override
    public String getAddition(double a, double b) {
        return String.format("%.3f", a + b);
    }

    @Override
    public String getSubtraction(double a, double b) {
        return String.format("%.3f", a - b);
    }

    @Override
    public String getMultiplication(double a, double b) {
        return String.format("%.3f", a * b);
    }

    @Override
    public String getDivision(double a, double b) {
        return String.format("%.3f", a / b);
    }

}
