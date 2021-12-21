package com.hznu.lin.project.fragment.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.hznu.lin.project.MainActivity;
import com.hznu.lin.project.R;
import com.hznu.lin.project.dao.CityDao;
import com.hznu.lin.project.db.CityDataBase;
import com.hznu.lin.project.entity.City;
import com.hznu.lin.project.fragment.weather.TodayFragmentNew;
import com.hznu.lin.project.service.impl.GetRequestServiceImpl;
import com.hznu.lin.project.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private Preference login;
    private Preference username;
    private Preference password;
    private Preference city;
    private Preference confirm;
//    private Preference datPast;
    private Preference dayFuture;
    private Preference preferCity;
    private String passwordStr = "123";
    private String oldCity = "杭州";

    private SharedPreferences.Editor editor;
    private SharedPreferences sp;

    public MyPreferenceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spInit();
        // 设置页面绑定
        addPreferencesFromResource(R.xml.setting_preference);
        init();
    }

    /**
     * sp初始化
     */
    private void spInit() {
        sp = getActivity().getSharedPreferences("com.hznu.lin.project_preferences", Context.MODE_PRIVATE);
        //sp取数据，如果不存在则返回定义好的值
        passwordStr = sp.getString("password", passwordStr);
        oldCity = sp.getString("city", oldCity);
        //sp存入数据
        editor = sp.edit();
        editor.putString("confirm", "");
        editor.commit();
    }

    /**
     * Preference初始化
     */
    public void init() {
        login = findPreference("login");
        username = findPreference("username");
        password = findPreference("password");
        city = findPreference("city");
        confirm = findPreference("confirm");
        //用于设置历史天数
//        datPast = findPreference("day_past");
        dayFuture = findPreference("day_future");
        //用于添加喜欢的城市
        preferCity = findPreference("add_city");
        //因为定义PreferenceFragment的时候已经实现两个方法，所以直接定义自身类
        login.setOnPreferenceChangeListener(this);
        username.setOnPreferenceChangeListener(this);
        password.setOnPreferenceChangeListener(this);
        city.setOnPreferenceChangeListener(this);
        confirm.setOnPreferenceChangeListener(this);
//        datPast.setOnPreferenceChangeListener(this);
        dayFuture.setOnPreferenceChangeListener(this);
        preferCity.setOnPreferenceChangeListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case "login":
                ToastUtil.showToast(getContext(), "自动登录设置修改成功", Toast.LENGTH_SHORT);
                break;
            case "username":
                ToastUtil.showToast(getContext(), "用户名修改成功", Toast.LENGTH_SHORT);
                break;
            case "password":
                ToastUtil.showToast(getContext(), "密码修改成功", Toast.LENGTH_SHORT);
                break;
            case "city":
                isValidCity(newValue.toString());
                break;
            case "confirm":
                String confirmStr = newValue.toString();
                if (confirmStr.equals(passwordStr)) {
                    username.setEnabled(true);
                    password.setEnabled(true);
                    confirm.setEnabled(false);
                    ToastUtil.showToast(getContext(), "密码验证成功", Toast.LENGTH_SHORT);
                } else {
                    ToastUtil.showToast(getContext(), "密码验证失败", Toast.LENGTH_SHORT);
                }
                break;
//            case "day_past":
//                ToastUtil.showToast(getContext(), "历史天数修改为" + newValue + "天", Toast.LENGTH_SHORT);
//                break;
            case "day_future":
                ToastUtil.showToast(getContext(), "城市个数修改为" + newValue + "个", Toast.LENGTH_SHORT);
                break;
            case "add_city" :
                CityDataBase cityDataBase = Room.databaseBuilder(getActivity().getApplicationContext(), CityDataBase.class, "city.db").allowMainThreadQueries().build();
                CityDao cityDao = cityDataBase.cityDao();
                City city = new City();
                city.setName(String.valueOf(newValue));
                cityDao.insertAll(city);
                cityDao.getPreferCity();
                System.out.println("操作成功");
                ToastUtil.showToast(getContext(),"添加喜欢城市 "+newValue,Toast.LENGTH_LONG);
//                addCity(newValue.toString());
//                CityDataBase cityDataBase = Room.databaseBuilder(getApplicationContext(), CityDataBase.class, "city.db").allowMainThreadQueries().build();
//                CityDao cityDao = cityDataBase.cityDao();
                List<String> preferCity = cityDao.getPreferCity();
                for (int i = 0; i < preferCity.size(); i++) {
                    if (i==0){
                        MainActivity.WeatherListToDatabase(preferCity.get(0));
                    }
                    if (i==1){
                        MainActivity.WeatherListToDatabase2(preferCity.get(1));
                    }
                    if (i==2){
                        MainActivity.WeatherListToDatabase3(preferCity.get(2));
                    }
                    if (i==3){
                        MainActivity.WeatherListToDatabase4(preferCity.get(3));
                    }
                }
                MainActivity.WeatherForecastInit();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 判断城市名是否合法
     * @param city
     */
    public void isValidCity(String city) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                GetRequestServiceImpl getRequestService = new GetRequestServiceImpl();
                Call<ResponseBody> call = getRequestService.getWeather(city);
                try {
                    Response<ResponseBody> response = call.execute();
                    String jsonStr = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jsonObject.getJSONObject("data");

                    Looper.prepare();
                    ToastUtil.showToast(getActivity(), "默认城市已修改为" + city, Toast.LENGTH_LONG);
                    TodayFragmentNew.getCityWeather(city);
                    Looper.loop();

//                    GeoCoder mSearch = GeoCoder.newInstance();
//                    mSearch.setOnGetGeoCodeResultListener(this);
//                    mSearch.geocode(new GeoCodeOption().city(city));


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    Looper.prepare();
                    ToastUtil.showToast(getActivity(), "城市名无效，请输入正确的城市名！", Toast.LENGTH_SHORT);
                    editor.putString("city", oldCity);
                    editor.commit();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start();
    }



//    /**
//     * 发起地址搜索
//     *
//     * @param v
//     */
//    public void SearchButtonProcess() {
//        // Geo搜索
//
//    }



    public void addCity(String city){
        System.out.println("到了这里");
        ToastUtil.showToast(getContext(), "添加成功" + city, Toast.LENGTH_LONG);

    }
}
