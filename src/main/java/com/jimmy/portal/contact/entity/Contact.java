package com.jimmy.portal.contact.entity;

import com.jimmy.portal.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "contacts")
public class Contact extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(message = "Subject cannot be empty")
    @Size(min = 5, max = 100, message = "subject should have min. 5 characters")
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotBlank(message = "Message cannot be empty")
    @Size(min = 5, max = 100, message = "message should have min. 5 characters")
    @Column(name = "message",  nullable = false)
    private String message;

    @NotBlank(message = "Status cannot be empty")
    private String status;

    @NotBlank(message = "UserType cannot be empty")
    @Column(name = "user_type",  nullable = false)
    private String userType;

}
