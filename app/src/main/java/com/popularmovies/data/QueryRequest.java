package com.popularmovies.data;





import com.popularmovies.data.models.ConfigurationResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface QueryRequest {


    String URL_CONFIGURATIONS = "configuration";




    @GET(URL_CONFIGURATIONS)
    Observable<ConfigurationResponse> requestConfigurations(@Query(RequestParams.API_KEY) String apiKey);
//
//    @GET(URL_SPORT_EVENTS)
//    Observable<SectionHeader> requestSportEventById(@Query(QueryRequest.PARAM_EVENT_ID) int eventId);
//
//
//    @GET(URL_SPORT_EVENTS_FAVORITES)
//    Observable<List<Child>> requestFavoritesSportEvents(@Query(PARAM_SPORT_EVENTS_IDS) int[] sportIds);
//
//
//    @GET(URL_PROMOTION)
//    Observable<List<PromotionModel>> requestPromotions(@Query(PARAM_PROMO_TYPE) String type, @Query(PARAM_PROMO_SUBSCRIPTION) String subscriptionType);
//
//    @GET(URL_MEMBERSHIP)
//    Observable<List<Membership>> requestMembershipPlans();



}
