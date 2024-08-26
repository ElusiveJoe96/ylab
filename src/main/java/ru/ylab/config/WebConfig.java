package ru.ylab.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.ylab.audit.AuditLogRepository;
import ru.ylab.audit.AuditService;
import ru.ylab.repository.CarRepository;
import ru.ylab.repository.OrderRepository;
import ru.ylab.repository.UserRepository;
import ru.ylab.service.CarService;
import ru.ylab.service.OrderService;
import ru.ylab.service.UserService;
import ru.ylab.service.implementation.CarServiceImpl;
import ru.ylab.service.implementation.OrderServiceImpl;
import ru.ylab.service.implementation.UserServiceImpl;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.yml")
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "ru.ylab")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    //TODO
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://postgres/car_shop_db");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/5.17.14/");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Shop API")
                        .version("1.0")
                        .description("API shop"))
                .addServersItem(new Server()
                        .url("http://localhost:8080/")
                        .description("Local server"));
    }



    @Bean
    public AuditLogRepository auditLogRepository(DataSource dataSource) {
        return new AuditLogRepository(dataSource);
    }

    @Bean
    public AuditService auditService(AuditLogRepository auditLogRepository) {
        return new AuditService(auditLogRepository);
    }

    @Bean
    public UserRepository userRepository(DataSource dataSource) {
        return new UserRepository(dataSource);
    }

    @Bean
    public OrderRepository orderRepository(DataSource dataSource) {
        return new OrderRepository(dataSource);
    }

    @Bean
    public CarRepository carRepository(DataSource dataSource) {
        return new CarRepository(dataSource);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository) {
        return new OrderServiceImpl(orderRepository);
    }

    @Bean
    public CarService carService(CarRepository carRepository) {
        return new CarServiceImpl(carRepository);
    }
}
