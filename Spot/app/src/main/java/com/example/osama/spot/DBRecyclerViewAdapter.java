package com.example.osama.spot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.database.Cursor;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DBRecyclerViewAdapter extends RecyclerView.Adapter<DBRecyclerViewAdapter.ViewHolder> {


    Context mContext;
    private Cursor mCursor;
    public DBRecyclerViewAdapter(Context context) {
        mContext=context;
    }

    @Override
    public DBRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new DBRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DBRecyclerViewAdapter.ViewHolder holder, final int position) {

        mCursor.moveToPosition(position);
        int index_name = mCursor.getColumnIndex(PostContract.PostEntry.COLUMN_NAME);
        holder.mNameView.setText(mCursor.getString(index_name));
        int index_title = mCursor.getColumnIndex(PostContract.PostEntry.COLUMN_TITLE);
        holder.mTitleView.setText(mCursor.getString(index_title));
        int index_path = mCursor.getColumnIndex(PostContract.PostEntry.COLUMN_PATH);
        StringBuilder imageURL = new StringBuilder();
        imageURL.append(mCursor.getString(index_path));
        if (mCursor.getString(index_path)==null|| TextUtils.isEmpty(mCursor.getString(index_path))) {
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


    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }
    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_title) TextView mTitleView;
        @BindView(R.id.text_name) TextView  mNameView ;
        @BindView(R.id.thumbnail_view)  de.hdodenhof.circleimageview.CircleImageView mThumbnailView;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
