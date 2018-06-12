package services;

import models.entities.TokenAction;
import models.entities.UserEntity;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;

public class TokenActionService implements ITokenActionService {
    private final JPAApi JpAApi;

    @Inject
    public TokenActionService(JPAApi jpaApi) {
        JpAApi = jpaApi;
    }

    @Override
    public TokenAction findByToken(final String token, final TokenAction.Type type) {
        TypedQuery<TokenAction> query = JpAApi.em()
                .createQuery("from TokenAction where token = :token and type = :type", TokenAction.class);
        query.setParameter("token", token);
        query.setParameter("type", type);
        return query.getSingleResult();
    }

    @Override
    public void deleteByUser(final UserEntity u, final TokenAction.Type type) {
        Query query = JpAApi.em().createQuery("delete from TokenAction where targetUser.id=:userId and type=:type");
        query.setParameter("userId", u.getId());
        query.setParameter("type", type);
        query.executeUpdate();
    }

    @Override
    public TokenAction create(final TokenAction.Type type, final String token,
                              final UserEntity targetUser) {
        final TokenAction ua = new TokenAction();
        ua.targetUser = targetUser;
        ua.token = token;
        ua.type = type;
        final Date created = new Date();
        ua.created = created;
        ua.expires = new Date(created.getTime() + TokenAction.VERIFICATION_TIME * 1000);
        JpAApi.em().persist(ua);
        return ua;
    }
}
