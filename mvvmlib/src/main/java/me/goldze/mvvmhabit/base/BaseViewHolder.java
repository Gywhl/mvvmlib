package me.goldze.mvvmhabit.base;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private ViewDataBinding binding;
    private View convertView;

    public BaseViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.convertView = binding.getRoot();
        this.binding = binding;
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    public void onBaseBinding(T model, int variableId, final int position) {
        /*list_item_excel 布局*/
        binding.setVariable(variableId, model);
        binding.executePendingBindings();
    }

//    private SparseArray<View> views = new SparseArray<>();
//
//    public <T extends View> T getView(int resId) {
//        View v = views.get(resId);
//        if (null == v) {
//            v = convertView.findViewById(resId);
//            views.put(resId, v);
//        }
//        return (T) v;
//    }

    public View getConvertView() {
        return convertView;
    }
}
