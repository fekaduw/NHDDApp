package et.gov.fmoh.nhddapp.nhddapp.views.adapter;

public class CustomListViewAdapter{// extends RecyclerView.Adapter<CustomListViewAdapter.ViewHolder> {

    /*private ArrayList<NcodConcept> concept;
    private ArrayList<NcodConcept> conceptFiltered;
    private Integer favoriteImgIcon;

    private Context context;

    public CustomListViewAdapter(Context context, ArrayList<NcodConcept> concept, Integer favoriteImgIcon) {
        this.context = context;

        update(concept);

        this.favoriteImgIcon = favoriteImgIcon;

        this.conceptFiltered = new ArrayList<NcodConcept>();

        //todo restore it
        if (Tab1Fragment.concepts != null) {
            this.conceptFiltered.addAll(Tab1Fragment.concepts);
        }
    }

    public void update(ArrayList<NcodConcept> concept){
        this.concept = concept;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View r = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pagination, parent, false);

        return new ViewHolder(r);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final NcodConcept ncodConcept = concept.get(position);

        holder.textViewName.setText(ncodConcept.getDisplay_name());
        holder.textViewDesc.setText(ncodConcept.getConcept_class());
        holder.iconConcept.setText(ncodConcept.getDisplay_name().substring(0, 1));
        holder.favoriteImgViewIcon.setImageResource(favoriteImgIcon);
        holder.favoriteImgViewIcon.setId(position);

        Log.d("CV_VALUE", "Display name: " + ncodConcept.getDisplay_name());

        holder.setItemClickListener(new ItemClickListenerHelper() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(context, concept.get(position).getDisplay_name(),Toast.LENGTH_LONG).show();

                Log.v(TAG, ncodConcept.getDisplay_name() + " is tapped.");

                Intent intent = new Intent(context, NcodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("display_name", ncodConcept.getDisplay_name());
               // bundle.putString("description", ncodConcept.getDescriptions());
                bundle.putString("category", ncodConcept.getConcept_class());
                bundle.putString("version_created_on", ncodConcept.getVersion_created_on());
                bundle.putBoolean("is_version_latest", ncodConcept.is_latest_version());

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
    //todo restore
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Tab1Fragment.concepts.clear();

        if (charText.length() == 0) {
            Tab1Fragment.concepts.addAll(conceptFiltered);
        } else {
            for (NcodConcept row : conceptFiltered) {

                Log.d(TAG, row.getDisplay_name());

                if (row.getDisplay_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    Tab1Fragment.concepts.add(row);
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