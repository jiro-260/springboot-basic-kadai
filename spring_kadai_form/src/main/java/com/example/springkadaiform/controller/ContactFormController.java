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

    private static final String CONTACT_FORM = "contactForm";
	private final HttpSession session;

    public ContactFormController(HttpSession session) {
        this.session = session;
    }

    @GetMapping("/form")
    public String showForm(@ModelAttribute ContactForm contactForm, Model model) {
        // この時点でフラッシュスコープから自動的に取得されているはず
        return "contactFormView";
    }
    
    @PostMapping("/form")
    public String submitForm(@Valid @ModelAttribute(CONTACT_FORM) ContactForm contactForm,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // フラッシュにエラーと値を保存
            redirectAttributes.addFlashAttribute(CONTACT_FORM, contactForm);
            // BindingResultも必ず指定
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactForm", bindingResult);
            return "redirect:/form";
        }
        // 正常処理
        session.setAttribute("contactFormData", contactForm);
        return "redirect:/confirm";
    }

    @GetMapping("/confirm")
    public String showConfirm(@ModelAttribute(CONTACT_FORM) ContactForm contactForm, Model model) {
        ContactForm formFromSession = (ContactForm) session.getAttribute("contactFormData");
        if (formFromSession == null) {
            return "redirect:/form"; // セッションがなければフォームに戻る
        }
        model.addAttribute(CONTACT_FORM, formFromSession);
        return "confirmView";
    }

    // これを追加：最終的な「送信」処理
    @PostMapping("/submit")
    public String processSubmission(@ModelAttribute(CONTACT_FORM) ContactForm contactForm) {
        // 例：DBに保存、メール送信等の処理
        // ここではダミーとして、成功したら完了ページに遷移
        // 最終処理を行った後、セッションの破棄
        session.removeAttribute("contactFormData");
        return "thankYou"; // 完了ページ
    }
}