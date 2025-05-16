package cloud.verbatim.client.demo;

import cloud.verbatim.client.auth.Key;
import cloud.verbatim.client.auth.KeyLoader;
import cloud.verbatim.client.auth.Session;
import cloud.verbatim.client.resteasy.api.AuthenticateApi;
import cloud.verbatim.client.resteasy.invoker.ApiClient;
import cloud.verbatim.client.resteasy.invoker.ApiException;
import cloud.verbatim.client.resteasy.invoker.Configuration;
import cloud.verbatim.client.resteasy.invoker.auth.HttpBearerAuth;
import cloud.verbatim.client.resteasy.models.TokenInfo;

/**
 * The WhoAmIExample class demonstrates the process of authenticating with a remote API service
 * using a provided key file and making requests to retrieve user-specific token information.
 * <a href="https://www.verbatim.cloud/api-docs/index.html#tag/Authenticate/operation/whoami">OpenAPI specs here</a>
 * <p>
 * This class includes the following key steps:
 * 1. Loading a security key from a local resource file (key.json).
 * 2. Initializing a session with the loaded key for authenticated communication.
 * 3. Configuring the API client for bearer token-based authentication.
 * 4. Using the Authenticate API to retrieve token metadata such as the organization ID.
 * 5. Handling errors gracefully in case of API call failures.
 * <p>
 * Key Considerations:
 * - The key file must be replaced with a valid key.json file to authenticate successfully.
 * - Never expose sensitive authentication information as part of client-facing applications.
 * - Follow the OpenAPI specification provided by the service to understand API endpoints and payload requirements.
 */
public class WhoAmIExample {

    public static void main(String[] args) {

        ///  First, we load a session with the key.json.
        ///  /!\ Replace src/main/resources/key.json file with your key.json.
        ///  Download your key.json from your console int https://console.verbatim.cloud > Keys
        Key key = new KeyLoader().from(WhoAmIExample.class.getResourceAsStream("/key.json")).get();

        ///  Init the session
        Session.init(key);

        ///  Init the API Client
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api.verbatim.cloud");

        /// Configure HTTP bearer authorization: JWT from Session
        HttpBearerAuth JWT = (HttpBearerAuth) defaultClient.getAuthentication("JWT");
        JWT.setBearerToken(Session.getToken());

        /// Init the Authenticate API
        AuthenticateApi authenticateApi = new AuthenticateApi(defaultClient);
        try {
            ///  Fire API call
            ///  OpenAPI spec here
            /// https://www.verbatim.cloud/api-docs/index.html#tag/Authenticate/operation/whoami
            TokenInfo tokenInfo = authenticateApi.whoami();

            ///  Sounds good! The API succeeds!
            System.out.println("WhoAmI Api succeed");
            System.out.println(tokenInfo.getOrganizationId());
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticateApi#whoami");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
