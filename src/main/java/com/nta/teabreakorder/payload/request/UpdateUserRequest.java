package com.nta.teabreakorder.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.common.Const;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.*;

public class UpdateUserRequest {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 3, max = 40)
    private String fullName;

    private Set<String> role;

    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    @JsonProperty(value = "time_remaining")
    @Column(name = "time_remaining")
    private LocalDateTime timeRemaining;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public LocalDateTime getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(LocalDateTime timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
}
