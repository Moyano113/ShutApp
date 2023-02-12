package com.example.shutapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shutapp.R;
import com.example.shutapp.modelo.Envio;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Envio> listMsg;
    private final int RECIBIDO = 1;
    private final int ENVIADO = 0;

    public void setListaMensajes(List<Envio> listMsg) {
        this.listMsg = listMsg;
    }

    public RecyclerAdapter(List<Envio> listMsg) {
        this.listMsg = listMsg;
    }

    public void addMsg(Envio envio){
        listMsg.add(envio);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ENVIADO:
                View vEnviado = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_enviado, parent, false);
                return new ViewHolderEnviado(vEnviado);
            case RECIBIDO:
            default:
                View vRecibido = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_recibido, parent, false);
                return new ViewHolderRecibido(vRecibido);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        switch (holder.getItemViewType()) {
            case ENVIADO:
                ViewHolderEnviado vhEnviado = (ViewHolderEnviado) holder;
                Envio enviado = listMsg.get(i);
                vhEnviado.txtEnviado.setText(enviado.getNombre() + ": " + enviado.getMsg());
                break;
            case RECIBIDO:
                ViewHolderRecibido vhRecibido = (ViewHolderRecibido) holder;
                Envio recibido = listMsg.get(i);
                vhRecibido.txtRecibido.setText(recibido.getMsg());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listMsg.size();
    }



    class ViewHolderEnviado extends RecyclerView.ViewHolder {
        EditText txtEnviado;

        ViewHolderEnviado(View itemView) {
            super(itemView);
            this.txtEnviado = itemView.findViewById(R.id.txtEnviado);
        }

    }

    class ViewHolderRecibido extends RecyclerView.ViewHolder {
        EditText txtRecibido;

        ViewHolderRecibido(View itemView) {
            super(itemView);
            this.txtRecibido = itemView.findViewById(R.id.txtRecibido);
        }
    }
}
