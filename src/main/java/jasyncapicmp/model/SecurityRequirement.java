package jasyncapicmp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class SecurityRequirement {
    List<String> userPassword = new ArrayList<>();
    List<String> apiKey = new ArrayList<>();
    List<String> X509 = new ArrayList<>();
    List<String> symmetricEncryption = new ArrayList<>();
    List<String> asymmetricEncryption = new ArrayList<>();
    List<String> httpApiKey = new ArrayList<>();
    List<String> http = new ArrayList<>();
    List<String> oauth2 = new ArrayList<>();
    List<String> openIdConnect = new ArrayList<>();
    List<String> plain = new ArrayList<>();
    List<String> scramSha256 = new ArrayList<>();
    List<String> scramSha512 = new ArrayList<>();
    List<String> gssapi = new ArrayList<>();
}
