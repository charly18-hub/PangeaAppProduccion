package com.example.pangeaappproduccion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pangeaappproduccion.Adapters.AdapterUsuarios;
import com.example.pangeaappproduccion.Util.UtilFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LlamadasFragment extends UtilFragment {

    private List<UsuarioVideo> listUsuarios;
    private AdapterUsuarios adapterUsuarios;
    private RecyclerView recyclerViewUsuariosVideo;
    private String uid,correo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_llamadas, container, false);



        SharedPreferences preferences = getActivity().getSharedPreferences("usuarioRegistroNormal",Context.MODE_PRIVATE);
        correo = preferences.getString("email", "");

        establecerIdioma();
        Context context = view.getContext();
        listUsuarios = new ArrayList<>();
        adapterUsuarios = new AdapterUsuarios(context,listUsuarios);
        recyclerViewUsuariosVideo = view.findViewById(R.id.recyclerViewUsuariosVideo);
        recyclerViewUsuariosVideo.setAdapter(adapterUsuarios);
        recyclerViewUsuariosVideo.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewUsuariosVideo.setHasFixedSize(true);



        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("users").whereEqualTo("emailAddress",correo).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            uid = documentSnapshot.getString("uid");
                        }
                        FirebaseFirestore.getInstance().collection("users").document(uid).collection("Solicitudes").whereEqualTo("estatus","aceptada").addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.d(TAG, "Error:" + error.getMessage());
                            } else {
                                for (DocumentChange documentChange : value.getDocumentChanges()) {
                                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                        listUsuarios.add(documentChange.getDocument().toObject(UsuarioVideo.class));
                                        adapterUsuarios.notifyDataSetChanged();
                                        recyclerViewUsuariosVideo.smoothScrollToPosition(listUsuarios.size());

                                    }
                                }
                            }
                        });
                    }
                });


        return view;
    }




}