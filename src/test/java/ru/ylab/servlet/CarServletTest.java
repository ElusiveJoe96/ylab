package ru.ylab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.domain.dto.CarDTO;
import ru.ylab.service.CarService;
import ru.ylab.servlet.car.CarServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CarServletTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarServlet carServlet;

    private ObjectMapper objectMapper;
    private ByteArrayOutputStream responseOutputStream;

    @BeforeEach
    public void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        responseOutputStream = new ByteArrayOutputStream();
        carServlet.init();
    }

    @Test
    public void testDoGet_withId() throws Exception {
        CarDTO car = new CarDTO(1, "Toyota", "Corolla");
        when(carService.getCarById(1)).thenReturn(car);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = new PrintWriter(responseOutputStream);

        when(request.getParameter("id")).thenReturn("1");
        when(response.getWriter()).thenReturn(writer);

        carServlet.doGet(request, response);

        writer.flush();
        String responseBody = responseOutputStream.toString();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(objectMapper.writeValueAsString(car), responseBody);
    }

    @Test
    public void testDoGet_withoutId() throws Exception {
        CarDTO car1 = new CarDTO(1, "Toyota", "Corolla");
        CarDTO car2 = new CarDTO(2, "Honda", "Civic");
        when(carService.getAllCars()).thenReturn(Arrays.asList(car1, car2));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = new PrintWriter(responseOutputStream);

        when(request.getParameter("id")).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        carServlet.doGet(request, response);

        writer.flush();
        String responseBody = responseOutputStream.toString();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(objectMapper.writeValueAsString(Arrays.asList(car1, car2)), responseBody);
    }

    @Test
    public void testDoPost() throws Exception {
        CarDTO carDTO = new CarDTO(1, "Toyota", "Corolla");
        String carJson = objectMapper.writeValueAsString(carDTO);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(carJson)));

        carServlet.doPost(request, response);

        verify(carService).addCar(any(CarDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testDoPut_success() throws Exception {
        CarDTO carDTO = new CarDTO(1, "Toyota", "Corolla");
        String carJson = objectMapper.writeValueAsString(carDTO);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(carJson)));
        when(carService.updateCar(any(CarDTO.class))).thenReturn(true);

        carServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoPut_notFound() throws Exception {
        CarDTO carDTO = new CarDTO(1, "Toyota", "Corolla");
        String carJson = objectMapper.writeValueAsString(carDTO);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(carJson)));
        when(carService.updateCar(any(CarDTO.class))).thenReturn(false);

        carServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void testDoDelete_success() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn("1");
        when(carService.deleteCar(anyInt())).thenReturn(true);

        carServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    public void testDoDelete_notFound() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn("1");
        when(carService.deleteCar(anyInt())).thenReturn(false);

        carServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void testDoDelete_badRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn(null);

        carServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
