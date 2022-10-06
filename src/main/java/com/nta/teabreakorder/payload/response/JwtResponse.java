package com.nta.teabreakorder.payload.response;

import com.nta.teabreakorder.model.BankingPaymentInfo;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String fullName;
    private String img;
    private String email;
    private List<String> roles;
    private List<BankingPaymentInfo> bankingPaymentInfoList;

    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles,String fullName,String img) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.fullName = fullName;
        this.img = img;
    }

    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles,String fullName,String img, List<BankingPaymentInfo> bankingPaymentInfoList) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.fullName = fullName;
        this.img = img;
        this.bankingPaymentInfoList = bankingPaymentInfoList;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
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

    public List<BankingPaymentInfo> getBankingPaymentInfoList() {
        return bankingPaymentInfoList;
    }

    public void setBankingPaymentInfoList(List<BankingPaymentInfo> bankingPaymentInfoList) {
        this.bankingPaymentInfoList = bankingPaymentInfoList;
    }
}
