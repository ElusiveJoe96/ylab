package ru.ylab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.service.OrderService;
import ru.ylab.servlet.order.OrderServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class OrderServletTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderServlet orderServlet;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }



    @Test
    void testDoPut_success() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("status")).thenReturn(OrderStatus.COMPLETED.name());
        when(orderService.updateOrderStatus(1, OrderStatus.COMPLETED)).thenReturn(true);

        orderServlet.doPut(request, response);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPut_notFound() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("status")).thenReturn(OrderStatus.CANCELED.name());
        when(orderService.updateOrderStatus(1, OrderStatus.CANCELED)).thenReturn(false);

        orderServlet.doPut(request, response);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoDelete_success() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("id")).thenReturn("1");
        when(orderService.deleteOrder(1)).thenReturn(true);

        orderServlet.doDelete(request, response);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDelete_notFound() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("id")).thenReturn("1");
        when(orderService.deleteOrder(1)).thenReturn(false);

        orderServlet.doDelete(request, response);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoGet_allOrders() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<OrderDTO> orders = Arrays.asList(new OrderDTO(), new OrderDTO());
        when(request.getParameter("action")).thenReturn("all");
        when(orderService.getAllOrders()).thenReturn(orders);

        orderServlet.doGet(request, response);

        verify(response, times(1)).setContentType("application/json");
        verify(response.getWriter(), times(1)).write(objectMapper.writeValueAsString(orders));
    }

    @Test
    void testDoGet_byStatus() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        OrderStatus status = OrderStatus.PENDING;
        List<OrderDTO> orders = Arrays.asList(new OrderDTO(), new OrderDTO());
        when(request.getParameter("action")).thenReturn("byStatus");
        when(request.getParameter("status")).thenReturn(status.name());
        when(orderService.getOrdersByStatus(status)).thenReturn(orders);

        orderServlet.doGet(request, response);

        verify(response, times(1)).setContentType("application/json");
        verify(response.getWriter(), times(1)).write(objectMapper.writeValueAsString(orders));
    }

    @Test
    void testDoGet_badRequest() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("action")).thenReturn("unknown");

        orderServlet.doGet(request, response);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
