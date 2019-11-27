package com.example.apkatrening;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TreningAdapter extends RecyclerView.Adapter<TreningAdapter.TreningViewHolder> {


    private Context mCtx;
    private List<Trening> treningiList;

    public TreningAdapter(Context mCtx, List<Trening> treningiList) {
        this.mCtx = mCtx;
        this.treningiList = treningiList;
    }


    @NonNull
    @Override
    public TreningViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_trening, viewGroup, false);
        TreningViewHolder holder = new TreningViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TreningViewHolder treningViewHolder, final int i) {
        final Trening trening = treningiList.get(i);
        treningViewHolder.textViewNazwaCwiczenia.setText(trening.getNazwa());
        treningViewHolder.textViewSerie.setText("Serie: " + String.valueOf(trening.getSerie()));
        treningViewHolder.textViewPowtorzenia.setText("Powtórzenia: " + String.valueOf(trening.getPowtorzenia()));
        treningViewHolder.textViewObciazenie.setText("Obciążenie: " + String.valueOf(trening.getObciazenie()) + "kg");
        treningViewHolder.textViewOpis.setText(trening.getOpis());
    }

    @Override
    public int getItemCount() {
        return treningiList.size();
    }

    class TreningViewHolder extends RecyclerView.ViewHolder{

        TextView textViewNazwaCwiczenia;
        TextView textViewSerie;
        TextView textViewPowtorzenia;
        TextView textViewObciazenie;
        TextView textViewOpis;

        public TreningViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNazwaCwiczenia = itemView.findViewById(R.id.textView_nazwaCwiczenia);
            textViewObciazenie = itemView.findViewById(R.id.textView_Obciazenie);
            textViewPowtorzenia = itemView.findViewById(R.id.textView_liczbaPowtorzen);
            textViewSerie = itemView.findViewById(R.id.textView_liczbaSerii);
            textViewOpis = itemView.findViewById(R.id.textView_opisCwiczenia);
        }
    }

}
