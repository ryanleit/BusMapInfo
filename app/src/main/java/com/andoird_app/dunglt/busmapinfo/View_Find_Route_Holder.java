package com.andoird_app.dunglt.busmapinfo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by WIN10 on 1/4/2018.
 */

public class View_Find_Route_Holder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView title;
    TextView distance;
    TextView description;
    ImageView imageView;

    View_Find_Route_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        distance = (TextView) itemView.findViewById(R.id.distance);
        description = (TextView) itemView.findViewById(R.id.description);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
