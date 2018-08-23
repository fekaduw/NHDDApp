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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import et.gov.fmoh.nhddapp.nhddapp.helpers.CONST;
import et.gov.fmoh.nhddapp.nhddapp.helpers.DatabaseHelper;
import et.gov.fmoh.nhddapp.nhddapp.helpers.DataHelper;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicatorConcept;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;
import et.gov.fmoh.nhddapp.nhddapp.service.BaseAPI;
import et.gov.fmoh.nhddapp.nhddapp.service.HmisDataSyncIntentService;
import et.gov.fmoh.nhddapp.nhddapp.service.NcodDataSyncIntentService;
import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.views.adapter.CustomListViewAdapter;
import et.gov.fmoh.nhddapp.nhddapp.views.adapter.HMISIndicatorsCustomListViewAdapter;
import io.realm.Realm;
import io.realm.RealmChangeListener;

import static et.gov.fmoh.nhddapp.nhddapp.helpers.CONST.TAG;


public class Tab2Fragment extends Fragment implements SearchView.OnQueryTextListener{
    //views
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textViewNotFound;
    private SearchView searchView;

    //set the favorite icon for the recycler view
    private Integer imageViewFavorite;

    //custom adapter
    private HMISIndicatorsCustomListViewAdapter customListViewAdapter;

    private Realm realm;
    public static ArrayList<HMISIndicatorConcept> concepts =null;
    private Context context;

    private DatabaseHelper databaseHelper;

    public Tab2Fragment() {
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
        customListViewAdapter.filter(text);
        return false;
    }

    private RealmChangeListener mRealmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            Log.d(TAG, "OnChange() method in change listener called");

            concepts = databaseHelper.getHMISIndicatorConcepts(realm);

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

        concepts = databaseHelper.getHMISIndicatorConcepts(realm);

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
        customListViewAdapter = new HMISIndicatorsCustomListViewAdapter(getActivity(), concepts, imageViewFavorite);
        recyclerView.setAdapter(customListViewAdapter);
    }

    // setup different views on the fragment layout
    private void setupViews(View view) {
        recyclerView = view.findViewById(R.id.recView);

        progressBar = view.findViewById(R.id.progress);
        swipeRefreshLayout = view.findViewById(R.id.refreshTab1ListView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

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
            if (!swipeRefreshLayout.isRefreshing()) {
                progressBar.setVisibility(View.VISIBLE);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    ///This method will fetch data from the server in case there is no data in the database
    private void loadData() {
        showRecyclerView(false);

        showProgressBar(true);

        Toast.makeText(context,"Starting updating the data", Toast.LENGTH_LONG).show();

        //updating the data
        if (!databaseHelper.isHMISDataAvailable(realm)) {
            Intent intent = new Intent(context, HmisDataSyncIntentService.class);
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


    /*//adapter
    HMISIndicatorsCustomListViewAdapter customListViewAdapter;

    //views
    //private ListView listView;
    private RecyclerView listView;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textViewNotFound;
    private ImageView imageViewFavorite;

    //variable for image icon on the custom listview
    private Integer imgIcon;
    private Integer isUpdatedImgIcon;
    private SearchView searchView;

    //object to establish API communication
    private BaseAPI baseAPI;

    private Realm realm;
    public static ArrayList<HMISIndicatorConcept> concepts =null;

    private Context context;

    DatabaseHelper databaseHelper;

    public Tab2Fragment() {
        //set the default icon for the listview
        imgIcon = R.drawable.ic_ncod;
        isUpdatedImgIcon = R.mipmap.ic_favorite_;

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
        customListViewAdapter.filter(text);
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        *//*//search
        //inflater.inflate(R.menu.search_menu, menu);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v("Filtered", "Inside QueryTextChange: " + newText);

                *//**//*String text = newText;
                RealmList<HMISIndicatorConcept> filtered = new RealmList<>();

                if (text.length() <= 0)
                    filtered = concepts;
                else
                    filtered = customListViewAdapter.filter(text);

                customListViewAdapter = new HMISIndicatorsCustomListViewAdapter(getActivity(), filtered, imgIcon, isUpdatedImgIcon);

                listView.setAdapter(customListViewAdapter);*//**//*
                return false;
            }
        });
        *//**//*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(context, query, Toast.LENGTH_LONG).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
     *//**//**//**//*todo:           if ((newText != null || !newText.isEmpty()) && ncodConcept != null) {

                    List<NCoD> conceptList = new ArrayList<>();
                    for (NCoD c : ncodConcept) {
                        if (c.getConcepts().get(0).getDisplay_name().contains(newText)) {
                            conceptList.add(c);
                            Log.d("<<RESULT....>>", newText + c.getConcepts().get(0).getDisplay_name());
                        }
                    }

                    Log.d("<<RESULT....>>", newText + ncodConcept.get(0).getConcepts().get(0).getDisplay_name());
                    customListViewAdapter = new CustomListViewAdapter(getActivity(), conceptList, imgIcon, isUpdatedImgIcon);
                    listView.setAdapter(customListViewAdapter);
                } else {
                    customListViewAdapter = new CustomListViewAdapter(getActivity(), ncodConcept, imgIcon, isUpdatedImgIcon);
                    listView.setAdapter(customListViewAdapter);
                }*//**//**//**//*

                return false;
            }
        });
*//**//*
        imageViewFavorite = getView().findViewById(R.id.favorite_imageview);*//*

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_layout,container,false);

        initViews(view);

        Log.d(TAG, "Inside populateCustomListView");



        hideProgressBar();

        populateData();

        return view;
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.recView);

        progressBar = view.findViewById(R.id.progress);
        swipeRefreshLayout = view.findViewById(R.id.refreshTab1ListView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        textViewNotFound = view.findViewById(R.id.textViewNotfound);

        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, linearLayoutManager.getOrientation());
        listView.addItemDecoration(itemDecoration);

        progressBar.setVisibility(View.GONE);
    }

    private void populateData() {
        if(databaseHelper.isNCODDataAvailable(realm)) {
            Log.d(TAG, "Concept has some values " + DataHelper.getHMISConcepts().size() + "\nDisplay Name: " + DataHelper.getHMISConcepts().get(3).getDisplay_name());

            customListViewAdapter = new HMISIndicatorsCustomListViewAdapter(getActivity(), DataHelper.getHMISConcepts(), imgIcon, isUpdatedImgIcon);
            listView.setAdapter(customListViewAdapter);

            textViewNotFound.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        else
        {
            //fetch the data from the OCL using the API and cache it on the local db
            loadData();
        }
    }

    *//*private ArrayList<HMISIndicatorConcept> getHMISConcepts() {
        //baseAPI = BaseAPI.Factory.create();
        realm = Realm.getDefaultInstance();

        //Get data from the database
        RealmResults<HMISIndicator> hmisConcept = realm.where(HMISIndicator.class).findAll();

        if(hmisConcept.size() > 0) {
            RealmList<HMISIndicatorConcept> newData = hmisConcept.get(0).getConcepts();
            concepts = (ArrayList<HMISIndicatorConcept>) realm.copyFromRealm(newData);//ncodConcept.get(0).getConcepts();
        }
        else{
            concepts = null;
        }
        return concepts;
    }*//*

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }
    private void showProgressBar() {
        if (!swipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void resetSearch() {
        populateCustomListView();
    }

    private void loadData() {
        textViewNotFound.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        showProgressBar();
        Toast.makeText(context,"Starting updating the data", Toast.LENGTH_LONG).show();

        //updating the data
        if (!databaseHelper.isNCODDataAvailable(realm)) {
            Intent intent = new Intent(context, NcodDataSyncIntentService.class);
            context.startService(intent);
        }

        if (!databaseHelper.isHMISDataAvailable(realm)) {
            Intent intent = new Intent(context, HmisDataSyncIntentService.class);
            context.startService(intent);
        }

      *//*  //API communication...
        Call<List<NcodConcept>> concepts = baseAPI.getNcodConcepts();
        concepts.enqueue(new Callback<List<NcodConcept>>() {
            @Override
            public void onResponse(Call<List<NcodConcept>> call, final Response<List<NcodConcept>> response) {
                if (response.isSuccessful()) {
                    Log.d("DOWNLOAD_FILE", "server contacted and has file");

                    //check if data is of new version
                    ncodConcept = response.body();
                    populateCustomListView();
                    hideProgressBar();

                    for (int i = 0; i < response.body().size(); i++) { *//**//*
        realm.beginTransaction();
        HMISIndicatorConcept concept = realm.createObject(HMISIndicatorConcept.class);
                     *//**//*   concept.setDisplay_name(response.body().get(i).getDisplay_name());
                        concept.setConcept_class(response.body().get(i).getConcept_class());
                        concept.setIs_latest_version(response.body().get(i).getIs_latest_version());
                        // concept.setNcodExtras(response.body().get(i).getNcodExtras());*//**//*

        realm.commitTransaction();
        if(realm!=null)
            realm.close();
        // }

        listView.setVisibility(View.VISIBLE);

                *//**//*} else {
                    Log.d("DOWNLOAD_FILE", "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<List<NcodConcept>> call, Throwable t) {
                hideProgressBar();
                textViewNotFound.setVisibility(View.VISIBLE);
            }
        });*//*
    }

    private void populateCustomListView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, linearLayoutManager.getOrientation());
        listView.addItemDecoration(itemDecoration);
        customListViewAdapter = new HMISIndicatorsCustomListViewAdapter(getActivity(), concepts, imgIcon, isUpdatedImgIcon);
        listView.setAdapter(customListViewAdapter);

        progressBar.setVisibility(View.GONE);

        Log.d(TAG, "Inside populateCustomListView");
    }*/
}