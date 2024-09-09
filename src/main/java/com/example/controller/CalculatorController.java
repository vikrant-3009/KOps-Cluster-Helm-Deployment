
package com.example.controller;

import com.example.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/calc")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @GetMapping("/addition")
    public ModelAndView getAddition(
            @RequestParam double a,
            @RequestParam double b,
            RedirectAttributes redirectAttributes) {

        String additionResponse = calculatorService.getAddition(a, b);
        redirectAttributes.addFlashAttribute("a", a);
        redirectAttributes.addFlashAttribute("b", b);
        redirectAttributes.addFlashAttribute("result", true);
        redirectAttributes.addFlashAttribute("operation", "Addition Response");
        redirectAttributes.addFlashAttribute("response", additionResponse);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/subtraction")
    public ModelAndView getSubtraction(
            @RequestParam double a,
            @RequestParam double b,
            RedirectAttributes redirectAttributes) {

        String subtractionResponse = calculatorService.getSubtraction(a, b);
        redirectAttributes.addFlashAttribute("a", a);
        redirectAttributes.addFlashAttribute("b", b);
        redirectAttributes.addFlashAttribute("result", true);
        redirectAttributes.addFlashAttribute("operation", "Subtraction Response");
        redirectAttributes.addFlashAttribute("response", subtractionResponse);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/multiplication")
    public ModelAndView getMultiplication(
            @RequestParam double a,
            @RequestParam double b,
            RedirectAttributes redirectAttributes) {

        String multiplicationResponse = calculatorService.getMultiplication(a, b);
        redirectAttributes.addFlashAttribute("a", a);
        redirectAttributes.addFlashAttribute("b", b);
        redirectAttributes.addFlashAttribute("result", true);
        redirectAttributes.addFlashAttribute("operation", "Multiplication Response");
        redirectAttributes.addFlashAttribute("response", multiplicationResponse);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/division")
    public ModelAndView getDivision(
            @RequestParam double a,
            @RequestParam double b,
            RedirectAttributes redirectAttributes) {

        String divisionResponse;

        if (b == 0) {
            divisionResponse = "Undefined";
            redirectAttributes.addFlashAttribute("error", true);
        }
        else {
            divisionResponse = calculatorService.getDivision(a, b);
        }
        redirectAttributes.addFlashAttribute("a", a);
        redirectAttributes.addFlashAttribute("b", b);
        redirectAttributes.addFlashAttribute("result", true);
        redirectAttributes.addFlashAttribute("operation", "Division Response");
        redirectAttributes.addFlashAttribute("response", divisionResponse);
        return new ModelAndView("redirect:/");
    }

}
