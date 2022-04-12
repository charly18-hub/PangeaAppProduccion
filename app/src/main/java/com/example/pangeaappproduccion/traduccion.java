package com.example.pangeaappproduccion;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeaappproduccion.databinding.FragmentSlideshowBinding;
import com.example.pangeaappproduccion.ui.gallery.GalleryFragment;
import com.example.pangeaappproduccion.ui.slideshow.SlideshowFragment;
import com.example.pangeaappproduccion.ui.slideshow.SlideshowViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.Translator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link traduccion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class traduccion extends Fragment {

    private List<com.example.pangeaappproduccion.listTraducciones> listTraducciones;
    private AdapterTraducciones adapterTraducciones;

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerViewTraducciones;





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText text;
    Button identificar;
    TextView textView10,textView11;
    //FirebaseTranslator englishGermanTranslator;
    Translator englishGermanTranslator2;

    public traduccion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment traduccion.
     */
    // TODO: Rename and change types and number of parameters
    public static traduccion newInstance(String param1, String param2) {
        traduccion fragment = new traduccion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_traduccion, container, false);

        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpagerTraductores);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabsTraductores);
        tabs.setupWithViewPager(viewPager);




        text = (EditText)view.findViewById(R.id.textoNativo);
        textView10 = (TextView)view.findViewById(R.id.textView10);
        textView11 = (TextView)view.findViewById(R.id.textView11);







        identificar = (Button)view.findViewById(R.id.idenfiticar);

        identificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                listTraducciones listTraducciones = new listTraducciones();
                listTraducciones.setTraduccion(text.getText().toString());
                listTraducciones.setUsuario("angel");
                FirebaseFirestore.getInstance().collection("traducciones").add(listTraducciones);

                translate_api translate=new translate_api();
                translate.setOnTranslationCompleteListener(new translate_api.OnTranslationCompleteListener() {
                    @Override
                    public void onStartTranslation() {

                    }

                    @Override
                    public void onCompleted(String text) {

                        textView10.setText(text);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                translate.execute(text.getText().toString(),"en","es");


            }
        });


        return view;





    }



    private void downloadModal(String input) {

        Log.i("valo1",input);



        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        englishGermanTranslator2.downloadModelIfNeeded(conditions)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Toast.makeText(getContext(),"paso a traucir",Toast.LENGTH_LONG).show();
                                translateLanguage(input);

                            }
                        }
                ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i("error ",e.toString());
                    }
                }
        );

    }


    private void translateLanguage(String input) {

        Log.i("valo2",input);

        String palabra = input;

        Log.i("palabra",input);

        englishGermanTranslator2.translate(text.getText().toString())
                .addOnCompleteListener(
                        new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<String> task) {
                                textView10.setText(task.toString());
                                Toast.makeText(getActivity(),"traducido",Toast.LENGTH_LONG).show();

                                Log.i("traducido",task.toString());
                            }
                        }
                )
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.i("error final",e.toString());

                            }
                        }
                );


    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        traduccion.Adapter adapter = new traduccion.Adapter(getChildFragmentManager());
        Fragment miFragment = null;
        Bundle datos_a_fragment = new Bundle();

        miFragment = new GalleryFragment();
        miFragment.setArguments(datos_a_fragment);




        adapter.addFragment(new PangeaTraducciones(), "Traducciones");
        adapter.addFragment(new MisTraducciones(), "Mis Traducciones");




        viewPager.setAdapter(adapter);



    }
}