package com.jimmy.portal.contact.service.impl;


import com.jimmy.portal.constant.ApplicationConstant;
import com.jimmy.portal.contact.dto.ContactResponseDto;
import com.jimmy.portal.contact.entity.Contact;
import com.jimmy.portal.contact.mapper.ContactResponseMapper;
import com.jimmy.portal.contact.repository.ContactRepository;
import com.jimmy.portal.contact.service.IContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    private final ContactResponseMapper contactResponseMapper;

    @Override
    public boolean addContact(Contact contact) {
        boolean result = false;
        contact.setStatus("NEW");
        Contact newContact = contactRepository.save(contact);
        if (newContact.getId() != null) {
            result = true;
        }

        return result;
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgs() {
        List<Contact> allNewContacts = contactRepository.findByStatusOrderByCreatedAtAsc(ApplicationConstant.NEW_MESSAGE);
        return allNewContacts.stream().map(contactResponseMapper::entityToDto).toList();
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgsWithSort(String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        List<Contact> contacts = contactRepository.findByStatus(ApplicationConstant.NEW_MESSAGE, sort);
        return contacts.stream().map(contactResponseMapper::entityToDto).toList();
    }

    @Override
    public Page<ContactResponseDto> fetchNewContactMsgsWithPaginationAndSort(
            int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Create Sort object based on sortBy and sortDir parameters
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        // Create Pageable object with page number, page size, and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        // Fetch paginated and sorted contacts from repository
        Page<Contact> contactPage = contactRepository.findByStatus(
                ApplicationConstant.NEW_MESSAGE, pageable);

        // Transform Contact entities to ContactResponseDto
        return contactPage.map(contactResponseMapper::entityToDto);
    }

    @Transactional
    @Override
    public boolean closeContactMsg(Long id, String status) {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact == null) {
            return false;
        } else {
            contact.setStatus(status);

        }
        return true;
    }
}
