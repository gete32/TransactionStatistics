package com.n26.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TransactionTimeStampValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeStampTransaction {

    String message() default "{Wrong transaction}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
