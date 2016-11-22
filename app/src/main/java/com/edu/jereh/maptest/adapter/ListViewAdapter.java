package com.edu.jereh.maptest.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.jereh.maptest.R;
import com.edu.jereh.maptest.entity.Info;
import com.edu.jereh.maptest.entity.Location;
import com.edu.jereh.maptest.route.RouteActivity;

import java.util.List;

import static com.edu.jereh.maptest.R.id.call;

/**
 * Created by lenovo on 2016/9/9.
 */
public class ListViewAdapter extends BaseAdapter {
    private List<Info> myData;
    private Context context;
    private String phone;
    private List<Location> locationList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ListViewAdapter(List<Info> myData, Context context) {
        this.myData = myData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Object getItem(int position) {
        return myData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.info_list_item,null);
            vh.cityName= (TextView) convertView.findViewById(R.id.name);
            vh.title= (TextView) convertView.findViewById(R.id.title);
            vh.distance= (TextView) convertView.findViewById(R.id.distance);
            vh.tel= (TextView) convertView.findViewById(R.id.tel);
            vh.call= (Button) convertView.findViewById(call);
            vh.go= (Button) convertView.findViewById(R.id.go);
            vh.call.setOnClickListener(new ClickListener(position));
            vh.go.setOnClickListener(new ClickListener(position));
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        Info info=myData.get(position);
        vh.cityName.setText(info.getCityName());
        vh.title.setText(info.getTitle());
        vh.tel.setText(info.getTel());
        Log.d("====-==t", info.getTel() + "");
        vh.distance.setText(info.getDistance()+"米");
        Log.d("==bbb==", info.getCityName());
        return convertView;
    }

    private class ViewHolder{
        TextView cityName;
        TextView title;
        TextView tel;
        TextView distance;
        Button call;
        Button go;
        TextView latitude;
        TextView longitude;
        TextView nlatitude;
        TextView nlongitude;
        LinearLayout rl;
    }
public class ClickListener implements View.OnClickListener{
            private int position;
            public ClickListener(int position) {
            this.position = position;
        }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.call){
            Info info=myData.get(position);
            phone=info.getTel().split(";")[0];
            Uri uri1 = Uri.parse("tel:"+phone);
            Log.d("======-==",phone+"");
            Intent intent = new Intent(Intent.ACTION_CALL, uri1);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            context.startActivity(intent);
        }else if (id==R.id.go){
            Info info=myData.get(position);
            Intent intent=new Intent(getContext(), RouteActivity.class);
            intent.putExtra("latitude",info.getLatitude());
            intent.putExtra("longitude",info.getLongitude());
            intent.putExtra("nlatitude",info.getNlatitude());
            intent.putExtra("nlongitude",info.getNlongitude());
            context.startActivity(intent);



        }
    }
}
}
