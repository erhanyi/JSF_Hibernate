package com.erhan.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(value={ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=TCKimlikNoValidator.class)
public @interface TCKimlikNo {
 String message() default "{validation.identityNo}";

 Class<?>[] groups() default {};

 Class<? extends Payload>[] payload() default {};
}
