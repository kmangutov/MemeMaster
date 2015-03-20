package com.kmangutov.mememaster.services;

import com.kmangutov.mememaster.models.RedditResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.GET;
import rx.Observable;

/**
 * Created by kmangutov on 3/2/15.
 */
public class RedditService {

    //http://blog.robinchutaux.com/blog/a-smart-way-to-use-retrofit/
    //http://stackoverflow.com/questions/21890338/when-should-one-use-rxjava-observable-and-when-simple-callback-on-android

    private static final String TASK_SERVER_URL = "http://www.reddit.com";
    private static final String CHAN_NEW = "r/4chan/new.json";

    public RedditApi mApi;

    public RedditService() {

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(TASK_SERVER_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mApi = restAdapter.create(RedditApi.class);

        //Response raw = mApi.rawNew();
        //System.out.println(flush(raw));
    }



    public static String flush(Response response) {

        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try{
            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
            String line;
            while((line = reader.readLine()) != null)
                sb.append(line);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public interface RedditApi {

        @GET("/r/4chan/new.json")
        public Observable<RedditResponse> getNew();

        @GET("/r/4chan/hot.json")
        public Observable<RedditResponse> getHot();
    }
}
