package me.goldze.mvvmhabit.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import me.goldze.mvvmhabit.R;

public class BasePopupWindow<V extends ViewDataBinding, VM extends BaseViewModel> extends PopupWindow {

    protected V binding;
    protected VM viewModel;
    private FragmentActivity activity;
    /**
     * 自定义宽度
     */
    private int customWidth;

    public BasePopupWindow(Context context) {
        super(context.getApplicationContext());
    }

    public BasePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 创建pop
     * @param activity 上下文
     * @param viewModelId  viewModelId
     * @param layoutID 界面资源id
     * @param owner owner
     * @param lifecycle  lifecycle
     * @return
     */
    public BasePopupWindow createPop(FragmentActivity activity, int viewModelId, int layoutID, LifecycleOwner owner, Lifecycle lifecycle) {
        //默认居中
        return createPop(activity,viewModelId,layoutID,owner,lifecycle,RelativeLayout.CENTER_IN_PARENT,true);
    }

    /**
     * 创建pop
     * @param activity 上下文
     * @param viewModelId  viewModelId
     * @param layoutID 界面资源id
     * @param owner owner
     * @param lifecycle  lifecycle
     * @return
     */
    public BasePopupWindow createPop(FragmentActivity activity, int viewModelId, int layoutID, LifecycleOwner owner, Lifecycle lifecycle,int location) {
        //默认居中
        return createPop(activity,viewModelId,layoutID,owner,lifecycle,location,true);
    }

    /**
     * 创建pop
     * @param activity 上下文
     * @param viewModelId  viewModelId
     * @param layoutID 界面资源id
     * @param owner owner
     * @param lifecycle  lifecycle
     * @return
     */
    public BasePopupWindow createPop(FragmentActivity activity, int viewModelId, int layoutID, LifecycleOwner owner, Lifecycle lifecycle,boolean isNeedLayer) {
        //默认居中
        return createPop(activity,viewModelId,layoutID,owner,lifecycle,RelativeLayout.CENTER_IN_PARENT,isNeedLayer);
    }

    /**
     * 创建pop
     * @param activity 上下文
     * @param viewModelId  viewModelId
     * @param layoutID 界面资源id
     * @param owner owner
     * @param lifecycle  lifecycle
     * @param location 弹框位置
     * @param isNeedLayer 是否需要蒙层
     * @return
     */
    public BasePopupWindow createPop(FragmentActivity activity, int viewModelId, int layoutID, LifecycleOwner owner, Lifecycle lifecycle,int location,boolean isNeedLayer) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        this.activity = activity;

        View contentView = LayoutInflater.from(activity).inflate(layoutID, null);
        if (contentView == null) {
            return null;
        }
        binding = DataBindingUtil.bind(contentView);
        if (binding == null || owner == null || lifecycle == null) {
            return null;
        }

        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(activity, modelClass);
        }

        if(isNeedLayer){
            View bgView = LayoutInflater.from(activity).inflate(R.layout.include_bg_trans, null);
            if (bgView != null) {
                bgView.findViewById(R.id.layout_bg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isOutsideTouchable()){
                            dismiss();
                        }
                    }
                });
            }

            RelativeLayout container = bgView.findViewById(R.id.layout_root);
            // 创建内容视图，放置在容器的底部
            RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            // 将内容视图放置在底部
            contentParams.addRule(location);
            container.addView(contentView, contentParams);

            // 设置 contentView 不传递点击事件，否则点击事件会传递到背景上取消popwindow
            contentView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });  // 返回 true 阻止事件向下传递

            //View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏状态栏 + 导航栏
            //View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY：沉浸模式，避免出现后消失
            bgView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );

            setContentView(container);

            setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        }else{
            //View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏状态栏 + 导航栏
            //View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY：沉浸模式，避免出现后消失
            contentView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
            setContentView(contentView);
        }

        //注册RxBus
        viewModel.registerRxBus();
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(owner);
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel);
        //解决dialog中pop不显示问题
        setClippingEnabled(false);
        initParams();
        getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!(v instanceof EditText)){
                    hideSoftKeyboard();
                }
                return false;
            }
        });
        return this;
    }

    private void initParams() {
        //超出屏幕外会自动截取
        //不加的话键盘会挡住输入框
        setClippingEnabled(true);
        //能够响应触摸事件
        setTouchable(true);
        //获取焦点可以弹出键盘
        setFocusable(true);
        //获取外部触摸事件setTouchable或setFocusable为true时setOutsideTouchable设置false无效
        setOutsideTouchable(true);

        //设置透明背景
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //测量popwindow的宽高
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //setAnimationStyle(android.R.style.Animation_InputMethod);   // 设置窗口显示的动画效果

    }

    @Override
    public void dismiss() {
        hideSoftKeyboard();
        super.dismiss();
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getContentView().getWindowToken(), 0);
    }

    public void show() {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (customWidth > 0) {
            setWidth(customWidth);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (customWidth > 0) {
            setWidth(customWidth);
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 设置自定义宽度
     * @param width
     */
    public void setCustomWidth(int width) {
        this.customWidth = width;
    }

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity, ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication())).get(cls);
    }

}