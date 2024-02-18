package edu.java.bot.DAO;

import edu.java.bot.model.UserModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserDAO implements UserDAO {

    private static final Map<Long, UserModel> users = new HashMap<>();

    @Override
    public void addUser(Long userId) {
        if (!users.containsKey(userId)) {
            users.put(userId, new UserModel(userId));
        }
    }

    @Override
    public Optional<UserModel> getUserById(Long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        }
        return Optional.empty();
    }

    @Override
    public void addTrackedLink(Long userId, String url) throws IllegalArgumentException {
        if (!users.containsKey(userId)) {
            addUser(userId);
        }
        users.get(userId).addLink(url);
    }

    @Override
    public void removeTrackedLink(Long userId, String url) {
        if (users.containsKey(userId)) {
            addUser(userId);
        }
        users.get(userId).removeLink(url);
    }
}
