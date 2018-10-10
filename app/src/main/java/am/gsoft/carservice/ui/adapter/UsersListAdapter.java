package am.gsoft.carservice.ui.adapter;

import am.gsoft.carservice.R;
import am.gsoft.carservice.data.database.entity.User;
import am.gsoft.carservice.ui.adapter.UsersListAdapter.UsersListViewHolder;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListViewHolder> {

  private List<User> mUserList;
  @Nullable
  private OnUsersListItemClickListener itemClickListener;
//  private int counterNumber;
//  boolean asc;

  public UsersListAdapter(OnUsersListItemClickListener itemClickListener) {
//    this.mUserList = usersArrayList;
    this.itemClickListener = itemClickListener;
//    counterNumber = mUserList.size();
//    asc=false;
  }

//  public void setGeofencePlaces(List<Oil> places) {
//
//    if (places == null || places.size() == 0) {
//      return;
//    }
//    if (places != null && places.size() > 0) {
//      this.mUserList.clear();
//    }
//    this.mUserList.addAll(places);
//    notifyDataSetChanged();
//
//  }
//
//  public void addGeofencePlace(Oil spinnerItem) {
//    this.mUserList.add(spinnerItem);
//    notifyDataSetChanged();
//  }
//
//  public void deleteGeofencePlace(int placeId) {
//    for (Oil spinnerItem: mUserList) {
//      if (placeId == spinnerItem.getId()) {
//        this.mUserList.remove(spinnerItem);
//        notifyDataSetChanged();
//        return;
//      }
//    }
//  }

  public boolean isEmpty(){
    return mUserList.size()==0;
  }

  @Override
  public UsersListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Get the RecyclerView item layout
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_users_list, parent, false);
    return new UsersListViewHolder(view, itemClickListener);
  }

  @Override
  public void onBindViewHolder(UsersListViewHolder holder, int position) {

    holder.bindData(mUserList.get(position));

//    holder.itemView.setTag(mUserList.getAppNotification(position).getPlaceId());

  }

  public void swapPlaces(ArrayList<User> users) {
    mUserList = users;
    if (mUserList != null) {
      // Force the RecyclerView to refresh
      this.notifyDataSetChanged();
    }
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  @Override
  public int getItemCount() {
    return mUserList == null ? 0 : mUserList.size();
  }

  public void setCounterNumber(int number) {
//    if(number!=1){
//      asc=false;
//    }else {
//      asc=true;
//    }
//    this.counterNumber = number;
  }

  public void setUserList(List<User> users) {
//    if (mUserList != null) {
//      mUserList.clear();
//    }
//    mUserList = users;
//    notifyDataSetChanged();

    if (mUserList == null) {
      mUserList = users;
      notifyItemRangeInserted(0, users.size());
    } else {
      DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
        @Override
        public int getOldListSize() {
          return mUserList.size();
        }

        @Override
        public int getNewListSize() {
          return users.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
          return mUserList.get(oldItemPosition).getKey()
              .equals(users.get(newItemPosition).getKey());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
          User newUser = users.get(newItemPosition);
          User oldUser = mUserList.get(oldItemPosition);
          return newUser.getKey().equals(oldUser.getKey())
              && Objects.equals(newUser.getFirstName(), oldUser.getFirstName())
              && Objects.equals(newUser.getLastName(), oldUser.getLastName())
              && Objects.equals(newUser.getPhoneNumber(), oldUser.getPhoneNumber())
              && Objects.equals(newUser.getMail(), oldUser.getMail());
        }
      });
      mUserList = users;
      result.dispatchUpdatesTo(this);
    }


  }

  /**
   * UsersListViewHolder class for the recycler view item
   */
  public class UsersListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private CardView rootRl;
    private TextView oilBrandItemTv;
    private TextView oilTypeItemTv;
    private TextView oilServiceDoneDateItemTv;
    private TextView oilVolumeItemTv;
    private TextView numberItemTv;

    public User mUser;

    private OnUsersListItemClickListener itemClickListener;


    public UsersListViewHolder(View itemView, OnUsersListItemClickListener itemClickListener) {
      super(itemView);
      this.itemClickListener = itemClickListener;
      findViews(itemView);
    }

    void findViews(View view) {
      rootRl = (CardView) view.findViewById(R.id.item_users_list_card_root);
      rootRl.setOnClickListener(this);
      oilBrandItemTv = (TextView) view.findViewById(R.id.tv_item_oil_brand);
      oilTypeItemTv = (TextView) view.findViewById(R.id.tv_item_oil_type);
      oilServiceDoneDateItemTv = (TextView) view.findViewById(R.id.tv_item_oil_service_done_date);
      oilVolumeItemTv = (TextView) view.findViewById(R.id.tv_item_oil_volume);
      numberItemTv = (TextView) view.findViewById(R.id.tv_item_number);
    }

    public void bindData(User user) {

      this.mUser = user;

      oilBrandItemTv.setText(oilBrandItemTv != null ? String.valueOf(user.getPhoneNumber()) : "");
      oilTypeItemTv.setText(oilTypeItemTv != null ? String.valueOf(user.getFirstName()) : "");

//       if (asc) {
//        numberItemTv.setText(Integer.toString(counterNumber++));
//      } else {
//        numberItemTv.setText(Integer.toString(counterNumber--));
//      }
    }

    private void notifyPlaceItemClicked() {
      if (itemClickListener != null) {
        itemClickListener.onUserListItemClick(mUser);
      }
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.item_users_list_card_root:
          notifyPlaceItemClicked();
          break;
      }
    }

  }

  public interface OnUsersListItemClickListener {

    void onUserListItemClick(User user);

  }

}
