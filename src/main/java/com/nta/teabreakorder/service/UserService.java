package com.nta.teabreakorder.service;

import com.nta.teabreakorder.exception.ResourceNotFoundException;
import com.nta.teabreakorder.model.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService{

    ResponseEntity changePassword(String password) throws Exception;

    ResponseEntity getInfo() throws Exception;

    ResponseEntity deletesUser(List<Long> ids) throws Exception;

    User getUserByToken(HttpServletRequest request) throws ResourceNotFoundException;
}
