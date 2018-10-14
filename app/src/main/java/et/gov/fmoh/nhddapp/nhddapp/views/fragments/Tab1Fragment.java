package et.gov.fmoh.nhddapp.nhddapp.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
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
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodExtras;
import et.gov.fmoh.nhddapp.nhddapp.utils.CONST;
import et.gov.fmoh.nhddapp.nhddapp.utils.DatabaseHelper;
import et.gov.fmoh.nhddapp.nhddapp.service.NcodDataSyncIntentService;
import et.gov.fmoh.nhddapp.nhddapp.utils.GenerateColor;
import et.gov.fmoh.nhddapp.nhddapp.utils.ItemClickListenerHelper;
import et.gov.fmoh.nhddapp.nhddapp.views.ConceptListActivity;
import et.gov.fmoh.nhddapp.nhddapp.views.adapter.CategoryListViewAdapter;
import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.views.adapter.CustomListViewAdapter;
import et.gov.fmoh.nhddapp.nhddapp.views.adapter.ViewHolder;
import io.realm.Realm;
import io.realm.RealmChangeListener;

import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.CATEGORY_NCOD;
import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.TAG;

public class Tab1Fragment extends Fragment implements SearchView.OnQueryTextListener{

    //views
    @BindView(R.id.recView)
    RecyclerView recyclerView;
    // GridView recyclerView;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    //private SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.textViewNotfound)
    TextView textViewNotFound;

    @BindView(R.id.search_view)
    SearchView searchView;

    //set the favorite icon for the recycler view
    private Integer imageViewFavorite;

    //custom adapter
    private CategoryListViewAdapter customListViewAdapter;

    private Realm realm;
    public static ArrayList<NcodExtras> concepts =null;
    private Context context;

    private DatabaseHelper databaseHelper;

    private ArrayList<Integer> color;
    ArrayList<NcodExtras> conceptFiltered;


    public Tab1Fragment() {
        databaseHelper = new DatabaseHelper();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        setHasOptionsMenu(true);

        color = new ArrayList<>();

        /*conceptFiltered = new ArrayList<>();

        if (concepts != null) {
            this.conceptFiltered.addAll(concepts);
        }*/
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

            concepts = databaseHelper.getNCoDCategories(realm);

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
        ButterKnife.bind((Activity) context);

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
        //customListViewAdapter = new CustomListViewAdapter(getActivity(), concepts, imageViewFavorite);
        customListViewAdapter = new CategoryListViewAdapter<NcodExtras>(getActivity(), concepts, new ItemClickListenerHelper() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(context, ConceptListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("category", CATEGORY_NCOD);

                bundle.putString("categoryName", concepts.get(position).getICD10Chapter());
                //bundle.putString("categoryName", ("ICD-10 Chapter: ").concat(concepts.get(position).getICD10Chapter()));

                bundle.putInt("color", color.get(position));
                i.putExtras(bundle);
                startActivity(i);
            }
        }){


            @Override
            public void onBindData(ViewHolder holder1, NcodExtras concept) {
                Log.d(TAG, "Current concept name: " + concept.getICD10Chapter());

                if(concept!=null) {
                    ViewHolder holder = holder1;

                    holder.textViewName.setText(("ICD-10 Chapter: ").concat(concept.getICD10Chapter()));

                    holder.textViewDesc.setText(("ICD-10 Block: ").concat(concept.getICD10Block()));

                    holder.iconConcept.setImageDrawable(getColor(concept));

                    showProgressBar(false);
                }
            }

            @Override
            public void filter(String charText) {
                charText = charText.toLowerCase(Locale.getDefault());
                concepts.clear();

                if (charText.length() == 0) {
                    concepts.addAll(conceptFiltered);
                } else {
                    for (NcodExtras row : conceptFiltered) {

                        Log.d(TAG, row.getICD10Chapter());

                        if (row.getICD10Chapter().toLowerCase(Locale.getDefault()).contains(charText)) {
                            Tab1Fragment.concepts.add(row);
                            Log.d(TAG, "Filtered row added.");
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void update(ArrayList<NcodExtras> concepts){}
        };

        recyclerView.setAdapter(customListViewAdapter);
        showRecyclerView(true);

    }

    private TextDrawable getColor(NcodExtras concept){
        // generate random color
        GenerateColor<NcodExtras> generateColor = new GenerateColor<>();
        int c = generateColor.getColor(concept);
        color.add(c);
        return generateColor.getTextDrawable(concept.getICD10Chapter(), c);
    }

    // setup different views on the fragment layout
    private void setupViews(View view) {
        recyclerView = view.findViewById(R.id.recView);

        progressBar = view.findViewById(R.id.progress);
   /*     //swipeRefreshLayout = view.findViewById(R.id.refreshTab1ListView);
        *//*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });*//*
         */
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

        /*//updating the data
        if (!databaseHelper.isNCODDataAvailable(realm)) {
            Intent intent = new Intent(context, NcodDataSyncIntentService.class);
            context.startService(intent);
        }*/
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