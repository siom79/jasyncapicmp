package jasyncapicmp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageExample implements Model {
    private Object headers;
    private Object payload;
    @ListId
    private String name;
    private String summary;
}
