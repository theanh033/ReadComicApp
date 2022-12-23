package theanh.android.readcomicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import theanh.android.readcomicapp.Object.Comics;
import theanh.android.readcomicapp.R;

public class ComicsUpdateAdapter extends RecyclerView.Adapter<ComicsUpdateAdapter.ComicsViewHolder> {

    private Context context;
    private List<Comics> comicsList;
    private onClick onClick;

    public interface onClick {
        void onClickUpdate(Comics comics);
        void onClickDelete(Comics comics);
    }

    public ComicsUpdateAdapter(Context context, List<Comics> comicsList, onClick onClick) {
        this.context = context;
        this.comicsList = comicsList;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ComicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComicsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic_update, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComicsViewHolder holder, int position) {
        Comics comics = comicsList.get(position);
        holder.comicName.setText(comicsList.get(position).getName());
        holder.comicCategory.setText(comicsList.get(position).getCategory());
        Glide.with(holder.comicImage).load(comicsList.get(position).getImage()).into(holder.comicImage);
        Glide.with(holder.comicImage).load(comicsList.get(position).getImage())
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .into((holder.comicImage));

        holder.comicUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClickUpdate(comics);
            }
        });

        holder.comicDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClickDelete(comics);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (comicsList != null) {
            return comicsList.size();
        }
        return 0;
    }

    public class ComicsViewHolder extends RecyclerView.ViewHolder {

        ImageView comicImage;
        TextView comicName, comicCategory;
        Button comicUpdate, comicDelete;

        public ComicsViewHolder(@NonNull View itemView) {
            super(itemView);

            comicImage = itemView.findViewById(R.id.comicImage);
            comicName = itemView.findViewById(R.id.comicName);
            comicCategory = itemView.findViewById(R.id.comicCategory);

            comicUpdate = itemView.findViewById(R.id.comicUpdate);
            comicDelete = itemView.findViewById(R.id.comicDelete);
        }
    }
}
