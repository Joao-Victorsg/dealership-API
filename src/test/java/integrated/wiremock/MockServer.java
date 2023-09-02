package integrated.wiremock;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static integrated.EnvironmentInitializer.getWiremockHost;
import static integrated.EnvironmentInitializer.getWiremockPort;

public final class MockServer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        WireMock.configureFor(getWiremockHost(),getWiremockPort());
    }

    public static void mockGetAddressByPostCode(String postCode){
        final String VIA_CEP_PATH="/ws/" + postCode + "/json";
        try {
            WireMock.stubFor(get(urlMatching(VIA_CEP_PATH))
                    .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                            .withHeader("Content-Type","application/json")
                            .withBody(MAPPER.writeValueAsString(addressMock(postCode))))
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void mockGetAddressByPostCodeWithServerError(String postCode){
        final String VIA_CEP_PATH="/ws/" + postCode + "/json";
            WireMock.stubFor(get(urlMatching(VIA_CEP_PATH))
                    .willReturn(serverError()));
    }

    private static AddressDtoResponse addressMock(String postCode){
        return AddressDtoResponse.builder()
                .postCode(postCode)
                .city("Test")
                .stateAbbreviation("TT")
                .streetName("Rua teste")
                .isAddressSearched(true)
                .build();
    }

    private static AddressDtoResponse addressMockWhenServerUnavailable(String postCode){
        return AddressDtoResponse.builder()
                .postCode(postCode)
                .isAddressSearched(false)
                .build();
    }

}
