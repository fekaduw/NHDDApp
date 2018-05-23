package et.gov.fmoh.nhddapp.nhddapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<String> {

    private List<String> names;
    private List<String> descs;
    private Integer imgIcon ;

    private Activity context;

    public CustomListViewAdapter(Activity context, List<String> names, List<String> descs, Integer imgIcon) {
        super(context, R.layout.list_item_pagination, names);

        this.context = context;
        this.names = names;
        this.descs = descs;
        this.imgIcon = imgIcon;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder ;

        if(r==null){
            LayoutInflater inflater = context.getLayoutInflater();
            r = inflater.inflate(R.layout.list_item_pagination, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.textViewName.setText (names.get(position));
        viewHolder.textViewDesc.setText (descs.get(position));
        viewHolder.imgViewIcon.setImageResource(imgIcon);

        return r;
    }

    class ViewHolder{
        TextView textViewName;
        TextView textViewDesc;
        ImageView imgViewIcon;

        ViewHolder(View view){
            textViewName = view.findViewById(R.id.concept_name_textview);
            textViewDesc = view.findViewById(R.id.concept_desc_textview);
            imgViewIcon = view.findViewById(R.id.imageView);
        }
    }
}
