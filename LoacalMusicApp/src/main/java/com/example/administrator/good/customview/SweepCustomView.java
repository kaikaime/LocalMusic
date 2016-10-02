package com.example.administrator.good.customview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.good.R;
import com.example.administrator.good.adapter.MusicListAdapter;
import com.example.administrator.good.faceinter.ItemOnclikListener;


/**
 * Created by lvkaixue on 2016/9/18.
 */
public class SweepCustomView extends ViewGroup {
    public static int CONTENTLEFT = 0;
    public static int CONTENTLEFTX;
    private LinearLayout mContentView;
    private LinearLayout mDeleteView;
    private int mDeleteViewWidth;
    private int mDeMeasuredWidth;
    private ViewDragHelper mDragHelper;
    private int mConViewWidth;
    private int mConViewHeight;
    private int mDelViewWidth;
    private int mDelViewHeight;
    private OnModifyListener modifyListener;
    private OnDeleteListener deleteListener;
    private boolean isOpen = false;
    private OnOpenListener onOpenListener;
    private OnLeftXIsZeroListener leftIsZeroListener;
    private boolean isReleased = false;
    private ItemOnclikListener item;
    private int viewIndex;
    private int ItemDown=0;
    private int ItemMove = 0;
    private int itemX = 0;

    public SweepCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, 0.1f, callback);
    }


    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mContentView || child == mDeleteView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //内容容器限定边界
            if (child == mContentView) {
                if (left < -mDeleteView.getMeasuredWidth()) {
                    return -mDeleteView.getMeasuredWidth();
                } else if (left > 0) {
                    return 0;
                }
            }
            //删除容器限定边界
            if (child == mDeleteView) {
                int mConDelWdith = mContentView.getMeasuredWidth() -mDeleteView.getMeasuredWidth();
                if (left < mConDelWdith) {
                    return mConDelWdith;
                } else if (left > mContentView.getMeasuredWidth()) {
                    return mContentView.getMeasuredWidth();
                }
            }
            System.out.println("left: ====="+left);
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mConViewWidth = mContentView.getMeasuredWidth();
            mConViewHeight = mContentView.getMeasuredHeight();
            mDelViewWidth = mDeleteView.getMeasuredWidth();
            mDelViewHeight = mDeleteView.getMeasuredHeight();

            //计算动作容器偏移量
            if (changedView == mContentView) {
               mDeleteView.layout(mConViewWidth + left, 0, mConViewWidth + left + mDelViewWidth, mDelViewHeight);
            } else if (changedView == mDeleteView) {
                //计算删除容器的偏移量
                mContentView.layout(left - mConViewWidth, 0, mConViewWidth + left, mConViewHeight);
            }

            if(leftIsZeroListener != null){
                leftIsZeroListener.isLeftIsZero(left,isOpen);
            }
        }

        /**
         * 当删除按钮显示的时候为打开状态，当删除按钮隐藏的时候为关闭状态
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            isReleased = true;
            if(releasedChild == mContentView){
                //向右滑动
                if(xvel >=0){
                    if(mContentView.getLeft()<-mDelViewWidth/2f && mContentView.getLeft() >= -mDelViewWidth){
                        //打开
                        open();
                    }else if( mContentView.getLeft()>-mDelViewWidth/2f && mContentView.getLeft() <=0){
                        //关闭
                        close();
                    }
                }
                if(xvel <0) {
                    //向左滑动
                    if (mContentView.getLeft() < -mDelViewWidth / 2f && mContentView.getLeft() >= -mDelViewWidth) {
                        //打开
                        open();
                    }
                    if (mContentView.getLeft() > -mDelViewWidth / 2f && mContentView.getLeft() <= 0) {
                        //关闭
                        close();
                    }
                }
            }
          ViewCompat.postInvalidateOnAnimation(SweepCustomView.this);
        }
    };

    //反复让动画效果触发
    @Override
    public void computeScroll() {
        if(mDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SweepCustomView.this);
            System.out.println("================");
        }else {
            if(isReleased){
                //如果用户松手了，当内容容器滑到边缘的时候调用
                System.out.println("容器滑到最边缘了: " + mContentView.getLeft());
                //关闭
                if(mContentView.getLeft() == CONTENTLEFT){
                    isOpen = false;
                    listener();
                    System.out.println("现在关闭");
                }else if(mContentView.getLeft() == CONTENTLEFTX){
                    isOpen = true;
                    listener();
                    //打开
                    System.out.println("现在打开");
                }
            }
        }

        //获取内容容器的左边移动变量left
        if(leftIsZeroListener != null){
            leftIsZeroListener.isLeftIsZero(mContentView.getLeft(), isOpen);
        }
    }

    private void listener(){
        if(onOpenListener != null){
            onOpenListener.isOpen(isOpen, this);
        }
    }
    public void close() {
        boolean mFlag = mDragHelper.smoothSlideViewTo(mContentView,0,0);
        boolean mDeFlag =  mDragHelper.smoothSlideViewTo(mDeleteView, mConViewWidth, 0);
        if(mFlag&&mDeFlag){
            ViewCompat.postInvalidateOnAnimation(SweepCustomView.this);
        }
    }

    public void open() {
        boolean mFlag = mDragHelper.smoothSlideViewTo(mContentView, -mDelViewWidth, 0);
        boolean mDeFlag = mDragHelper.smoothSlideViewTo(mDeleteView, mConViewWidth - mDelViewWidth, 0);
        if(mFlag && mDeFlag){
            ViewCompat.postInvalidateOnAnimation(SweepCustomView.this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = (LinearLayout) getChildAt(0);
        mDeleteView = (LinearLayout) getChildAt(1);
        //动作框的宽度
        mDeleteViewWidth = mDeleteView.getLayoutParams().width;

        //修改按钮
        (((TextView) mDeleteView.findViewById(R.id.modify))).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifyListener != null) {
                    modifyListener.isModify(true, isOpen, SweepCustomView.this);
                }
            }
        });

        //删除按钮
        (((TextView)mDeleteView.findViewById(R.id.delete))).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(deleteListener != null){
                        deleteListener.isDelete(true,isOpen,SweepCustomView.this);
                    }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //主界面内容容器和父窗体的大小一致，
        mContentView.measure(widthMeasureSpec, heightMeasureSpec);

        //确定删除视图的宽度,设置固定的宽度值
        mDeMeasuredWidth = MeasureSpec.makeMeasureSpec(mDeleteViewWidth, MeasureSpec.EXACTLY);
        mDeleteView.measure(mDeMeasuredWidth, heightMeasureSpec);

        //重新设置父容器的大小
        int parentWidht = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(parentWidht, parentHeight);

        //获取动作容器的宽度
        CONTENTLEFTX = -mDeleteView.getMeasuredWidth();
    }

    /**
     * 关闭时候不实现动画效果
     */
    public void closeAll(){
        fixLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        fixLayout();
    }

    private void fixLayout(){
        //设置主视图的位置
        mContentView.layout(0, 0, mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());
        //设置删除视图的位置
        mDeleteView.layout(mContentView.getMeasuredWidth(), 0, mContentView.getMeasuredWidth() + mDeMeasuredWidth, mDeleteView.getMeasuredHeight());
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        mTouchEvent(event);
        return true;
    }



    public void setItemOnclikListener(int viewIndex,ItemOnclikListener item){
        this.viewIndex = viewIndex;
        this.item = item;
    }

    //判断条目是点击还是移动
    private void mTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            ItemDown = (int) event.getX();
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(event.getAction() == ItemDown){  ItemMove = ItemDown;    }
            ItemMove  = (int) event.getX();
            itemX = ItemMove - ItemDown;
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            if (item != null) {
                if (itemX ==0) {
                    item.itemOnclik(viewIndex,true);
                }else if(itemX <= 50 && itemX >= 0){
                    item.itemOnclik(viewIndex,true);
                }else {
                    item.itemOnclik(viewIndex,false);
                }
            }
        }
    }

    public void addDeleteListener(OnDeleteListener listener){
        this.deleteListener = listener;
    }
    public interface OnDeleteListener{
        void isDelete(boolean flag, boolean isOpen, SweepCustomView sweepCustomView);
    }
    public void addModifyListener(OnModifyListener listener){
        this.modifyListener = listener;
    }
    public interface OnModifyListener{
        void isModify(boolean flag, boolean isOpen, SweepCustomView sweepCustomView);
    }

    public void addOpenListener(OnOpenListener listener){
        this.onOpenListener = listener;
    }
    public interface OnOpenListener{
        void isOpen(boolean flag, SweepCustomView sweepCustomView);
    }

    public void addLeftXIsZeroListener(OnLeftXIsZeroListener listener) {
        this.leftIsZeroListener = listener;
    }
    public interface  OnLeftXIsZeroListener{
        void isLeftIsZero(int leftX, boolean isOpen);
    }
}
