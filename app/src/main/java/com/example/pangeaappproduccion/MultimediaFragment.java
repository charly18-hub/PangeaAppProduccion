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

import com.example.pangeaappproduccion.Adapters.AdapterPublicacionMultimedia;
import com.example.pangeaappproduccion.Listas.listPublicaciones;
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
 * Use the {@link MultimediaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultimediaFragment extends Fragment {

    private List<com.example.pangeaappproduccion.Listas.listPublicaciones> listPublicaciones;
    private AdapterPublicacionMultimedia adapterPublicacion;

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerViewPublicaciones;




    public MultimediaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multimedia, container, false);


        recyclerViewPublicaciones = view.findViewById(R.id.recycle_view_multimedia);

        Context context = view.getContext();


        listPublicaciones = new ArrayList<>();
        adapterPublicacion = new AdapterPublicacionMultimedia(listPublicaciones);
        recyclerViewPublicaciones.setAdapter(adapterPublicacion);
        recyclerViewPublicaciones.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewPublicaciones.setHasFixedSize(true);


        FirebaseFirestore.getInstance().collection("redSocial").whereEqualTo("status","2").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                } else {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            listPublicaciones.add(documentChange.getDocument().toObject(listPublicaciones.class));
                            adapterPublicacion.notifyDataSetChanged();
                            recyclerViewPublicaciones.smoothScrollToPosition(listPublicaciones.size());
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