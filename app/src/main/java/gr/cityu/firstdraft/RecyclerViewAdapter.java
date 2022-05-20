package gr.cityu.firstdraft;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends FirebaseRecyclerAdapter<ItemUploadModel, RecyclerViewAdapter.myViewHolder>{

    //variable to hold the interface
    private final RecyclerViewInterface recyclerViewInterface;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    public RecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<ItemUploadModel> options, RecyclerViewInterface recyclerViewInterface) {
        super(options);
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ItemUploadModel model) {

        //assigning values to each of the row in the recycler_view_row layout file
        holder.mItemName.setText(model.getmName());
        //holder.mItemDescription.setText(model.getmImageCategory());

        Glide.with(holder.mItemPhoto.getContext())
                .load(model.getmImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .circleCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.mItemPhoto);


    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView mItemPhoto;
        TextView mItemName,mItemDescription;


        public myViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {

            //taking the view form the recycler_view_row file similar to onCreate method
            super(itemView);

            mItemPhoto=itemView.findViewById(R.id.image1);
            mItemName=itemView.findViewById(R.id.TextViewItemName);
            //mItemDescription=itemView.findViewById(R.id.TextViewItemDesc);


            //====
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            String currentUserID= FirebaseAuth.getInstance().getUid();

            Query query = databaseReference.child("/item info/ "+currentUserID);
            DataSnapshot dataSnapshot;
            //its filling out the array everytime

            ArrayList<String> posKeyList = new ArrayList<>();
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.getKey();
                        posKeyList.add(id);
                        //System.out.println("array: "+posKeyList);
                        //System.out.println("iiiiiiddd is "+id);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            };
            query.addListenerForSingleValueEvent(valueEventListener);





                //attaching onClickListener to the itemView
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null ){
                        int pos = getAbsoluteAdapterPosition();
                        String id = posKeyList.get(pos);

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(id, pos);

                        }
                        System.out.println("iiiiiiddd is "+id);

                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {
        //inflating the layout and giving the look to each of the rows
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row,parent,false);

                return new myViewHolder(view, recyclerViewInterface);
    }
}
