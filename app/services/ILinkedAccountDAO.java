package services;

import com.feth.play.module.pa.user.AuthUser;
import models.entities.LinkedAccount;
import models.entities.UserEntity;

public interface ILinkedAccountDAO {
    LinkedAccount findByProviderKey(UserEntity user, String key);

    LinkedAccount create(AuthUser authUser);

    void update(LinkedAccount linkedAccount, AuthUser authUser);

    LinkedAccount create(LinkedAccount acc);
}
