package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityRowMapper implements RowMapper<AuthorityEntity> {
    public static final AuthAuthorityRowMapper instance = new AuthAuthorityRowMapper();

    private AuthAuthorityRowMapper() {
    }

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setUserId(rs.getObject("user_id", UUID.class));
        authority.setAuthority(Authority.valueOf(rs.getString("authority")));
        return authority;
    }
}
