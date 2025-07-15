package com.example.springkadaiform.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springkadaiform.form.ContactForm;

@Controller
public class ContactFormController {

    private final HttpSession session;

    public ContactFormController(HttpSession session) {
        this.session = session;
    }

    @GetMapping("/form")
    public String showForm(@ModelAttribute("contactForm") ContactForm contactForm) {
        // 既にFlashスコープから`contactForm`とエラー情報が自動的に取得されている
        return "contactFormView"; 
    }
    
    @PostMapping("/form")
    public String submitForm(@Valid @ModelAttribute("contactForm") ContactForm contactForm,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // エラーと入力値をフラッシュ属性として渡す
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactForm", bindingResult);
            redirectAttributes.addFlashAttribute("contactForm", contactForm);
            return "redirect:/form";  // リダイレクト
        }
        // OKの場合の処理
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