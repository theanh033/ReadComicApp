package theanh.android.readcomicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import theanh.android.readcomicapp.Object.Comics;
import theanh.android.readcomicapp.R;

public class PreviewAdapter extends PagerAdapter {

    private Context context;
    private List<Comics> comicsList;
//    private onPreviewClick onPreviewClick;
//    public interface onPreviewClick {
//        void onItemPreviewClick(Comics comics, ImageView imgPreview);
//    }

    public PreviewAdapter(Context context, List<Comics> comicsList/*, onPreviewClick onPreviewClick*/) {
        this.context = context;
        this.comicsList = comicsList;
//        this.onPreviewClick = onPreviewClick;
    }

    private RoundedImageView previewImg;
    private TextView previewName, previewCategory;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_preview, container, false);
        previewImg = view.findViewById(R.id.previewImg);
        previewName = view.findViewById(R.id.previewComicName);
        previewCategory = view.findViewById(R.id.previewComicCategory);

        Comics comics = comicsList.get(position);
        if (comics != null) {
            Glide.with(context).load(comics.getImage()).into(previewImg);
            previewName.setText(comics.getName());
            previewCategory.setText(comics.getCategory());
        }
//        previewImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onPreviewClick.onItemPreviewClick(comics, previewImg);
//            }
//        });
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (comicsList != null) {
            return comicsList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
