package et.gov.fmoh.nhddapp.nhddapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicator;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicatorConcept;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;

import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.TAG;

import et.gov.fmoh.nhddapp.nhddapp.utils.CONST;
import et.gov.fmoh.nhddapp.nhddapp.utils.CategoryInfo;
import et.gov.fmoh.nhddapp.nhddapp.utils.DatabaseHelper;
import et.gov.fmoh.nhddapp.nhddapp.utils.GenerateColor;
import et.gov.fmoh.nhddapp.nhddapp.utils.ItemClickListenerHelper;
import et.gov.fmoh.nhddapp.nhddapp.utils.SharedPref;
import et.gov.fmoh.nhddapp.nhddapp.views.adapter.CategoryListViewAdapter;
import et.gov.fmoh.nhddapp.nhddapp.views.adapter.ViewHolder;
import io.realm.Realm;
import io.realm.RealmChangeListener;

import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.CATEGORY_HMIS_INDICATOR;
import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.CATEGORY_NCOD;


public class ConceptListActivity<T> extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.recView)
    RecyclerView recyclerView;
    @BindView(R.id.textViewNotfound)
    TextView textViewNotFound;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.search_view)
    SearchView searchView;

    private SharedPref sharedPref;

    //custom adapter
    private CategoryListViewAdapter categoryListViewAdapter;

    private ArrayList<NcodConcept> conceptNcodFiltered;
    private ArrayList<NcodConcept> conceptNcod = null;

    private ArrayList<HMISIndicatorConcept> conceptHMISFiltered;
    private ArrayList<HMISIndicatorConcept> conceptHMIS;
    private DatabaseHelper databaseHelper;

    private Realm realm;

    String categoryName;

    public ConceptListActivity() {
        databaseHelper = new DatabaseHelper();
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        setTheme(sharedPref.loadNightModeState() ? R.style.DarkTheme : R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        setTitle(getCategoryInfo().getCategoryName());

        ButterKnife.bind(this);

        categoryName = getCategoryInfo().getCategoryName();

        setViews();

        conceptNcodFiltered = new ArrayList<>();
        conceptNcod = databaseHelper.getNcodConcepts(realm, categoryName);

        if (conceptNcod != null) {
            conceptNcodFiltered.addAll(conceptNcod);

            realm.addChangeListener(mRealmChangeListener);
        }

        conceptHMISFiltered = new ArrayList<>();
        conceptHMIS = databaseHelper.getHmisConcepts(realm, categoryName);

        if (conceptHMIS != null) {
            conceptHMISFiltered.addAll(conceptHMIS);

            realm.addChangeListener(mRealmChangeListener);
        }

        getData();
    }

    private void setViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(itemDecoration);

        searchView.setOnQueryTextListener(this);
    }

    private CategoryInfo getCategoryInfo() {
        CategoryInfo categoryInfo = new CategoryInfo();
        Bundle bundle = getIntent().getExtras();

        //category could be either NCoD or HMIS Indicators
        categoryInfo.setCategory(bundle.getString("category"));
        categoryInfo.setCategoryName(bundle.getString("categoryName"));
        categoryInfo.setColor(bundle.getInt("color"));

        return categoryInfo;
    }

    private void getData() {
        CategoryInfo categoryInfo = getCategoryInfo();
        //textViewCategory.setText(categoryInfo.getCategoryName());

        switch (categoryInfo.getCategory()) {
            case CATEGORY_NCOD:
                setAdapterNCOD(categoryInfo.getColor());
                break;
            case CATEGORY_HMIS_INDICATOR:
                setAdapterHMIS(categoryInfo.getColor());
                break;
        }
    }

    private void setAdapterNCOD(int color) {

        //conceptNcod = databaseHelper.getNcodConcepts(realm, categoryName);

        categoryListViewAdapter = new CategoryListViewAdapter<NcodConcept>(getApplicationContext(), conceptNcod,
                new ItemClickListenerHelper() {
                    @Override
                    public void onItemClick(int position) {
                        //Toast.makeText(getApplicationContext(), "Lucky you: item #" + conceptNcod.get(position).getDisplay_name(), Toast.LENGTH_SHORT).show();

                        Log.d(TAG, conceptNcod.get(position).getDisplay_name() + " item tapped");

                        Intent intent = new Intent(getApplicationContext(), NcodDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("conceptType", CATEGORY_NCOD);
                        bundle.putInt("color", getCategoryInfo().getColor());
                        bundle.putString("categoryName", categoryName);

                        bundle.putString("display_name", conceptNcod.get(position).getDisplay_name());
                        bundle.putString("description", "NA");
                        bundle.putString("class_name", conceptNcod.get(position).getConcept_class());
                        bundle.putString("version_created_on", conceptNcod.get(position).getVersion_created_on());
                        bundle.putString("version", conceptNcod.get(position).getVersion());

                        intent.putExtras(bundle);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }) {
            @Override
            public void onBindData(ViewHolder holder1, NcodConcept val) {
                NcodConcept concept = val;
                Log.d(TAG, "Current concept name: " + concept.getDisplay_name());
                ViewHolder holder = holder1;
                holder.textViewName.setText(concept.getDisplay_name());
                holder.textViewDesc.setText(concept.getConcept_class());

                holder.iconConcept.setImageDrawable(getColor(concept, color));

            }

            @Override
            public void filter(String charText) {
                charText = charText.toLowerCase(Locale.getDefault());
                conceptNcod.clear();

                if (charText.length() == 0) {
                    conceptNcod.addAll(conceptNcodFiltered);
                } else {
                    for (NcodConcept row : conceptNcodFiltered) {

                        Log.d(TAG, row.getDisplay_name());

                        if (row.getDisplay_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                            conceptNcod.add(row);
                            Log.d(TAG, "Filtered row added.");
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void update(ArrayList<NcodConcept> concepts) {
                conceptNcod = concepts;
                notifyDataSetChanged();
            }
        };

        recyclerView.setAdapter(categoryListViewAdapter);
        textViewNotFound.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void setAdapterHMIS(int color) {
        String categoryName = getCategoryInfo().getCategoryName();
        conceptHMIS = databaseHelper.getHmisConcepts(realm, categoryName);

        categoryListViewAdapter = new CategoryListViewAdapter<HMISIndicatorConcept>(getApplicationContext(), conceptHMIS, new ItemClickListenerHelper() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, conceptHMIS.get(position).getDisplay_name() + " item tapped");

                Intent intent = new Intent(getApplicationContext(), HmisDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("conceptType", CATEGORY_NCOD);
                bundle.putString("categoryName", categoryName);
                bundle.putInt("color", getCategoryInfo().getColor());

                bundle.putString("display_name", conceptHMIS.get(position).getDisplay_name());
                bundle.putString("datatype", conceptHMIS.get(position).getDatatype());
                bundle.putString("concept_class", conceptHMIS.get(position).getConcept_class());
                bundle.putString("source", conceptHMIS.get(position).getSource());
                bundle.putString("description", "NA");
                bundle.putString("version_created_on", conceptHMIS.get(position).getVersion_created_on());
                bundle.putString("version", conceptHMIS.get(position).getVersion());

                //dat from the HMISIndicatorExtras()
                Bundle hmisCategoryBundle = getIntent().getExtras();
                bundle.putAll(hmisCategoryBundle);

                /*bundle.putString("hmisCategory1", bundle.get("hmisCategory1"));
                bundle.putString("hmisCategory2", !TextUtils.isEmpty(concepts.get(position).getHmisCategory2())?concepts.get(position).getHmisCategory2():"NA");
                bundle.putString("hmisCategory3", !TextUtils.isEmpty(concepts.get(position).getHmisCategory3())?concepts.get(position).getHmisCategory3():"NA");
                bundle.putString("hmisCategory4", !TextUtils.isEmpty(concepts.get(position).getHmisCategory4())?concepts.get(position).getHmisCategory4():"NA");

                bundle.putBoolean("applicableReportingUnits", conceptHMIS.get(position).is_latest_version());
                bundle.putBoolean("numerator", conceptHMIS.get(position).is_latest_version());
                bundle.putBoolean("denominator", conceptHMIS.get(position).is_latest_version());
                bundle.putBoolean("disaggregation", conceptHMIS.get(position).is_latest_version());
                bundle.putBoolean("reportingFrequency", conceptHMIS.get(position).is_latest_version());
                bundle.putBoolean("multiplier", conceptHMIS.get(position).is_latest_version());
                bundle.putBoolean("primarySources", conceptHMIS.get(position).is_latest_version());
*/
                intent.putExtras(bundle);

                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        }) {

            @Override
            public void onBindData(ViewHolder holder1, HMISIndicatorConcept concept) {

                Log.d(TAG, "Current concept name: " + concept.getDisplay_name());
                ViewHolder holder = holder1;
                holder.textViewName.setText(concept.getDisplay_name());
                holder.textViewDesc.setText(concept.getConcept_class());

                holder.iconConcept.setImageDrawable(getColor(concept, color));
            }

            @Override
            public void filter(String charText) {
                charText = charText.toLowerCase(Locale.getDefault());
                conceptHMIS.clear();

                if (charText.length() == 0) {
                    conceptHMIS.addAll(conceptHMISFiltered);
                } else {
                    for (HMISIndicatorConcept row : conceptHMISFiltered) {

                        Log.d(TAG, row.getDisplay_name());

                        if (row.getDisplay_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                            conceptHMIS.add(row);
                            Log.d(TAG, "Filtered row added.");
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void update(ArrayList<HMISIndicatorConcept> concepts) {
                conceptHMIS = concepts;
                notifyDataSetChanged();
            }
        };

        recyclerView.setAdapter(categoryListViewAdapter);

        textViewNotFound.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
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

    private void showRecyclerView(boolean isShow) {
        if (isShow) {
            textViewNotFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            textViewNotFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private RealmChangeListener mRealmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            Log.d(TAG, "OnChange() method in change listener called");

            if (getCategoryInfo().getCategory() == CATEGORY_NCOD) {
                conceptNcod = databaseHelper.getNcodConcepts(realm, getCategoryInfo().getCategoryName());

                try {
                    if (conceptNcod != null) {
                        showProgressBar(false);

                        showRecyclerView(true);

                        Log.d(TAG, "The size of concepts is: " + conceptNcod.size());
                    }
                } catch (NullPointerException ex) {
                    conceptNcod = null;
                    Log.e(CONST.TAG, ex.getMessage());
                }
            } else if (getCategoryInfo().getCategory() == CATEGORY_HMIS_INDICATOR) {
                conceptHMIS = databaseHelper.getHmisConcepts(realm, getCategoryInfo().getCategoryName());

                try {
                    if (conceptHMIS != null) {
                        showProgressBar(false);

                        showRecyclerView(true);

                        Log.d(TAG, "The size of concepts is: " + conceptHMIS.size());
                    }
                } catch (NullPointerException ex) {
                    conceptHMIS = null;
                    Log.e(CONST.TAG, ex.getMessage());
                }
            } else {
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

    private TextDrawable getColor(NcodConcept concept, int color) {
        GenerateColor<T> generateColor = new GenerateColor<>();

        return generateColor.getTextDrawable(concept.getDisplay_name(), color);
    }

    private TextDrawable getColor(HMISIndicatorConcept concept, int color) {
        GenerateColor<HMISIndicatorConcept> generateColor = new GenerateColor<>();

        return generateColor.getTextDrawable(concept.getDisplay_name(), color);
    }
}


