package com.microfocus.example.web.validation;

import com.microfocus.example.web.form.PasswordForm;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordConstraintValidatorTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testInvalidPassword() {
        PasswordForm passwordForm = new PasswordForm();
        passwordForm.setUsername("test");
        passwordForm.setPassword("password");
        passwordForm.setConfirmPassword("password");

        Set<ConstraintViolation<PasswordForm>> constraintViolations = validator.validate(passwordForm);

        assertThat(constraintViolations.size()).isEqualTo(2);
    }

    @Test
    public void testValidPasswords() {
        PasswordForm passwordForm = new PasswordForm();
        passwordForm.setUsername("test");
        passwordForm.setPassword("xJ3!dij50");
        passwordForm.setConfirmPassword("xJ3!dij50");

        Set<ConstraintViolation<PasswordForm>> constraintViolations = validator.validate(passwordForm);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }
}
