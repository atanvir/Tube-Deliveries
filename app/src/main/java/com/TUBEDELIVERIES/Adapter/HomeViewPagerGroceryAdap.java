package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.TUBEDELIVERIES.Model.NearByGrocery;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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

import butterknife.ButterKnife;

import static android.view.View.GONE;

public class HomeViewPagerGroceryAdap extends PagerAdapter {
    private Context context;
    private List<NearByGrocery> groceryList;
    private com.TUBEDELIVERIES.CallBacks.onClickEvents onClickEvents;

    public HomeViewPagerGroceryAdap(Context context, List<NearByGrocery> groceryList,com.TUBEDELIVERIES.CallBacks.onClickEvents onClickEvents) {
        this.context = context;
        this.groceryList = groceryList;
        this.onClickEvents=onClickEvents;

    }

    @Override
    public int getCount() {
        return groceryList != null ? groceryList.size() : 0;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_view_pager_restaurants, container, false);
        TextView tvRestaurantNameHome = (TextView) itemView.findViewById(R.id.tvRestaurantNameHome);
        CardView cvPrentView = (CardView) itemView.findViewById(R.id.cvPrentView);
        TextView tvRestauAddressHome = (TextView) itemView.findViewById(R.id.tvRestauAddressHome);
        TextView tvRatingHome = (TextView) itemView.findViewById(R.id.tvRatingHome);
        ImageView ivLikeHome = (ImageView) itemView.findViewById(R.id.ivLikeHome);
        TextView tvReview = (TextView) itemView.findViewById(R.id.tvReview);
        RatingBar ivRatinngs = (RatingBar) itemView.findViewById(R.id.ivRatinngs);
        ImageView ivRestaurants = (ImageView) itemView.findViewById(R.id.ivRestaurants);
        ProgressBar progressbar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        TextView tvdistance = (TextView) itemView.findViewById(R.id.tvdistance);
        LinearLayout llFoodType = (LinearLayout) itemView.findViewById(R.id.llFoodType);
        ImageView ivdestLocation = (ImageView) itemView.findViewById(R.id.ivdestLocation);
        tvRestaurantNameHome.setText(groceryList.get(position).getName());
        tvRestauAddressHome.setText(groceryList.get(position).getAddress());
        new HomeViewPagerGroceryAdap.DistaceClass(tvdistance,position).execute();

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
                        .appendQueryParameter("destination", Double.parseDouble(groceryList.get(position).getLatitude()) + "," + Double.parseDouble(groceryList.get(position).getLongitude()));
                String url = builder.build().toString();
                Log.d("Directions", url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });

        tvRatingHome.setText(CommonUtilities.roundOff(String.valueOf(groceryList.get(position).getRating())));
        ivRatinngs.setRating(Float.parseFloat(String.valueOf(groceryList.get(position).getRating())));
        tvReview.setText("(" + groceryList.get(position).getReview() + " reviews" + ")");
        if(!groceryList.get(position).getImg().equalsIgnoreCase("")) {
            setImage(progressbar, groceryList.get(position).getImg(), ivRestaurants);
        }
        else {
            progressbar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.food_thali).override(110,150).into(ivRestaurants);
        }
        if(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {

            if (groceryList.get(position).getIsFavorite().equalsIgnoreCase("0")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivLikeHome.setBackground(context.getResources().getDrawable(R.drawable.unfav, null));
                } else {
                    ivLikeHome.setBackground(ContextCompat.getDrawable(context, R.drawable.unfav));
                }
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivLikeHome.setBackground(context.getResources().getDrawable(R.drawable.heart_like_, null));
                } else {
                    ivLikeHome.setBackground(ContextCompat.getDrawable(context, R.drawable.heart_like_));
                }
            }
        }
        else
        {
            ivLikeHome.setBackground(ContextCompat.getDrawable(context, R.drawable.unfav));
        }
        for (int i=0;i<2;i++)
        {
            TextView textView=new TextView(context);
            textView.setText("");
            textView.setCompoundDrawablePadding(10);
            textView.setTextSize(11);
            textView.setTextColor(ContextCompat.getColor(context,R.color.light_grey));
            textView.setPadding(0,0,15,0);
            textView.setMaxLines(1);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bg_dot_circle2,0,0,0);
            llFoodType.addView(textView);
        }


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickEvents!=null)
                    onClickEvents.onLinkClick(position,""+groceryList.get(position).getId(),tvdistance.getText().toString());
            }
        });

        ivLikeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickEvents!=null)
                {
//                    onClickEvents.onFavClick(position,groceryList.get(position).getId(),3);
            }}
        });

        container.addView(itemView);
        return itemView;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        ButterKnife.bind(this, view);
        return view == ((ConstraintLayout) o);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);

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



    public void updateList(List<NearByGrocery> restroList){
        this.groceryList=restroList;
        notifyDataSetChanged();

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
            Double prelatitute= Double.parseDouble(groceryList.get(pos).getLatitude());
            Double prelongitude= Double.parseDouble(groceryList.get(pos).getLongitude());
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
