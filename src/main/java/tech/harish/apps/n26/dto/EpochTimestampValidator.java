package tech.harish.apps.n26.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EpochTimestampValidator implements ConstraintValidator<PastTimestamp,Long> {

    @Override
    public boolean isValid(Long target, ConstraintValidatorContext context) {
        return target!=null && target < System.currentTimeMillis();
    }
}
