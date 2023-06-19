package com.kunminx.purenote.data.repo;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.kunminx.architecture.data.response.AsyncTask;
import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.data.response.ResponseStatus;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.data.bean.Weather;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by KunMinX at 2022/6/14
 */
public class DataRepository {

  //TODO 天气服务使用高德 API_KEY，如有需要，请自行在 "高德开放平台" 获取和在 DataRepository 类填入

  public final static String API_KEY = "";
  public final static String BASE_URL = "https://restapi.amap.com/v3/";

  private static final DataRepository instance = new DataRepository();
  private static final String DATABASE_NAME = "NOTE_DB.db";
  private final NoteDataBase mDataBase;
  private final Retrofit mRetrofit;

  {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .writeTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build();
    mRetrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
  }

  public static DataRepository getInstance() {
    return instance;
  }

  private DataRepository() {
    mDataBase = Room.databaseBuilder(Utils.getApp().getApplicationContext(),
            NoteDataBase.class, DATABASE_NAME).build();
  }

  public Observable<List<Note>> getNotes() {
    return AsyncTask.doIO(emitter -> emitter.onNext(mDataBase.noteDao().getNotes()));
  }

  public Observable<Boolean> insertNote(Note note) {
    return AsyncTask.doIO(emitter -> {
      mDataBase.noteDao().insertNote(note);
      emitter.onNext(true);
    });
  }

  public Observable<Boolean> updateNote(Note note) {
    return AsyncTask.doIO(emitter -> {
      mDataBase.noteDao().updateNote(note);
      emitter.onNext(true);
    });
  }

  public Observable<Boolean> deleteNote(Note note) {
    return AsyncTask.doIO(emitter -> {
      mDataBase.noteDao().deleteNote(note);
      emitter.onNext(true);
    });
  }

  @SuppressLint("CheckResult")
  public Observable<Weather> getWeatherInfo(String cityCode) {
    WeatherService service = mRetrofit.create(WeatherService.class);
    return service.getWeatherInfo(cityCode, API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
  }
}