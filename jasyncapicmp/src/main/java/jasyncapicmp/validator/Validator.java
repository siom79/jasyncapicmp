package jasyncapicmp.validator;

import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.model.Info;
import jasyncapicmp.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Validator {

	public List<ValidationError> validate(AsyncApi asyncApi) {
		List<ValidationError> validationErrors = new ArrayList<>();
		validateAsyncApi(validationErrors, asyncApi);
		return validationErrors;
	}

	private static void validateAsyncApi(List<ValidationError> validationErrors, AsyncApi asyncApi) {
		if (StringUtils.isEmpty(asyncApi.getAsyncapi())) {
			addValidationError(validationErrors, "asyncapi", AsyncApi.class.getSimpleName());
		}
		if (asyncApi.getInfo() == null) {
			addValidationError(validationErrors, "info", AsyncApi.class.getSimpleName());
		} else {
			validateInfo(validationErrors, asyncApi.getInfo());
		}
	}

	private static void addValidationError(List<ValidationError> validationErrors, String field, String objectType) {
		validationErrors.add(ValidationError.builder()
			.errorType(ValidationError.ValidationErrorType.REQUIRED)
			.objectType(objectType)
			.field(field)
			.build());
	}

	private static void validateInfo(List<ValidationError> validationErrors, Info info) {
		if (StringUtils.isEmpty(info.getTitle())) {
			addValidationError(validationErrors, "title", Info.class.getSimpleName());
		}
		if (StringUtils.isEmpty(info.getVersion())) {
			addValidationError(validationErrors, "version", Info.class.getSimpleName());
		}
	}
}
