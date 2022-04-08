package com.example.pangeaappproduccion.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.pangeaappproduccion.AdapterPublicacion;
import com.example.pangeaappproduccion.ImagenesPublicacion;
import com.example.pangeaappproduccion.MultimediaFragment;
import com.example.pangeaappproduccion.PublicacionesTextFragment;
import com.example.pangeaappproduccion.R;
import com.example.pangeaappproduccion.databinding.FragmentSlideshowBinding;
import com.example.pangeaappproduccion.ui.gallery.GalleryFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {
    private List<com.example.pangeaappproduccion.listPublicaciones> listPublicaciones;
    private AdapterPublicacion adapterPublicacion;

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerViewPublicaciones;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);


        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);


        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpagerPublicaciones);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabsPublicaciones);
        tabs.setupWithViewPager(viewPager);

        return view;
    }



    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        SlideshowFragment.Adapter adapter = new SlideshowFragment.Adapter(getChildFragmentManager());
        Fragment miFragment = null;
        Bundle datos_a_fragment = new Bundle();

        miFragment = new GalleryFragment();
        miFragment.setArguments(datos_a_fragment);




        adapter.addFragment(new PublicacionesTextFragment(), "Publicaciones");
        adapter.addFragment(new ImagenesPublicacion(), "Imagenes");
        adapter.addFragment(new MultimediaFragment(), "Multimedia");




        viewPager.setAdapter(adapter);



    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface OnFragmentInteractionListener {
    }




}