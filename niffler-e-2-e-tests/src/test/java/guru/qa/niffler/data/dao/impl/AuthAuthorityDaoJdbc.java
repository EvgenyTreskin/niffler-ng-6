package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
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
    public AuthAuthorityEntity create(AuthAuthorityEntity authAuthority) {

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO authority (user_id, authority) VALUES (?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setObject(1, authAuthority.getUser().getId());
            statement.setString(2, authAuthority.getAuthority().name());

            statement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Cant find id in ResultSet");
                }
            }
            authAuthority.setId(generatedKey);
            return authAuthority;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthAuthorityEntity> findById(UUID id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM authority WHERE id = ?"
        )) {
            statement.setObject(1, id);
            statement.execute();
            try (ResultSet rs = statement.getResultSet()) {
                if (rs.next()) {

                    AuthAuthorityEntity authAuthority = new AuthAuthorityEntity();

                    authAuthority.setId(rs.getObject("id", UUID.class));
                    authAuthority.setUser(rs.getObject("user_id", UserEntity.class));
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

    public Optional<AuthAuthorityEntity> findByUserId(UUID userId) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM authority WHERE user_id = ?"
        )) {
            statement.setObject(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {

                    AuthAuthorityEntity authAuthority = new AuthAuthorityEntity();

                    authAuthority.setId(rs.getObject("id", UUID.class));
                    authAuthority.setUser(rs.getObject("user_id", UserEntity.class));
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

    public void delete(AuthAuthorityEntity authAuthority) {
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
