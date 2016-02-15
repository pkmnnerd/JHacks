package layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brian.barcodescanner.InfoActivity;
import com.example.brian.barcodescanner.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public static String pUrl = "http://www.walmart.com";

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_product, container, false);
        String upc = ((InfoActivity)getActivity()).getUpc();
        new DoHttpRequest().execute(upc);

        /*String[] information = retrieveInfo_h(upc);
        Log.d("NAME",information[0]);
        final TextView nameText = (TextView) view.findViewById(R.id.name);
        nameText.setText(upc); //Should really be information[0]
        final TextView priceText = (TextView) view.findViewById(R.id.price);
        priceText.setText("$" + information[1]);
        final TextView descriptionText = (TextView) view.findViewById(R.id.description);
        descriptionText.setText(information[2]);
        final ImageView productImage = (ImageView) view.findViewById(R.id.product_image);*/
        /*productImage.setImageResource();*/
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d("VIEW", "Destroyed");
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



    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private class DoHttpRequest extends AsyncTask<String, String, String[]> {
        protected String[] doInBackground(String... upcs) {
            return retrieveInfo_h(upcs[0]);
        }

        protected void onPostExecute(String[] information) {
            /*Here is where we're going to get the item info*/
            /*Grabs the UPC from the intent channel, and then
            uses Hank's function to grab the information. */
            final TextView nameText = (TextView) getView().findViewById(R.id.name);
            nameText.setText(information[0]);
            final TextView priceText = (TextView) getView().findViewById(R.id.price);
            priceText.setText("$" + information[1]);
            final TextView descriptionText = (TextView) getView().findViewById(R.id.description);
            descriptionText.setText(information[2]);
            new DownloadImageTask((ImageView) getView().findViewById(R.id.product_image))
                    .execute(information[3]);
        }

    }



    public String[] retrieveInfo_h(String upc) {
        Integer id = 0;
        String name = "";
        Double price = 0.0;
        String description = "";
        String imageUrl = "";
        String content;

        ArrayList<String> reviews = new ArrayList<String>();
        try {
            String url = "http://api.walmartlabs.com/v1/items?apiKey=xj2er5488bjkyu9drcmznz7k&upc=" + upc;
            URL urlObj = new URL(url);
            URLConnection urlConnection = urlObj.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            try {
                content = readStream(in);
            }
            finally {
                in.close();
            }
            String line;

            // read from the urlconnection via the bufferedreader
            JSONObject json = new JSONObject(content.toString());

            JSONObject jsonObject = json.getJSONArray("items").getJSONObject(0);
            id = jsonObject.getInt("itemId");
            if (id != 0) {
                name = jsonObject.getString("name");
                price = jsonObject.getDouble("salePrice");
                description = jsonObject.getString("shortDescription");
                description = description.substring(9,description.length()-10);

                imageUrl = jsonObject.getString("largeImage");
                pUrl = jsonObject.getString(("productUrl"));

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new String[] {name, price.toString(), description.toString(), imageUrl};
    }


}
