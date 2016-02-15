package layout;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brian.barcodescanner.InfoActivity;
import com.example.brian.barcodescanner.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TweetsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TweetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TweetsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView rv;

    public TweetsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TweetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TweetsFragment newInstance(String param1, String param2) {
        TweetsFragment fragment = new TweetsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }




    class Tweet {
        String text;

        Tweet(String text) {
            this.text = text;

        }
    }

    private List<Tweet> tweets;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);

        return view;
    }

    public void onStart() {
        super.onStart();
        rv = (RecyclerView)getView().findViewById(R.id.rv2);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        String upc = ((InfoActivity)getActivity()).getUpc();
        new GetTweets().execute(upc);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class GetTweets extends AsyncTask<String, String, String> {
        protected String doInBackground(String... upcs) {
            String name = retrieveInfo_h(upcs[0]);
            String q = "";
            String[] arr = name.split(" ");
            if (arr.length == 1){
                q = arr[0];
            } else if (arr.length == 2) {
                q = arr[0] + " " + arr[1];
            } else if (arr.length >= 3) {
                q = arr[0] + " " + arr[1] + " " + arr[2];
            }
            name = URLEncoder.encode(q);
            return getATweet(name);
        }
        protected void onPostExecute(String information) {
            /*Here is where we're going to get the item info*/
            /*Grabs the UPC from the intent channel, and then
            uses Hank's function to grab the information. */

            double score = Double.parseDouble(information);
            score = (score * 5 + 5)/2;
            String val = String.format("%.2f", score);
            Log.d("Score", val);
            final TextView nameText = (TextView) getView().findViewById(R.id.score23);
            nameText.setText(val);
            RVAdapterTweet adapter = new RVAdapterTweet(tweets);
            rv.setAdapter(adapter);
        }
    }



    String getATweet(String s) {

        String url = "http://162.243.225.66:5000/api?q="+s;
        Log.d("URL", url);
        String tweet = "";
        tweets = new ArrayList<>();
        try {
            URLConnection connection = new URL(url).openConnection();
            StringBuilder content = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
            JSONArray jsonArray = new JSONArray(content.toString());
            tweets = new ArrayList<Tweet>();
            for (int i = 0; i < jsonArray.length() - 1; i++) {
                tweets.add(new Tweet(jsonArray.getString(i)));
            }

            tweet = jsonArray.getString(jsonArray.length() - 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public String retrieveInfo_h(String upcp) {
        Integer id = 0;
        String upc = "";
        String name = "";
        Double price = 0.0;
        String description = "";
        String imageUrl = "";
        ArrayList<String> toReturn = new ArrayList<String>();
        ArrayList<String> reviews = new ArrayList<String>();
        try {
            String url = "http://api.walmartlabs.com/v1/items?apiKey=xj2er5488bjkyu9drcmznz7k&upc=" + upcp;
            URLConnection connection = new URL(url).openConnection();
            StringBuilder content = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
            JSONObject json = new JSONObject(content.toString());
            JSONObject jsonObject = json.getJSONArray("items").getJSONObject(0);
            name = jsonObject.getString("name");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return name;
    }


}
