package services;

import data.HibernateUtils;
import models.entities.TokenAction;
import models.entities.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;

public class TokenActionDAO {
    private EntityManager entityManager = HibernateUtils.getSessionFactory().createEntityManager();

    public TokenAction findByToken(final String token, final TokenAction.Type type) {
        TypedQuery<TokenAction> query = entityManager
                .createQuery("select TokenAction from TokenAction where token=:token and type=:type", TokenAction.class);
        query.setParameter("token", token);
        query.setParameter("type", type);
        return query.getSingleResult();
    }

    public void deleteByUser(final UserEntity u, final TokenAction.Type type) {
        Query query = entityManager.createQuery("delete from TokenAction where targetUser.id=:userId and type=:type");
        query.setParameter("userId", u.getId());
        query.setParameter("type", type);
        query.executeUpdate();
    }

    public TokenAction create(final TokenAction.Type type, final String token,
                                     final UserEntity targetUser) {
        final TokenAction ua = new TokenAction();
        ua.targetUser = targetUser;
        ua.token = token;
        ua.type = type;
        final Date created = new Date();
        ua.created = created;
        ua.expires = new Date(created.getTime() + TokenAction.VERIFICATION_TIME * 1000);
        entityManager.persist(ua);
        return ua;
    }
}
