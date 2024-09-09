package jasyncapicmp.validator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ValidationError {

	public enum ValidationErrorType {
		REQUIRED
	}

	private ValidationErrorType errorType;
	private String objectType;
	private String field;
}
