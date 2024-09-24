package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface GhApi {

  @GET("repos/EvgenyTreskin/niffler-ng-6/issues/{issue_number}")
  @Headers({
      "Accept: application/vnd.github+json",
//      "Authorization: Bearer YOUR_TOKEN (add GH private token at here to run test with issue blocking)
      "X-GitHub-Api-Version: 2022-11-28"
  })
  Call<JsonNode> issue(@Path("issue_number") String issueNumber);
  // you can also put token here with (@Header("Authorization")String YOUR_TOKEN)
// figure out how to add a token to the command line environment file in Windows (lesson3.2)
}
