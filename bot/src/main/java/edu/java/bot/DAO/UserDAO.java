package edu.java.bot.DAO;

import edu.java.bot.model.UserModel;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import java.util.Optional;

public interface UserDAO {
    void addUser(Long UserId);

    Optional<UserModel> getUserById(Long userId);

    void addTrackedLink(Long userId, String url);

    void removeTrackedLink(Long userId, String url);
}
