package com.example.apkatrening;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WiadomosciAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<Wiadomosc> wiadomosciList;
    private User user;
    private SessionHandler session;

    public WiadomosciAdapter(Context context, List<Wiadomosc> wiadomosciList) {
        this.mContext = context;
        this.wiadomosciList = wiadomosciList;
    }

    @Override
    public int getItemCount(){
        return wiadomosciList.size();
    }

    @Override
    public int getItemViewType(int position){
        Wiadomosc wiadomosc = wiadomosciList.get(position);
        session = new SessionHandler(mContext.getApplicationContext());
        user = session.getUserDetails();

        if (wiadomosc.getLogin().equals(user.getLogin())){
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        Wiadomosc message = wiadomosciList.get(position);

        switch (holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView wiadomosc;
        TextView czas;
        TextView nazwa;

        ReceivedMessageHolder(View itemView){
            super(itemView);
            wiadomosc = itemView.findViewById(R.id.text_message_body);
            czas = itemView.findViewById(R.id.text_message_time);
            nazwa = itemView.findViewById(R.id.text_message_name);
        }

        void bind(Wiadomosc message){
            wiadomosc.setText(message.getTresc());
            czas.setText(message.getData());
            nazwa.setText(message.getLogin());
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder{
        TextView wiadomosc;
        TextView czas;

        SentMessageHolder(View itemView){
            super(itemView);

            wiadomosc = itemView.findViewById(R.id.text_message_body);
            czas = itemView.findViewById(R.id.text_message_time);
        }

        void bind(Wiadomosc message){
            wiadomosc.setText(message.getTresc());
            czas.setText(message.getData());
        }
    }
}
