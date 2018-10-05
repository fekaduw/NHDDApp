package et.gov.fmoh.nhddapp.nhddapp.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import et.gov.fmoh.nhddapp.nhddapp.model.NcodExtras;
import et.gov.fmoh.nhddapp.nhddapp.utils.CONST;
import et.gov.fmoh.nhddapp.nhddapp.utils.DatabaseHelper;
import et.gov.fmoh.nhddapp.nhddapp.service.NcodDataSyncIntentService;
import et.gov.fmoh.nhddapp.nhddapp.views.adapter.CategoryListViewAdapter;
import et.gov.fmoh.nhddapp.nhddapp.R;
import io.realm.Realm;
import io.realm.RealmChangeListener;

import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.TAG;

public class Tab1Fragment extends Fragment implements SearchView.OnQueryTextListener{

    //views
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textViewNotFound;
    private SearchView searchView;

    //set the favorite icon for the recycler view
    private Integer imageViewFavorite;

    //custom adapter
    private CategoryListViewAdapter categoryListViewAdapter;

    private Realm realm;
    public static ArrayList<NcodExtras> concepts =null;
    private Context context;

    private DatabaseHelper databaseHelper;

    public Tab1Fragment() {
        databaseHelper = new DatabaseHelper();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        categoryListViewAdapter.filter(text);
        return false;
    }

    private RealmChangeListener mRealmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            Log.d(TAG, "OnChange() method in change listener called");

            concepts = databaseHelper.getNCoDCategories(realm); //databaseHelper.getNCoDConcepts(realm);

            try{
            if (concepts!=null) {
                showProgressBar(false);
                showRecyclerView(true);

                initCustomAdapter();

                Log.d(TAG, "The size of concepts is: "+concepts.size());
        }
        }catch (NullPointerException ex) {
                concepts = null;
                Log.e(CONST.TAG, ex.getMessage());
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        realm.removeChangeListener(mRealmChangeListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        realm.addChangeListener(mRealmChangeListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_layout,container,false);

        setupViews(view);

        Log.d(TAG, "Inside onCreateView() Tab1Fragment");

        concepts = databaseHelper.getNCoDCategories(realm);

        if (concepts!=null) {

            Log.d(TAG, "Concept has some values. It's size is: "+ concepts.size());

            initCustomAdapter();

            showProgressBar(false);

            showRecyclerView(true);

            realm.addChangeListener(mRealmChangeListener);
       }

        return view;
    }

    private void showRecyclerView(boolean isShow) {
        if (isShow) {
            textViewNotFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            textViewNotFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    //initializes the custom adapter
    private void initCustomAdapter() {
        categoryListViewAdapter = new CategoryListViewAdapter(getActivity(), concepts, imageViewFavorite);
        recyclerView.setAdapter(categoryListViewAdapter);
    }

    // setup different views on the fragment layout
    private void setupViews(View view) {
        recyclerView = view.findViewById(R.id.recView);

        progressBar = view.findViewById(R.id.progress);
        //swipeRefreshLayout = view.findViewById(R.id.refreshTab1ListView);
        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });*/

        textViewNotFound = view.findViewById(R.id.textViewNotfound);
        textViewNotFound.setText("Loading...");

        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        //init the favorite icon
        imageViewFavorite = R.mipmap.ic_favorite_;


        initCustomAdapterRecycleView();

        showProgressBar(true);
    }

    /*shows/hides the progress bar based on the value of isShow - true/show, false/hide*/
    private void showProgressBar(boolean isShow) {
        if (isShow) {
            //if (!swipeRefreshLayout.isRefreshing()) {
                progressBar.setVisibility(View.VISIBLE);
            //}
        } else {
            progressBar.setVisibility(View.GONE);
            //swipeRefreshLayout.setRefreshing(false);
        }
    }

    ///This method will fetch data from the server in case there is no data in the database
    private void loadData() {
        showRecyclerView(false);

        showProgressBar(true);

        Toast.makeText(context,"Starting updating the data", Toast.LENGTH_LONG).show();

        //updating the data
        if (!databaseHelper.isNCODDataAvailable(realm)) {
            Intent intent = new Intent(context, NcodDataSyncIntentService.class);
            context.startService(intent);
        }
     }

     //set up the custom adapter and recycleview
     private void initCustomAdapterRecycleView() {
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
         recyclerView.setLayoutManager(linearLayoutManager);
         recyclerView.setHasFixedSize(true);
         DividerItemDecoration itemDecoration = new DividerItemDecoration(context, linearLayoutManager.getOrientation());
         recyclerView.addItemDecoration(itemDecoration);

         initCustomAdapter();

         showProgressBar(false);

         Log.d(TAG, "Inside initCustomAdapterRecycleView");
     }
}
