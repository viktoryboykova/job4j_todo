package ru.job4j.store;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Category;
import ru.job4j.model.Item;
import ru.job4j.model.Role;
import ru.job4j.model.User;

import java.util.List;
import java.util.function.Function;

public class HbmStore implements Store {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item, String[] ids) {
        return this.tx(
                session -> {
                    for (String id : ids) {
                        Category category = session.find(Category.class, Integer.parseInt(id));
                        item.addCategory(category);
                    }
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
                session -> session.createQuery(
                            "select distinct c from Item c join fetch c.categories"
                    ).list()

        );
    }

    @Override
    public List<Category> findAllCategories() {
        return this.tx(
                session -> session.createQuery("from ru.job4j.model.Category").list()
        );
    }

    @Override
    public User add(User user) {
        return this.tx(
                session -> {
                    session.save(user);
                    return user;
                }
        );
    }

    @Override
    public User findUserByName(String name) {
        return this.tx(
              session -> {
                  List<User> result = session.createQuery("from ru.job4j.model.User where name =:name ")
                          .setParameter("name", name)
                          .list();
                  if (result.isEmpty()) {
                      return  null;
                  }
                  return result.get(0);
              }
        );
    }

    @Override
    public Role findRoleById(int id) {
        return this.tx(
                session -> session.get(Role.class, id)
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
