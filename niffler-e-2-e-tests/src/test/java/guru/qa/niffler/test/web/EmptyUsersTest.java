package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(1)
public class EmptyUsersTest {

    @Test
    @User
    void checkIfUsersListIsEmptyTest(UserJson user){
        UsersApiClient usersApiClient = new UsersApiClient();
        List<UserJson> response = usersApiClient.getAllUsers(user.username(),null);
        assertTrue(response.isEmpty(),"The user list must be empty.");
    }
}