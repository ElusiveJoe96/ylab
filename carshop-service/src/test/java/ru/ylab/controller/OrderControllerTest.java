package ru.ylab.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.service.OrderService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private final OrderDTO sampleOrder = new OrderDTO(1, 1, LocalDateTime.now(), null, null);

    @Test
    @DisplayName("getAllOrders should return a list of orders")
    public void getAllOrders_ShouldReturnOrderList() throws Exception {
        Mockito.when(orderService.getAllOrders()).thenReturn(Collections.singletonList(sampleOrder));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].carId").value(sampleOrder.getCarId()))
                .andExpect(jsonPath("$[0].userId").value(sampleOrder.getUserId()));
    }

    @Test
    @DisplayName("getOrdersByUserId should return a list of orders for a specific user")
    public void getOrdersByUserId_ShouldReturnOrderList() throws Exception {
        Mockito.when(orderService.getOrdersByUserId(1)).thenReturn(Collections.singletonList(sampleOrder));

        mockMvc.perform(get("/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].carId").value(sampleOrder.getCarId()))
                .andExpect(jsonPath("$[0].userId").value(sampleOrder.getUserId()));
    }

    @Test
    @DisplayName("addOrder should return the created order")
    public void addOrder_ShouldReturnCreatedOrder() throws Exception {
        Mockito.when(orderService.addOrder(any(OrderDTO.class))).thenReturn(sampleOrder);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carId\": 1, \"userId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carId").value(sampleOrder.getCarId()))
                .andExpect(jsonPath("$.userId").value(sampleOrder.getUserId()));
    }

    @Test
    @DisplayName("updateOrder should return the updated order when it exists")
    public void updateOrder_ShouldReturnUpdatedOrder_WhenOrderExists() throws Exception {
        Mockito.when(orderService.updateOrder(eq(1), any(OrderDTO.class))).thenReturn(sampleOrder);

        mockMvc.perform(put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carId\": 1, \"userId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(sampleOrder.getCarId()))
                .andExpect(jsonPath("$.userId").value(sampleOrder.getUserId()));
    }

    @Test
    @DisplayName("updateOrder should return 404 when the order does not exist")
    public void updateOrder_ShouldReturnNotFound_WhenOrderDoesNotExist() throws Exception {
        Mockito.when(orderService.updateOrder(eq(1), any(OrderDTO.class))).thenReturn(null);

        mockMvc.perform(put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carId\": 1, \"userId\": 1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deleteOrder should return 204 No Content when the order is deleted")
    public void deleteOrder_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(1);

        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());
    }
}
