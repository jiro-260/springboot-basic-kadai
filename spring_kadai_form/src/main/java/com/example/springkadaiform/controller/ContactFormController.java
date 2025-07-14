package com.example.springkadaiform.controller;

import jakarta.servlet.http.HttpSession;
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

    private final HttpSession session;

    public ContactFormController(HttpSession session) {
        this.session = session;
    }

    @GetMapping("/form")
    public String showForm(@ModelAttribute("contactForm") ContactForm contactForm) {
        return "contactFormView"; // 元のフォーム表示
    }

    @PostMapping("/form")
    public String submitForm(@Valid @ModelAttribute("contactForm") ContactForm contactForm,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "contactFormView"; // 入力エラーでフォームに戻る
        }
        // バリデーションOK → セッションに保存して確認画面にリダイレクト
        session.setAttribute("contactFormData", contactForm);
        return "redirect:/confirm";
    }

    @GetMapping("/confirm")
    public String showConfirm(@ModelAttribute("contactForm") ContactForm contactForm, Model model) {
        ContactForm formFromSession = (ContactForm) session.getAttribute("contactFormData");
        if (formFromSession == null) {
            return "redirect:/form"; // セッションがなければフォームに戻る
        }
        model.addAttribute("contactForm", formFromSession);
        return "confirmView";
    }

    // これを追加：最終的な「送信」処理
    @PostMapping("/submit")
    public String processSubmission(@ModelAttribute("contactForm") ContactForm contactForm) {
        // 例：DBに保存、メール送信等の処理
        // ここではダミーとして、成功したら完了ページに遷移
        // 最終処理を行った後、セッションの破棄
        session.removeAttribute("contactFormData");
        return "thankYou"; // 完了ページ
    }
}