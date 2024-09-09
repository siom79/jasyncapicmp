package jasyncapicmp.validator;

import jasyncapicmp.model.AsyncApi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ValidatorTest {

	@Test
	void test() {
		Validator validator = new Validator();
		AsyncApi asyncApi = new AsyncApi();

		List<ValidationError> errors = validator.validate(asyncApi);

		Assertions.assertThat(errors).isNotEmpty();
		Assertions.assertThat((int) errors.stream().filter(e -> e.getObjectType().equals(AsyncApi.class.getSimpleName())).count()).isEqualTo(2);
		Assertions.assertThat(errors).contains(ValidationError.builder()
			.errorType(ValidationError.ValidationErrorType.REQUIRED)
			.objectType(AsyncApi.class.getSimpleName())
			.field("asyncapi")
			.build());
		Assertions.assertThat(errors).contains(ValidationError.builder()
			.errorType(ValidationError.ValidationErrorType.REQUIRED)
			.objectType(AsyncApi.class.getSimpleName())
			.field("info")
			.build());
	}
}
