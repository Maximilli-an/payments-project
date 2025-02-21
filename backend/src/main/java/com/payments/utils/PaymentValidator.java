package com.payments.utils;

import com.payments.dataModel.Payment;

import java.util.regex.Pattern;

public class PaymentValidator {
    private static final Pattern NAME_CONVENTION = Pattern.compile("^[A-Za-z'-]+$");
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{16,19}$");


    public static void validateNames(String firstName, String lastName) {
        if (!NAME_CONVENTION.matcher(firstName).matches()) {
            throw new IllegalArgumentException("Invalid first name, Only letters, apostrophes(') and dashes(-) are allowed.");
        }
        if (!NAME_CONVENTION.matcher(lastName).matches()) {
            throw new IllegalArgumentException("Invalid last name. Only letters, apostrophes(') and dashes(-) are allowed.");
        }
    }

    public static void validateCardNumber(String cardNumber) {
        if (!CARD_NUMBER_PATTERN.matcher(cardNumber).matches()) {
            throw new IllegalArgumentException("Your credit card number is invalid, must be 16-19 digits and only numbers.");
        }
    }

    public static void validatePayment(Payment payment) {
        validateNames(payment.getFirstName(), payment.getLastName());
        validateZipCode(payment.getZipCode());
        validateCardNumber(payment.getCardNumber());
    }

    public static void validateZipCode(String zipCode) {
        if (zipCode == null || zipCode.isBlank()) {
            throw new IllegalArgumentException("Zip code is required");
        }
    }
}
