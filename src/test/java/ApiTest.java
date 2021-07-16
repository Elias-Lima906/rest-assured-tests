import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class ApiTest {

    public static final String STANDARD_URL = "https://api.thecatapi.com/v1";
    public static final String APPLICATION_JSON = "application/json";
    public static final String SUCCESS_MESSAGE = "SUCCESS";
    public static final int STATUS_CODE = 200;
    public static final String API_KEY = "cefbe722-6253-4c52-9ccc-598db3f3faca";


    @BeforeClass
    public static void urlBase() {
        RestAssured.baseURI = STANDARD_URL;
    }

    @Test
    public void signIn() {
        String url = "/user/passwordlesssignup";
        String body = "{\"email\": \"eliaslima541@hotmail.com\", \"appDescription\": \"teste the cat api\"}";

        Response response = given()
                .contentType(APPLICATION_JSON)
                .body(body)
                .when()
                .post(url);

        validacao(response);
    }

    @Test
    public void voteSequenceTest() {
        String voteId = vote();
        deleteVote(voteId);
    }

    @Test
    public void favoriteSequenceTest() {
        String favouriteId = addFavoriteImage();
        deleteFavoriteImage(favouriteId);
    }

    private String vote() {
        String url = "/votes/";
        String body = "{\"image_id\": \"G__PvOxyd\", \"sub_id\": \"demo-aa8680\", \"value\": true}";

        Response response = getResponse(url, body);

        validacao(response);

        return response
                .jsonPath()
                .getString("id");
    }

    private void deleteVote(String voteId) {
        String url = "/votes/{vote_id}";
        Response response = getResponsePathParam(url, "vote_id", voteId);
        validacao(response);
    }

    private String addFavoriteImage() {
        String url = "/favourites";
        String body = "{\"image_id\": \"G__PvOxyd\", \"sub_id\": \"demo-aa8680\"}";

        Response response = getResponse(url, body);

        validacao(response);

        return response
                .jsonPath()
                .getString("id");
    }

    private void deleteFavoriteImage(String favouriteId) {
        String url = "/favourites/{favourite_id}";
        Response response = getResponsePathParam(url, "favourite_id", favouriteId);
        validacao(response);
    }

    private void validacao(Response response) {
        response.then().body("message", containsString(SUCCESS_MESSAGE)).statusCode(STATUS_CODE);
        System.out.println("\nRETORNO DA API -:> " + response.body().asString() + "\nSTATUS_CODE: " + response.statusCode());
    }

    private Response getResponse(String url, String body) {
        return given()
                .contentType(APPLICATION_JSON)
                .header("x-api-key", API_KEY)
                .body(body)
                .when()
                .post(url);
    }

    private Response getResponsePathParam(String url, String path, String id) {
        return given()
                .contentType(APPLICATION_JSON)
                .header("x-api-key", API_KEY)
                .pathParam(path, id)
                .when()
                .delete(url);
    }
}
