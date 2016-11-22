package com.edu.jereh.maptest;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.edu.jereh.maptest.adapter.ListViewAdapter;
import com.edu.jereh.maptest.entity.Info;
import com.edu.jereh.maptest.entity.Location;
import com.edu.jereh.maptest.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener
//        , RadioGroup.OnCheckedChangeListener
        ,OnPoiSearchListener,View.OnClickListener,AMap.OnMarkerClickListener,AMap.InfoWindowAdapter
{
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private View popView;
    private PopupWindow pw;
    PopupWindow popupWindow;
    private List<Info> list;
    private Info info;
    private Location location;
    private EditText et;
//    private MyApplication app;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private Button bt1,bt2;
    private PoiResult poiResult; // poi返回的结果
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private LatLonPoint point;//当前经纬度

    public List<Info> getList() {
        return list;
    }

    public void setList(List<Info> list) {
        this.list = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et= (EditText) findViewById(R.id.et);
        bt1= (Button) findViewById(R.id.bt1);
        bt2= (Button) findViewById(R.id.bt2);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        init();
    }

    /**
     * 初始化Amap从Fragment中获得
     */
    private void init() {
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            setUpMap();
        }
        list=new ArrayList<>();


    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
    }
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//            case R.id.et:
//                // 设置定位的类型为定位模式
//                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//                break;
//            case R.id.bt1:
//                // 设置定位的类型为 跟随模式
//                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
//                break;
//            case R.id.bt2:
//                // 设置定位的类型为根据地图面向方向旋转
//                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
//                break;
//        }
//
//    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//            case R.id.gps_locate_button:
//                // 设置定位的类型为定位模式
//                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//                break;
//            case R.id.gps_follow_button:
//                // 设置定位的类型为 跟随模式
//                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
//                break;
//            case R.id.gps_rotate_button:
//                // 设置定位的类型为根据地图面向方向旋转
//                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
//                break;
//        }
//
//    }

    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" );
        progDialog.show();
    }

    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                if (point==null){
                    point=new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());
                }else {
                    point.setLatitude(amapLocation.getLatitude());
                    point.setLongitude(amapLocation.getLongitude());
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    //POI信息查询回调方法
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
//                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//                    List<SuggestionCity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
//                        app=MyApplication.getApp();
                        for (PoiItem poi:poiItems) {
                            info=new Info();
                            info.setCityName(poi.getCityName());
                            info.setTitle(poi.getTitle());
                            info.setTel(poi.getTel());
                            info.setDistance(poi.getDistance() + "");
                            info.setNlatitude(poi.getLatLonPoint().getLatitude());
                            info.setNlongitude(poi.getLatLonPoint().getLongitude());
                            info.setLatitude(point.getLatitude());
                            info.setLongitude(point.getLongitude());
                            Log.d("========-",""+info.getNlatitude());
                            list.add(info);
//                            app.setInfoList(list);
                        }
                        popView=getLayoutInflater().inflate(R.layout.pop_list_layout, null);
                        ListView listView= (ListView) popView.findViewById(R.id.listview);
                        listView.setAdapter(new ListViewAdapter(list, this));

                    } else {
                        ToastUtil.show(MainActivity.this,
                                R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(MainActivity.this,
                        R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.bt1){
            poiSearch();
        }else if (id==R.id.bt2){
            pw=getPopWindow();

        }
    }
    //构建一个PopWindow弹出附近商家列表
    public PopupWindow getPopWindow() {
        // popupWindow.setFocusable(true);
        popupWindow = new PopupWindow(popView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //点击pop外面是否消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popstyle);
        //设置背景透明度
        backgroundAlpha(0.5f);
        //————————
//        //设置View隐藏
//        bt2.setVisibility(View.GONE);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(bt2, Gravity.RIGHT, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //设置背景透明度
//                 backgroundAlpha(1f);如果不写这句话  即使popwindow消失了  背景还是灰的
                backgroundAlpha(1f);
                //设置View可见
                bt2.setVisibility(View.VISIBLE);
            }
        });
        return popupWindow;
    }

    //设置屏幕背景透明度方法
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams ll = getWindow().getAttributes();
        ll.alpha = bgAlpha;
        getWindow().setAttributes(ll);
    }

    public void poiSearch(){
        showProgressDialog();
        String keyWord=et.getText().toString();
        query = new PoiSearch.Query(keyWord,"","烟台");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        poiSearch = new PoiSearch(this, query);

        //设置搜索范围
        if (point!=null){
            PoiSearch.SearchBound bound=new PoiSearch.SearchBound(point,20000);
            poiSearch.setBound(bound);
        }

        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
                null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAMapNavi(marker);
            }
        });
        return view;
    }
    //调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);
        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());
        }
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }
}