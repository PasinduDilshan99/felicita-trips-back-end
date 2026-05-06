package com.felicita.security.config;

import com.felicita.filter.JwtFilter;
import com.felicita.util.AuthorizeEndPoints;
import com.felicita.util.PublicEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/actuator/**").permitAll()
                        // DESTINATIONS
                        .requestMatchers("/v0/api/destination/add-destination").hasAuthority("DESTINATION_CREATE")
                        .requestMatchers("/v0/api/destination/update-destination").hasAuthority("DESTINATION_UPDATE")
                        .requestMatchers("/v0/api/destination/terminate-destination").hasAuthority("DESTINATION_TERMINATE")
                        // ACTIVITIES
                        .requestMatchers("/api/v0/activities/add-activity").hasAuthority("ACTIVITY_CREATE")
                        .requestMatchers("/api/v0/activities/update-activity").hasAuthority("ACTIVITY_UPDATE")
                        .requestMatchers("/api/v0/activities/terminate-activity").hasAuthority("ACTIVITY_TERMINATE")
                        // TOURS
                        .requestMatchers("/v0/api/tour/add-tour").hasAuthority("TOUR_CREATE")
                        .requestMatchers("/v0/api/tour/update-tour").hasAuthority("TOUR_UPDATE")
                        .requestMatchers("/v0/api/tour/terminate-tour").hasAuthority("TOUR_TERMINATE")
                        // PACKAGES
                        .requestMatchers("/v0/api/package/add-package").hasAuthority("PACKAGE_CREATE")
                        .requestMatchers("/v0/api/package/update-package").hasAuthority("PACKAGE_UPDATE")
                        .requestMatchers("/v0/api/package/terminate-package").hasAuthority("PACKAGE_TERMINATE")

                        //LINK BAR
//                        .requestMatchers("/api/v0/link-bar/all").hasAuthority("LINK_BAR_VIEW")
//                        .requestMatchers("/api/v0/link-bar/active").hasAuthority("LINK_BAR_VIEW")

                        // LINK BAR
//                        .requestMatchers("/api/v0/nav-bar/all").hasAuthority("NAV_BAR_VIEW")
//                        .requestMatchers("/api/v0/nav-bar/active").hasAuthority("NAV_BAR_VIEW")

                        // Hero Section
//                        .requestMatchers("/api/v0/hero-section/home-all").hasAuthority("HERO_SECTION_HOME_VIEW")
//                        .requestMatchers("/api/v0/hero-section/home").hasAuthority("HERO_SECTION_HOME_VIEW")
//                        .requestMatchers("/api/v0/hero-section/about-us").hasAuthority("HERO_SECTION_ABOUT_US_VIEW")
//                        .requestMatchers("/api/v0/hero-section/contact-us").hasAuthority("HERO_SECTION_CONTACT_US_VIEW")
//                        .requestMatchers("/api/v0/hero-section/blog").hasAuthority("HERO_SECTION_BLOG_VIEW")
//                        .requestMatchers("/api/v0/hero-section/faq").hasAuthority("HERO_SECTION_FAQ_VIEW")
//                        .requestMatchers("/api/v0/hero-section/tour").hasAuthority("HERO_SECTION_TOUR_VIEW")
//                        .requestMatchers("/api/v0/hero-section/package").hasAuthority("HERO_SECTION_PACKAGE_VIEW")
//                        .requestMatchers("/api/v0/hero-section/package-schedule/{packageScheduleId}").hasAuthority("HERO_SECTION_PACKAGE_SCHEDULE_VIEW")
//                        .requestMatchers("/api/v0/hero-section/booked-tour/{bookingId}").hasAuthority("HERO_SECTION_BOOKED_TOUR_VIEW")
//                        .requestMatchers("/api/v0/hero-section/destination").hasAuthority("HERO_SECTION_DESTINATION_VIEW")
//                        .requestMatchers("/api/v0/hero-section/activity").hasAuthority("HERO_SECTION_ACTIVITY_VIEW")

                        // Why Choose Us Section
//                        .requestMatchers("/api/v0/why-choose-us/all").hasAuthority("WHY_CHOOSE_US_VIEW")
//                        .requestMatchers("/api/v0/why-choose-us/active").hasAuthority("WHY_CHOOSE_US_VIEW")

                        // Our Service Section
//                        .requestMatchers("/api/v0/our-service/all").hasAuthority("OUR_SERVICES_VIEW")
//                        .requestMatchers("/api/v0/our-service/active").hasAuthority("OUR_SERVICES_VIEW")

                        // Destinations
//                        .requestMatchers("/api/v0/destination/all").hasAuthority("DESTINATION_VIEW")
//                        .requestMatchers("/api/v0/destination/active-destinations").hasAuthority("DESTINATION_VIEW")
//                        .requestMatchers("/api/v0/destination/destinations").hasAuthority("DESTINATION_VIEW")
//                        .requestMatchers("/api/v0/destination/all-categories").hasAuthority("DESTINATION_CATEGORY_VIEW")
//                        .requestMatchers("/api/v0/destination/active-categories").hasAuthority("DESTINATION_CATEGORY_VIEW")
//                        .requestMatchers("/api/v0/destination/popular-destinations").hasAuthority("DESTINATION_POPULAR_VIEW")
//                        .requestMatchers("/api/v0/destination/new-destinations").hasAuthority("DESTINATION_NEW_VIEW")
//                        .requestMatchers("/api/v0/destination/trending-destinations").hasAuthority("DESTINATION_TRENDING_VIEW")
//                        .requestMatchers("/api/v0/destination/tour-map").hasAuthority("DESTINATION_TOUR_MAP_VIEW")
//                        .requestMatchers("/api/v0/destination/tour-id/{tourId}").hasAuthority("DESTINATION_VIEW")
//                        .requestMatchers("/api/v0/destination/{destinationId}").hasAuthority("DESTINATION_VIEW")
//                        .requestMatchers("/api/v0/destination/reviews").hasAuthority("DESTINATION_REVIEWS_VIEW")
//                        .requestMatchers("/api/v0/destination/reviews/{destinationId}").hasAuthority("DESTINATION_REVIEWS_VIEW")
//                        .requestMatchers("/api/v0/destination/history").hasAuthority("DESTINATION_HISTORY_VIEW")
//                        .requestMatchers("/api/v0/destination/history/{destinationId}").hasAuthority("DESTINATION_HISTORY_VIEW")
//                        .requestMatchers("/api/v0/destination/history-images").hasAuthority("DESTINATION_HISTORY_IMAGES_VIEW")
//                        .requestMatchers("/api/v0/destination/history-images/{destinationId}").hasAuthority("DESTINATION_HISTORY_IMAGES_VIEW")
//                        .requestMatchers("/api/v0/destination/destination-for-terminate").hasAuthority("DESTINATION_TERMINATE")
//                        .requestMatchers("/api/v0/destination/add-destination").hasAuthority("DESTINATION_CREATE")
//                        .requestMatchers("/api/v0/destination/update-destination").hasAuthority("DESTINATION_UPDATE")
//                        .requestMatchers("/api/v0/destination/terminate-destination").hasAuthority("DESTINATION_TERMINATE")
//                        .requestMatchers("/api/v0/destination/destination-names").hasAuthority("DESTINATION_VIEW")

                        // Tours
//                        .requestMatchers("/api/v0/tour/all").hasAuthority("TOUR_VIEW")
//                        .requestMatchers("/api/v0/tour/active").hasAuthority("TOUR_VIEW")
//                        .requestMatchers(HttpMethod.POST,"/api/v0/tour/tours").hasAuthority("TOUR_VIEW")
//                        .requestMatchers("/api/v0/tour/popular").hasAuthority("TOUR_VIEW_POPULAR")
//                        .requestMatchers("/api/v0/tour/*").hasAuthority("TOUR_VIEW")
//                        .requestMatchers("/api/v0/tour/tout-all-details/*").hasAuthority("TOUR_VIEW")
//                        .requestMatchers("/api/v0/tour/reviews").hasAuthority("TOUR_REVIEW_VIEW")
//                        .requestMatchers("/api/v0/tour/reviews/*").hasAuthority("TOUR_REVIEW_VIEW")
//                        .requestMatchers("/api/v0/tour/history").hasAuthority("TOUR_HISTORY_VIEW")
//                        .requestMatchers("/api/v0/tour/history/*").hasAuthority("TOUR_HISTORY_VIEW")
//                        .requestMatchers("/api/v0/tour/tour-map/*").hasAuthority("TOUR_MAP_VIEW")
//                        .requestMatchers("/api/v0/tour/history-images").hasAuthority("TOUR_HISTORY_IMAGES_VIEW")
//                        .requestMatchers("/api/v0/tour/history-images/*").hasAuthority("TOUR_HISTORY_IMAGES_VIEW")
//                        .requestMatchers("/api/v0/tour/tour-details/*").hasAuthority("TOUR_DETAILS_DAY_BY_DAY_VIEW")
//                        .requestMatchers("/api/v0/tour/tour-extra-details/*").hasAuthority("TOUR_EXTRA_DETAILS_DAY_BY_DAY_VIEW")
//                        .requestMatchers("/api/v0/tour/tour-schedules/*").hasAuthority("TOUR_SCHEDULE_VIEW")
//                        .requestMatchers("/api/v0/tour/all-tours-basic").hasAuthority("TOUR_VIEW")
//                        .requestMatchers("/api/v0/tour/tour-for-terminate").hasAuthority("TOUR_TERMINATE")
//                        .requestMatchers("/api/v0/tour/terminate-tour").hasAuthority("TOUR_TERMINATE")
//                        .requestMatchers("/api/v0/tour/add-tour").hasAuthority("TOUR_CREATE")
//                        .requestMatchers("/api/v0/tour/update-tour").hasAuthority("TOUR_UPDATE")
//                        .requestMatchers("/api/v0/tour/tourId-and-tourName").hasAuthority("TOUR_VIEW")
//                        .requestMatchers("/api/v0/tour/tour-details-for-add-package/*").hasAuthority("TOUR_VIEW")

                        // Packages
//                        .requestMatchers("/api/v0/package/all").hasAuthority("PACKAGE_VIEW")
//                        .requestMatchers("/api/v0/package/active").hasAuthority("PACKAGE_VIEW")
//                        .requestMatchers("/api/v0/package/packages").hasAuthority("PACKAGE_VIEW")
//                        .requestMatchers("/api/v0/package/*").hasAuthority("PACKAGE_VIEW")
//                        .requestMatchers("/api/v0/package/reviews").hasAuthority("PACKAGE_REVIEW_VIEW")
//                        .requestMatchers("/api/v0/package/reviews/*").hasAuthority("PACKAGE_REVIEW_VIEW")
//                        .requestMatchers("/api/v0/package/history").hasAuthority("PACKAGE_HISTORY_VIEW")
//                        .requestMatchers("/api/v0/package/history/*").hasAuthority("PACKAGE_HISTORY_VIEW")
//                        .requestMatchers("/api/v0/package/history-images").hasAuthority("PACKAGE_HISTORY_IMAGES_VIEW")
//                        .requestMatchers("/api/v0/package/history-images/*").hasAuthority("PACKAGE_HISTORY_IMAGES_VIEW")
//                        .requestMatchers("/api/v0/package/package-details/*").hasAuthority("PACKAGE_DETAILS_DAY_BY_DAY_VIEW")
//                        .requestMatchers("/api/v0/package/package-compare/*").hasAuthority("PACKAGE_DETAILS_COMPARE_VIEW")
//                        .requestMatchers("/api/v0/package/package-extra-details/*").hasAuthority("PACKAGE_EXTRA_DETAILS_DAY_BY_DAY_VIEW")
//                        .requestMatchers("/api/v0/package/package-schedules/*").hasAuthority("PACKAGE_SCHEDULE_VIEW")
//                        .requestMatchers("/api/v0/package/package-schedules-details/*").hasAuthority("PACKAGE_SCHEDULE_VIEW")
//                        .requestMatchers("/api/v0/package/package-for-terminate").hasAuthority("PACKAGE_TERMINATE")
//                        .requestMatchers("/api/v0/package/terminate-package").hasAuthority("PACKAGE_TERMINATE")
//                        .requestMatchers("/api/v0/package/add-package").hasAuthority("PACKAGE_CREATE")
//                        .requestMatchers("/api/v0/package/update-package").hasAuthority("PACKAGE_UPDATE")
//                        .requestMatchers("/api/v0/package/package-all-details/*").hasAuthority("PACKAGE_VIEW")
//                        .requestMatchers("/api/v0/package/packageId-and-packageName").hasAuthority("PACKAGE_VIEW")

                        // Activities
//                        .requestMatchers("/api/v0/activity/all").hasAuthority("ACTIVITY_VIEW")
//                        .requestMatchers("/api/v0/activity/activities").hasAuthority("ACTIVITY_VIEW")
//                        .requestMatchers("/api/v0/activity/active").hasAuthority("ACTIVITY_VIEW")
//                        .requestMatchers("/api/v0/activity/category").hasAuthority("ACTIVITY_CATEGORY_VIEW")
//                        .requestMatchers("/api/v0/activity/active-category").hasAuthority("ACTIVITY_CATEGORY_VIEW")
//                        .requestMatchers("/api/v0/activity/*").hasAuthority("ACTIVITY_VIEW")
//                        .requestMatchers("/api/v0/activity/reviews").hasAuthority("ACTIVITY_REVIEW_VIEW")
//                        .requestMatchers("/api/v0/activity/reviews/*").hasAuthority("ACTIVITY_REVIEW_VIEW")
//                        .requestMatchers("/api/v0/activity/history").hasAuthority("ACTIVITY_HISTORY_VIEW")
//                        .requestMatchers("/api/v0/activity/history/*").hasAuthority("ACTIVITY_HISTORY_VIEW")
//                        .requestMatchers("/api/v0/activity/history-images").hasAuthority("ACTIVITY_HISTORY_IMAGES_VIEW")
//                        .requestMatchers("/api/v0/activity/history-images/*").hasAuthority("ACTIVITY_HISTORY_IMAGES_VIEW")
//                        .requestMatchers("/api/v0/activity/activity-for-terminate").hasAuthority("ACTIVITY_TERMINATE")
//                        .requestMatchers("/api/v0/activity/terminate-activity").hasAuthority("ACTIVITY_TERMINATE")
//                        .requestMatchers("/api/v0/activity/add-activity").hasAuthority("ACTIVITY_CREATE")
//                        .requestMatchers("/api/v0/activity/update-activity").hasAuthority("ACTIVITY_UPDATE")
//                        .requestMatchers("/api/v0/activity/activityId-and-activityName").hasAuthority("ACTIVITY_VIEW")

                        // Blogs
//                        .requestMatchers("/api/v0/blog/all").hasAuthority("BLOG_VIEW")
//                        .requestMatchers("/api/v0/blog/active").hasAuthority("BLOG_VIEW")
//                        .requestMatchers("/api/v0/blog/tags").hasAuthority("BLOG_TAG_VIEW")
//                        .requestMatchers("/api/v0/blog/tags/*").hasAuthority("BLOG_TAG_VIEW")
//                        .requestMatchers("/api/v0/blog/writer/*").hasAuthority("BLOG_VIEW")
//                        .requestMatchers("/api/v0/blog/tag/*").hasAuthority("BLOG_VIEW")
//                        .requestMatchers("/api/v0/blog/blog-details").hasAuthority("BLOG_VIEW")
//                        .requestMatchers("/api/v0/blog/add-blog").hasAuthority("BLOG_CREATE")
//                        .requestMatchers("/api/v0/blog/add-bookmark").hasAuthority("BLOG_BOOKMARK_CREATE")
//                        .requestMatchers("/api/v0/blog/add-react").hasAuthority("BLOG_REACT_CREATE")
//                        .requestMatchers("/api/v0/blog/add-comment-react").hasAuthority("BLOG_COMMENT_REACT_CREATE")
//                        .requestMatchers("/api/v0/blog/add-comment").hasAuthority("BLOG_COMMENT_CREATE")

                        // Gallery
//                        .requestMatchers("/api/v0/gallery/all").hasAuthority("GALLERY_VIEW")
//                        .requestMatchers("/api/v0/gallery/active").hasAuthority("GALLERY_VIEW")

                        // Inquiry
//                        .requestMatchers("/api/v0/inquiry/create").hasAuthority("INQUIRY_CREATE")

                        // Footer
    //                        .requestMatchers("/api/v0/footer/all").hasAuthority("FOOTER_VIEW")
//                        .requestMatchers("/api/v0/footer/active").hasAuthority("FOOTER_VIEW")

                        // Our Story
//                        .requestMatchers("/api/v0/our-story/details").hasAuthority("OUR_STORY_DETAILS_VIEW")

                        // Contact Us
//                        .requestMatchers("/api/v0/contact-us/contact-methods").hasAuthority("CONTACT_US_CONTACT_METHODS_VIEW")

                        // FAQ
//                        .requestMatchers("/api/v0/faq/all").hasAuthority("FAQ_VIEW")
//                        .requestMatchers("/api/v0/faq/active").hasAuthority("FAQ_VIEW")
//                        .requestMatchers("/api/v0/faq/view-count").hasAuthority("FAQ_VIEW_COUNT_UPDATE")
//                        .requestMatchers("/api/v0/faq/options").hasAuthority("FAQ_OPTIONS_VIEW")
//                        .requestMatchers("/api/v0/faq/insert-faq-request").hasAuthority("FAQ_REQUEST_CREATE")

                                .requestMatchers(PublicEndpoints.ENDPOINTS).permitAll()
                        .requestMatchers(AuthorizeEndPoints.ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://stg.felicitatrips.com","https://felicitatrips.com","https://admin-stg.felicitatrips.com","https://admin.felicitatrips.com"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type","Cookie"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
