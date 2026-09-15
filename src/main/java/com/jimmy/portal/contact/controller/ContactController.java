package com.jimmy.portal.contact.controller;

import com.jimmy.portal.constant.ApplicationConstant;
import com.jimmy.portal.contact.dto.ContactRequestDto;
import com.jimmy.portal.contact.dto.ContactResponseDto;
import com.jimmy.portal.contact.entity.Contact;
import com.jimmy.portal.contact.mapper.ContactRequestMapper;
import com.jimmy.portal.contact.service.IContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ContactController")
@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService contactService;

    private final ContactRequestMapper contactRequestMapper;

    @Operation(summary = "Add contact")
    @PostMapping(path = "/public", version = "1.0")
    public ResponseEntity<String> addContact(@RequestBody @Valid ContactRequestDto contactRequestDto) {
        Contact contactDto =  contactRequestMapper.dtoToEntity(contactRequestDto);
         boolean result = contactService.addContact(contactDto);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Added contact successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request processing failed");
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgs() {
        List<ContactResponseDto> contactResponseDtos = contactService.fetchNewContactMsgs();
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);
    }

    @GetMapping("/sort/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgsWithSort(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        List<ContactResponseDto> contactResponseDtos = contactService
                .fetchNewContactMsgsWithSort(sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);
    }

    @GetMapping("/page/admin")
    public ResponseEntity<Page<ContactResponseDto>> fetchNewContactMsgsWithPaginationAndSort(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Page<ContactResponseDto> contactResponseDtoPage = contactService
                .fetchNewContactMsgsWithPaginationAndSort(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtoPage);
    }


    @PatchMapping("/{id}/status/admin")
    public ResponseEntity<String> closeContactMsg(@PathVariable String id) {
        boolean isUpdated = contactService.closeContactMsg(Long.valueOf(id),
                ApplicationConstant.CLOSED_MESSAGE);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body("Contact message updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update contact message.");
        }
    }

}
