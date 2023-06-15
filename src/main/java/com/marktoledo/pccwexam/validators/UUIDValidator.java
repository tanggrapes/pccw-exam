package com.marktoledo.pccwexam.validators;

import com.marktoledo.pccwexam.annotations.ValidUUID;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class UUIDValidator implements ConstraintValidator<ValidUUID, Object> {
    private final String regex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";//

    @Override
    public void initialize(ValidUUID validUUID) {
    }

    @Override
    public boolean isValid(Object id, ConstraintValidatorContext cxt) {
        if(id instanceof String){
            return id.toString().matches(this.regex);
        }
        List<String> ids = (List<String>) id;

        for(String _id: ids){
            if(!_id.matches(this.regex)){
                return false;
            }
        }
        return true;
    }
}
