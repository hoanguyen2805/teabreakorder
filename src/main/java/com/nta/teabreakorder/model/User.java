package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.common.Const;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 120)
    private String password;

    @Size(max = 40)
    private String fullName;

    private String img;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<BankingPaymentInfo> bankingPaymentInfoList;

    @Column(name = "is_deleted")
    @JsonProperty("isDeleted")
    private boolean isDeleted = false;

    public User() {
    }

    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    @JsonProperty(value = "time_remaining")
    @Column(name = "time_remaining")
    private LocalDateTime timeRemaining;

    public User(String username, String email, String fullName, String password, LocalDateTime timeRemaining) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.timeRemaining = timeRemaining;
    }

    public User(Long id, String username, List<BankingPaymentInfo> bankingPaymentInfoList) {
        this.id = id;
        this.username = username;
        this.bankingPaymentInfoList = bankingPaymentInfoList;
    }

    public User(Long id, @NotBlank @Size(max = 20) String username, @Size(max = 40) String fullName, List<BankingPaymentInfo> bankingPaymentInfoList) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.bankingPaymentInfoList = bankingPaymentInfoList;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(LocalDateTime timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public List<BankingPaymentInfo> getBankingPaymentInfoList() {
        return bankingPaymentInfoList;
    }

    public void setBankingPaymentInfoList(List<BankingPaymentInfo> bankingPaymentInfoList) {
        this.bankingPaymentInfoList = bankingPaymentInfoList;
    }

    public void addBankingPayment(BankingPaymentInfo bankingPaymentInfo) {
        this.bankingPaymentInfoList.add(bankingPaymentInfo);
    }

    @JsonIgnore
    public User getUserDTO() {
        return new User(id, username, fullName, bankingPaymentInfoList);
    }

}