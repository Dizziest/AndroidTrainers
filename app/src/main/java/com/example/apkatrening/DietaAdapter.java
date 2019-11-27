package com.example.apkatrening;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DietaAdapter extends RecyclerView.Adapter<DietaAdapter.DietaViewHolder> {


    private Context mCtx;
    private List<Dieta> dietyList;

    public DietaAdapter(Context mCtx, List<Dieta> dietyList) {
        this.mCtx = mCtx;
        this.dietyList = dietyList;
    }


    @NonNull
    @Override
    public DietaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_dieta, viewGroup, false);
        DietaViewHolder holder = new DietaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DietaViewHolder dietaViewHolder, final int i) {
        final Dieta dieta = dietyList.get(i);
        dietaViewHolder.textViewNazwaDania.setText(dieta.getNazwa());
        dietaViewHolder.textViewKalorie.setText("Kaloryczność: " + String.valueOf(dieta.getKalorycznosc()) + "kcal");
        dietaViewHolder.textViewOpis.setText(dieta.getOpis());
    }

    @Override
    public int getItemCount() {
        return dietyList.size();
    }

    class DietaViewHolder extends RecyclerView.ViewHolder{

        TextView textViewNazwaDania;
        TextView textViewOpis;
        TextView textViewKalorie;

        public DietaViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNazwaDania = itemView.findViewById(R.id.textView_nazwaDania);
            textViewOpis = itemView.findViewById(R.id.textView_opisDania);
            textViewKalorie = itemView.findViewById(R.id.textView_kalorycznosc);
        }
    }

}
