/*
        Insecure Web App (IWA)

        Copyright (C) 2020 Micro Focus or one of its affiliates

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.microfocus.example.web.validation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.io.FileUtils;
import org.passay.CharacterRule;
import org.passay.DictionaryRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Custom Password Validator (using org.passay)
 * @author Kevin A. Lee
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private static final Logger log = LoggerFactory.getLogger(PasswordConstraintValidator.class);

    private DictionaryRule dictionaryRule;

    @Value("${app.invalidPasswordList}")
    private String invalidPasswordList = "/invalid-password-list.txt";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        FileReader[] fr = new FileReader[0];
        FileReader frPassList = null;
        try {
            //String filename = System.getProperty("com.microfocus.example.passwordList");
            //File dictionaryFile = new File(filename);
            //String invalidPasswordList = FileUtils.readFileToString(dictionaryFile);
            File invalidPasswordFile = new File(this.getClass().getResource(invalidPasswordList).getFile());
            frPassList = new FileReader(invalidPasswordFile);
            fr = new FileReader[] { frPassList };
            dictionaryRule = new DictionaryRule(
                    new WordListDictionary(WordLists.createFromReader(
                            // Reader around the word list file
                            fr,
                            // True for case sensitivity, false otherwise
                            false,
                            // Dictionaries must be sorted
                            new ArraysSort()
                    )));
        } catch (IOException e) {
            throw new RuntimeException("could not load word list", e);
        } finally {
            if (frPassList != null) {
                try {
                    frPassList.close();
                } catch (IOException ignored) {
                    log.error(ignored.getMessage());
                }
            }
        }
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password.isEmpty()) {
            return false;
        }
        PasswordValidator validator = new PasswordValidator(Arrays.asList(

                // at least 8 characters
                new LengthRule(8, 30),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // no whitespace
                new WhitespaceRule(),

                // no common passwords
                dictionaryRule
        ));

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = messages.stream().collect(Collectors.joining(","));
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
