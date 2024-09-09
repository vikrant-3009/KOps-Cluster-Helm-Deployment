package com.example.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


import com.example.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CalculatorController.class)
public class CalculatorControllerTest {

    @MockBean
    private CalculatorService calculatorService;

    @Autowired
    private MockMvc mockMvc;

    Double a;
    Double b;

    @BeforeEach
    void init() {
        a = 3.6;
        b = 1.2;
    }

    @Test
    public void getAddition() {
        String expectedResponse = String.format("%.3f", a + b);

        Mockito.when(calculatorService.getAddition(
                Mockito.any(Double.class), Mockito.any(Double.class))
        ).thenReturn(expectedResponse);

        try {
            mockMvc.perform(get("/calc/addition")
                            .queryParam("a", String.valueOf(a))
                            .queryParam("b", String.valueOf(b)))
                    .andExpect(redirectedUrl("/"))
                    .andExpect(flash().attribute("a", a))
                    .andExpect(flash().attribute("b", b))
                    .andExpect(flash().attribute("result", true))
                    .andExpect(flash().attribute("operation", "Addition Response"))
                    .andExpect(flash().attribute("response", expectedResponse));
        }
        catch (Exception e) {
            e.printStackTrace();
//            throw new Exception("Error in executing test method getAddition.");
        }
    }

    @Test
    public void getSubtraction() {
        String expectedResponse = String.format("%.3f", a - b);

        Mockito.when(calculatorService.getSubtraction(
                Mockito.any(Double.class), Mockito.any(Double.class))
        ).thenReturn(expectedResponse);

        try {
            mockMvc.perform(get("/calc/subtraction")
                            .queryParam("a",String.valueOf(a))
                            .queryParam("b",String.valueOf(b)))
                    .andExpect(redirectedUrl("/"))
                    .andExpect(flash().attribute("a", a))
                    .andExpect(flash().attribute("b", b))
                    .andExpect(flash().attribute("result", true))
                    .andExpect(flash().attribute("operation", "Subtraction Response"))
                    .andExpect(flash().attribute("response", expectedResponse));
        }
        catch (Exception e) {
            e.printStackTrace();
//            throw new Exception("Error in executing test method getSubtraction.");
        }
    }

    @Test
    public void getMultiplication() {
        String expectedResponse = String.format("%.3f", a * b);

        Mockito.when(calculatorService.getMultiplication(
                Mockito.any(Double.class), Mockito.any(Double.class))
        ).thenReturn(expectedResponse);

        try {
            mockMvc.perform(get("/calc/multiplication")
                            .queryParam("a",String.valueOf(a))
                            .queryParam("b",String.valueOf(b)))
                    .andExpect(redirectedUrl("/"))
                    .andExpect(flash().attribute("a", a))
                    .andExpect(flash().attribute("b", b))
                    .andExpect(flash().attribute("result", true))
                    .andExpect(flash().attribute("operation", "Multiplication Response"))
                    .andExpect(flash().attribute("response", expectedResponse));
        }
        catch (Exception e) {
            e.printStackTrace();
//            throw new Exception("Error in executing test method getMultiplication.");
        }
    }

    @Test
    public void getDivision_NormalCase() {
        String expectedResponse = String.format("%.3f", a / b);

        Mockito.when(calculatorService.getDivision(
                Mockito.any(Double.class), Mockito.any(Double.class))
        ).thenReturn(expectedResponse);

        try {
            mockMvc.perform(get("/calc/division")
                            .queryParam("a",String.valueOf(a))
                            .queryParam("b",String.valueOf(b)))
                    .andExpect(redirectedUrl("/"))
                    .andExpect(flash().attribute("a", a))
                    .andExpect(flash().attribute("b", b))
                    .andExpect(flash().attribute("result", true))
                    .andExpect(flash().attribute("operation", "Division Response"))
                    .andExpect(flash().attribute("response", expectedResponse));
        }
        catch (Exception e) {
            e.printStackTrace();
//            throw new Exception("Error in executing test method getDivision.");
        }
    }

    @Test
    public void getDivision_DivideByZero() {
        b = 0.0;

        Mockito.when(calculatorService.getDivision(
                Mockito.any(Double.class), Mockito.any(Double.class))
        ).thenReturn("Undefined");

        try {
            mockMvc.perform(get("/calc/division")
                            .queryParam("a",String.valueOf(a))
                            .queryParam("b",String.valueOf(b)))
                    .andExpect(redirectedUrl("/"))
                    .andExpect(flash().attribute("a", a))
                    .andExpect(flash().attribute("b", b))
                    .andExpect(flash().attribute("error", true))
                    .andExpect(flash().attribute("result", true))
                    .andExpect(flash().attribute("operation", "Division Response"))
                    .andExpect(flash().attribute("response", "Undefined"));
        }
        catch (Exception e) {
            e.printStackTrace();
//            throw new Exception("Error in executing test method getDivision.");
        }
    }

}