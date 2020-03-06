package com.hua.stick_test;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Behaivor extends CoordinatorLayout.Behavior {
    private static final String TAG = "Behaivor";
    private View stick;
    private View targetRV;
    private float gravity = 0.5f;//阻力系数
    private float real_time_gravity = 0;//实时阻力
    private ValueCallBack callBack;


    public Behaivor() {
    }

    public Behaivor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCallBack(ValueCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        Log.d(TAG, "layoutDependsOn() called with: parent = [" + parent + "], child = [" + child + "], dependency = [" + dependency + "]");
        if (child.getId() == R.id.stick){
            stick = child;
            targetRV = dependency;
            return true;
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
        Log.d(TAG, "onLayoutChild() called with: parent = [" + parent + "], child = [" + child + "], layoutDirection = [" + layoutDirection + "]");
        child.layout(targetRV.getRight(),targetRV.getTop(),targetRV.getRight() + child.getMeasuredWidth(),targetRV.getBottom());
        return true;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        Log.d(TAG, "onStartNestedScroll() called with: coordinatorLayout = [" + coordinatorLayout + "], child = [" + child + "], directTargetChild = [" + directTargetChild + "], target = [" + target + "], axes = [" + axes + "], type = [" + type + "]");
        if (!target.canScrollHorizontally(1)){
            return true;
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
//        Log.d(TAG, "onNestedPreScroll() called with: coordinatorLayout = [" + coordinatorLayout + "], child = [" + child + "], target = [" + target + "], dx = [" + dx + "], dy = [" + dy + "], consumed = [" + consumed + "], type = [" + type + "]");
        if (targetRV.canScrollHorizontally(1)){
            return;
        }
        if (targetRV.getLeft() < -stick.getWidth() && dx >0){
            return;
        }
        if (dx < 0 && targetRV.getLeft() >= 0){
            return;
        }
        int offset = 0;
        if (dx > 0){// 拉出有阻力，回去不设处理
            real_time_gravity = gravity * (1 + (float)targetRV.getLeft() / (float) stick.getWidth());
        }else {
            real_time_gravity = 1.0f;
        }
        real_time_gravity = 0.5f;
        int temp = (int) (dx * real_time_gravity);
        if (dx > 0){
            int canScroll = stick.getWidth() + targetRV.getLeft();
            offset = Math.min(canScroll,temp);
        }else {
            int canScroll = targetRV.getLeft();
            offset = Math.max(temp,canScroll);
        }

        targetRV.offsetLeftAndRight(-offset);
        stick.offsetLeftAndRight(-offset);
        consumed[0] = dx;
        if (callBack!=null){
            callBack.onValueChange(-(float) targetRV.getLeft()/stick.getWidth());
        }

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        return true;
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return true;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        int back = -targetRV.getLeft();
        if (back >= stick.getWidth() && callBack!=null){
            callBack.onRelease();
        }
        targetRV.offsetLeftAndRight(back);
        stick.offsetLeftAndRight(back);
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
    }
    public interface ValueCallBack{
        void onValueChange(float progress);
        void onRelease();
    }
}
