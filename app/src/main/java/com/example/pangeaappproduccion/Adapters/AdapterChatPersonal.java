package com.example.pangeaappproduccion.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pangeaappproduccion.MensajeChatPersonal;
import com.example.pangeaappproduccion.R;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterChatPersonal extends RecyclerView.Adapter<AdapterChatPersonal.ChatPersonalHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    FirebaseUser fuser;
    private List<MensajeChatPersonal> listMensajesPersonal;

    public  AdapterChatPersonal(List<MensajeChatPersonal> listMensajesPersonal){
        this.listMensajesPersonal = listMensajesPersonal;
    }




    @NonNull
    @NotNull
    @Override
    public AdapterChatPersonal.ChatPersonalHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {

        if (i == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false);
            return new AdapterChatPersonal.ChatPersonalHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_right, viewGroup, false);
            return new AdapterChatPersonal.ChatPersonalHolder(view);
        }


        /*View chatView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat,viewGroup,false);

        return new AdapterChatPersonal.ChatPersonalHolder(chatView);*/
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterChatPersonal.ChatPersonalHolder chatPersonalHolder, int i) {

        chatPersonalHolder.chatUsuario.setText(listMensajesPersonal.get(i).getName());
        chatPersonalHolder.chatMensaje.setText(listMensajesPersonal.get(i).getMessage());



    }

    @Override
    public int getItemCount() {
        return listMensajesPersonal.size();
    }

    public class ChatPersonalHolder extends RecyclerView.ViewHolder{

        private TextView chatUsuario;
        private TextView chatMensaje;

        public ChatPersonalHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            chatMensaje = itemView.findViewById(R.id.vistaMensaje);
            chatUsuario = itemView.findViewById(R.id.vistaUsuario);
        }
    }

  /*  @Override
    public int getItemViewType(int position) {

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (listMensajesPersonal.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }*/
}