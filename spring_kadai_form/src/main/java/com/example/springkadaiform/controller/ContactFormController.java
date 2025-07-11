package com.example.springkadaiform.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.springkadaiform.form.ContactForm;

@Controller
public class ContactFormController {

    @GetMapping("/form")
    public String showForm(@ModelAttribute("contactForm") ContactForm contactForm) {
        return "contactFormView";
    }

    @PostMapping("/form")
    public String submitForm(@Valid @ModelAttribute("contactForm") ContactForm contactForm,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "contactFormView"; // 入力エラーある場合はフォームに戻る
        }
        // バリデーションOKの場合、確認画面へ
        model.addAttribute("contactForm", contactForm);
        return "confirmView";
    }
}