//package com.ibm.webdev.app.jersey.start.config;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//@ControllerAdvice
//@RestController
//public class MethodArgumentNotValidExceptionHandler {
//	
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Error onException(MethodArgumentNotValidException e) {
//        List<FieldError> fieldErrors = e.getFieldErrors();
//        List<String> messages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
//        String message = messages.toString();//StringUtils.join(messages, ";");
//        List<ErrorDetails> errors = fieldErrors.stream().map(
//                fieldError -> new ErrorDetails(
//                        fieldError.getObjectName(),
//                        fieldError.getField(),
//                        fieldError.getCode(),
//                        fieldError.getDefaultMessage()
//                )
//        ).collect(Collectors.toList());
//        return new Error(message, errors);
//    }
//
//    static class Error {
//        private final String exception = MethodArgumentNotValidException.class.getSimpleName();
//        private final String message;
//        private final List<ErrorDetails> errors;
//        public Error(String message, List<ErrorDetails> errors) {
//        	this.message = message;
//        	this.errors = errors;
//        }
//		public String getException() {
//			return exception;
//		}
//		public String getMessage() {
//			return message;
//		}
//		public List<ErrorDetails> getErrors() {
//			return errors;
//		}
//        
//    }
//
//    static class ErrorDetails {
//        public ErrorDetails(String objectName2, String field2, String code2, String defaultMessage) {
//        	objectName = objectName2;
//        	field = field2;
//        	code = code2;
//        	message = defaultMessage;
//		}
//		private final String objectName;
//        private final String field;
//        private final String code;
//        private final String message;
//
//    }
//}
