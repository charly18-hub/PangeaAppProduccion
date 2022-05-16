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
    Button inMeeting ;
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    public LlamadasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_llamadas, container, false);



        SharedPreferences preferences = getActivity().getSharedPreferences("accesos",Context.MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");
        Toast.makeText(getActivity(),email_perfil+" shared",Toast.LENGTH_LONG).show();

        establecerIdioma();
        Context context = view.getContext();
        recyclerViewUsuariosVideo = view.findViewById(R.id.recyclerViewUsuariosVideo);


        listUsuarios = new ArrayList<>();
        adapterUsuarios = new AdapterUsuarios(context,listUsuarios);

        adapterUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),
                        "selecciono: "+ listUsuarios.get(recyclerViewUsuariosVideo.getChildAdapterPosition(view)).getFirts_name(),
                        Toast.LENGTH_LONG).show();

                enviarNotificacion(listUsuarios.get(recyclerViewUsuariosVideo.getChildAdapterPosition(view)).getFirts_name());
            }
        });

        recyclerViewUsuariosVideo.setAdapter(adapterUsuarios);
        recyclerViewUsuariosVideo.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewUsuariosVideo.setHasFixedSize(true);
        String usuario_recibido_chat1 = "Homero Simpson";



        FirebaseFirestore dbDataPerfil = FirebaseFirestore.getInstance();
        dbDataPerfil.collection("users").whereEqualTo("emailAddress",email_perfil).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String usuario = documentSnapshot.getString("uid");
                                SharedPreferences.Editor editor = context.getSharedPreferences("usuarioObtenido", Context.MODE_PRIVATE).edit();
                                editor.putString("usuarioObtenido", usuario);
                                editor.apply();
                            }
                        }
                    }
                });


        SharedPreferences preferencesIdioma = context.getSharedPreferences("usuarioObtenido", Context.MODE_PRIVATE);
        String usuarioObtenido = preferencesIdioma.getString("usuarioObtenido", "No existe idioma");

        Toast.makeText(getActivity(),usuarioObtenido+" usuario obtenido afuera",Toast.LENGTH_LONG).show();




        FirebaseFirestore.getInstance().collection("users").document(usuarioObtenido).collection("Solicitudes").whereEqualTo("estatus","aceptada").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
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
            }
        });




        return view;
    }

    public void enviarNotificacion(String nombre){

        Toast.makeText(getActivity(),"llego enviar"+nombre,Toast.LENGTH_LONG).show();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JSONObject json = new JSONObject();

        try {
            int numero = ThreadLocalRandom.current().nextInt(5, 50 + 1);


            String token = "dDmdxYtmT6OCl3-e_qQV_J:APA91bF0x2tKsJr8f0GPCeJOKWC4E7rx5RjN33gWcWz9xw2elMERVxrosW8Z1EgV1rMh58dMdeoTmLx8W5cdlZkNPJsYICORjMIZjeklL7BONQfqxsandzyoz30NgzqBN_VABy38n3oc";
            json.put("to",token);
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo","Tienes una solicitud de llamada");
            notificacion.put("detalle","Call"+numero+"da click para contestar ");

            json.put("data",notificacion);

            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){

                @Override
                public Map<String,String> getHeaders(){
                    Map<String,String> header = new HashMap<>();

                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAv-g-1UA:APA91bFR2frYGt4WPzSACY4Yhh-nO8K64dx7qtR8yAKTKlZ7oM3ongCOi6pQBLZOun2TBgK9KUSL_WWlhaVWbXz2l4H-XIfEdMcjWBXuMv5gy_uF3Y0DmSMOq1UrmUrnu3lwnPHN2uK9");
                    return header;
                }

            };

            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}