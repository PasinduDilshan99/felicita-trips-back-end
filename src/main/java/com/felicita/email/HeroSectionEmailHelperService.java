package com.felicita.email;

import com.felicita.model.other.HeroSectionComparisonResult;
import com.felicita.model.request.heroSection.HeroSectionInsertRequest;
import com.felicita.model.response.heroSection.HeroSectionDetailsResponse;
import com.felicita.security.model.User;

public interface HeroSectionEmailHelperService {
    String buildHeroSectionCreateSuccessfullBody(HeroSectionInsertRequest heroSectionInsertRequest, Long heroSectionId, User loggedUser);

    String buildHeroSectionCreateSuccessfullSubject(HeroSectionInsertRequest heroSectionInsertRequest, Long heroSectionId, User loggedUser);

    String buildHeroSectionUpdateSuccessfullSubject(User loggedUser, Long heroSectionId);

    String buildHeroSectionUpdateSuccessfullBody(User loggedUser, Long heroSectionId, HeroSectionComparisonResult comparisonResult);

    String buildHeroSectionTerminateSuccessfullSubject(User loggedUser, HeroSectionDetailsResponse heroSectionData);

    String buildHeroSectionTerminateSuccessfullBody(User loggedUser, HeroSectionDetailsResponse heroSectionData, String heroSectionType);
}
