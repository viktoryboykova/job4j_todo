package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.model.Category;
import ru.job4j.model.Item;
import ru.job4j.store.HbmStore;
import ru.job4j.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class IndexServlet extends HttpServlet {

    private HbmStore hbmStore = new HbmStore();
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected  void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Item> tasks = hbmStore.findAll();
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(tasks);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected  void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String taskDescription = req.getParameter("task");
        String creator = req.getParameter("userName");
        User userCreator = hbmStore.findUserByName(creator);
        Item item = new Item(taskDescription, false, userCreator);
        String[] cIds = req.getParameterValues("cIds");
        for (String id : cIds) {
            Category category = hbmStore.findCategoryById(Integer.parseInt(id));
            item.addCategory(category);
        }
        hbmStore.add(item);
        resp.sendRedirect("http://localhost:8080/todo/index.html");
    }
}
