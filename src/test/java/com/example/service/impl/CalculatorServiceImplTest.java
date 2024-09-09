package com.example.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceImplTest {

    @InjectMocks
    private CalculatorServiceImpl calculatorService;

    Double a;
    Double b;

    @BeforeEach
    public void init() {
        a = 3.6;
        b = 1.2;
    }

    @Test
    public void getAddition() {
        String expectedResult = String.format("%.3f", a + b);
        String actualResult = calculatorService.getAddition(a, b);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getSubtraction() {
        String expectedResult = String.format("%.3f", a - b);
        String actualResult = calculatorService.getSubtraction(a, b);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getMultiplication() {
        String expectedResult = String.format("%.3f", a * b);
        String actualResult = calculatorService.getMultiplication(a, b);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getDivision() {
        String expectedResult = String.format("%.3f", a / b);
        String actualResult = calculatorService.getDivision(a, b);

        Assertions.assertEquals(expectedResult, actualResult);
    }

}