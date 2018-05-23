package et.gov.fmoh.nhddapp.nhddapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import et.gov.fmoh.nhddapp.nhddapp.CustomListViewAdapter;
import et.gov.fmoh.nhddapp.nhddapp.R;


public class Tab2Fragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{
    CustomListViewAdapter customListViewAdapter;
    private ListView listView;
    private List<String> names;
    private List<String> descs;
    private Integer imgIcon;

    private Context context;

    public Tab2Fragment() {
    }

    private void populateList() {
        names = new ArrayList<String>();
        names.add("Antenatal care coverage - Four visits");
        names.add("Contraceptive acceptance rate");
        names.add("Antenatal care coverage – First visit");
        names.add("Percentage of pregnant women attending antenatal care clinics tested for syphilis");
        names.add("Proportion of births attended by skilled health professional");
        names.add("Early postnatal care coverage");
        names.add("Caesarean section rate");
        names.add("Institutional maternal deaths");

        descs = new ArrayList<String>();
        descs.add("Some description...");
        descs.add("Some description");
        descs.add("Some description");
        descs.add("Some description");
        descs.add("Some description");
        descs.add("Some description");
        descs.add("Some description");
        descs.add("Some description");

        imgIcon = R.drawable.ic_indicator_icon;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        setHasOptionsMenu(true);
        populateList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);

        listView = view.findViewById(R.id.hmis_listview);

        customListViewAdapter = new CustomListViewAdapter(getActivity(),names,descs, imgIcon);
        listView.setAdapter(customListViewAdapter);

        return view;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText==null || newText.trim().isEmpty()){
            resetSearch();
            return false;
        }

        List<String> filteredList = new ArrayList<>(names);
        for (String value : names){
            if(value.toLowerCase().contains(newText.toLowerCase())){
                filteredList.remove(value);
            }
        }

        customListViewAdapter = new CustomListViewAdapter(getActivity(),filteredList,descs, imgIcon);
        listView.setAdapter(customListViewAdapter);

        return false;
    }

    private void resetSearch() {
        customListViewAdapter = new CustomListViewAdapter(getActivity(),names,descs, imgIcon);
        listView.setAdapter(customListViewAdapter);
    }
}
