package com.kmangutov.mememaster.views;

import android.content.Context;
import android.graphics.Picture;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmangutov.mememaster.R;
import com.kmangutov.mememaster.models.Data;
import com.kmangutov.mememaster.models.RedditResponse;
import com.kmangutov.mememaster.services.IShakeListener;
import com.kmangutov.mememaster.services.RedditService;
import com.kmangutov.mememaster.services.ShakeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnItemClickEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by kmangutov on 3/18/15.
 */
public class ImageFragment extends Fragment implements IShakeListener {

    public static final String LOG_NAME = "ImageFragment";

    SensorManager mSensorManager;
    ShakeListener mShakeListener;

    @InjectView(R.id.touchImageView)
    ImageViewTouch mImageViewTouch;

    RedditService mRedditService;
    List<Data> mRedditData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.inject(this, view);

        mImageViewTouch.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        mRedditService = new RedditService();

        mShakeListener = new ShakeListener(this);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mShakeListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        loadData();

        return view;
    }

    public void displayRandom() {

        if(mRedditData == null)
            return;

        int count = mRedditData.size();
        int random = (new Random()).nextInt(count);

        String url = mRedditData.get(random).data.url;

        Log.d(LOG_NAME, "Loading url: " + url + "...");

        Picasso.with(this.getActivity()).load(url).into(mImageViewTouch);
    }

    public void loadData() {

        mRedditService.mApi.getHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RedditResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RedditResponse redditResponse) {

                        mRedditData = redditResponse.data.children;
                        displayRandom();
                    }
                });
    }

    @Override
    public void onResume() {

        super.onResume();
        mSensorManager.registerListener(mShakeListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {

        mSensorManager.unregisterListener(mShakeListener);
        super.onPause();
    }

    static final long mShakeTimeThreshold = 2000;
    long mLastUpdate = 0;

    public void accelerationUpdate(float accel) {

        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - mLastUpdate;

        Log.d(LOG_NAME, "Accel: " + accel + " dT: " + deltaTime);

        if(Math.abs(accel) > 5 && deltaTime > mShakeTimeThreshold) {

            displayRandom();
            mLastUpdate = currentTime;

            Toast.makeText(getActivity(), "Rerolling!", Toast.LENGTH_SHORT);
        }
    }
}
