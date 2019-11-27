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

public class PlanDniaAdapter extends RecyclerView.Adapter<PlanDniaAdapter.PlanDniaViewHolder> {


    private Context mCtx;
    private List<PlanDnia> planyList;

    public PlanDniaAdapter(Context mCtx, List<PlanDnia> planyList) {
        this.mCtx = mCtx;
        this.planyList = planyList;
    }


    @NonNull
    @Override
    public PlanDniaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_plan_dnia, viewGroup, false);
        PlanDniaViewHolder holder = new PlanDniaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlanDniaViewHolder planDniaViewHolder, final int i) {
        final PlanDnia planDnia = planyList.get(i);
        planDniaViewHolder.textViewData.setText(planDnia.getDate());
        planDniaViewHolder.buttonPokaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, PlanyDniaActivity.class);
                intent.putExtra("id", planDnia.getId());
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return planyList.size();
    }

    class PlanDniaViewHolder extends RecyclerView.ViewHolder{

        TextView textViewData;
        Button buttonPokaz;

        public PlanDniaViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewData = itemView.findViewById(R.id.textView_DataPlanu);
            buttonPokaz = itemView.findViewById(R.id.buttonPokazPlan);
        }
    }

}
