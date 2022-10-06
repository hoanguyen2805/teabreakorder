package com.nta.teabreakorder.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.nta.teabreakorder.payload.request.UserRequest;
import com.nta.teabreakorder.payload.response.MessageResponse;
import com.nta.teabreakorder.security.jwt.JwtUtils;
import com.nta.teabreakorder.service.UserService;
import com.nta.teabreakorder.service.impl.FilesStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.nta.teabreakorder.exception.ResourceNotFoundException;
import com.nta.teabreakorder.model.User;
import com.nta.teabreakorder.repository.UserRepository;

import java.util.ArrayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import java.util.*;
import java.util.stream.Collectors;

import com.nta.teabreakorder.model.Role;
import com.nta.teabreakorder.payload.request.UpdateUserRequest;
import com.nta.teabreakorder.repository.RoleRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.nta.teabreakorder.model.ERole;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllEmployees(@RequestParam(required = false) String username, @RequestParam(value = "page", defaultValue = "0", required = false) int page, @RequestParam(value = "size", defaultValue = "5", required = false) int size) {
        try {
            List<User> users = new ArrayList<User>();
            Pageable paging = PageRequest.of(page, size);

            Page<User> pageUsers;
            if (username == null) {
                pageUsers = userRepository.findAllByIsDeleted(paging);
            } else {
                pageUsers = userRepository.searchUser(username, paging);
            }
            users = pageUsers.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("users", users);
            response.put("currentPage", pageUsers.getNumber());
            response.put("totalItems", pageUsers.getTotalElements());
            response.put("totalPages", pageUsers.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ORDERER')")
    public ResponseEntity<User> getUserById(HttpServletRequest request)
            throws ResourceNotFoundException {
        String token = null;
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            token = headerAuth.substring(7, headerAuth.length());
        }
        String username = jwtUtils.getUserNameFromJwtToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserRequest userDetails) throws Exception {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userDetails.getId()));
        String oldImg = user.getImg();
        user.setEmail(userDetails.getEmail());
        user.setFullName(userDetails.getFullName());
        user.setImg(userDetails.getImg());

        userRepository.save(user);
        if (oldImg != null && !oldImg.equals(userDetails.getImg())) {
            filesStorageService.delete(oldImg);
        }
        return userService.getInfo();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ORDERER')")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        user.setEmail(userDetails.getEmail());
        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORDERER')")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @PatchMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity changePassword(@RequestBody UpdateUserRequest updateUserRequest) throws Exception {
        return userService.changePassword(updateUserRequest.getPassword());
    }


    @GetMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity getInfo()
            throws Exception {
        return userService.getInfo();
    }

    @PatchMapping("/update_user_by_admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserByAdmin(HttpServletRequest request, @RequestBody UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(updateUserRequest.getId()).get();

        if (!user.getUsername().equals(updateUserRequest.getUsername())) {
            if (userRepository.existsByUsername(updateUserRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }
        }

        if (!user.getEmail().equals(updateUserRequest.getEmail())) {
            if (userRepository.existsByEmail(updateUserRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
            }
        }

        user.setUsername(updateUserRequest.getUsername());
        user.setEmail(updateUserRequest.getEmail());
        user.setFullName(updateUserRequest.getFullName());
        user.setTimeRemaining(updateUserRequest.getTimeRemaining());

        if (updateUserRequest.getPassword() != null) {
            if (!updateUserRequest.getPassword().isEmpty()) {
                user.setPassword(encoder.encode(updateUserRequest.getPassword()));
            }
        }

        Set<String> strRoles = updateUserRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "orderer":
                        Role ordererRole = roleRepository.findByName(ERole.ROLE_ORDERER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(ordererRole);

                        break;
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletes(@RequestBody List<Long> ids) throws Exception {
        return userService.deletesUser(ids);
    }
}
