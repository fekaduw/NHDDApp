package et.gov.fmoh.nhddapp.nhddapp.views.adapter;

public class HMISIndicatorsCustomListViewAdapter {//extends RecyclerView.Adapter<HMISIndicatorsCustomListViewAdapter.ViewHolder>{
    /*private ArrayList<HMISIndicatorConcept> concept;
    private ArrayList<HMISIndicatorConcept> conceptFiltered;
    private Integer favoriteImgIcon;

    private Context context;

    public HMISIndicatorsCustomListViewAdapter(Context context, ArrayList<HMISIndicatorConcept> concept, Integer favoriteImgIcon) {
        this.context = context;

        update(concept);

        this.favoriteImgIcon = favoriteImgIcon;

        this.conceptFiltered = new ArrayList<HMISIndicatorConcept>();

        if (Tab2Fragment.concepts != null) {
            this.conceptFiltered.addAll(Tab2Fragment.concepts);
        }
    }

    public void update(ArrayList<HMISIndicatorConcept> concept){
        this.concept = concept;
        notifyDataSetChanged();
    }

    @Override
    public HMISIndicatorsCustomListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View r = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pagination, parent, false);

        return new HMISIndicatorsCustomListViewAdapter.ViewHolder(r);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final HMISIndicatorConcept hmisIndicatorConcept = concept.get(position);

        holder.textViewName.setText (hmisIndicatorConcept.getDisplay_name());
        holder.textViewDesc.setText (hmisIndicatorConcept.getConcept_class());
        //String version = concept.get(position).getIs_latest_version()=="true" ? "Latest version": "Update required";
        //viewHolder.textViewLastUpdated.setText (version);
        holder.iconConcept.setText(hmisIndicatorConcept.getDisplay_name().substring(0,1));
        holder.favoriteImgViewIcon.setImageResource(favoriteImgIcon);
        holder.favoriteImgViewIcon.setId(position);
        Log.d("CV_VALUE", "Display name: " + hmisIndicatorConcept.getDisplay_name());

        holder.setItemClickListener(new ItemClickListenerHelper() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(context, concept.get(position).getDisplay_name(),Toast.LENGTH_LONG).show();
                Log.d(TAG, hmisIndicatorConcept.getDisplay_name() + " item tapped");

                Intent intent = new Intent(context, NcodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("display_name", hmisIndicatorConcept.getDisplay_name());
             //todo   bundle.putString("description", HMISIndicatorConcept.getDescriptions());
                bundle.putString("category", hmisIndicatorConcept.getConcept_class());
                bundle.putString("version_created_on", hmisIndicatorConcept.getVersion_created_on());
                bundle.putBoolean("is_version_latest", hmisIndicatorConcept.is_latest_version());

                //todo: add a title for the detail activity bundle.putString("activity_title", );

                intent.putExtras(bundle);

                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return concept!=null? concept.size() : 0;
    }

    *//*
        This method handles the text search
     *//*
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Tab2Fragment.concepts.clear();

        if (charText.length() == 0) {
            Tab2Fragment.concepts.addAll(conceptFiltered);
        } else {
            for (HMISIndicatorConcept row : conceptFiltered) {

                Log.d(TAG, row.getDisplay_name());

                if (row.getDisplay_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    Tab2Fragment.concepts.add(row);
                    Log.d(TAG, "Filtered row added.");
                }
            }
        }
        notifyDataSetChanged();
    }

    final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.concept_name_textview)
        TextView textViewName;
        @BindView(R.id.concept_desc_textview)
        TextView textViewDesc;
        @BindView(R.id.concept_icon)
        TextView iconConcept;
        @BindView(R.id.favorite_imageview)
        ImageView favoriteImgViewIcon;

        ViewHolder(View view){
            super(view);
            textViewName = view.findViewById(R.id.concept_name_textview);
            textViewDesc = view.findViewById(R.id.concept_desc_textview);
            iconConcept = view.findViewById(R.id.concept_icon);
            favoriteImgViewIcon = view.findViewById(R.id.favorite_imageview);

            iconConcept.setBackgroundResource(R.drawable.circle_concept_icon_green);
            //ButterKnife.bind(this, view);

            Log.d("CV_VALUE", "Inside ViewHolder");
        }

        private ItemClickListenerHelper itemClickListenerHelper;

        public void setItemClickListener (ItemClickListenerHelper itemClickListenerHelper){
            this.itemClickListenerHelper = itemClickListenerHelper;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListenerHelper.onItemClick(this.getLayoutPosition());
        }
    }*/
}