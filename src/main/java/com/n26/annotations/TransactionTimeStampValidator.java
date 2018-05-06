package com.n26.annotations;

import com.n26.dto.Transaction;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TransactionTimeStampValidator implements ConstraintValidator<TimeStampTransaction, Transaction> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(TimeStampTransaction constraintAnnotation) {}

    @Override
    public boolean isValid(Transaction transaction, ConstraintValidatorContext constraintValidatorContext) {
        return !transaction.isExpired();
    }

}
