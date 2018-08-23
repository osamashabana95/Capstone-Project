package com.example.osama.spot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.osama.spot.R;
import com.example.osama.spot.fragments.HomeFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} makes a call to the
 * specified {@link OnListFragmentInteractionListener}.

 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {


    private final OnListFragmentInteractionListener mListener;
    private  String[] names;
    private String[] titles;
    private String[] thumbnails;
    private String[] detailsUrls;
    Context mContext;
    public MyItemRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener) {
        mListener = listener;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTitleView.setText(titles[position]);
        holder.mNameView.setText(names[position]);
        StringBuilder imageURL = new StringBuilder();
        imageURL.append(thumbnails[position]);
        if (thumbnails==null|| TextUtils.isEmpty(thumbnails[position])) {
            Picasso
                    .with(mContext)
                    .cancelRequest(holder.mThumbnailView)
                       ;

            holder.mThumbnailView.setImageResource(R.mipmap.ic_spot);

        }
        else {
            holder.mThumbnailView.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(imageURL.toString())
                    .into(holder.mThumbnailView);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(position,names[position],detailsUrls[position]);
                    Log.v(mContext.toString(),"45484"+position);
                }
            }
        });
    }
    public  void  setData (String[][] data){
        int len = (data.length);
        String[] list0=new String[len];
        String[] list1=new String[len];
        String[] list2=new String[len];
        String[] list3=new String[len];
      // Log.v(mContext.toString(), String.valueOf(data[0].length)+String.valueOf(data[1].length)+data.length);
      //  Log.v(mContext.toString(), data[0][0]+" "+data[0][1]+" "+data[0][2]);

        for(int i = 0; i<data.length;i++) {
            for (int j = 0; j < 4 ;  j++) {
                if (j == 0) {
                    list0[i] = data[i][j];
                }
                else if (j == 1) {
                    list1[i] = data[i][j];
                }
                else if (j == 2) {
                    list2[i] = data[i][j];
                }
                else if (j == 3) {
                    list3[i] = data[i][j];
                }

            }
        }


        names=list0;
        titles=list1;
        thumbnails=list2;
        detailsUrls=list3;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == names) {
            return 0;
        }
        return names.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public final View mView;
        @BindView(R.id.text_title) TextView mTitleView;
        @BindView(R.id.text_name) TextView  mNameView ;
        @BindView(R.id.thumbnail_view)  de.hdodenhof.circleimageview.CircleImageView mThumbnailView;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView=view;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
