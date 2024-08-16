package integrated.util;

import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "integration-test", url="localhost:8080/v1/dealership")
public interface FeignClientIntegrationTest {

    @PostMapping(value = "/users", produces = "application/json")
    void createUser(UserDtoRequest userDtoRequest);

    @PostMapping(value = "/auths", produces = "application/json")
    TokenDto getToken(UserDtoRequest user);

}