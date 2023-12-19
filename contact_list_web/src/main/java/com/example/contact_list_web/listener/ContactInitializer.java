package com.example.contact_list_web.listener;

import com.example.contact_list_web.Contact;
import com.example.contact_list_web.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty("app.init-on-startup.enabled")
public class ContactInitializer {

    private final ContactService contactService;

    @EventListener(ApplicationStartedEvent.class)
    public void initializeContacts() {
        log.debug("Calling ContactInitializer->initializeContacts...");

        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            int value = i + 1;
            Contact contact = new Contact();
            contact.setId((long) value);
            contact.setFirstName("Firstname" + value);
            contact.setLastName("Lastname" + value);
            contact.setEmail(value + "@test.tst");
            contact.setPhone(Long.toString(value));

            contacts.add(contact);
        }

        contactService.batchInsert(contacts);
    }
}
