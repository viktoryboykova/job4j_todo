package ru.job4j.store;

import ru.job4j.model.Item;

import java.util.List;

public interface Store {
    Item add(Item item);
    Item findById(int id);
    void update(int id, Item item);
    List<Item> findAll();
}
