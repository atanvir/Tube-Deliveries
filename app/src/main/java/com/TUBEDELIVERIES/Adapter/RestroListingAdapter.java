package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.internal.$Gson$Preconditions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class RestroListingAdapter extends RecyclerView.Adapter<RestroListingAdapter.MyFevViewHolder> implements Filterable {
    private Context context;
    private List<RestaurantResponse> list=null;
    private List<RestaurantResponse> restroFilterList=null;
    private onRestaurantClick listener;

    public RestroListingAdapter(Context context, List<RestaurantResponse> list,onRestaurantClick listener) {
        this.context = context;
        this.list = list;
        this.restroFilterList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyFevViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_view_pager_restaurants,viewGroup,false);
        return new MyFevViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFevViewHolder myFevViewHolder, int i) {
        myFevViewHolder.tvRestaurantNameHome.setText(restroFilterList.get(i).getName());
        myFevViewHolder.tvRestauAddressHome.setText(restroFilterList.get(i).getAddress());
       // myFevViewHolder.tvRatingHome.setText(restroFilterList.get(i).getRating());
        myFevViewHolder.tvReview.setText("(" + restroFilterList.get(i).getReview() + " reviews" + ")");

        myFevViewHolder.tvRatingHome.setText(CommonUtilities.roundOff(restroFilterList.get(i).getRating()));
        myFevViewHolder.ivRatinngs.setRating(Float.parseFloat(restroFilterList.get(i).getRating()));

        if(!restroFilterList.get(i).getImg().equalsIgnoreCase("")) {
            setImage(myFevViewHolder.progressbar, restroFilterList.get(i).getImg(), myFevViewHolder.ivRestaurants);
        }
        else {
            myFevViewHolder.progressbar.setVisibility(GONE);
            Glide.with(context).load(R.drawable.food_thali).placeholder(R.drawable.food_thali).override(110, 150).into(myFevViewHolder.ivRestaurants);
        }

        ArrayList<String> dishes = new ArrayList<>();
        if(restroFilterList.get(i).getCuisines()!=null) {
            if(restroFilterList.get(i).getCuisines().size()>0) {
                for (int j = 0; j < restroFilterList.get(i).getCuisines().size(); j++) {
                    TextView textView = new TextView(context);
                    textView.setText(restroFilterList.get(i).getCuisines().get(j).getName());
                    textView.setCompoundDrawablePadding(10);
                    textView.setTextSize(11);
                    textView.setTextColor(ContextCompat.getColor(context, R.color.light_grey));
                    textView.setPadding(0, 0, 15, 0);
                    textView.setMaxLines(1);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bg_dot_circle2, 0, 0, 0);
                    myFevViewHolder.llFoodType.addView(textView);
                }
            }
        }







// if(restroFilterList.get(i).isHeartAnimCheck()){
// startHeartAnimation(myFevViewHolder);
// }else {
// disableofflineAnimation(myFevViewHolder);
// }

// if(restroFilterList.get(i).isHeartAnimCheck()){
// startHeartAnimation(myFevViewHolder);
// }
        new DistaceClass(myFevViewHolder.tvdistance,i).execute();

        Log.e("isFav", restroFilterList.get(i).getIs_favorite());

        if (restroFilterList.get(i).getIs_favorite().equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myFevViewHolder.ivLikeHome.setBackground(context.getResources().getDrawable(R.drawable.unfav, null));
            } else {
                myFevViewHolder.ivLikeHome.setBackground(ContextCompat.getDrawable(context, R.drawable.unfav));
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myFevViewHolder.ivLikeHome.setBackground(context.getResources().getDrawable(R.drawable.heart_like_, null));
            } else {
                myFevViewHolder.ivLikeHome.setBackground(ContextCompat.getDrawable(context, R.drawable.heart_like_));
            }
        }





    }


    public void updateList(List<RestaurantResponse> restroFilterList){
        this.restroFilterList=restroFilterList;
        notifyDataSetChanged();
    }

    public void removeItem(int pos){
        list.remove(pos);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return restroFilterList!=null?restroFilterList.size():0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    restroFilterList = list;
                } else {
                    ArrayList<RestaurantResponse> newList = new ArrayList<>();
                    for (RestaurantResponse host : list) {
                        if (host.getName().toLowerCase().contains(charString.toLowerCase())) {
                            newList.add(host);

                        }

                    }
                    restroFilterList = newList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = restroFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                restroFilterList = (ArrayList<RestaurantResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };



    }

    public class MyFevViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivRestaurants)
        ImageView ivRestaurants;

        @BindView(R.id.tvRestaurantNameHome)
        TextView tvRestaurantNameHome;


        @BindView(R.id.tvRestauAddressHome)
        TextView tvRestauAddressHome;
        @BindView(R.id.tvRatingHome)
        TextView tvRatingHome;
        @BindView(R.id.ivRatinngs)
        RatingBar ivRatinngs;
        @BindView(R.id.tvReview)
        TextView tvReview;
        @BindView(R.id.progressbar)
        ProgressBar progressbar;

        @BindView(R.id.ivLikeHome)
        ImageView ivLikeHome;

        @BindView(R.id.llFoodType)
        LinearLayout llFoodType;

        @BindView(R.id.ivdestLocation)
        ImageView ivdestLocation;


        @BindView(R.id.lottieAnimationView)
        LottieAnimationView lottieAnimationView;

        @BindView(R.id.tvdistance)
        TextView tvdistance;

        public MyFevViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            ivdestLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("www.google.com")
                            .appendPath("maps")
                            .appendPath("dir")
                            .appendPath("")
                            .appendQueryParameter("api", "1")
                            .appendQueryParameter("destination", Double.parseDouble(restroFilterList.get(getAdapterPosition()).getLatitude()) + "," + Double.parseDouble(restroFilterList.get(getAdapterPosition()).getLongitude()));
                    String url = builder.build().toString();
                    Log.d("Directions", url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null)
                        listener.onClickRestaurant(restroFilterList.get(getAdapterPosition()).getId());

                }
            });


            ivLikeHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null){
                        if(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())){
                            listener.onFavClick(getAdapterPosition(),restroFilterList.get(getAdapterPosition()));
                        }
                        else{
                            CommonUtilities.showAlertDialog(context);
                        }


                    }

                }
            });

        }
    }

    private void setImage( ProgressBar progressBar,final String imageUri,final ImageView imageView) {

        progressBar.setVisibility(View.VISIBLE);

        Glide.with(context.getApplicationContext())
                .load(imageUri)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                }).into(imageView);
    }

    public interface onRestaurantClick{

        void onClickRestaurant(String restaurantId);
        void onFavClick(int position, RestaurantResponse response);

    }


    ////start heart animation when user click on fav icon
    private void startHeartAnimation(MyFevViewHolder holder){
        holder.lottieAnimationView.setVisibility(View.VISIBLE);
        holder.lottieAnimationView.setAnimation("362-like.json");
        holder.lottieAnimationView.playAnimation();
        holder.lottieAnimationView.loop(false);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.lottieAnimationView.setVisibility(View.GONE);

            }
        },1000);

    }

    ////disable heart animation when user click on fav icon
    public void disableofflineAnimation(MyFevViewHolder holder){
        if(holder.lottieAnimationView!=null){
            holder.lottieAnimationView.pauseAnimation();
        }
        holder.lottieAnimationView.setVisibility(View.GONE);

    }

    public class DistaceClass extends AsyncTask<Integer, Void ,String>
    {
        private TextView textView;
        private int pos;

        public DistaceClass(TextView textView,int pos)
        {
            this.textView=textView;
            this.pos=pos;
        }


        @Override
        protected void onPostExecute(String o) {
            textView.setText(o);
        }

        @Override
        protected String doInBackground(Integer... voids) {

            Double latitude= Double.valueOf(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.LATITUDE));
            Double longitude= Double.valueOf(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.LONGITUDE));
            Double prelatitute = 0.0;
            Double prelongitude =0.0 ;
            if(pos<restroFilterList.size()) {
                prelatitute = Double.parseDouble(restroFilterList != null ? restroFilterList.get(pos).getLatitude() : "0.0");
                prelongitude = Double.parseDouble(restroFilterList != null ? restroFilterList.get(pos).getLongitude() : "0.0");
            }

            final String[] result_in_kms = {""};
            String url = "https://maps.google.com/maps/api/directions/xml?origin="
                    + latitude + "," + longitude + "&destination=" + prelatitute
                    + "," + prelongitude + "&key="+context.getString(R.string.google_map_api_key);


            Log.e("url:--",url);
            String tag[] = { "text" };
            HttpResponse response = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);
                response = httpClient.execute(httpPost, localContext);
                Log.e("response",response.toString());

                InputStream is = response.getEntity().getContent();


                DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                Document doc = builder.parse(is);
                if (doc != null) {
                    NodeList nl;
                    ArrayList args = new ArrayList();
                    for (String s : tag) {
                        nl = doc.getElementsByTagName(s);
                        if (nl.getLength() > 0) {
                            Node node = nl.item(nl.getLength() - 1);
                            args.add(node.getTextContent());
                        } else {
                            args.add(" - ");
                        }
                    }
                    result_in_kms[0] = String.format("%s", args.get(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }






            return result_in_kms[0];
        }
    }

}