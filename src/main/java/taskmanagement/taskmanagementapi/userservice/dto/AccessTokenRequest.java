package taskmanagement.taskmanagementapi.userservice.dto;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Service
public class AccessTokenRequest {
    private String refreshToken;
    
}
