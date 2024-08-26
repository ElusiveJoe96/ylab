package ru.ylab.servlet.user;

import ru.ylab.service.UserService;
import ru.ylab.util.SingletonProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/viewMyInfo")
public class ViewMyInfoServlet extends HttpServlet {
    private final UserService userService;

    public ViewMyInfoServlet() {
        this.userService = SingletonProvider.getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userService.viewMyInfo(resp);
    }
}
