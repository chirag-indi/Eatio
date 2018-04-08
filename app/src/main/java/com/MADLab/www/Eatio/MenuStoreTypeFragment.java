package com.MADLab.www.Eatio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuStoreTypeFragment extends Fragment {


    public MenuStoreTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_menu_store_type, container, false);

        String menuType = getArguments().getString(Config.menuItemTag);
        Button storeButton1 = (Button) rootView.findViewById(R.id.jltt_store_button);
        Button storeButton2 = (Button) rootView.findViewById(R.id.food_adda_store_button);

        if (menuType.equals(Config.menuMaggi) || menuType.equals(Config.menuMacroni)) {
            storeButton1.setVisibility(View.INVISIBLE);
        }

        if (menuType.equals(Config.menuPasta) || menuType.equals(Config.menuNoodles)) {
            storeButton2.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

}
