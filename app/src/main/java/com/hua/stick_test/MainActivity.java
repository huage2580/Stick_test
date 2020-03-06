package com.hua.stick_test;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ViewPager2 list;
    View stick;
    View icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        list.setAdapter(new TestAdapter());
        stick = findViewById(R.id.stick);
        icon = stick.findViewById(R.id.icon);
        CoordinatorLayout.LayoutParams lp= (CoordinatorLayout.LayoutParams) stick.getLayoutParams();
        Behaivor behaivor = (Behaivor) lp.getBehavior();
        behaivor.setCallBack(new Behaivor.ValueCallBack() {
            @Override
            public void onValueChange(float progress) {
                icon.setRotation(progress * 180);
            }

            @Override
            public void onRelease() {
                Toast.makeText(MainActivity.this,"跳转",Toast.LENGTH_SHORT).show();
            }
        });
    }

    class TestAdapter extends RecyclerView.Adapter<TestVH>{
        ArgbEvaluator evaluator = new ArgbEvaluator();
        @NonNull
        @Override
        public TestVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
            return new TestVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TestVH testVH, int i) {
            int color = (int) evaluator.evaluate((float) i/getItemCount(),0xffffffff,0xff000000);
            testVH.itemView.setBackgroundColor(color);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    class TestVH extends RecyclerView.ViewHolder{

        public TestVH(@NonNull View itemView) {
            super(itemView);
        }
    }

}
