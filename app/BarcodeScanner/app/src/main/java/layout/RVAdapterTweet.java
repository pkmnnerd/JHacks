package layout;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Brian on 2/14/2016.
 */
public class RVAdapterTweet extends RecyclerView.Adapter<RVAdapterTweet.PersonViewHolder>{

    List<TweetsFragment.Tweet> tweets;

    RVAdapterTweet(List<TweetsFragment.Tweet> tweets){
        this.tweets = tweets;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_tweet, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.text.setText(tweets.get(i).text);
    }



    @Override
    public int getItemCount() {
        return tweets.size();
    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView text;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv3);
            text = (TextView)itemView.findViewById(R.id.tweet3);
        }
    }

}

