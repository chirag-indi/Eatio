package com.MADLab.www.Eatio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    SliderLayout sliderLayout1;
    HashMap<String, Integer> Hash_file_maps1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Hash_file_maps1 = new HashMap<>();
        sliderLayout1 = (SliderLayout) rootView.findViewById(R.id.slider1);

        Hash_file_maps1.put("C1", R.drawable.slider_image1);
        Hash_file_maps1.put("C2", R.drawable.slider_image2);
        Hash_file_maps1.put("C3", R.drawable.slider_image3);

        for (String name : Hash_file_maps1.keySet()) {

            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView
                    .description(name)
                    .image(Hash_file_maps1.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout1.addSlider(textSliderView);
        }
        sliderLayout1.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout1.setCustomAnimation(new DescriptionAnimation());
        sliderLayout1.setDuration(3000);
        sliderLayout1.addOnPageChangeListener(this);

        return rootView;
    }

    @Override
    public void onStop() {
        sliderLayout1.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        //Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
