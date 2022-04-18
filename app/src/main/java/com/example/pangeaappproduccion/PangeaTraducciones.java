package com.example.pangeaappproduccion;

import android.content.Context;
import android.os.Bundle;

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

import com.example.pangeaappproduccion.Listas.listTraducciones;
import com.example.pangeaappproduccion.databinding.FragmentSlideshowBinding;
import com.example.pangeaappproduccion.ui.slideshow.SlideshowViewModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PangeaTraducciones#newInstance} factory method to
 * create an instance of this fragment.
 */


public class PangeaTraducciones extends Fragment {




    private List<com.example.pangeaappproduccion.Listas.listTraducciones> listTraducciones;
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

    public PangeaTraducciones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PangeaTraducciones.
     */
    // TODO: Rename and change types and number of parameters
    public static PangeaTraducciones newInstance(String param1, String param2) {
        PangeaTraducciones fragment = new PangeaTraducciones();
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
        View view = inflater.inflate(R.layout.fragment_pangea_traducciones, container, false);


        recyclerViewTraducciones = view.findViewById(R.id.recycle_view_traducciones);

        Context context = view.getContext();


        listTraducciones = new ArrayList<>();
        adapterTraducciones = new AdapterTraducciones(context,listTraducciones);
        recyclerViewTraducciones.setAdapter(adapterTraducciones);
        recyclerViewTraducciones.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewTraducciones.setHasFixedSize(true);


        FirebaseFirestore.getInstance().collection("traducciones").addSnapshotListener(new EventListener<QuerySnapshot>() {
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