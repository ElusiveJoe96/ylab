package ru.ylab.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.service.UserService;
import ru.ylab.servlet.user.DeleteUserServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
public class DeleteUserServletTest {

    @InjectMocks
    private DeleteUserServlet deleteUserServlet;

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
    public void testDoPost_InvalidUserId() throws Exception {
        when(request.getParameter("userId")).thenReturn("invalid");

        deleteUserServlet.doPost(request, response);

        verify(userService, never()).deleteUser(anyInt(), eq(response));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(captor.capture());
        assertTrue(captor.getValue().contains("Invalid user ID format"));
    }

    @Test
    public void testDoPost_NoUserIdParameter() throws Exception {
        when(request.getParameter("userId")).thenReturn(null);

        deleteUserServlet.doPost(request, response);

        verify(userService, never()).deleteUser(anyInt(), eq(response));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(captor.capture());
        assertTrue(captor.getValue().contains("Invalid user ID format"));
    }
}
