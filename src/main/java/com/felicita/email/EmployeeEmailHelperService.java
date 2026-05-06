package com.felicita.email;

import com.felicita.model.dto.UserBasicDetailsDto;
import com.felicita.model.dto.WelcomeEmployeeDto;
import com.felicita.security.model.User;

public interface EmployeeEmailHelperService {
    String buildEmployeeCreateSuccessfullBody(WelcomeEmployeeDto welcomeEmployeeDto, User loggedUser);

    String buildEmployeeCreateSuccessfullSubject(WelcomeEmployeeDto welcomeEmployeeDto, User loggedUser);

    String buildEmployeeCreateSuccessfullBodyForEmployee(UserBasicDetailsDto userBasicDetailsDto, WelcomeEmployeeDto welcomeEmployeeDto, User loggedUser);

    String buildEmployeeCreateSuccessfullSubjectForEmployee(UserBasicDetailsDto userBasicDetailsDto, WelcomeEmployeeDto welcomeEmployeeDto, User loggedUser);
}
