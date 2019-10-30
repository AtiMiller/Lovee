package com.example.lovee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovee.R;
import com.example.lovee.models.ModelAllUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.mViewHolder> {

    private List<ModelAllUser> usersList;
    private Context mContext;


    public CardStackAdapter(List<ModelAllUser> usersList, Context mContext) {
        this.usersList = usersList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_image, parent, false);
        CardStackAdapter.mViewHolder vh = new CardStackAdapter.mViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {

        String userName = usersList.get(position).getUserName();
        String userProfile = usersList.get(position).getProfilePicture();
        String userAddress = usersList.get(position).getAddress();
        String userAge = usersList.get(position).getAge();

        holder.dName.setText(userName);
        holder.dLocation.setText(userAddress);
        holder.dAge.setText(userAge);

        Picasso.get().load(userProfile).placeholder(R.drawable.star_icon_yellow).into(holder.dImage);

    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {

        ImageView dImage;
        TextView dName;
        TextView dLocation;
        TextView dAge;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);

            dImage = (ImageView) itemView.findViewById(R.id.ivUserProfile);
            dName = (TextView) itemView.findViewById(R.id.tvUserName);
            dLocation = (TextView) itemView.findViewById(R.id.tvUserLocation);
            dAge = (TextView) itemView.findViewById(R.id.tvUserAge);

        }
    }
}
