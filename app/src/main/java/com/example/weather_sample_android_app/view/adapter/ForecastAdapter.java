package com.example.weather_sample_android_app.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.weather_sample_android_app.BR;
import com.example.weather_sample_android_app.R;

public class ForecastAdapter extends BaseRecyclerAdapter<ForecastAdapter.ItemViewType, BindingViewHolder> {

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindingViewHolder holder;

        switch (viewType) {
            case ItemViewType.ITEM_TYPE_CURRENT_FORECAST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_forecast_item, parent, false);
                holder = new BindingViewHolder(view);
                break;
            }
            case ItemViewType.ITEM_TYPE_DAILY_FORECAST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_forecast_item, parent, false);
                holder = new BindingViewHolder(view);
                break;
            }
            default:
                throw new IllegalStateException();
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BindingViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ItemViewType.ITEM_TYPE_CURRENT_FORECAST:

            case ItemViewType.ITEM_TYPE_DAILY_FORECAST:
                holder.getBinding().setVariable(BR.viewModel, getItem(position));
                holder.getBinding().executePendingBindings();
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }

    public interface ItemViewType {
        int ITEM_TYPE_CURRENT_FORECAST = 0;
        int ITEM_TYPE_DAILY_FORECAST = 1;

        int getItemType();
    }
}