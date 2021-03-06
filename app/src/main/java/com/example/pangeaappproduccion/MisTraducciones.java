package com.example.pangeaappproduccion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pangeaappproduccion.Model.listTraducciones;
import com.example.pangeaappproduccion.databinding.FragmentSlideshowBinding;
import com.example.pangeaappproduccion.ui.slideshow.SlideshowViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MisTraducciones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisTraducciones extends Fragment {


    private List<com.example.pangeaappproduccion.Model.listTraducciones> listTraducciones;
    private AdapterTraducciones adapterTraducciones;

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerViewTraducciones;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MisTraducciones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisTraducciones.
     */
    // TODO: Rename and change types and number of parameters
    public static MisTraducciones newInstance(String param1, String param2) {
        MisTraducciones fragment = new MisTraducciones();
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
        View view =  inflater.inflate(R.layout.fragment_mis_traducciones, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("accesos", MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");


        recyclerViewTraducciones = view.findViewById(R.id.recycle_view_mistraducciones);

        Context context = view.getContext();


        listTraducciones = new ArrayList<>();
        adapterTraducciones = new AdapterTraducciones(context,listTraducciones);
        recyclerViewTraducciones.setAdapter(adapterTraducciones);
        recyclerViewTraducciones.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewTraducciones.setHasFixedSize(true);




        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("users").whereEqualTo("emailAddress",email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                    String userName = (String) documentSnapshot.get("userName");

                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("UsernameData", MODE_PRIVATE).edit();
                                    editor.putString("userName", userName);
                                    editor.apply();



                            }
                        }
                    }
                });


        SharedPreferences preferencesIdioma = getActivity().getSharedPreferences("UsernameData", MODE_PRIVATE);
        String UserNameObtenido = preferencesIdioma.getString("username", "No existe idioma");



        FirebaseFirestore.getInstance().collection("traducciones").whereEqualTo("usuario",UserNameObtenido).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                } else {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            listTraducciones.add(documentChange.getDocument().toObject(listTraducciones.class));
                            adapterTraducciones.notifyDataSetChanged();
                            recyclerViewTraducciones.smoothScrollToPosition(listTraducciones.size());
                        }
                    }
                }
            }
        });

        return view;
    }


    public static class Adapter extends FragmentPagerAdapter {
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
}