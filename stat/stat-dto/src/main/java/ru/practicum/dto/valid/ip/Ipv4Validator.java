package ru.practicum.dto.valid.ip;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Ipv4Validator implements ConstraintValidator<Ip, String> {

    @Override
    public void initialize(Ip ip) {
    }

    @Override
    public boolean isValid(String ip, ConstraintValidatorContext cxt) {
        return ip.matches("(^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$)" +
                "|(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|(([0-9a-fA-F]{1,4}:){1,7}:" +
                "|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}" +
                "(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}" +
                "|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}" +
                "(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})" +
                "|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}" +
                "|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}" +
                "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]" +
                "|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])))");
    }
}
