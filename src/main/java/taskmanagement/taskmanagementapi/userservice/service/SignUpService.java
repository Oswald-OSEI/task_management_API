package taskmanagement.taskmanagementapi.userservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanagement.taskmanagementapi.userservice.dto.SignUpRequestDto;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;
import taskmanagement.taskmanagementapi.userservice.exceptions.SignUpException;
import taskmanagement.taskmanagementapi.userservice.repository.UserRepository;

@Service
@Slf4j
public class SignUpService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String signUp(SignUpRequestDto signUpRequestDto) {
        if (signUpRequestDto.username().isEmpty() || signUpRequestDto.password().isEmpty()) {
            throw new SignUpException("username or password cannot be empty");
        }
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(signUpRequestDto.username());
            userEntity.setPassword(passwordEncoder.encode(signUpRequestDto.password()));
            userRepository.save(userEntity);
            return "sign up successful";
        }catch(DataIntegrityViolationException e){
            throw new SignUpException("user already exists");
        }
    }
}
