package integrated;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.Network;

import java.util.Map;

import static integrated.container.LocalstackContainerDefinition.getLocalstackUrl;
import static integrated.container.LocalstackContainerDefinition.startLocalstackContainer;
import static integrated.container.PostgresContainerDefinition.getPostgresUrl;
import static integrated.container.PostgresContainerDefinition.startPostgresContainer;
import static integrated.container.RedisContainerDefinition.getRedisPort;
import static integrated.container.RedisContainerDefinition.startRedisContainer;
import static integrated.container.WiremockContainerDefinition.getWiremockUrl;
import static integrated.container.WiremockContainerDefinition.startWiremockContainer;

public class EnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final Network NETWORK = Network.newNetwork();

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        startWiremockContainer();
        startPostgresContainer();
        startRedisContainer();
        startLocalstackContainer();

        final var properties = Map.of(
                "spring.datasource.url",getPostgresUrl(),
                "via-cep.url",getWiremockUrl()+"/ws/",
                "spring.data.redis.port",getRedisPort(),
                "spring.cloud.aws.sns.endpoint",getLocalstackUrl()
        );

        TestPropertyValues.of(properties).applyTo(applicationContext);
    }

}