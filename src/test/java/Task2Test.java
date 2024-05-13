import api.model.PostsDataResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.qameta.allure.Allure.addAttachment;
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

            Map<String, Integer> wordCounts = sortedList(postsData);

            StringBuilder message = new StringBuilder("Top 10 words:\n");
            int count = 1;
            for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                if (count > 10) break;
                message.append(count).append(". ").append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
                count++;
            }

            addAttachment("Top 10 Words", message.toString());
        });
    }

    private static Map<String, Integer> sortedList(List<PostsDataResponse> allElements) {
        List<Map<String, String>> listOfMaps = new ArrayList<>();

        for (int i = 0; i < allElements.size(); i++) {
            Map<String, String> item = new HashMap<>();
            item.put("body", allElements.get(i).getBody());
            listOfMaps.add(item);
        }

        Map<String, Integer> wordCounts = new HashMap<>();

        for (Map<String, String> item : listOfMaps) {
            String body = item.get("body").replace("\n", " ");
            String[] words = body.split("\\s+");
            for (String word : words) {
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordCounts.entrySet());
        sortedWords.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        Map<String, Integer> top10Words = new LinkedHashMap<>();

        for (int i = 0; i < Math.min(10, sortedWords.size()); i++) {
            Map.Entry<String, Integer> entry = sortedWords.get(i);
            top10Words.put(entry.getKey(), entry.getValue());
        }

        return top10Words;
    }
}