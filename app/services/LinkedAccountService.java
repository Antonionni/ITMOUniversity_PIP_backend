package services;

import com.feth.play.module.pa.user.AuthUser;
import models.entities.LinkedAccount;
import models.entities.UserEntity;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.TypedQuery;

public class LinkedAccountService implements ILinkedAccountService {
    private final JPAApi JpAApi;

    @Inject
    public LinkedAccountService(JPAApi jpaApi) {
        JpAApi = jpaApi;
    }

    @Override
    public LinkedAccount findByProviderKey(final UserEntity user, String key) {
        TypedQuery<LinkedAccount> query = JpAApi.em()
                .createQuery("from LinkedAccount where user=:user and providerKey=:key", LinkedAccount.class);
        query.setParameter("user", user);
        query.setParameter("key", key);
        return query.getSingleResult();
    }

    @Override
    public LinkedAccount create(final AuthUser authUser) {
        final LinkedAccount ret = new LinkedAccount();
        update(ret, authUser);
        return ret;
    }

    @Override
    public void update(LinkedAccount linkedAccount, final AuthUser authUser) {
        linkedAccount.setProviderKey(authUser.getProvider());
        linkedAccount.setProviderUserId(authUser.getId());
    }

    @Override
    public LinkedAccount create(final LinkedAccount acc) {
        final LinkedAccount ret = new LinkedAccount();
        ret.setProviderKey(acc.getProviderKey());
        ret.setProviderUserId(acc.getProviderUserId());
        return ret;
    }
}