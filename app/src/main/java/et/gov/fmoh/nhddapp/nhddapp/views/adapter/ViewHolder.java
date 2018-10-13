package et.gov.fmoh.nhddapp.nhddapp.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.utils.ItemClickListenerHelper;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.concept_name_textview)
    public TextView textViewName;
    @BindView(R.id.concept_desc_textview)
    public TextView textViewDesc;
    @BindView(R.id.concept_icon)
    public ImageView iconConcept;
  /*  @BindView(R.id.favorite_imageview)
    public ImageView favoriteImgViewIcon;*/

    public ViewHolder(View view) {
        super(view);
        textViewName = view.findViewById(R.id.concept_name_textview);
        textViewDesc = view.findViewById(R.id.concept_desc_textview);
      iconConcept = view.findViewById(R.id.concept_icon);
        //favoriteImgViewIcon = view.findViewById(R.id.favorite_imageview);

        Log.d("CV_VALUE", "Inside ViewHolder");
    }

   private ItemClickListenerHelper itemClickListenerHelper;

/*    public void setItemClickListener(ItemClickListenerHelper itemClickListenerHelper) {
        this.itemClickListenerHelper = itemClickListenerHelper;
    }*/

    @Override
    public void onClick(View v) {
        this.itemClickListenerHelper.onItemClick(this.getLayoutPosition());
    }
}