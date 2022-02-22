package ru.job4j.store;

import ru.job4j.model.Category;
import ru.job4j.model.Item;
import ru.job4j.model.Role;
import ru.job4j.model.User;

import java.util.List;

public interface Store {
    Item add(Item item, String[] ids);
    Item findById(int id);
    void update(int id, Item item);
    List<Item> findAll();
    List<Category> findAllCategories();
    User add(User user);
    User findUserByName(String name);
    Role findRoleById(int id);
}
