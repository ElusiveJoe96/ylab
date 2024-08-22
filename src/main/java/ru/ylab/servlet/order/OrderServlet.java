package ru.ylab.servlet.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ylab.config.DatabaseConfig;
import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.repository.OrderRepository;
import ru.ylab.service.OrderService;
import ru.ylab.service.implementation.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    private final OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderServlet() {
        this.orderService = new OrderServiceImpl(new OrderRepository(new DatabaseConfig()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OrderDTO orderDTO = objectMapper.readValue(request.getInputStream(), OrderDTO.class);
        orderService.createOrder(orderDTO);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("id"));
        OrderStatus status = OrderStatus.valueOf(request.getParameter("status"));
        boolean updated = orderService.updateOrderStatus(orderId, status);
        if (updated) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("id"));
        boolean deleted = orderService.deleteOrder(orderId);
        if (deleted) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("all".equals(action)) {
            List<OrderDTO> orders = orderService.getAllOrders();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(orders));
        } else if ("byStatus".equals(action)) {
            OrderStatus status = OrderStatus.valueOf(request.getParameter("status"));
            List<OrderDTO> orders = orderService.getOrdersByStatus(status);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(orders));
        } else if ("byUser".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(orders));
        } else if ("byCar".equals(action)) {
            int carId = Integer.parseInt(request.getParameter("carId"));
            List<OrderDTO> orders = orderService.getOrdersByCarId(carId);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(orders));
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
