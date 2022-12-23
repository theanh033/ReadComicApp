package theanh.android.readcomicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import theanh.android.readcomicapp.Object.Comics;
import theanh.android.readcomicapp.R;

public class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicsViewHolder> {

    private Context context;
    private List<Comics> comicsList;
    private onComicClick onComicClick;

    public interface onComicClick {
        void onItemComicClick(Comics comics, ImageView comicImg);
    }

    public ComicsAdapter(Context context, List<Comics> comicsList, onComicClick onComicClick) {
        this.context = context;
        this.comicsList = comicsList;
        this.onComicClick = onComicClick;
    }

    @NonNull
    @Override
    public ComicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComicsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComicsViewHolder holder, int position) {
        holder.comicName.setText(comicsList.get(position).getName());
        Glide.with(holder.comicImg).load(comicsList.get(position).getImage()).into(holder.comicImg);
        Glide.with(holder.comicImg).load(comicsList.get(position).getImage())
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .into((holder.comicImg));

    }

    @Override
    public int getItemCount() {
        if (comicsList != null) {
            return comicsList.size();
        }
        return 0;
    }

    public class ComicsViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView comicImg;
        TextView comicName;

        public ComicsViewHolder(@NonNull View itemView) {
            super(itemView);

            comicImg = itemView.findViewById(R.id.comicImage);
            comicName = itemView.findViewById(R.id.comicName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onComicClick.onItemComicClick(comicsList.get(getAdapterPosition()), comicImg);
                }
            });
        }
    }
}
