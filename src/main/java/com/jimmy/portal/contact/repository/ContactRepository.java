package com.jimmy.portal.contact.repository;

import com.jimmy.portal.contact.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByStatus(String status);

    List<Contact> findByStatusOrderByCreatedAtAsc(String status);

    List<Contact> findByStatus(String status, Sort sort);

    Page<Contact> findByStatus(String status, Pageable pageable);

}
