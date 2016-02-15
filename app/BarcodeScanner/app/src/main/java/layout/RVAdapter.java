package layout;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brian.barcodescanner.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Brian on 2/14/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

    List<ReviewsFragment.Review> reviews;

    RVAdapter(List<ReviewsFragment.Review> reviews){
        this.reviews = reviews;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.title.setText(reviews.get(i).title);
        personViewHolder.content.setText(reviews.get(i).content);
        personViewHolder.rating.setText((reviews.get(i).rating));
    }



    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView content;
        TextView rating;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            content = (TextView)itemView.findViewById(R.id.content1);
            rating = (TextView) itemView.findViewById((R.id.star_number));
        }
    }

}
