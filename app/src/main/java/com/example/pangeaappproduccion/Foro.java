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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pangeaappproduccion.Adapters.AdapterPublicacionForo;
import com.example.pangeaappproduccion.Listas.listForo;
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
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Foro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Foro extends Fragment {




    private List<com.example.pangeaappproduccion.Listas.listForo> listForo;
    private AdapterPublicacionForo adapterForo;

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerViewForo;

    private EditText preguntaForo;
    private Button btnPregunta;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Foro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Foro.
     */
    // TODO: Rename and change types and number of parameters
    public static Foro newInstance(String param1, String param2) {
        Foro fragment = new Foro();
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
        View view = inflater.inflate(R.layout.fragment_foro, container, false);

        recyclerViewForo = view.findViewById(R.id.recyclerPreguntas);

        Context context = view.getContext();

        SharedPreferences preferences = getActivity().getSharedPreferences("accesos", MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");

        Toast.makeText(getActivity(),email_perfil + " shared",Toast.LENGTH_LONG).show();


        FirebaseFirestore dbDataUserPerfil = FirebaseFirestore.getInstance();
        dbDataUserPerfil.collection("users").whereEqualTo("emailAddress",email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){


                                String usuarioEmisor = documentSnapshot.getString("userName");
                                Toast.makeText(getActivity(),usuarioEmisor + " obtenido",Toast.LENGTH_LONG).show();


                                SharedPreferences.Editor editor1 = getActivity().getSharedPreferences("usuario_recibido_chat1", MODE_PRIVATE).edit();
                                editor1.putString("usuario_recibido_chat1", usuarioEmisor);
                                editor1.apply();
                            }
                        }
                    }
                });

        SharedPreferences preferencesusuario = getActivity().getSharedPreferences("usuario_recibido_chat1", Context.MODE_PRIVATE);
        String usuario_foro = preferencesusuario.getString("usuario_recibido_chat1", "No existe idioma");
        Toast.makeText(getActivity(),usuario_foro + " guardado",Toast.LENGTH_LONG).show();


        listForo = new ArrayList<>();
        adapterForo = new AdapterPublicacionForo(context,listForo);
        recyclerViewForo.setAdapter(adapterForo);
        recyclerViewForo.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewForo.setHasFixedSize(true);


        FirebaseFirestore.getInstance().collection("foro").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                } else {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            listForo.add(documentChange.getDocument().toObject(listForo.class));
                            adapterForo.notifyDataSetChanged();
                            recyclerViewForo.smoothScrollToPosition(listForo.size());
                        }
                    }
                }
            }
        });

        preguntaForo = (EditText)view.findViewById(R.id.foroComentar);
        btnPregunta = (Button)view.findViewById(R.id.publicarPreguntaBtn2);
        btnPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String clave = UUID.randomUUID().toString().toUpperCase();

                listForo preguntaForolist = new listForo();
                preguntaForolist.setId(clave);
                preguntaForolist.setMensaje(preguntaForo.getText().toString());
                preguntaForolist.setUsuario(usuario_foro);
                FirebaseFirestore.getInstance().collection("foro").add(preguntaForolist);
                preguntaForo.setText("");
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