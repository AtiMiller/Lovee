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
import com.example.lovee.models.ModelFavUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {

    List<ModelFavUser> favUserList;
    Context mContext;

    public FavAdapter(List<ModelFavUser> favUserList, Context mContext) {
        this.favUserList = favUserList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_grid, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



    String userName = favUserList.get(position).getUserName();
    String userProfile = favUserList.get(position).getProfilePicture();
    String userAddress = favUserList.get(position).getAddress();

    holder.name.setText(userName);
    holder.address.setText(userAddress);

    Picasso.get().load(userProfile).placeholder(R.drawable.star_icon_yellow).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return favUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);

            address = (TextView) itemView.findViewById(R.id.location);
            name = (TextView) itemView.findViewById(R.id.userName);
            image = (ImageView) itemView.findViewById(R.id.gridProfile);


        }
    }


}
