package guru.qa.niffler.service;

import guru.qa.niffler.model.rest.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UsersClient {
  @Nonnull
  UserJson createUser(String username, String password);

  void addIncomeInvitation(UserJson targetUser, int count);

  void addOutcomeInvitation(UserJson targetUser, int count);

  void addFriend(UserJson targetUser, int count);

  List<UserJson> getAllUsers(String username, @Nullable String searchQuery);
}
