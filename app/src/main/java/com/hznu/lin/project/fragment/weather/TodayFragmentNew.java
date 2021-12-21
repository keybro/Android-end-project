package com.hznu.lin.project.fragment.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hznu.lin.project.R;
import com.hznu.lin.project.adapter.CityWeatherAdapter;
import com.hznu.lin.project.dao.CityDao;
import com.hznu.lin.project.db.CityDataBase;
import com.hznu.lin.project.entity.City;
import com.hznu.lin.project.entity.CityWeather;
import com.hznu.lin.project.entity.CityWeatherDetail;
import com.hznu.lin.project.entity.Weather;
import com.hznu.lin.project.fragment.history.HistoryFragment;
import com.hznu.lin.project.service.impl.GetRequestServiceImpl;
import com.hznu.lin.project.util.HttpUtil;
import com.hznu.lin.project.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 今日界面的具体fragment
 */
public class TodayFragmentNew extends Fragment {

    //历史天数
    private Integer dayPast;
    //未来天数
    private Integer dayFuture;

    private SharedPreferences sp;
    public static String defaultCity = "杭州";
    public static String[] citys = {"杭州", "昆明", "北京", "上海", "深圳", "宁波", "温州"};
    public static List<CityWeather> weatherList = new ArrayList<>();

    private List<PointValue> pointLowFuture = new ArrayList<>();
    private List<PointValue> pointHighFuture = new ArrayList<>();
    private List<AxisValue> axisXPast = new ArrayList<>();
    private List<AxisValue> axisXFuture = new ArrayList<>();


//    @BindView(R.id.detailRecyclerView)
//    RecyclerView recyclerView;

    @BindView(R.id.line_chart_future_today)
    LineChartView lineChartFuture;

    @BindView(R.id.otherCity)
    ListView otherCityListView;

    @BindView(R.id.cardViewTextLocation)
    TextView location;

    @BindView(R.id.cardViewTextLocationTemper)
    TextView locationTemp;
    @BindView(R.id.cityPhoto)
    ImageView imageView;

    public static CityWeatherDetail weatherDetail;
    public static List<String> mapList;
    public static List<String> temperList;

    public TodayFragmentNew() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_new, container, false);
        ButterKnife.bind(this,view);

        sp = getActivity().getSharedPreferences("com.hznu.lin.project_preferences", Context.MODE_PRIVATE);
        String searchCity = sp.getString("city","杭州");
        location.setText(searchCity);
        locationTemp.setText(weatherDetail.getTemperature());
        if (searchCity.equals("北京")){
            imageView.setImageResource(R.drawable.beijingtianan);
        }
        if (searchCity.equals("杭州")){
            imageView.setImageResource(R.drawable.xihu);
        }


        Log.i("sheng",searchCity);
        String str[] = {"气温:  "+weatherDetail.getTemperature(),"天气:  "+weatherDetail.getWeather(),"空气湿度:  "+weatherDetail.getHumidity(),"风向:  "+weatherDetail.getWind(),"风力:  "+weatherDetail.getWinp()};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, str);
        ListView listView = view.findViewById(R.id.detailListView);
        listView.setAdapter(adapter);
        CityDataBase cityDataBase = Room.databaseBuilder(getActivity().getApplicationContext(), CityDataBase.class, "city.db").allowMainThreadQueries().build();
        CityDao cityDao = cityDataBase.cityDao();
//        City wenzhou = new City(5, "宁波");
//        cityDao.delete(wenzhou);
        List<String> preferCity = cityDao.getPreferCity();
        //图标初始化，设置透明度和sp
        for (String test :
                preferCity) {
            Log.i("uuu", test);
        }
        Log.i("sheng","我这个时候在爬");
        int cityNumber = Integer.valueOf(sp.getString("day_future", "1"));

        init();
        //获取x轴的标注
        getAxisXLables();
        //获取坐标点
        getAxisPoints();
        //初始化helloChart
        FutureInit();
        String cityAll[] = preferCity.toArray(new String[preferCity.size()]);
        String endCity[] = preferCity.subList(0,cityNumber).toArray(new String[cityNumber]);
        Log.i("end",String.valueOf(cityNumber));

        //每个listVIew的温度
//        for (int i = 0; i <preferCity.size(); i++) {
//            getCityWeatherOther(preferCity.get(i));
//        }
//        for (String temp :
//                temperList) {
//            Log.i("ttt",temp);
//        }


        for (String test :
                endCity) {
            Log.i("end", test);
        }


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, endCity);
        otherCityListView.setAdapter(adapter2);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getTodayWeather(defaultCity);
//        initWeatherList();
        Log.i("sheng","我这个时候执行");
    }

    /**
     * 获取某个城市天气信息
     * @param city
     */
    public static void getCityWeather(String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("我竟然开始跑了");
                Log.i("sheng","我竟然开始跑了");
                String jsonStr = HttpUtil.get("http://api.k780.com/?app=weather.future&weaid="+city+"&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json", null);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonStr);
                    //结果放在result中
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    List<CityWeatherDetail> temp = new Gson().fromJson(jsonArray.toString(),
                            new TypeToken<List<CityWeatherDetail>>() {
                            }.getType());

                    weatherDetail = temp.get(0);
                    System.out.println(weatherDetail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取其他感兴趣城市天气信息
     * @param city
     */
    public static void getCityWeatherOther(String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("我竟然开始跑了");
                Log.i("sheng","我竟然开始跑了");
                String jsonStr = HttpUtil.get("http://api.k780.com/?app=weather.future&weaid="+city+"&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json", null);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonStr);
                    //结果放在result中
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    List<CityWeatherDetail> temp = new Gson().fromJson(jsonArray.toString(),
                            new TypeToken<List<CityWeatherDetail>>() {
                            }.getType());

//                    String high = temp.get(0).getTemp_high();
//                    String slow = temp.get(0).getTemp_low();

//                    temperList.add("asdf");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 初始化
     */
    private void init() {
        // sp初始化
        sp = getActivity().getSharedPreferences("com.hznu.lin.project_preferences", Context.MODE_PRIVATE);
        dayFuture = Integer.valueOf(sp.getString("day_future", "7"));
        // recyclerView初始化
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 天气列表初始化
     */
    public void  initWeatherList() {
//        CityWeatherAdapter adapter = new CityWeatherAdapter(weatherList);
//        recyclerView.setAdapter(adapter);


    }

    /**
     * 获取默认城市当天天气
     * @param city
     */
    public void getTodayWeather(String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetRequestServiceImpl getRequestService = new GetRequestServiceImpl();
                Call<ResponseBody> call = getRequestService.getWeather(city);
                try {
                    Response<ResponseBody> response = call.execute();

                    String jsonStr = response.body().string();

                    Log.i("ccc",jsonStr);

                    // 将json传到handle中进行操作
//                    Message msg = Message.obtain();
//                    Bundle b = new Bundle();
//                    b.putString("jsonStr", jsonStr);
//                    msg.setData(b);
//                    handlerWeather.sendMessage(msg);
                } catch (IOException e) {
                    Looper.prepare();
                    ToastUtil.showToast(getContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT);
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取默认城市当天天气handle
     */
    @SuppressLint("HandlerLeak")
    private Handler handlerWeather = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle dataMsg = msg.getData();
            String jsonStr = dataMsg.getString("jsonStr");
            // Json处理
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONObject data = jsonObject.getJSONObject("data");

                String city = data.getString("city");
                JSONArray forecastArray = data.getJSONArray("forecast");
                // 只获取当日天气信息
                JSONObject weatherObject = forecastArray.getJSONObject(0);
                Weather weather = new Gson().fromJson(weatherObject.toString(), Weather.class);
//                Log.i("WEATHER", weather.toString());
                String temperature = weather.getLow().substring(3) + " ~ " + weather.getHigh().substring(3);
                String windPower = "风力：" + weather.getFengli().charAt(9) + "级";
                String windDt = "风向：" + weather.getFengxiang();
                int resource;
                switch (weather.getType()) {
                    case "多云":
                        resource = R.drawable.ic_vastcloud;
                        break;
                    case "小雨":
                        resource = R.drawable.ic_rainday;
                        break;
                    case "晴":
                        resource = R.drawable.ic_sunday;
                        break;
                    case "阴":
                        resource = R.drawable.ic_morecloud;
                        break;
                    default:
                        resource = R.drawable.default_weather;
                }

            } catch (JSONException e) {
                ToastUtil.showToast(getContext(), "系统异常，请稍后再试", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };


    /**
     * 设置X轴的显示
     */
    private void getAxisXLables() {
        try {
            // Future
            for (int i = 0; i < 7; i++) {
                axisXFuture.add(new AxisValue(i).setLabel(HistoryFragment.dateFuturetoday[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT);
        }

    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {

        try {
            // Future
            for (int i = 0; i < 7; i++) {
                pointLowFuture.add(new PointValue(i, HistoryFragment.tempLowFuture[i]));
            }

            for (int i = 0; i < 7; i++) {
                pointHighFuture.add(new PointValue(i, HistoryFragment.tempHighFuture[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT);
        }
    }

    private void FutureInit() {
        List<Line> lines = new ArrayList<>();
        // low
        Line lineLow = new Line(pointLowFuture).setColor(Color.parseColor("#027AFF"));
        lineLow.setShape(ValueShape.CIRCLE);
        lineLow.setCubic(true);//曲线是否平滑，即是曲线还是折线
        lineLow.setFilled(false);//是否填充曲线的面积
        lineLow.setHasLabels(true);//曲线的数据坐标是否加上备注
        lineLow.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lineLow.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(lineLow);

        // high
        Line lineHigh = new Line(pointHighFuture).setColor(Color.parseColor("#027AFF"));
        lineHigh.setShape(ValueShape.CIRCLE);
        lineHigh.setCubic(true);//曲线是否平滑，即是曲线还是折线
        lineHigh.setFilled(false);//是否填充曲线的面积
        lineHigh.setHasLabels(true);//曲线的数据坐标是否加上备注
        lineHigh.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lineHigh.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(lineHigh);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setValueLabelBackgroundEnabled(false);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setName("未来" + 7 + "天天气信息");  //表格名称
        axisX.setTextSize(13);//设置字体大小
        axisX.setTextColor(Color.WHITE);
        axisX.setValues(axisXFuture);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线


        //设置行为属性，支持缩放、滑动以及平移
        lineChartFuture.setInteractive(true);
        lineChartFuture.setZoomType(ZoomType.HORIZONTAL);
        lineChartFuture.setMaxZoom((float) 2);//最大方法比例
        lineChartFuture.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartFuture.setLineChartData(data);
        lineChartFuture.setVisibility(View.VISIBLE);

        // 视角设置
        Viewport v = new Viewport(lineChartFuture.getMaximumViewport());
        v.top = Arrays.stream(HistoryFragment.tempHighFuture).max().getAsInt() + 1; //最高点为最大值+1
        v.bottom = Arrays.stream(HistoryFragment.tempLowFuture).min().getAsInt() - 1; //最低点为最小值-1
        lineChartFuture.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        lineChartFuture.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图

    }



}