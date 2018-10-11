package adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hva.gamebacklogmanager.R;
import models.Game;

/**
 * Created by khaled on 10-10-18.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {
    private List<Game> gameList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Game game);
    }

    public GameAdapter(List<Game> gameList, OnItemClickListener listener) {
        this.gameList = gameList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Game game = gameList.get(position);
        holder.title.setText(game.getTitle());
        holder.platform.setText(game.getPlatform());
        holder.status.setText(game.getStatus());
        holder.date.setText(game.getDate());
        holder.bind(gameList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView title;
        public TextView platform;
        public TextView status;
        public TextView date;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardViewItem);
            title = view.findViewById(R.id.titleItem);
            platform = view.findViewById(R.id.platformItem);
            status = view.findViewById(R.id.statusItem);
            date = view.findViewById(R.id.dateItem);
        }

        public void bind(final Game game, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(game);
                }
            });
        }
    }

    public void swapList(List<Game> newList) {
        gameList = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}
