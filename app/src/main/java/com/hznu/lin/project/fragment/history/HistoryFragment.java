package com.hznu.lin.project.fragment.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.hznu.lin.project.MainActivity;
import com.hznu.lin.project.R;
import com.hznu.lin.project.dao.CityDao;
import com.hznu.lin.project.dao.WeatherDataDao;
import com.hznu.lin.project.db.CityDataBase;
import com.hznu.lin.project.db.WeatherDataBase;
import com.hznu.lin.project.entity.City;
import com.hznu.lin.project.entity.WeatherData;
import com.hznu.lin.project.util.ToastUtil;

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

public class HistoryFragment extends Fragment {


    @BindView(R.id.line_chart_past)
    LineChartView lineChartPast;
    @BindView(R.id.backgroundPast)
    LinearLayout backgroundPast;
    @BindView(R.id.line_chart_future)
    LineChartView lineChartFuture;
    @BindView(R.id.line_chart_three)
    LineChartView lineChart3;
    @BindView(R.id.line_chart_four)
    LineChartView lineChart4;
    @BindView(R.id.backgroundFuture)
    LinearLayout backgroundFuture;

    @BindView(R.id.visitHistory)
    TextView visitRecord;

    @BindView(R.id.firstPreferCity)
    TextView firstTextView;
    @BindView(R.id.secondPreferCity)
    TextView secondText;
    @BindView(R.id.threePreferCity)
    TextView threeText;
    @BindView(R.id.fourPreferCity)
    TextView fourText;

    @BindView(R.id.backgroudThree)
    LinearLayout backgroundThree;

    @BindView(R.id.backgroudFour)
    LinearLayout backGroundFour;

    private SharedPreferences sp;

    //历史天数
    private Integer dayPast;
    //未来天数
    private Integer dayFuture;

//    //X轴的标注
//    public static String[] datePast = new String[8];
    public static String[] dateFuturetoday = new String[8];
//    public static String[] date3 = new String[8];//X轴的标注
//    public static String[] dateFuture = new String[8];

    //图表的数据点
    public static int[] tempLowFuturetoday = new int[8];//图表的数据点
    public static int[] tempHighFuturetoday = new int[8];//图表的数据点
    public static int[] tempLowPast = new int[8];
    public static int[] tempHighPast = new int[8];//图表的数据点
    public static int[] tempLowFuture = new int[8];//图表的数据点
    public static int[] tempHighFuture = new int[8];//图表的数据点
    public static int[] tempLowFuture3 = new int[8];//图表的数据点
    public static int[] tempHighFuture3 = new int[8];//图表的数据点
    public static int[] tempLowFuture4 = new int[8];//图表的数据点
    public static int[] tempHighFuture4 = new int[8];//图表的数据点


    private List<PointValue> pointLowFuturetoday = new ArrayList<>();
    private List<PointValue> pointHighFuturetoday = new ArrayList<>();
    private List<PointValue> pointLowPast = new ArrayList<>();
    private List<PointValue> pointHighPast = new ArrayList<>();
    private List<PointValue> pointLowFuture = new ArrayList<>();
    private List<PointValue> pointHighFuture = new ArrayList<>();
    private List<PointValue> pointLow3 = new ArrayList<>();
    private List<PointValue> pointHigh3 = new ArrayList<>();
    private List<PointValue> pointLow4 = new ArrayList<>();
    private List<PointValue> pointHigh4 = new ArrayList<>();

    private List<AxisValue> axisXFuturetoday = new ArrayList<>();
    private List<AxisValue> axisXPast = new ArrayList<>();
    private List<AxisValue> axisXFuture = new ArrayList<>();
    private List<AxisValue> axisX3 = new ArrayList<>();
    private List<AxisValue> axisX4 = new ArrayList<>();


    //以下是新的
    //第一个城市的最高温
    public static int[] tempHighFutureOfCity1 = new int[8];
    //第一个城市的最低温
    public static int[] tempLowFutureOfCity1 = new int[8];
    //第一个城市的日期
    public static String[] dateFutureofCity1 = new String[8];



    //第一个城市的最高温
    public static int[] tempHighFutureOfCity2 = new int[8];
    //第一个城市的最低温
    public static int[] tempLowFutureOfCity2 = new int[8];
    //第一个城市的日期
    public static String[] dateFutureofCity2 = new String[8];

    //第一个城市的最高温
    public static int[] tempHighFutureOfCity3 = new int[8];
    //第一个城市的最低温
    public static int[] tempLowFutureOfCity3 = new int[8];
    //第一个城市的日期
    public static String[] dateFutureofCity3 = new String[8];

    //第一个城市的最高温
    public static int[] tempHighFutureOfCity4 = new int[8];
    //第一个城市的最低温
    public static int[] tempLowFutureOfCity4 = new int[8];
    //第一个城市的日期
    public static String[] dateFutureofCity4 = new String[8];



    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        CityDataBase cityDataBase = Room.databaseBuilder(getActivity().getApplicationContext(), CityDataBase.class, "city.db").allowMainThreadQueries().build();
        CityDao cityDao = cityDataBase.cityDao();
        List<String> preferCity = cityDao.getPreferCity();
        for (int i = 0; i < preferCity.size(); i++) {
            if (i==0){
                firstTextView.setText(preferCity.get(0));
            }
            if (i==1){
                secondText.setText(preferCity.get(1));
            }
            if (i==2){
                threeText.setText(preferCity.get(2));
            }
            if (i==3){
                fourText.setText(preferCity.get(3));
            }

        }
//        if (!preferCity.get(0).equals("")){
//            firstTextView.setText(preferCity.get(0));
//        }
//        if (!preferCity.get(1).equals("")){
//            secondText.setText(preferCity.get(1));
//        }
//        if (!preferCity.get(2).equals("")){
////            threeText.setText(preferCity.get(2));
//        }
////        if (!preferCity.get(3).equals("")){
////            fourText.setText(preferCity.get(3));
////        }


        //这里执行时为了添加一个城市后能够刷新历史页面
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
        //访问的城市记录
        String answer = "";
        for (String city :
                preferCity) {
            Log.i("hh", city);
            answer+=city+"/ ";
        }
        visitRecord.setText("访问记录:"+answer);

        //历史图标初始化
        pastDateInit();
        //图标初始化，设置透明度和sp
        init();
        //获取x轴的标注
        getAxisXLables();
        //获取坐标点
        getAxisPoints();
        //初始化helloChart
        HelloChartInit();
        return view;
    }

    /**
     * 初始化
     */
    private void init() {
//        // 设置背景透明
        backgroundPast.getBackground().setAlpha(50);
        backgroundFuture.getBackground().setAlpha(50);
        backgroundThree.getBackground().setAlpha(50);
        backGroundFour.getBackground().setAlpha(50);
//        // sp初始化
        sp = getActivity().getSharedPreferences("com.hznu.lin.project_preferences", Context.MODE_PRIVATE);
        dayPast = Integer.valueOf(sp.getString("day_past", "7"));
        dayFuture = Integer.valueOf(sp.getString("day_future", "7"));
    }

    /**
     * HelloChart初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void HelloChartInit() {
        FutureInit();
        pastInit();
        IniteChartThree();
        IniteFourChart();
    }

    /**
     * 设置X轴的显示
     */
    private void getAxisXLables() {
        try {
            // Past
            for (int i = 0; i < 7; i++) {
                axisXFuturetoday.add(new AxisValue(i).setLabel(dateFuturetoday[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "历史天数不足，无法显示历史天气", Toast.LENGTH_SHORT);
        }
        try {
            // Past
            for (int i = 0; i < 7; i++) {
                axisXPast.add(new AxisValue(i).setLabel(dateFutureofCity1[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "历史天数不足，无法显示历史天气", Toast.LENGTH_SHORT);
        }
        try {
             //Future
            for (int i = 0; i < 7; i++) {
                axisXFuture.add(new AxisValue(i).setLabel(dateFutureofCity2[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT);
        }
        try {
            //three
            for (int i = 0; i < 7; i++) {
                axisX3.add(new AxisValue(i).setLabel(dateFutureofCity3[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT);
        }
        try {
            //Four
            for (int i = 0; i < 7; i++) {
                axisX4.add(new AxisValue(i).setLabel(dateFutureofCity4[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "还可以添加城市哦~", Toast.LENGTH_SHORT);
        }

    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        try {
            // today
            for (int i = 0; i < 7; i++) {
                pointLowFuturetoday.add(new PointValue(i, tempLowFuturetoday[i]));
            }

            for (int i = 0; i < 7; i++) {
                pointHighFuturetoday.add(new PointValue(i, tempHighFuturetoday[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "历史天数不足，无法显示历史天气", Toast.LENGTH_SHORT);
        }
        try {
            // Past
            for (int i = 0; i < 7; i++) {
                pointLowPast.add(new PointValue(i, tempLowFutureOfCity1[i]));
            }

            for (int i = 0; i < 7; i++) {
                pointHighPast.add(new PointValue(i, tempHighFutureOfCity1[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "历史天数不足，无法显示历史天气", Toast.LENGTH_SHORT);
        }

        try {
            // Future
            for (int i = 0; i < 7; i++) {
                pointLowFuture.add(new PointValue(i, tempLowFutureOfCity2[i]));
            }

            for (int i = 0; i < 7; i++) {
                pointHighFuture.add(new PointValue(i, tempHighFutureOfCity2[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT);
        }
        try {
            // three
            for (int i = 0; i < 7; i++) {
                pointLow3.add(new PointValue(i, tempLowFutureOfCity3[i]));
            }

            for (int i = 0; i < 7; i++) {
                pointHigh3.add(new PointValue(i, tempHighFutureOfCity3[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT);
        }

        try {
            // four
            for (int i = 0; i < 7; i++) {
                pointLow4.add(new PointValue(i, tempLowFutureOfCity4[i]));
            }

            for (int i = 0; i < 7; i++) {
                pointHigh4.add(new PointValue(i, tempHighFutureOfCity4[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "还可以添加城市哦~", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 历史图表初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void pastInit() {
        List<Line> lines = new ArrayList<>();
        // low
        Line lineLow = new Line(pointLowPast).setColor(Color.parseColor("#0099ff"));
        lineLow.setShape(ValueShape.CIRCLE);
        lineLow.setCubic(true);//曲线是否平滑，即是曲线还是折线
        lineLow.setFilled(false);//是否填充曲线的面积
        lineLow.setHasLabels(true);//曲线的数据坐标是否加上备注
        lineLow.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lineLow.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(lineLow);

        // high
        Line lineHigh = new Line(pointHighPast).setColor(Color.parseColor("#0099ff"));
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
        axisX.setName("第一个城市的天气预报");  //表格名称
        axisX.setTextSize(13);//设置字体大小
        axisX.setTextColor(Color.WHITE);
        axisX.setValues(axisXPast);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线

        //设置行为属性，支持缩放、滑动以及平移
        lineChartPast.setInteractive(true);
        lineChartPast.setZoomType(ZoomType.HORIZONTAL);
        lineChartPast.setMaxZoom((float) 2);//最大方法比例
        lineChartPast.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartPast.setLineChartData(data);
        lineChartPast.setVisibility(View.VISIBLE);

        // 视角设置
        Viewport v = new Viewport(lineChartPast.getMaximumViewport());
        v.top = Arrays.stream(tempHighFutureOfCity1).max().getAsInt() + 1; //最高点为最大值+1
        v.bottom = Arrays.stream(tempLowFutureOfCity1).min().getAsInt() - 1; //最低点为最小值-1
        lineChartPast.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        lineChartPast.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图
    }

    /**
     * 未来图表初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
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
        axisX.setName("第二个城市的天气预报");  //表格名称
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
        v.top = Arrays.stream(tempHighFutureOfCity2).max().getAsInt() + 1; //最高点为最大值+1
        v.bottom = Arrays.stream(tempLowFutureOfCity2).min().getAsInt() - 1; //最低点为最小值-1
        lineChartFuture.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        lineChartFuture.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图

    }

    /**
     * 历史图表初始化
     * 第三个表
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void IniteChartThree() {
        List<Line> lines = new ArrayList<>();
        // low
        Line lineLow = new Line(pointLow3).setColor(Color.parseColor("#0099ff"));
        lineLow.setShape(ValueShape.CIRCLE);
        lineLow.setCubic(true);//曲线是否平滑，即是曲线还是折线
        lineLow.setFilled(false);//是否填充曲线的面积
        lineLow.setHasLabels(true);//曲线的数据坐标是否加上备注
        lineLow.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lineLow.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(lineLow);

        // high
        Line lineHigh = new Line(pointHigh3).setColor(Color.parseColor("#0099ff"));
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
        axisX.setName("三个城市的天气预报");  //表格名称
        axisX.setTextSize(13);//设置字体大小
        axisX.setTextColor(Color.WHITE);
        axisX.setValues(axisX3);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线

        //设置行为属性，支持缩放、滑动以及平移
        lineChart3.setInteractive(true);
        lineChart3.setZoomType(ZoomType.HORIZONTAL);
        lineChart3.setMaxZoom((float) 2);//最大方法比例
        lineChart3.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart3.setLineChartData(data);
        lineChart3.setVisibility(View.VISIBLE);

        // 视角设置
        Viewport v = new Viewport(lineChart3.getMaximumViewport());
        v.top = Arrays.stream(tempHighFutureOfCity3).max().getAsInt() + 1; //最高点为最大值+1
        v.bottom = Arrays.stream(tempLowFutureOfCity3).min().getAsInt() - 1; //最低点为最小值-1
        lineChart3.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        lineChart3.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图
    }


    /**
     * 历史图表初始化
     * 第四个表
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void IniteFourChart() {
        List<Line> lines = new ArrayList<>();
        // low
        Line lineLow = new Line(pointLow4).setColor(Color.parseColor("#0099ff"));
        lineLow.setShape(ValueShape.CIRCLE);
        lineLow.setCubic(true);//曲线是否平滑，即是曲线还是折线
        lineLow.setFilled(false);//是否填充曲线的面积
        lineLow.setHasLabels(true);//曲线的数据坐标是否加上备注
        lineLow.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lineLow.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(lineLow);

        // high
        Line lineHigh = new Line(pointHigh4).setColor(Color.parseColor("#0099ff"));
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
        axisX.setName("第四个城市的天气预报");  //表格名称
        axisX.setTextSize(13);//设置字体大小
        axisX.setTextColor(Color.WHITE);
        axisX.setValues(axisX4);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线

        //设置行为属性，支持缩放、滑动以及平移
        lineChart4.setInteractive(true);
        lineChart4.setZoomType(ZoomType.HORIZONTAL);
        lineChart4.setMaxZoom((float) 2);//最大方法比例
        lineChart4.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart4.setLineChartData(data);
        lineChart4.setVisibility(View.VISIBLE);

        // 视角设置
        Viewport v = new Viewport(lineChart4.getMaximumViewport());
        v.top = Arrays.stream(tempHighFutureOfCity4).max().getAsInt() + 1; //最高点为最大值+1
        v.bottom = Arrays.stream(tempLowFutureOfCity4).min().getAsInt() - 1; //最低点为最小值-1
        lineChart4.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        lineChart4.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图
    }






    /**
     * 历史图标初始化
     */
    private void pastDateInit() {
//        WeatherDataBase weatherDataBase = Room.databaseBuilder(getContext(), WeatherDataBase.class, "WeatherDataBase.db").allowMainThreadQueries().build();
//        WeatherDataDao weatherDataDao = weatherDataBase.weatherDataDao();
//        // 获取最近8天数据
//        List<WeatherData> all = weatherDataDao.getRecently();
//        int count = 7;
//        for (WeatherData data : all) {
//            Log.i("WEATHER", data.toString());
//            datePast[count] = data.getDate();
//            tempLowPast[count] = Integer.parseInt(data.getLow());
//            tempHighPast[count] = Integer.parseInt(data.getHigh());
//            count--;
//        }

    }

}