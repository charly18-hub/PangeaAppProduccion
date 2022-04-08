package com.example.pangeaappproduccion;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterUsuariosVideo extends RecyclerView.Adapter<AdapterUsuariosVideo.UsuariosHolder>
        implements View.OnClickListener{

    private List<UsuarioVideo> listUsuarios;
    private Context context;
    private  View.OnClickListener listener;






    public AdapterUsuariosVideo(List<UsuarioVideo> listUsuarios)
    {
        //this.context = context1;

        this.listUsuarios = listUsuarios;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterUsuariosVideo.UsuariosHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_usuarios,viewGroup,false);


        Context mcontext = view.getContext();

        view.setOnClickListener(this);

        return new AdapterUsuariosVideo.UsuariosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterUsuariosVideo.UsuariosHolder usuariosHolder, int i) {


        usuariosHolder.firtsname.setText(listUsuarios.get(i).getFirts_name());
        usuariosHolder.lastname.setText(listUsuarios.get(i).getTelefono());
        usuariosHolder.email.setText(listUsuarios.get(i).getEmail());
        usuariosHolder.password.setText(listUsuarios.get(i).getPassword());
        usuariosHolder.fcm_token.setText(listUsuarios.get(i).getToken());
        String usuario_recibido_chat1 = "Homero Simpson";



        String numero = listUsuarios.get(i).getTelefono();
        String destinatario = listUsuarios.get(i).getFirts_name();


        usuariosHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Uri number = Uri.parse("tel:" + numero);
                Toast.makeText(view.getContext(), number.toString(), Toast.LENGTH_LONG).show();

                Intent i = new Intent(Intent.ACTION_DIAL, number);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(i);




            }
        });

        usuariosHolder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("destinaraio", destinatario);
                intent.putExtra("usuario",usuario_recibido_chat1);
                context.startActivity(intent);

                Log.d("bien","funciona el evento");
            }
        });


    }



    @Override
    public int getItemCount() {
        return listUsuarios.size();


    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button4:



                break;

            case  R.id.button5:



                break;

            case R.id.button6:
                break;

        }

    }

    class  UsuariosHolder extends  RecyclerView.ViewHolder{

        private TextView firtsname;
        private TextView email;
        private TextView lastname;
        private TextView password;
        private TextView fcm_token;

        private Button video, call,chat;

        public UsuariosHolder(@NonNull View itemView){
            super(itemView);

            firtsname = itemView.findViewById(R.id.firts_name);
            lastname = itemView.findViewById(R.id.last_name);
            email = itemView.findViewById(R.id.email);
            password = itemView.findViewById(R.id.password);
            fcm_token= itemView.findViewById(R.id.fcm_token);
            video = itemView.findViewById(R.id.button4);
            call = itemView.findViewById(R.id.button5);
            chat = itemView.findViewById(R.id.button6);



        }
    }

}
