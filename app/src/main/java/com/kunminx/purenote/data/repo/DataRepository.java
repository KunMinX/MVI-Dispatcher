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
    return AsyncTask.doIO(emitter -> mDataBase.noteDao().insertNote(note));
  }

  public Observable<Boolean> updateNote(Note note) {
    return AsyncTask.doIO(emitter -> mDataBase.noteDao().updateNote(note));
  }

  public Observable<Boolean> deleteNote(Note note) {
    return AsyncTask.doIO(emitter -> mDataBase.noteDao().deleteNote(note));
  }

  public void getWeatherInfo(String cityCode, DataResult.Result<Weather.Live> result) {
    WeatherService service = mRetrofit.create(WeatherService.class);
    Call<Weather> call = service.getWeatherInfo(cityCode, API_KEY);
    call.enqueue(new Callback<Weather>() {
      @Override
      public void onResponse(@NonNull Call<Weather> call, @NonNull Response<Weather> response) {
        Weather weather = response.body();
        if (weather != null && weather.getLives() != null && !weather.getLives().isEmpty()) {
          result.onResult(new DataResult<>(weather.getLives().get(0)));
        }
      }
      @Override
      public void onFailure(@NonNull Call<Weather> call, @NonNull Throwable t) {
        result.onResult(new DataResult<>(new Weather.Live(), new ResponseStatus(t.getMessage())));
      }
    });
  }
}