package com.example.pangeaappproduccion;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.UsuariosHolder>
        implements View.OnClickListener{

    private List<UsuarioVideo> listUsuarios;
    private Context context;
    private  View.OnClickListener listener;






    public AdapterUsuarios(Context context1,List<UsuarioVideo> listUsuarios)
    {
        this.context = context1;

        this.listUsuarios = listUsuarios;

    }


    @NonNull
    @NotNull
    @Override
    public AdapterUsuarios.UsuariosHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_usuarios,viewGroup,false);


        Context mcontext = view.getContext();

        view.setOnClickListener(this);

        return new AdapterUsuarios.UsuariosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterUsuarios.UsuariosHolder usuariosHolder, int i) {


        usuariosHolder.firtsname.setText(listUsuarios.get(i).getFirts_name());
        usuariosHolder.lastname.setText(listUsuarios.get(i).getTelefono());
        usuariosHolder.email.setText(listUsuarios.get(i).getEmail());
        usuariosHolder.password.setText(listUsuarios.get(i).getPassword());
        usuariosHolder.fcm_token.setText(listUsuarios.get(i).getToken());
        Glide.with(usuariosHolder.itemView.getContext()).load(listUsuarios.get(i).getMultimedia()).into(usuariosHolder.ImgUserPerfil);


        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("accesos",Context.MODE_PRIVATE);
        String email_perfil = preferences.getString("email", "No name defined");




        String numero = listUsuarios.get(i).getTelefono();
        String destinatario = listUsuarios.get(i).getUsuario();





        usuariosHolder.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(),videoCall.class);
                intent.putExtra("destinaraio", destinatario);
                intent.putExtra("usuario",email_perfil);
                context.startActivity(intent);
            }
        });


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
                Intent intent = new Intent(context.getApplicationContext(),ChatActivity.class);
                intent.putExtra("destinaraio", destinatario);
                Toast.makeText(context,"se envio destinatario"+destinatario, Toast.LENGTH_LONG).show();
                intent.putExtra("usuario",email_perfil);
                Toast.makeText(context,"se envio usuario"+email_perfil, Toast.LENGTH_LONG).show();

                context.startActivity(intent);
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

        private ImageView ImgUserPerfil;

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
            ImgUserPerfil =  itemView.findViewById(R.id.ImgUserPerfil);


        }
    }

}
