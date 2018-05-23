package et.gov.fmoh.nhddapp.nhddapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import et.gov.fmoh.nhddapp.nhddapp.CustomListViewAdapter;
import et.gov.fmoh.nhddapp.nhddapp.R;


public class Tab1Fragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{
    CustomListViewAdapter customListViewAdapter;
    private ListView listView;
    private List<String> names;
    private List<String> descs;
    private Integer imgIcon;

    private Context context;

    public Tab1Fragment() {
    }

    private void populateList() {
        names = new ArrayList<String>();
        names.add("Cholera");
        names.add("Typhoid Fever (due to Salmonella typhi)");
        names.add("Typhoid Fever (With Other Complications)");
        names.add("Typhoid Fever");
        names.add("Salmonella enteritis");
        names.add("Sepsis (Salmonella sepsis)");
        names.add("Diarrhea (Shigellosis due to Shigella dysenteriae)");
        names.add("Shigellosis (Shigellosis, unspecified)");

        descs = new ArrayList<String>();
        descs.add("Some description...");
        descs.add("Some description");
        descs.add("Some description");
        descs.add("Some description");
        descs.add("Some description");
        descs.add("Some description");
        descs.add("Some description");
        descs.add("Some description");

        imgIcon = R.drawable.concept_icon;
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
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        listView = view.findViewById(R.id.listview);

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
