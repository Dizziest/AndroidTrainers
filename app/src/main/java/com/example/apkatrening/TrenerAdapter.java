package com.example.apkatrening;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TrenerAdapter extends RecyclerView.Adapter<TrenerAdapter.TrenerViewHolder> {


    private Context mCtx;
    private List<Trener> trenerzyList;

    public TrenerAdapter(Context mCtx, List<Trener> trenerzyList) {
        this.mCtx = mCtx;
        this.trenerzyList = trenerzyList;
    }


    @NonNull
    @Override
    public TrenerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_trener, viewGroup, false);
        TrenerViewHolder holder = new TrenerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrenerViewHolder trenerViewHolder, final int i) {
        final Trener trener = trenerzyList.get(i);
        trenerViewHolder.textViewImie.setText(trener.getImie() + " " + trener.getNazwisko());
        trenerViewHolder.textViewEmail.setText(trener.getEmail());
        trenerViewHolder.textViewLogin.setText(trener.getLogin());
        trenerViewHolder.textViewDoswiadczenie.setText(String.valueOf(trener.getDoswiadczenie()) + " lat");
        trenerViewHolder.buttonProsba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, PopConfirmProsbaActivity.class);
                intent.putExtra("login", trener.getLogin());
                intent.putExtra("id", trener.getId());
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trenerzyList.size();
    }

    class TrenerViewHolder extends RecyclerView.ViewHolder{

        TextView textViewImie;
        TextView textViewEmail;
        TextView textViewDoswiadczenie;
        Button buttonProsba;
        TextView textViewLogin;

        public TrenerViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewImie = itemView.findViewById(R.id.textViewImie_trenera);
            textViewEmail = itemView.findViewById(R.id.textViewEmail_Trenera);
            textViewDoswiadczenie = itemView.findViewById(R.id.doswiadczenie);
            buttonProsba = itemView.findViewById(R.id.ButtonPoprosTrenera);
            textViewLogin = itemView.findViewById(R.id.textViewLogin_trenera);
        }

    }

}
