package com.example.cloth_area.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("아이디 중복");

        if (userRepository.existsByNickname(request.getNickname()))
            throw new RuntimeException("닉네임 중복");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("이메일 중복");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());

        userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
