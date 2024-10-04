package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.spend.UserEntity;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUserId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthorityEntity> findById(UUID id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM authority WHERE id = ?"
        )) {
            statement.setObject(1, id);
            statement.execute();
            try (ResultSet rs = statement.getResultSet()) {
                if (rs.next()) {

                    AuthorityEntity authAuthority = new AuthorityEntity();

                    authAuthority.setId(rs.getObject("id", UUID.class));
                    authAuthority.setUserId(rs.getObject("user_id", UserEntity.class));
                    authAuthority.setAuthority(rs.getObject("authority", Authority.class));

                    return Optional.of(authAuthority);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<AuthorityEntity> findByUserId(UUID userId) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM authority WHERE user_id = ?"
        )) {
            statement.setObject(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {

                    AuthorityEntity authAuthority = new AuthorityEntity();

                    authAuthority.setId(rs.getObject("id", UUID.class));
                    authAuthority.setUserId(rs.getObject("user_id", UserEntity.class));
                    authAuthority.setAuthority(rs.getObject("authority", Authority.class));

                    return Optional.of(authAuthority);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(AuthorityEntity authAuthority) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM authority WHERE id = ?"
        )) {

            statement.setObject(1, authAuthority.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
