package edu.java.bot.DAO;

import edu.java.bot.model.UserModel;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import java.util.Optional;

public interface UserDAO {
    void addUser(Long UserId) throws IllegalArgumentException;

    Optional<UserModel> getUserById(Long userId);

    void addTrackedLink(Long userId, String url) throws IllegalArgumentException;

    void removeTrackedLink(Long userId, String url) throws IllegalArgumentException;
}
