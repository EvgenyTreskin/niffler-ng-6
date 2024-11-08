package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.io.IOException;
import java.util.List;

public interface UsersClient {

    UserJson createUser(String username, String password) throws IOException;

    void addIncomeInvitation(UserJson targetUser, int count) throws IOException;

    void addOutcomeInvitation(UserJson targetUser, int count) throws IOException;

    void addFriend(UserJson targetUser, int count) throws IOException;

    List<String> addIncomeInvitationList(UserJson targetUser, int count) throws IOException;

    List<String> addOutcomeInvitationList(UserJson targetUser, int count) throws IOException;

    List<String> addFriendList(UserJson targetUser, int count) throws IOException;

}
