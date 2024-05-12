import api.model.PostsDataResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class Task2Test {

    @Test
    public void sortTest() {
        step("Вывод топ-10 слов из post, которые чаще всего встречаются", () -> {
        List<PostsDataResponse> postsData = given()
                .when()
                .contentType(ContentType.JSON)
                .get(Task1Test.URL + "posts")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList("", PostsDataResponse.class);

        sortedList(postsData);
        });
    }

    private static void sortedList(List<PostsDataResponse> allElements) {
        List<Map<String, String>> listOfMaps = new ArrayList<>();

        for (int i = 0; i < allElements.size(); i++) {
            Map<String, String> item = new HashMap<>();
            item.put("body", allElements.get(i).getBody());
            listOfMaps.add(item);
        }

        Map<String, Integer> wordCounts = new HashMap<>();

        for (Map<String, String> item : listOfMaps) {
            String body = item.get("body").replace("\n"," ");
            String[] words = body.split("\\s+");
            for (String word : words) {
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordCounts.entrySet());
        sortedWords.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        System.out.println("Top 10 words:");
        for (int i = 0; i < Math.min(10, sortedWords.size()); i++) {
            String word = sortedWords.get(i).getKey();
            int count = sortedWords.get(i).getValue();
            System.out.println((i + 1) + ". " + word + " - " + count);
        }
    }
}