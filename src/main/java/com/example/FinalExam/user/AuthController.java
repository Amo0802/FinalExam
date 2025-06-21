package com.example.FinalExam.user;

import com.example.FinalExam.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<String> login(@RequestBody AuthRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        User user = (User) userService.loadUserByUsername(req.getUsername());
        return ResponseEntity.ok(jwtUtil.generateToken(user));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        User saved = userService.registerNewUser(user);
        return ResponseEntity.ok(jwtUtil.generateToken(saved));
    }
}