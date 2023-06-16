package com.example.shutapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shutapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    public List<String> listMsg;

    public RecyclerAdapter() {
        this.listMsg = new ArrayList<String>();
    }

    public void insertarItem(String mensaje){
        listMsg.add(mensaje);
        notifyDataSetChanged();
    }

    public List<String> getListMsg() {
        return listMsg;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerHolder holder;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_msg_list, parent, false);
        holder = new RecyclerHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int i) {

        String mensaje = listMsg.get(i);

        if(mensaje.substring(0,1).equals("e")){
            holder.txtEnviado.setText(mensaje.substring(3));
            holder.txtRecibido.setText("");
        }else{
            holder.txtRecibido.setText(mensaje.substring(3));
            holder.txtEnviado.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return listMsg.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder{
        TextView txtEnviado;
        TextView txtRecibido;

        public RecyclerHolder(@NonNull View itemView){
            super(itemView);
            txtEnviado = (TextView) itemView.findViewById(R.id.enviado);
            txtRecibido = (TextView) itemView.findViewById(R.id.recibido);
        }
    }
}
