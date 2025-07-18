package com.example.springkadaiform.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.Conventions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public String form(Model model) {

        // すでにインスタンスが存在する場合は行わない
        if (!model.containsAttribute("contactForm")) {
            // ビューにフォームクラスのインスタンスを渡す
            model.addAttribute("contactForm", new ContactForm());
        }

        // お問い合わせフォームを表示
        return "contactFormView";
    }

@PostMapping("/confirm")
    public String contact(Model model , RedirectAttributes redirectAttributes,
            @Validated ContactForm form, BindingResult result) { // 引数にModel を追加

        // バリデーションエラーがあったら終了
        if (result.hasErrors()) {

            // フォームクラスをビューに受け渡す
            redirectAttributes.addFlashAttribute("contactForm", form);

            // バリデーション結果をビューに受け渡す　// 15章を参照のことhttps://terakoya.sejuku.net/programs/176/chapters/2435
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX
                    + Conventions.getVariableName(form), result);

            // formにリダイレクト
            return "redirect:/form";
        }

  // 確認画面に情報を受け渡すために必要　12章をもう一度読んでください　https://terakoya.sejuku.net/programs/176/chapters/2432
        model.addAttribute("contactForm", form);
        // confirmにリダイレクトして確認画面
        return "confirmView";
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
