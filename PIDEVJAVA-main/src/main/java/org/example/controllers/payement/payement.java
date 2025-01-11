package org.example.controllers.payement;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;

public class payement {
    public TextField cardNumberField;
    public TextField expiryDateField;
    public TextField cvcField;

    public void processPayment() {
        String cardNumber = cardNumberField.getText();
        String expiryDate = expiryDateField.getText();
        String cvc = cvcField.getText();

        if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvc.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        // Validating card details
        if (!isValidCardDetails(cardNumber, expiryDate, cvc)) {
            showAlert("Error", "Invalid card details.");
            return;
        }

        Stripe.apiKey = "sk_test_51OnfjpIUcD4J9UQYS32pfvQXJMAtOffv9lzsZaxmCk4GHyezuQ8PTS0bVlmtVeMor0Xw7uNN6BA8i34LEVtczCSt00YL6gLVDj";

        try {
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", 2000); // Montant en cents
            chargeParams.put("currency", "usd");
            chargeParams.put("source", "tok_visa"); // Utilisez un numéro de carte de test fourni par Stripe
            chargeParams.put("description", "Test Charge");

            Charge.create(chargeParams);

            showAlertSuccess("Success", "Payment successful!");
        } catch (StripeException e) {
            e.printStackTrace();
            showAlert("Error", "Payment failed. Please try again later.");
        }
    }

    private boolean isValidCardDetails(String cardNumber, String expiryDate, String cvc) {
        // Remove all non-digit characters from card number
        String sanitizedCardNumber = cardNumber.replaceAll("[^\\d]", "");

        // Check if card number is of valid length (between 13 and 19 digits)
        if (sanitizedCardNumber.length() < 13 || sanitizedCardNumber.length() > 19) {
            return false;
        }

        // Perform Luhn algorithm validation for card number
        if (!isValidLuhn(sanitizedCardNumber)) {
            return false;
        }

        // Check if expiry date is in the format MM/YY
        if (!expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            return false;
        }

        // Check if CVC is 3 or 4 digits
        if (!cvc.matches("\\d{3,4}")) {
            return false;
        }

        return true;
    }

    // Luhn algorithm implementation
    private boolean isValidLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlertSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
