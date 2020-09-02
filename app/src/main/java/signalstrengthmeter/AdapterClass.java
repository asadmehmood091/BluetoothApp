package signalstrengthmeter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder> {


    private LayoutInflater mInflater;
    Context context;
    ArrayList<DataModel> itemslist;

    // devicesList is passed into the constructor
    public AdapterClass(Context context, ArrayList<DataModel> itemslist) {
        this.mInflater = LayoutInflater.from(context);
        this.context=context;
        this.itemslist =itemslist;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.itemview, parent, false);
        return new ViewHolder(view);
    }

    // binds the devicesList to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String device = itemslist.get(position).getdeviceName();
        holder.device.setText(device);
        String status = itemslist.get(position).getmacAddress();
        holder.mac.setText(status);
        String signal = itemslist.get(position).getsignal();
        holder.signal.setText(signal);
       int index = signal.indexOf("d");
        int percent = Integer.valueOf(signal.substring(0,index));
        holder.progressBar.setProgress(percent);
        double obtained = (percent*-1)-30;
        double percentAge = (obtained/60.0);
        percentAge = ((percentAge*100)-100)*-1;

        holder.percent.setText(new DecimalFormat("##").format(percentAge)+"%");
        
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return itemslist.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView device,mac,signal,percent;
        View view;
        ProgressBar progressBar;

        ViewHolder(final View itemView) {
            super(itemView);
            device = itemView.findViewById(R.id.devicename);
            mac = itemView.findViewById(R.id.deviceMac);
            signal = itemView.findViewById(R.id.signal);
            progressBar = itemView.findViewById(R.id.progress);
            percent = itemView.findViewById(R.id.percent);
            view = itemView;

        }
    }



}
