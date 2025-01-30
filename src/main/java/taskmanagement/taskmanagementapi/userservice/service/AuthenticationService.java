package taskmanagement.taskmanagementapi.userservice.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanagement.taskmanagementapi.common.security.jwt.TokenService;
import taskmanagement.taskmanagementapi.userservice.dto.LoginRequestDto;
import taskmanagement.taskmanagementapi.userservice.dto.LoginResponseDto;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;
import taskmanagement.taskmanagementapi.common.exceptions.InvalidAuthorizationHeaderException;
import taskmanagement.taskmanagementapi.userservice.exceptions.InvalidUserCredentialException;
import taskmanagement.taskmanagementapi.userservice.repository.UserRepository;

@Service
@Slf4j
public class AuthenticationService {
    @Value("${jwt.refresh.secret}")
    private String refreshSecretString;

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationService(TokenService tokenService, UserDetailsService userDetailsService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        UserEntity user = userRepository.findByUsername(loginRequestDto.username()).orElseThrow(()->new InvalidUserCredentialException("invalid username or password"));
        if(passwordEncoder.matches(loginRequestDto.password(), user.getPassword())){
            return new LoginResponseDto(
                    user.getUsername(),
                    tokenService.generateAccessToken(user.getId(), user.getUsername()),
                    tokenService.generateRefreshToken(user.getId(), user.getUsername())
            );
        }else{
            throw new InvalidUserCredentialException("invalid username or password");
        }
    }
    public String getNewAccessToken(String refreshToken){
        if(tokenService.validateToken(refreshToken, refreshSecretString).isValid()){
            return tokenService.generateAccessToken(tokenService.extractId(refreshToken, refreshSecretString), tokenService.extractUsername(refreshToken, refreshSecretString));
        }
        else{
            throw new InvalidAuthorizationHeaderException("invalid refresh token");
        }
    }


}
