package com.example.weather_sample_android_app.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_sample_android_app.R;
import com.example.weather_sample_android_app.databinding.ActivityMainBinding;
import com.example.weather_sample_android_app.model.Forecast;
import com.example.weather_sample_android_app.utlis.GenericParcelable;
import com.example.weather_sample_android_app.view.adapter.ForecastAdapter;
import com.example.weather_sample_android_app.viewmodel.ForecastViewModel;

import java.util.List;

public class MainActivity extends BaseActivity<ForecastViewModel> {

    private ActivityMainBinding binding;

    @BindingAdapter({"app:items"})
    public static void setItems(RecyclerView recyclerView, List<ForecastAdapter.ItemViewType> items) {
        ForecastAdapter adapter = (ForecastAdapter) recyclerView.getAdapter();
        if (items != null && !items.isEmpty() && adapter != null) {
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel);

        setupView();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ForecastAdapter());
    }

    @Nullable
    protected ForecastViewModel createViewModel(@Nullable Bundle savedInstanceState) {
        viewModel = new ForecastViewModel(this);

        if (savedInstanceState != null) {
            GenericParcelable parcelable = savedInstanceState.getParcelable(EXTRA_VIEW_MODEL_STATE);
            if (parcelable != null && parcelable.getValue() != null) {
                viewModel.setModelData((Forecast) parcelable.getValue());
            }
        }

        return viewModel;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (viewModel != null) {
            GenericParcelable viewModelState = new GenericParcelable<>(viewModel.getModelData());
            outState.putParcelable(EXTRA_VIEW_MODEL_STATE, viewModelState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            viewModel.fetchWeatherForecast(viewModel.getLastLocation());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
