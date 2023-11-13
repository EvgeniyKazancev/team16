package dbservices.util;

import dbservices.entity.Users;
import dbservices.enums.ResponseType;
import dbservices.repository.UsersRepository;
import dbservices.services.UsersServices;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    public final UsersRepository usersRepository;

    public UserValidator(UsersRepository usersRepository) {

        this.usersRepository = usersRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Users.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Users users = (Users) o;

        if (usersRepository.existsByEmail(users.getEmail())) {
            errors.rejectValue("email", "" + ResponseType.UNAUTHORIZED.getCode(), "Такой email уже существует");

        }
        if (!Character.isUpperCase(users.getFirstName().codePointAt(0)) || !Character.isUpperCase(users.getLastName().codePointAt(0))) {
            errors.rejectValue("firstName", "" + ResponseType.UNAUTHORIZED.getCode(), "Имя должно начинаться с заглавной буквы");
            errors.rejectValue("lastName", "" + ResponseType.UNAUTHORIZED.getCode(), "Фамилия должна начинаться с заглавной буквы");
        }
//        if (!Character.isUpperCase(usersEntity.getLastName().codePointAt(0))) {
//            errors.rejectValue("lastName", "" + ResponseType.UNAUTHORIZED.getCode(), "Фамилия должна начинаться с заглавной буквы");
//        }


    }
}
