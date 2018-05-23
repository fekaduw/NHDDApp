package et.gov.fmoh.nhddapp.nhddapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import et.gov.fmoh.nhddapp.nhddapp.model.Concept;

public class ConceptAdaptor extends ArrayAdapter<Concept> {

    private Context context;
    private List<Concept> values;

    public ConceptAdaptor(Context context, List<Concept> values) {
        super(context, R.layout.list_item_pagination, values);

        this.context = context;
        this.values = values;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

       /* if (row == null) {

            LayoutInflater inflater =
                    (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_pagination, parent, false);
        }

        TextView textView = row.findViewById(R.id.list_item_pagination_text);

        Concept item = values.get(position);
        String message = item.getDisplay_name();
        textView.setText(message);*/

        return row;
    }
}
