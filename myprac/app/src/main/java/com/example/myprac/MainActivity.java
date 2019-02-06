package com.example.myprac;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<String> list=new ArrayList<>();
    Mypulltoview mypulltoview;
    RefreshView refreshView2;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                recyclerView.setAdapter(new MyAdapter(list));
                refreshView2.stopAnimation();
                mypulltoview.success();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.myrecycler);
        for (int i=0;i<20;i++){
            list.add("小明"+i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(list));
        mypulltoview=findViewById(R.id.mypullview);
        refreshView2 =findViewById(R.id.myrefresh);
        mypulltoview.setListener(new RefreshListener() {
            @Override
            public void startRefresh() {
                refreshView2.startAnimation();
            }

            @Override
            public void successrefresh() {
                refreshView2.stopAnimation();
                mypulltoview.success();
            }

            @Override
            public void startUpdate() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //更新逻辑
                            Thread.sleep(2000);
                            Collections.reverse(list);
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
    class MyAdapter extends RecyclerView.Adapter{
        List<String> list=new ArrayList<>();
        public MyAdapter(List<String> list){
            this.list=list;
        }
         class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.mytext);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mylayoutitem,viewGroup,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ViewHolder holder= (ViewHolder) viewHolder;
            holder.textView.setText(list.get(i));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshView2.stopAnimation();
    }
}
