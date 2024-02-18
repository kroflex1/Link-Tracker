package edu.java.bot.DAO;

import edu.java.bot.model.UserModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserDAO implements UserDAO {

    private static final Map<Long, UserModel> users = new HashMap<>();
    private final static String NO_USER_WITH_ID_MESSAGE = "User with this ID has not been detected";
    private final static String ALREADY_REGISTERED_USER_MESSAGE = "User with this ID is already in the database";

    @Override
    public void addUser(Long userId) throws IllegalArgumentException {
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException(ALREADY_REGISTERED_USER_MESSAGE);
        } else {
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
        if (users.containsKey(userId)) {
            users.get(userId).addLink(url);
        } else {
            throw new IllegalArgumentException(NO_USER_WITH_ID_MESSAGE);
        }
    }

    @Override
    public void removeTrackedLink(Long userId, String url) throws IllegalArgumentException {
        if (users.containsKey(userId)) {
            users.get(userId).removeLink(url);
        } else {
            throw new IllegalArgumentException(NO_USER_WITH_ID_MESSAGE);
        }
    }
}
