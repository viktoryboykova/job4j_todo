package ru.job4j.servlet;

import ru.job4j.store.HbmStore;
import ru.job4j.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    private HbmStore hbmStore = new HbmStore();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        User user = hbmStore.findUserByName(name);
        if (user != null) {
            req.setAttribute("error", "Пользователь с таким именем уже существует");
            resp.sendRedirect("http://localhost:8080/todo/reg.html");
            return;
        }
        User newUser = User.of(name);
        hbmStore.add(newUser);
        resp.sendRedirect("http://localhost:8080/todo/auth.html");
    }
}
