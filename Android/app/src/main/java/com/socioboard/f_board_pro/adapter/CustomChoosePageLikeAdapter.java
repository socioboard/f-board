package com.socioboard.f_board_pro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.fragments.AutoLiker;
import com.socioboard.f_board_pro.models.ChoosePageLikeModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by GLB-122 on 5/8/2017.
 */

public class CustomChoosePageLikeAdapter extends BaseAdapter
{
    //private ChoosePage context;
    AutoLiker context;
    //SelectAccountAdapter context;
    private LayoutInflater inflater;
    private List<ChoosePageLikeModel> likeItems;

    public CustomChoosePageLikeAdapter(AutoLiker activity, List<ChoosePageLikeModel> movieItems)
    {
        this.context = activity;
        this.likeItems = movieItems;
    }

    @Override
    public int getCount() {
        return likeItems.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        if (inflater == null)
           // inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.choosepage_list_items, null);


        final ChoosePageLikeModel choosePageLikeModel = likeItems.get(position);

        ImageView imageView = (ImageView)convertView.findViewById(R.id.pageImage);
        Picasso.with(parent.getContext()).load("https://graph.facebook.com/"+choosePageLikeModel.getId()+"/picture?type=large").into(imageView);


        TextView name = (TextView) convertView.findViewById(R.id.pageNAme);
        name.setText(choosePageLikeModel.getName());



        TextView category = (TextView) convertView.findViewById(R.id.pagecategory);
        category.setText(choosePageLikeModel.getCategory());


//        TextView id = (TextView) convertView.findViewById(R.id.id);
//        id.setText(choosePageLikeModel.getId());



        //TextView createtime = (TextView) convertView.findViewById(R.id.createtime);
        TextView accesstoken = (TextView)convertView.findViewById(R.id.accessToken);
        accesstoken.setText(choosePageLikeModel.getAccess_token());



        // getting movie data for the row



//        System.out.println("Name   "+choosePageLikeModel.getName());
//        System.out.println("Category   "+choosePageLikeModel.getCategory());
//        System.out.println("ID   "+choosePageLikeModel.getId());
//        System.out.println("AccessToken==="+choosePageLikeModel.getAccess_token());











        return convertView;
    }
}
