package com.example.contact_list_web.controlller;

import com.example.contact_list_web.Contact;
import com.example.contact_list_web.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("contacts", contactService.findAll());
        return "index";
    }

    @GetMapping("/contact/create")
    public String showFormForCreate(Model model) {
        model.addAttribute("contact", new Contact());
        model.addAttribute("formAction", "/contact/create");
        return "editor";
    }

    @PostMapping("/contact/create")
    public String createContact(@ModelAttribute Contact contact) {
        contactService.save(contact);
        return "redirect:/";
    }

    @GetMapping("/contact/edit/{id}")
    public String showFormForEdit(@PathVariable Long id, Model model) {

        Contact contact = contactService.findById(id);
        if (contact != null) {
            model.addAttribute("contact", contact);
            model.addAttribute("formAction", "/contact/edit");
            return "editor";
        }
        return "redirect:/";
    }

    @PostMapping("/contact/edit")
    public String editContact(@ModelAttribute Contact contact) {
        contactService.update(contact);
        return "redirect:/";
    }

    @GetMapping("/contact/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteById(id);
        return "redirect:/";
    }

}
