package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.store.HbmStore;
import ru.job4j.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    private final HbmStore hbmStore = new HbmStore();
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        User user = hbmStore.findUserByName(name);
        if (user != null) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            resp.sendRedirect("http://localhost:8080/todo/index.html");
        } else {
            req.setAttribute("error", "Не верный email или пароль");
            resp.sendRedirect("http://localhost:8080/todo/auth.html");
        }
    }
}
