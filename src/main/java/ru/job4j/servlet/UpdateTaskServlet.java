package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.model.Item;
import ru.job4j.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateTaskServlet extends HttpServlet {

    private HbmStore hbmStore = new HbmStore();
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected  void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("taskForUpdate");
        String checked = req.getParameter("checked");
        Item item = hbmStore.findById(Integer.parseInt(id));
        if (checked.equals("true")) {
            item.setDone(true);
        } else if (checked.equals("false")) {
            item.setDone(false);
        }
        hbmStore.update(Integer.parseInt(id), item);
        resp.sendRedirect("http://localhost:8080/todo/index.html");
    }
}
