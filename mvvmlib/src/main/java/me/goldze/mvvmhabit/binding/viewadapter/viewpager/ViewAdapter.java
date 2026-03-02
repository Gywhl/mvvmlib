package me.goldze.mvvmhabit.binding.viewadapter.viewpager;

import androidx.databinding.BindingAdapter;
import androidx.viewpager2.widget.ViewPager2;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Created by goldze on 2017/6/18.
 */
public class ViewAdapter {
    @BindingAdapter(value = {"onPageChangeCommand"}, requireAll = false)
    public static void onPageSelectedCommand(final ViewPager2 viewPager2,
                                             final BindingCommand<ViewPagerDataWrapper> onPageChangeCommand) {
        //ViewPage2选中改变监听
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (onPageChangeCommand != null) {
                    onPageChangeCommand.execute(new ViewPagerDataWrapper(position,positionOffset,positionOffsetPixels));
                }
            }
        });
    }

    public static class ViewPagerDataWrapper {
        public float positionOffset;
        public float position;
        public int positionOffsetPixels;

        public ViewPagerDataWrapper(int position, float positionOffset, int positionOffsetPixels) {
            this.positionOffset = positionOffset;
            this.position = position;
            this.positionOffsetPixels = positionOffsetPixels;
        }
    }
}
