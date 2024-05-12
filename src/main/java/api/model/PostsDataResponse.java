package api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PostsDataResponse {

    private Integer userId;
    private Integer id;
    private String title;
    private String body;
}