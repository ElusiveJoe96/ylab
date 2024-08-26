package ru.ylab.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.User;
import ru.ylab.service.UserService;
import ru.ylab.servlet.user.LoginServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LoginServletTest {

    @InjectMocks
    private LoginServlet loginServlet;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to mock PrintWriter", e);
        }
    }

    @Test
    public void testDoPost_SuccessfulLogin() throws Exception {
        String email = "john.doe@example.com";
        String password = "password123";
        User user = new User(1, "John Doe", email, password, Role.CLIENT, "123-456-7890");
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.login(email, password, response)).thenReturn(Optional.of(user));

        loginServlet.doPost(request, response);

        verify(userService).login(email, password, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(anyString()); // Ensure that response writer was called
    }

    @Test
    public void testDoPost_FailedLogin() throws Exception {
        String email = "john.doe@example.com";
        String password = "wrongpassword";
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.login(email, password, response)).thenReturn(Optional.empty());

        loginServlet.doPost(request, response);

        verify(userService).login(email, password, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(captor.capture());
        assertTrue(captor.getValue().contains("Invalid email or password"));
    }

    @Test
    public void testDoPost_Exception() throws Exception {
        String email = "john.doe@example.com";
        String password = "password123";
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.login(anyString(), anyString(), eq(response))).thenThrow(new RuntimeException("Test exception"));

        loginServlet.doPost(request, response);

        verify(userService).login(email, password, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(captor.capture());
        assertTrue(captor.getValue().contains("Error: Test exception"));
    }
}
