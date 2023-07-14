package jasyncapicmp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Contact implements Model {
    private String name;
    private String url;
    private String email;
}
