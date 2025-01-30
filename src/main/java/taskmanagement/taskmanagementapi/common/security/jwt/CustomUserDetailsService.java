package taskmanagement.taskmanagementapi.common.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;
import taskmanagement.taskmanagementapi.userservice.exceptions.InvalidUserCredentialException;
import taskmanagement.taskmanagementapi.userservice.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new InvalidUserCredentialException("invalid username or password"));
            return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword());
        }catch(Exception e){
            throw new InvalidUserCredentialException("invalid username or password");
        }
    }
}
