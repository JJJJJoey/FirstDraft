package gr.cityu.firstdraft;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageAdapter extends FirebaseRecyclerAdapter<ItemImageUpload,ImageAdapter.myViewHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ImageAdapter(@NonNull FirebaseRecyclerOptions<ItemImageUpload> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ItemImageUpload model) {
        //assigning values to each of the row in the recycler_view_row layout file
        holder.mItemName.setText(model.getmName());
        holder.mItemCategory.setText(model.getmImageCategory());


        Glide.with(holder.mItemPhoto.getContext())
                .load(model.getmImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .circleCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.mItemPhoto);

    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView mItemPhoto;
        TextView mItemName,mItemCategory;

        public myViewHolder(@NonNull View itemView) {

            //taking the view form the recycler_view_row file similar to onCreate method
            super(itemView);

            mItemPhoto=itemView.findViewById(R.id.image1);
            mItemName=itemView.findViewById(R.id.TextViewItemName);
            mItemCategory=itemView.findViewById(R.id.TextViewItemCategory);
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout and giving the look to each of the rows
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row,parent,false);

                return new myViewHolder(view);
    }



}
