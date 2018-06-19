package services;

import com.feth.play.module.pa.user.AuthUser;
import models.entities.LinkedAccount;
import models.entities.UserEntity;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import javax.persistence.TypedQuery;

public class LinkedAccountService extends BaseService implements ILinkedAccountService {
    @Inject
    public LinkedAccountService(JPAApi jpaApi, HttpExecutionContext ec) {
        super(jpaApi, ec);
    }

    @Override
    public LinkedAccount findByProviderKey(final UserEntity user, String key) {
        return wrap(em -> {
            TypedQuery<LinkedAccount> query = em
                    .createQuery("from LinkedAccount where user=:user and providerKey=:key", LinkedAccount.class);
            query.setParameter("user", user);
            query.setParameter("key", key);
            return query.getSingleResult();
        });
    }

    @Override
    public LinkedAccount create(final AuthUser authUser) {
        return wrap(em -> {
            final LinkedAccount ret = new LinkedAccount();
            update(ret, authUser);
            return ret;
        });
    }

    @Override
    public void update(LinkedAccount linkedAccount, final AuthUser authUser) {
        wrap(em -> {
            linkedAccount.setProviderKey(authUser.getProvider());
            linkedAccount.setProviderUserId(authUser.getId());
            return linkedAccount;
        });
    }

    @Override
    public LinkedAccount create(final LinkedAccount acc) {
        return wrap(em -> {
            final LinkedAccount ret = new LinkedAccount();
            ret.setProviderKey(acc.getProviderKey());
            ret.setProviderUserId(acc.getProviderUserId());
            return ret;
        });
    }
}
