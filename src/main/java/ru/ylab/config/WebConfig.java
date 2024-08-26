package ru.ylab.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.ylab.audit.AuditLogRepository;
import ru.ylab.audit.AuditService;
import ru.ylab.auth.JwtService;
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/5.17.14/");
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
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
    public UserService userService(UserRepository userRepository, JwtService jwtService) {
        return new UserServiceImpl(userRepository, jwtService);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository) {
        return new OrderServiceImpl(orderRepository);
    }

    @Bean
    public CarService carService(CarRepository carRepository) {
        return new CarServiceImpl(carRepository);
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }
}
