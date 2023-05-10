package jasyncapicmp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class OAuthFlow implements Model {
    private String authorizationUrl;
    private String tokenUrl;
    private String refreshUrl;
    @Version(since = Version.Versions.V3_0_0)
    private Map<String, String> availableScopes = new HashMap<>();
    @Version(until = Version.Versions.V2_6_0)
    private List<String> scopes = new ArrayList<>();
}
