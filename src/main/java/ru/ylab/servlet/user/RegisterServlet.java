package ru.ylab.servlet.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ylab.domain.dto.UserDTO;
import ru.ylab.service.UserService;
import ru.ylab.util.SingletonProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RegisterServlet() {
        this.userService = SingletonProvider.getUserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UserDTO userDTO = objectMapper.readValue(req.getInputStream(), UserDTO.class);
            userService.registerUser(userDTO, resp);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid input: " + e.getMessage());
        }
    }
}
