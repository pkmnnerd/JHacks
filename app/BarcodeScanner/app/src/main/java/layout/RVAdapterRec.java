package layout;

/**
 * Created by Brian on 2/14/2016.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brian.barcodescanner.R;

import java.util.List;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brian.barcodescanner.R;

import java.util.List;

/**
 * Created by Brian on 2/14/2016.
 */
public class RVAdapterRec extends RecyclerView.Adapter<RVAdapterRec.PersonViewHolder>{

    List<RecommendedFragment.Rec> recs;

    RVAdapterRec(List<RecommendedFragment.Rec> recs){
        this.recs = recs;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rec_card, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.name.setText(recs.get(i).name);
        personViewHolder.desc.setText(recs.get(i).desc);
        new RecommendedFragment.DownloadImageTask(personViewHolder.img)
                .execute(recs.get(i).img);
    }



    @Override
    public int getItemCount() {
        return recs.size();
    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView desc;
        ImageView img;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.name1);
            desc = (TextView)itemView.findViewById(R.id.desc1);
            img = (ImageView)itemView.findViewById(R.id.img1);
        }
    }

}
