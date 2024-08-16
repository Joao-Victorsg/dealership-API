package integrated.util;

import org.springframework.beans.factory.annotation.Autowired;

public class TokenUtil {

    @Autowired
    private static FeignClientIntegrationTest feignClientIntegrationTest;

    public static String getTokenStatic(){
        return "a";
    }
}
