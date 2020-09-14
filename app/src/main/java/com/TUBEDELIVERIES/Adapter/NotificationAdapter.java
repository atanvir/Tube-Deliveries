package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.NotificationList;
import com.TUBEDELIVERIES.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private Context context;
    private List<NotificationList> notificationLists;

    public NotificationAdapter(Context context, List<NotificationList> notificationLists)
    {
        this.context=context;
        this.notificationLists=notificationLists;
    }





    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_notification,viewGroup,false);
        return new NotificationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.titleText.setText(notificationLists.get(position).getTitle());
        myViewHolder.descText.setText(notificationLists.get(position).getDescription());

        String getDate = notificationLists.get(position).getCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            Date date = sdf.parse(server_format);
            String your_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
            String[] splitted = your_format.split(" ");
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date endDate=dateFormat.parse(your_format);
            Date startDate= Calendar.getInstance().getTime();

            long differenceDate=startDate.getTime()-endDate.getTime();
            String[] completeDate=splitted[0].split("-");
            String date1=completeDate[0];
            String month=completeDate[1];
            String year=completeDate[2];


            int days_in_months=new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date1)).getActualMaximum(Calendar.DAY_OF_MONTH);

            long secounds=1000;     // 1 secound
            long min=60*secounds;   // 1 min
            long hour=3600000;      // 1 hour
            long day=86400000;      // 1 days


            long monthdifference=differenceDate/(days_in_months*day);
            long daysDifference=differenceDate/day;
            long hourdifference=differenceDate/hour;
            long mindifference=differenceDate/min;
            long secoundsDiffer=differenceDate/secounds;

          
            if(monthdifference>0)
            {
                myViewHolder.dateText.setText(monthdifference+" "+context.getString(R.string.months_ago));
            }
            else if(daysDifference>0)
            {
                if(daysDifference==1)
                {
                    myViewHolder.dateText.setText(daysDifference+" "+context.getString(R.string.day_ago));

                }else
                {
                    myViewHolder.dateText.setText(daysDifference+" "+context.getString(R.string.days_ago));
                }

            }

            else if(hourdifference>0)
            {
                myViewHolder.dateText.setText(hourdifference+" "+context.getString(R.string.hour_ago));
            }
            else if(mindifference>0)
            {
                myViewHolder.dateText.setText(mindifference+" "+context.getString(R.string.min_ago));
            }
            else if(secoundsDiffer>0)
            {
                myViewHolder.dateText.setText(secoundsDiffer+" "+context.getString(R.string.sec_ago));

            }

        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }







    }

    @Override
    public int getItemCount() {
        return notificationLists.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleText) TextView titleText;
        @BindView(R.id.dateText) TextView dateText;
        @BindView(R.id.descText) TextView descText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);




        }
    }
}
