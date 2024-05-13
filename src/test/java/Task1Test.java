import api.model.PostsDataRequest;
import api.model.PostsDataResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class Task1Test {

    public final static String URL = "https://jsonplaceholder.typicode.com/";

    private final static String titleOfFirstElement = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit";

    @Test
    public void readFirstTest() {
        step("Получение одного post", () -> {
            PostsDataResponse postsData = given()
                    .when()
                    .contentType(ContentType.JSON)
                    .get(URL + "posts/1")
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(PostsDataResponse.class);


            Assertions.assertEquals(1, postsData.getId(), "Был взят не первый элемент");
            Assertions.assertEquals(titleOfFirstElement, postsData.getTitle(), "Title не совпал");
        });
    }

    @Test
    public void readSecondTest() {
        step("Получение всех posts", () -> {
            List<PostsDataResponse> postsData = given()
                    .when()
                    .contentType(ContentType.JSON)
                    .get(URL + "posts")
                    .then().log().all()
                    .statusCode(200)
                    .extract().jsonPath().getList("", PostsDataResponse.class);

            Assertions.assertEquals(100, postsData.size(), "Пришли не все элементы");
        });
    }

    @Test
    public void createPostTest() {
        step("Создание post", () -> {
            PostsDataRequest postDataRequest = PostsDataRequest.builder()
                                                               .title("foo")
                                                               .body("bar")
                                                               .userId(1)
                                                               .build();

            PostsDataResponse createdPost = given()
                    .contentType(ContentType.JSON)
                    .body(postDataRequest)
                    .when()
                    .post(URL + "posts")
                    .then().log().all()
                    .statusCode(201)
                    .extract().as(PostsDataResponse.class);

            Assertions.assertEquals(1, createdPost.getUserId(), "UserId не совпадает");
            Assertions.assertEquals(101, createdPost.getId(), "Id не совпадает");
            Assertions.assertEquals("foo", createdPost.getTitle(), "Title не совпадает");
            Assertions.assertEquals("bar", createdPost.getBody(), "Body не совпадает");
        });
    }

    @Test
    public void putPostTest() {
        step("Обновление всего post", () -> {
            PostsDataRequest postDataRequest = PostsDataRequest.builder()
                                                               .title("foo")
                                                               .body("bar")
                                                               .userId(4)
                                                               .build();

            PostsDataResponse updatedPost = given()
                    .contentType(ContentType.JSON)
                    .body(postDataRequest)
                    .when()
                    .put(URL + "posts/1")
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(PostsDataResponse.class);

            Assertions.assertEquals(1, updatedPost.getId(), "Id не совпадает");
            Assertions.assertEquals("foo", updatedPost.getTitle(), "Title не совпадает");
            Assertions.assertEquals("bar", updatedPost.getBody(), "Body не совпадает");
            Assertions.assertEquals(4, updatedPost.getUserId(), "UserId не совпадает");
        });
    }

    @Test
    public void patchPostTest() {
        step("Обновление части post", () -> {
            PostsDataRequest postDataRequest = PostsDataRequest.builder()
                                                               .title("foo")
                                                               .userId(16)
                                                               .build();

            PostsDataResponse updatedPost = given()
                    .contentType(ContentType.JSON)
                    .body(postDataRequest)
                    .when()
                    .patch(URL + "posts/1")
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(PostsDataResponse.class);

            Assertions.assertEquals(1, updatedPost.getId(), "Id не совпадает");
            Assertions.assertEquals("foo", updatedPost.getTitle(), "Title не совпадает");
            Assertions.assertEquals(16, updatedPost.getUserId(), "UserId не совпадает");
        });
    }

    @Test
    public void deletePostTest() {
        step("Удаление post", () -> {
            given()
                    .when()
                    .delete(URL + "posts/1")
                    .then().log().all()
                    .statusCode(200);
        });
    }
}