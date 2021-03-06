package com.example.pangeaappproduccion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pangeaappproduccion.Adapters.AdapterEvaluaciones;
import com.example.pangeaappproduccion.Adapters.listEvaluaciones;
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
 * Use the {@link EvaluacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvaluacionFragment extends Fragment {

    private List <com.example.pangeaappproduccion.Adapters.listEvaluaciones> listEvaluaciones;
    private AdapterEvaluaciones adapterEvaluaciones;
    private RecyclerView recyclerViewEvaluaciones;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EvaluacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EvaluacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EvaluacionFragment newInstance(String param1, String param2) {
        EvaluacionFragment fragment = new EvaluacionFragment();
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
        View view =  inflater.inflate(R.layout.fragment_evaluacion, container, false);


        recyclerViewEvaluaciones = view.findViewById(R.id.recyclerPreguntas);

        Context context = view.getContext();


        listEvaluaciones = new ArrayList<>();
        adapterEvaluaciones = new AdapterEvaluaciones(context,listEvaluaciones);
        recyclerViewEvaluaciones.setAdapter(adapterEvaluaciones);
        recyclerViewEvaluaciones.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewEvaluaciones.setHasFixedSize(true);


        FirebaseFirestore.getInstance().collection("Evaluaciones").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                } else {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            listEvaluaciones.add(documentChange.getDocument().toObject(com.example.pangeaappproduccion.Adapters.listEvaluaciones.class));
                            adapterEvaluaciones.notifyDataSetChanged();
                            recyclerViewEvaluaciones.smoothScrollToPosition(listEvaluaciones.size());
                        }
                    }
                }
            }
        });



        return view;


    }
}