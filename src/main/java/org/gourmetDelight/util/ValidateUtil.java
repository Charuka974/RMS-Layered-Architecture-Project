package org.gourmetDelight.util;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidateUtil {

    public void isValidEmail(String email, JFXTextField cusEmailTxt) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

        if (email.matches(emailRegex)) {
            cusEmailTxt.setStyle(cusEmailTxt.getStyle()+"-jfx-focus-color: #4059a9;");
        } else {
            cusEmailTxt.setStyle(cusEmailTxt.getStyle()+"-jfx-focus-color: red;");
            cusEmailTxt.requestFocus();
        }
    }

    public void isValidPhone(String phone, JFXTextField cusPhoneTxt) {
        String phoneRegex = "^(07[01245678])[0-9]{7}$";

        if (phone.matches(phoneRegex)) {
            cusPhoneTxt.setStyle(cusPhoneTxt.getStyle()+"-jfx-focus-color: #4059a9;");
        } else {
            cusPhoneTxt.setStyle(cusPhoneTxt.getStyle()+"-jfx-focus-color: red;");
            cusPhoneTxt.requestFocus();
        }
    }

    public void isValidAddress(String address, JFXTextField cusAddressTxt) {
        String addressRegex = "^(No\\.?\\s?\\d{1,4}\\/?\\d{0,2})?,?\\s?([\\w\\s,'-]+)?,?\\s?([\\w\\s-]+)?,?\\s?(\\d{5})?$";
       //ex:
//        No. 45/1, Temple Lane, Colombo 07, 00100
//        123 Galle Road, Matara
//        Temple Road, Kandy, 20000
//        No. 4-2, Fort, Colombo

        if (address.matches(addressRegex)) {
            cusAddressTxt.setStyle(cusAddressTxt.getStyle()+"-jfx-focus-color: #4059a9;");
        } else {
            cusAddressTxt.setStyle(cusAddressTxt.getStyle()+"-jfx-focus-color: red;");
            cusAddressTxt.requestFocus();
        }
    }

    public void isValidName(String name, JFXTextField NameTxt) {
        String nameRegex = "^[A-Za-zÀ-ÖØ-öø-ÿ'’\\-.\\s]{1,50}$";

        if (name.matches(nameRegex)) {
            NameTxt.setStyle(NameTxt.getStyle()+"-jfx-focus-color: #4059a9;");
        } else {
            NameTxt.setStyle(NameTxt.getStyle()+"-jfx-focus-color: red;");
            NameTxt.requestFocus();
        }
    }

    public void isValidDescription(String description, JFXTextField Txt) {
        String descriptionRegex = "^[A-Za-zÀ-ÖØ-öø-ÿ'’\\-.\\s]{1,50}$";

        if (description.matches(descriptionRegex)) {
            Txt.setStyle(Txt.getStyle()+"-jfx-focus-color: #4059a9;");
        } else {
            Txt.setStyle(Txt.getStyle()+"-jfx-focus-color: red;");
            Txt.requestFocus();
        }
    }

    public void isValidJobPosition(String name, ChoiceBox<String> cusNameTxt) {
        String nameRegex = "^[A-Za-zÀ-ÖØ-öø-ÿ'’\\-.\\s]{1,50}$";

        if (name.matches(nameRegex)) {
            cusNameTxt.setStyle(cusNameTxt.getStyle()+"-jfx-focus-color: #4059a9;");
        } else {
            cusNameTxt.setStyle(cusNameTxt.getStyle()+"-jfx-focus-color: red;");
            cusNameTxt.requestFocus();
        }
    }

    public void isValidDate(String date, JFXTextField DateTxt) {
        String dateRegex = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

        if (date.matches(dateRegex)) {
            DateTxt.setStyle(DateTxt.getStyle()+"-jfx-focus-color: #4059a9;");
        } else {
            DateTxt.setStyle(DateTxt.getStyle()+"-jfx-focus-color: red;");
            DateTxt.requestFocus();
        }

    }


    public void isValidPrice(String price, JFXTextField priceTxt) {

        String priceRegex = "^\\d{1,7}(\\.\\d{1,2})?$"; // allows up to 7 digits before the decimal and 2 after

        if (price.matches(priceRegex)) {
            priceTxt.setStyle(priceTxt.getStyle() + "-jfx-focus-color: #4059a9;");
        } else {
            priceTxt.setStyle(priceTxt.getStyle() + "-jfx-focus-color: red;");
            priceTxt.requestFocus();
        }
    }


    public void isValidQuantity(String quantity, JFXTextField quantityTxt) {
        // Regular expression to match a positive integer with up to 7 digits
        String quantityRegex = "^\\d{1,7}$";

        if (quantity.matches(quantityRegex)) {
            quantityTxt.setStyle(quantityTxt.getStyle() + "-jfx-focus-color: #4059a9;");
        } else {
            quantityTxt.setStyle(quantityTxt.getStyle() + "-jfx-focus-color: red;");
            quantityTxt.requestFocus();
        }
    }


    public void isValidUsername(String username, JFXTextField usernameTxt) {
        String usernameRegex = "^[a-zA-Z][a-zA-Z0-9_-]{4,19}$"; // Starts with a letter, allows 4-19 more characters (letters, digits, _ or -)

        if (username.matches(usernameRegex)) {
            usernameTxt.setStyle(usernameTxt.getStyle() + "-jfx-focus-color: #4059a9;");
        } else {
            usernameTxt.setStyle(usernameTxt.getStyle() + "-jfx-focus-color: red;");
            usernameTxt.requestFocus();
        }
        /*
        Should be 5-20 characters long.
        Can include letters (both uppercase and lowercase), digits, underscores (_), and hyphens (-).
        Must start with a letter.*/
    }

    public void isValidPassword(String password, JFXTextField passwordTxt) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()]).{8,}$";
        // At least 8 characters, 1 lowercase, 1 uppercase, 1 digit, and 1 special character

        if (password.matches(passwordRegex)) {
            passwordTxt.setStyle(passwordTxt.getStyle() + "-jfx-focus-color: #4059a9;");
        } else {
            passwordTxt.setStyle(passwordTxt.getStyle() + "-jfx-focus-color: red;");
            passwordTxt.requestFocus();
        }

        /*
        Must be at least 8 characters long.
        Must include at least one uppercase letter, one lowercase letter,
        one digit, and one special character (!@#$%^&*()).
        */
    }


}