package integrated;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class EnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String POSTGRES_URL = "jdbc:postgresql://%s:%d/dealershipdb";
    private static final String WIREMOCK_URL = "http://%s:%d";

    private static final DockerImageName WIREMOCK_IMAGE = DockerImageName
            .parse("wiremock/wiremock:latest");

    private static final DockerImageName POSTGRES_IMAGE = DockerImageName
            .parse("postgres:latest");

    private static final Network NETWORK = Network.newNetwork();

    private static final GenericContainer<?> WIREMOCK_CONTAINER = new GenericContainer<>(WIREMOCK_IMAGE)
            .withNetwork(NETWORK)
            .withNetworkAliases("wiremock")
            .withExposedPorts(8080);

    private static final GenericContainer<?> POSTGRES_CONTAINER = new GenericContainer<>(POSTGRES_IMAGE)
            .withNetwork(NETWORK)
            .withNetworkAliases("postgres")
            .withEnv("POSTGRES_USER","postgres")
            .withEnv("POSTGRES_PASSWORD","123456")
            .withEnv("POSTGRES_DB","dealershipdb")
            .withExposedPorts(5432)
            .waitingFor(Wait.forListeningPort());

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        WIREMOCK_CONTAINER.start();
        POSTGRES_CONTAINER.start();

        final var properties = Map.of(
                "spring.datasource.url",getPostgresUrl(),
                "via-cep.url",getWiremockUrl()+"/ws/"
        );

        TestPropertyValues.of(properties).applyTo(applicationContext);
    }

    private static String getWiremockUrl() {
        return String.format(WIREMOCK_URL,
                WIREMOCK_CONTAINER.getHost(),WIREMOCK_CONTAINER.getFirstMappedPort());
    }

    public static String getWiremockHost(){
        return WIREMOCK_CONTAINER.getHost();
    }

    public static Integer getWiremockPort(){
        return WIREMOCK_CONTAINER.getFirstMappedPort();
    }

    private static String getPostgresUrl() {
        return String.format(POSTGRES_URL,
                POSTGRES_CONTAINER.getHost(),POSTGRES_CONTAINER.getFirstMappedPort());
    }
}
