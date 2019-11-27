package com.example.apkatrening;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class WymiarAdapter extends RecyclerView.Adapter<WymiarAdapter.WymiarViewHolder> {


    private Context mCtx;
    private List<Wymiar> wymiarList;

    public WymiarAdapter(Context mCtx, List<Wymiar> wymiarList) {
        this.mCtx = mCtx;
        this.wymiarList = wymiarList;
    }


    @NonNull
    @Override
    public WymiarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_wymiar, viewGroup, false);
        WymiarViewHolder holder = new WymiarViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WymiarViewHolder wymiarViewHolder, int i) {
        final Wymiar wymiar = wymiarList.get(i);
        wymiarViewHolder.textViewData.setText(wymiar.getData());
        wymiarViewHolder.textViewWaga.setText(String.valueOf(wymiar.getWaga()) + " kg");
        wymiarViewHolder.textViewWzrost.setText(String.valueOf(wymiar.getWzrost()) + " cm");
        wymiarViewHolder.textViewBiceps.setText(String.valueOf(wymiar.getObw_biceps()) + " cm");
        wymiarViewHolder.textViewKlatka.setText(String.valueOf(wymiar.getObw_klatka()) + " cm");
        wymiarViewHolder.buttonUsunWymiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, PopConfirmWymiarDelete.class);
                intent.putExtra("id", wymiar.getId());
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wymiarList.size();
    }

    class WymiarViewHolder extends RecyclerView.ViewHolder{

        TextView textViewData;
        TextView textViewWaga;
        TextView textViewWzrost;
        TextView textViewBiceps;
        TextView textViewKlatka;
        Button buttonUsunWymiar;

        public WymiarViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewData = itemView.findViewById(R.id.textViewData);
            textViewWaga = itemView.findViewById(R.id.waga);
            textViewWzrost = itemView.findViewById(R.id.wzrost);
            textViewBiceps = itemView.findViewById(R.id.biceps);
            textViewKlatka = itemView.findViewById(R.id.klatka);
            buttonUsunWymiar = itemView.findViewById(R.id.buttonUsunWymiar);
        }
    }

}
