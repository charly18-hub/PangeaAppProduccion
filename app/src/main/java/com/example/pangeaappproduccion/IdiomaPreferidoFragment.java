package com.example.pangeaappproduccion;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.pangeaappproduccion.Util.UtilFragment;

public class IdiomaPreferidoFragment extends UtilFragment {
    String idioma;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_idioma_preferido, container, false);
        RadioGroup radio = view.findViewById(R.id.radio_group);
        idioma="Vacio!";
        // Inflate the layout for this fragment
        radio.setOnCheckedChangeListener((radioGroup, i) -> {
            // get selected radio button from radioGroup
            int selectedId = radio.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            RadioButton radioButton = view.findViewById(selectedId);
            idioma=radioButton.getText().toString();
        });
        Button btn = view.findViewById(R.id.button9);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        btn.setOnClickListener(view1 -> {
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton =view.findViewById(selectedId);
            if(!idioma.equals("Vacio!")){
                obtenerIdiomaPreferido(radioButton.getText().toString());
                requireActivity().recreate();
            }
        });
        return view;
    }
}