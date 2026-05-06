package com.felicita.email;

import com.felicita.model.other.RoleUpdateComparisonResult;
import com.felicita.model.request.RoleInsertRequest;
import com.felicita.model.response.RoleResponse;
import com.felicita.security.model.User;

public interface RoleEmailHelperService {
    String buildRoleCreateSuccessfullBody(RoleInsertRequest roleInsertRequest, Long roleId, User loggedUser);

    String buildRoleCreateSuccessfullSubject(RoleInsertRequest roleInsertRequest, Long roleId, User loggedUser);

    String buildRoleUpdateSuccessfullSubject(User loggedUser);

    String buildRoleUpdateSuccessfullBody(User loggedUser, RoleUpdateComparisonResult comparisonResult);

    String buildRoleTerminateSuccessfullSubject(User loggedUser, RoleResponse roleResponse);

    String buildRoleTerminateSuccessfullBody(User loggedUser, RoleResponse roleResponse);
}
