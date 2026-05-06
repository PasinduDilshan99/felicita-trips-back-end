package com.felicita.email;

import com.felicita.model.other.PrivilegeUpdateComparisonResult;
import com.felicita.model.request.PrivilegeInsertRequest;
import com.felicita.model.response.PrivilegeResponse;
import com.felicita.security.model.User;

public interface PrivilegeEmailHelperService {
    String buildPrivilegeCreateSuccessfullBody(PrivilegeInsertRequest privilegeInsertRequest, Long privilegeId, User loggedUser);

    String buildPrivilegeCreateSuccessfullSubject(PrivilegeInsertRequest privilegeInsertRequest, Long privilegeId, User loggedUser);

    String buildPrivilegeUpdateSuccessfullSubject(User loggedUser);

    String buildPrivilegeUpdateSuccessfullBody(User loggedUser, PrivilegeUpdateComparisonResult comparisonResult);

    String buildPrivilegeTerminateSuccessfullSubject(User loggedUser, PrivilegeResponse privilegeResponse);

    String buildPrivilegeTerminateSuccessfullBody(User loggedUser, PrivilegeResponse privilegeResponse);
}
