package tech.harish.apps.n26.dto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy=EpochTimestampValidator.class)

public @interface PastTimestamp {

    String message();
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
