package lk.ac.kln.unimart.service;

import lk.ac.kln.unimart.common.exception.ConflictException;
import lk.ac.kln.unimart.dto.AuthResponse;
import lk.ac.kln.unimart.dto.LoginRequest;
import lk.ac.kln.unimart.dto.RegisterRequest;
import lk.ac.kln.unimart.entity.User;
import lk.ac.kln.unimart.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUniversityEmail(request.getUniversityEmail())) {
            throw new ConflictException("Email is already registered");
        }

        User user = new User();
        user.setUniversityEmail(request.getUniversityEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        // Normalize role to uppercase and prefix with ROLE_ if not present, standard in Spring Security
        String role = request.getRole().toUpperCase();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        user.setRole(role);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser.getUniversityEmail(), savedUser.getId(), savedUser.getRole());

        return new AuthResponse(token, savedUser.getId(), savedUser.getUniversityEmail(), savedUser.getFullName(), savedUser.getRole());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUniversityEmail(request.getUniversityEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getUniversityEmail(), user.getId(), user.getRole());

        return new AuthResponse(token, user.getId(), user.getUniversityEmail(), user.getFullName(), user.getRole());
    }
}
