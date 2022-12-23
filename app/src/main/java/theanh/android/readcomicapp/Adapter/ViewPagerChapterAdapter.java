package theanh.android.readcomicapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import theanh.android.readcomicapp.Fragment.ChapterFragment;
import theanh.android.readcomicapp.Fragment.DescriptionFragment;

public class ViewPagerChapterAdapter extends FragmentStateAdapter {
    private static final int Fragment_size = 2;

    public ViewPagerChapterAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DescriptionFragment();
            case 1:
                return new ChapterFragment();
            default:
                return new DescriptionFragment();
        }
    }

    @Override
    public int getItemCount() {
        return Fragment_size;
    }
}