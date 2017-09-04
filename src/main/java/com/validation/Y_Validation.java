package com.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kirill.
 */
@FacesValidator("inputValidator")
public class Y_Validation implements Validator {

    private static final String Y_PATTERN = "^([-]?[0-9]+(([,.])[0-9]+)?)$";
    private Pattern pattern;

    public Y_Validation() {
        pattern = Pattern.compile(Y_PATTERN);
    }

    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {

        Matcher matcher = pattern.matcher(value.toString());
        if (!matcher.matches()) {
            FacesMessage msg =
                    new FacesMessage("Ошибка валидации 'y'",
                            "Некорректный формат 'y'");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        } else if ((Double.valueOf(value.toString().replace(",", ".")) < -3) ||
                (Double.valueOf(value.toString().replace(",", ".")) > 5)) {
            FacesMessage msg =
                    new FacesMessage("Ошибка валидации 'y'",
                            "'y' должно принадлежать промежутку [-3;5]");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

    }
}
