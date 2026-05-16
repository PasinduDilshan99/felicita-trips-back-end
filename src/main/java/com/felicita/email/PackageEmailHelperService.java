package com.felicita.email;

import com.felicita.model.dto.PackageResponseDto;
import com.felicita.model.other.PackageComparisonResult;
import com.felicita.model.other.PackageTypeComparisonResult;
import com.felicita.model.other.PcakageScheduleComparisonResult;
import com.felicita.model.request.PackageInsertRequest;
import com.felicita.model.request.packages.schedule.PackageScheduleInsertRequest;
import com.felicita.model.request.packages.schedule.PackageScheduleUpdateRequest;
import com.felicita.model.request.packages.type.PackageTypeInsertRequest;
import com.felicita.model.request.packages.type.PackageTypeUpdateRequest;
import com.felicita.model.response.packages.schedule.PacakgeScheduleBasicDetailsResponse;
import com.felicita.model.response.packages.type.PackageTypeBasicDetailsResponse;
import com.felicita.security.model.User;

public interface PackageEmailHelperService {
    String buildPackageCreateSuccessfullBody(PackageInsertRequest packageInsertRequest, Long packageId, User loggedUser);

    String buildPackageCreateSuccessfullSubject(PackageInsertRequest packageInsertRequest, Long packageId, User loggedUser);

    String buildPackageUpdateSuccessfullSubject(User loggedUser, String name);

    String buildPackageUpdateSuccessfullBody(User loggedUser, PackageComparisonResult comparisonResult);

    String buildPackageTerminateSuccessfullSubject(User loggedUser, PackageResponseDto packageResponseDto);

    String buildPackageTerminateSuccessfullBody(User loggedUser, PackageResponseDto packageResponseDto);

    String buildPackageTypeTerminateSuccessfullSubject(User loggedUser, PackageTypeBasicDetailsResponse packageTypeResponse);

    String buildPackageTypeTerminateSuccessfullBody(User loggedUser, PackageTypeBasicDetailsResponse packageTypeResponse);

    String buildPackageTypeCreateSuccessfullBody(Long packageTypeId, PackageTypeInsertRequest packageTypeInsertRequest, User loggedUser);

    String buildPackageTypeCreateSuccessfullSubject(Long packageTypeId, PackageTypeInsertRequest packageTypeInsertRequest, User loggedUser);

    String buildPackageTypeUpdateSuccessfullSubject(User loggedUser, PackageTypeUpdateRequest packageTypeUpdateRequest);

    String buildPackageTypeUpdateSuccessfullBody(User loggedUser, PackageTypeUpdateRequest packageTypeUpdateRequest, PackageTypeComparisonResult comparisonResult);

    String buildPackageScheduleCreateSuccessfullBody(Long packageScheduleId, PackageScheduleInsertRequest packageScheduleInsertRequest, User loggedUser);

    String buildPackageScheduleCreateSuccessfullSubject(Long packageScheduleId, PackageScheduleInsertRequest packageScheduleInsertRequest, User loggedUser);

    String buildPackageScheduleUpdateSuccessfullSubject(User loggedUser, PackageScheduleUpdateRequest packageScheduleUpdateRequest);

    String buildPackageScheduleUpdateSuccessfullBody(User loggedUser, PackageScheduleUpdateRequest packageScheduleUpdateRequest, PcakageScheduleComparisonResult comparisonResult);

    String buildPackageScheduleTerminateSuccessfullSubject(User loggedUser, PacakgeScheduleBasicDetailsResponse packageScheduleResponse);

    String buildPackageScheduleTerminateSuccessfullBody(User loggedUser, PacakgeScheduleBasicDetailsResponse packageScheduleResponse);
}
