package com.felicita.email;

import com.felicita.model.dto.PackageResponseDto;
import com.felicita.model.other.PackageComparisonResult;
import com.felicita.model.request.PackageInsertRequest;
import com.felicita.security.model.User;

public interface PackageEmailHelperService {
    String buildPackageCreateSuccessfullBody(PackageInsertRequest packageInsertRequest, Long packageId, User loggedUser);

    String buildPackageCreateSuccessfullSubject(PackageInsertRequest packageInsertRequest, Long packageId, User loggedUser);

    String buildPackageUpdateSuccessfullSubject(User loggedUser, String name);

    String buildPackageUpdateSuccessfullBody(User loggedUser, PackageComparisonResult comparisonResult);

    String buildPackageTerminateSuccessfullSubject(User loggedUser, PackageResponseDto packageResponseDto);

    String buildPackageTerminateSuccessfullBody(User loggedUser, PackageResponseDto packageResponseDto);
}
