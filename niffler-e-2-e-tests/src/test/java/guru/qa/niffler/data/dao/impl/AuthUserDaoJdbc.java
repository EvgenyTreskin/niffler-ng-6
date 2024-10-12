package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserDaoJdbc implements AuthUserDao {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public AuthUserEntity create(AuthUserEntity authUser) {
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired," +
                        "account_non_locked, credentials_non_expired) VALUES (?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, authUser.getUsername());
            statement.setString(2, pe.encode(authUser.getPassword()));
            statement.setBoolean(3, authUser.getEnabled());
            statement.setBoolean(4, authUser.getAccountNonExpired());
            statement.setBoolean(5, authUser.getAccountNonLocked());
            statement.setBoolean(6, authUser.getCredentialsNonExpired());

            statement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Cant find id in ResultSet");
                }
            }
            authUser.setId(generatedKey);
            return authUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            statement.setObject(1, id);
            statement.execute();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {

                    AuthUserEntity authUser = new AuthUserEntity();

                    authUser.setId(resultSet.getObject("id", UUID.class));
                    authUser.setUsername(resultSet.getString("username"));
                    authUser.setPassword(resultSet.getString("password"));
                    authUser.setEnabled(resultSet.getBoolean("enabled"));
                    authUser.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUser.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUser.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                    return Optional.of(authUser);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    AuthUserEntity authUser = new AuthUserEntity();

                    authUser.setId(resultSet.getObject("id", UUID.class));
                    authUser.setUsername(resultSet.getString("username"));
                    authUser.setPassword(resultSet.getString("password"));
                    authUser.setEnabled(resultSet.getBoolean("enabled"));
                    authUser.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUser.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUser.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                    return Optional.of(authUser);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(AuthUserEntity authUser) {
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )) {

            statement.setObject(1, authUser.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        List<AuthUserEntity> users = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement("SELECT * FROM \"user\"")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuthUserEntity user = new AuthUserEntity();
                    user.setId(rs.getObject("id", UUID.class));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
