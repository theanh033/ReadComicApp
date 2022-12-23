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

import java.util.ArrayList;
import java.util.List;

import theanh.android.readcomicapp.Object.Comics;
import theanh.android.readcomicapp.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private ArrayList<Comics> mComicData = new ArrayList<>();
    private onComicClick onComicClick;

    public interface onComicClick {
        void onComicClick(Comics comics, ImageView comicImg);
    }

    public SearchAdapter(Context context, onComicClick onComicClick) {
        this.context = context;
        this.onComicClick = onComicClick;
    }

    public void setDisplayData(ArrayList<Comics> comics) {
        mComicData.clear();
        mComicData.addAll(comics);
        notifyDataSetChanged();
    }

    public void setFilterList(ArrayList<Comics> comics) {
        this.mComicData = comics;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.comicName.setText(mComicData.get(position).getName());
        holder.comicCategory.setText(mComicData.get(position).getCategory());
        Glide.with(holder.comicImg).load(mComicData.get(position).getImage()).into(holder.comicImg);
        Glide.with(holder.comicImg).load(mComicData.get(position).getImage())
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .into((holder.comicImg));
    }

    @Override
    public int getItemCount() {
        if (mComicData != null) {
            return mComicData.size();
        }
        return 0;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView comicImg;
        TextView comicName, comicCategory;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            comicImg = itemView.findViewById(R.id.comicSearchImage);
            comicName = itemView.findViewById(R.id.comicSearchName);
            comicCategory = itemView.findViewById(R.id.comicSearchCategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onComicClick.onComicClick(mComicData.get(getAdapterPosition()), comicImg);
                }
            });
        }
    }
}
