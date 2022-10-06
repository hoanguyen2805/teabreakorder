package com.nta.teabreakorder.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nta.teabreakorder.service.impl.PusherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private PusherService pusherService;
    @GetMapping("/all")
    public String allAccess() throws IOException {
        pusherService.sendToBrowserNotify(new ArrayList<>(){{add("admin");}},"TEST","new content",
                "https://orderfoodnta.web.app/");
        return "public content";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('ORDERER')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/orderer")
    @PreAuthorize("hasRole('ORDERER')")
    public String ordererAccess() {
        return "Orderer Board.";
    }
}