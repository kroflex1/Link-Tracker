package edu.java.bot.dao;

import edu.java.bot.model.UserModel;
import java.util.Optional;

public interface UserDAO {
    void addUser(Long userId);

    Optional<UserModel> getUserByChatId(Long userId);

    void addTrackedLink(Long userId, String url);

    void removeTrackedLink(Long userId, String url);
}
