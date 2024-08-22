package ru.ylab.servlet.car;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ylab.config.DatabaseConfig;
import ru.ylab.domain.dto.CarDTO;
import ru.ylab.repository.CarRepository;
import ru.ylab.service.CarService;
import ru.ylab.service.implementation.CarServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/cars")
public class CarServlet extends HttpServlet {
    private CarService carService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        carService = new CarServiceImpl(new CarRepository(new DatabaseConfig()));
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String carId = req.getParameter("id");
        if (carId != null) {
            CarDTO car = carService.getCarById(Integer.parseInt(carId));
            if (car != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(car));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            List<CarDTO> cars = carService.getAllCars();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(cars));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CarDTO carDTO = objectMapper.readValue(req.getReader(), CarDTO.class);
        carService.addCar(carDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CarDTO carDTO = objectMapper.readValue(req.getReader(), CarDTO.class);
        boolean updated = carService.updateCar(carDTO);
        if (updated) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String carId = req.getParameter("id");
        if (carId != null) {
            boolean deleted = carService.deleteCar(Integer.parseInt(carId));
            if (deleted) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
