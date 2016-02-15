package layout;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brian.barcodescanner.InfoActivity;
import com.example.brian.barcodescanner.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView rv;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewsFragment newInstance(String param1, String param2) {
        ReviewsFragment fragment = new ReviewsFragment();
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

    class Review {
        String title;
        String content;
        String rating;

        Review(String title, String content, String rating) {
            this.title = title;
            this.content = content;
            this.rating = rating;

        }
    }

    private List<Review> reviews;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        rv = (RecyclerView)view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        String upc = ((InfoActivity)getActivity()).getUpc();
        new DoHttpRequest().execute(upc);
        return view;
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
    private class DoHttpRequest extends AsyncTask<String, String, String[]> {
        protected String[] doInBackground(String... upcs) {
            return reviewRetrieveInfo_h(upcToId(upcs[0]));
        }

        protected void onPostExecute(String[] information) {
            /*Here is where we're going to get the item info*/
            /*Grabs the UPC from the intent channel, and then
            uses Hank's function to grab the information. */

            RVAdapter adapter = new RVAdapter(reviews);
            rv.setAdapter(adapter);


            /*final TextView usernameText = (TextView) getView().findViewById(R.id.username);
            usernameText.setText(information[0]);
            final TextView contentText = (TextView) getView().findViewById(R.id.content1);
            contentText.setText(information[1]);
            final TextView titleText = (TextView) getView().findViewById(R.id.title);
            titleText.setText(information[2]);*/
        }
    }
    public String upcToId(String upc) {
        String id = "";
        try {
            String url = "http://api.walmartlabs.com/v1/items?apiKey=xj2er5488bjkyu9drcmznz7k&upc=" + upc;
            URLConnection connection = new URL(url).openConnection();
            StringBuilder content = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();

            // read from the urlconnection via the bufferedreader
            JSONObject json = new JSONObject(content.toString());
            JSONObject jsonObject = json.getJSONArray("items").getJSONObject(0);
            id = jsonObject.getString("itemId");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return id;
    }

    public String[] reviewRetrieveInfo_h(String itemId) {
        Integer id = 0;
        String username = "";
        String reviewContent = "";
        String title = "";
        String rating = "";
        reviews = new ArrayList<Review>();
        try {
            String url = "http://api.walmartlabs.com/v1/reviews/"+itemId+"?apiKey=xj2er5488bjkyu9drcmznz7k&format=json";
            URLConnection connection = new URL(url).openConnection();
            StringBuilder content = new StringBuilder();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();

            // read from the urlconnection via the bufferedreader
            JSONObject json = new JSONObject(content.toString());

            for (int i = 0; i < json.getJSONArray(("reviews")).length(); i++){
                JSONObject jsonObject = json.getJSONArray("reviews").getJSONObject(i);
                try {
                    username = jsonObject.getString("review");
                }catch (Exception e){

                }
                reviewContent = jsonObject.getString("reviewText");
                rating = jsonObject.getJSONObject("overallRating").getString("rating");
                title = jsonObject.getString("title");
                reviews.add(new Review(title, reviewContent, rating));
                Log.d("Review", reviewContent);

            }




        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new String[] {username, reviewContent, title};
    }




}
