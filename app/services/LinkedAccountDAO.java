package services;

import com.feth.play.module.pa.user.AuthUser;
import data.HibernateUtils;
import models.entities.LinkedAccount;
import models.entities.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class LinkedAccountDAO {
    private EntityManager entityManager = HibernateUtils.getSessionFactory().createEntityManager();

    public LinkedAccount findByProviderKey(final UserEntity user, String key) {
        TypedQuery<LinkedAccount> query = entityManager
                .createQuery("select LinkedAccount from LinkedAccount where user=:user and providerKey=:key", LinkedAccount.class);
        query.setParameter("user", user);
        query.setParameter("key", key);
        return query.getSingleResult();
    }

    public LinkedAccount create(final AuthUser authUser) {
        final LinkedAccount ret = new LinkedAccount();
        update(ret, authUser);
        return ret;
    }

    public void update(LinkedAccount linkedAccount, final AuthUser authUser) {
        linkedAccount.setProviderKey(authUser.getProvider());
        linkedAccount.setProviderUserId(authUser.getId());
    }

    public LinkedAccount create(final LinkedAccount acc) {
        final LinkedAccount ret = new LinkedAccount();
        ret.setProviderKey(acc.getProviderKey());
        ret.setProviderUserId(acc.getProviderUserId());
        return ret;
    }
}
