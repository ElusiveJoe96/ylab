package ru.ylab.servlet.user;

import ru.ylab.domain.model.User;
import ru.ylab.service.UserService;
import ru.ylab.util.SingletonProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/viewAllUsers")
public class ViewAllUsersServlet extends HttpServlet {
    private final UserService userService;

    public ViewAllUsersServlet() {
        this.userService = SingletonProvider.getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userService.viewAllUsers(resp);
        if (users.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
