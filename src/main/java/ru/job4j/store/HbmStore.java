package ru.job4j.store;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Item;

import java.util.List;
import java.util.function.Function;

public class HbmStore implements Store {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        return this.tx(
                session -> {
                    session.save(item);
                    return item;
                }
        );
    }

    @Override
    public Item findById(int id) {
        return this.tx(
                session -> session.get(Item.class, id)
        );
    }

    @Override
    public List<Item> findAll() {
        return this.tx(
                session -> session.createQuery("from ru.job4j.model.Item").list()
        );
    }

    @Override
    public void update(int id, Item item) {
        this.tx(
                session -> {
                    item.setId(id);
                    session.update(item);
                    return true;
                }
        );
    }

    private <T> T tx (final Function<Session, T> command) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            T rsl = command.apply(session);
            session.getTransaction().commit();
            return rsl;
        } catch (HibernateException e) {
            throw e;
        }
    }
}
