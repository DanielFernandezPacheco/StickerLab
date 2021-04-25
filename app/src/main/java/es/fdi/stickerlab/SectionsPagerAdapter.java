package es.fdi.stickerlab;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import es.fdi.stickerlab.CategoriesFragment;
import es.fdi.stickerlab.MakerFragment;
import es.fdi.stickerlab.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.stickers, R.string.maker};
    private final Context mContext;
    private Fragment categoriesFragment, makerFragment;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        categoriesFragment = new CategoriesFragment();
        makerFragment = new MakerFragment();
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return categoriesFragment;
        return makerFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}