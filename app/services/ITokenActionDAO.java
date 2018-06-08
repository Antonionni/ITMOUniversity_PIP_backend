package services;

import models.entities.TokenAction;
import models.entities.UserEntity;

public interface ITokenActionDAO {
    TokenAction findByToken(String token, TokenAction.Type type);

    void deleteByUser(UserEntity u, TokenAction.Type type);

    TokenAction create(TokenAction.Type type, String token,
                       UserEntity targetUser);
}
