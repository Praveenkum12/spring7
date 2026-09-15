package com.jimmy.portal.contact.service;

import com.jimmy.portal.contact.dto.ContactResponseDto;
import com.jimmy.portal.contact.entity.Contact;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IContactService {

    boolean addContact(Contact contact);

    List<ContactResponseDto> fetchNewContactMsgs();

    List<ContactResponseDto> fetchNewContactMsgsWithSort(String sortBy, String sortDir);

    Page<ContactResponseDto> fetchNewContactMsgsWithPaginationAndSort(int pageNumber, int pageSize,
                                                                      String sortBy, String sortDir);

    boolean closeContactMsg(Long id, String status);
}
