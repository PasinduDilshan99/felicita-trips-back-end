package com.felicita.repository;

import com.felicita.model.response.AllCategoriesResponse;

import java.util.List;

public interface CommonRepository {
    List<AllCategoriesResponse.ActivityCategory> getAllActivityCategories();

    List<AllCategoriesResponse.DestinationCategory> getAllDestinationCategories();

    List<AllCategoriesResponse.TourCategory> getAllTourCategories();

    List<AllCategoriesResponse.PackageCategory> getAllPackageCategories();

    List<AllCategoriesResponse.TourType> getAllTourTypes();

    List<AllCategoriesResponse.Seasons> getAllSeasons();
}
