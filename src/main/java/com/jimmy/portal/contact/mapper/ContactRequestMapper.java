package com.jimmy.portal.contact.mapper;

import com.jimmy.portal.contact.dto.ContactRequestDto;
import com.jimmy.portal.contact.entity.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactRequestMapper {

    Contact dtoToEntity(ContactRequestDto contactRequestDto);

}
