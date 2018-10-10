package am.gsoft.carservice.ui.fragment;

import am.gsoft.carservice.R;
import am.gsoft.carservice.app.App;
import am.gsoft.carservice.data.AppRepository;
import am.gsoft.carservice.data.InjectorUtils;
import am.gsoft.carservice.data.database.entity.Oil;
import am.gsoft.carservice.data.database.entity.User;
import am.gsoft.carservice.data.viewmodel.MainActivityViewModel;
import am.gsoft.carservice.ui.adapter.UsersListAdapter;
import am.gsoft.carservice.ui.adapter.UsersListAdapter.OnUsersListItemClickListener;
import am.gsoft.carservice.util.Constant.Argument;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UsersListFragment extends BaseFragment implements View.OnClickListener,OnUsersListItemClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    public static final String TAG = UsersListAdapter.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private Bundle mArgumentData;
    private NestedScrollView nestedScrollView;
    private RecyclerView mRecyclerView;
    private UsersListAdapter mAdapter;
    private LinearLayoutManager layoutManager;;
    private ArrayList<Oil> oils;
    private ArrayList<User> mUserList;
    private AppRepository mAppRepository;
    private MainActivityViewModel mViewModel;

    public boolean ascending = true;
    private OnUsersListFragmentInteractionListener mListener;

    public static UsersListFragment newInstance() {
        return new UsersListFragment();
    }

//    public static UsersListFragment newInstance(ArrayList<User> oils) {
//        Bundle args = new Bundle();
//        args.putParcelableArrayList(Constant.Argument.ARGUMENT_OIL_HISTORY_LIST, oils);
//        UsersListFragment fragment = new UsersListFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);
        findViews(view);
        initFields();
        setListeners();
        getData();
        initRecyclerView();
        customizeActionBar();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//    ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getActivity().getApplicationContext());
//    mViewModel = ViewModelProviders.of(getActivity(), factory).getProductById(MainActivityViewModel.class);
        mViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    setList(users);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setList(List<User> data) {
        mAdapter.setUserList(data);
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================


    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
    }

    private void findViews(View view) {
        nestedScrollView = view.findViewById(R.id.nscw);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.oil_history_recycler_view);
    }

    private void initFields() {
        mAppRepository = InjectorUtils.provideRepository(App.getInstance().getApplicationContext());
        mUserList=new ArrayList<>();
    }

    private void initRecyclerView() {
        // Set up the recycler view
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        Collections.reverse(oils);
//        Collections.sort(oils, new Comparator<Oil>() {
//          @Override
//          public int compare(Oil o1, Oil o2) {
//            return Long.compare(o2.getServiceDoneDate(),o1.getServiceDoneDate());
//          }
//        });
        mAdapter = new UsersListAdapter(this);
//        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void getData() {
        if (getArguments() != null) {
            oils = getArguments().getParcelableArrayList(Argument.ARGUMENT_OIL_HISTORY_LIST);
        }

    }

    private void customizeActionBar() {

    }

    public void sortData(boolean asc) {

        //SORT ARRAY ASCENDING AND DESCENDING
        if (asc) {
            Collections.sort(oils, new Comparator<Oil>() {
                @Override
                public int compare(Oil o1, Oil o2) {
                    return Long.compare(o1.getServiceDoneDate(), o1.getServiceDoneDate());
                }
            });
//            mAdapter.setCounterNumber(1);
        } else {
            Collections.reverse(oils);
//            mAdapter.setCounterNumber(oils.size());

        }
        focusOnView(nestedScrollView,mRecyclerView);
        mAdapter.notifyDataSetChanged();
        runLayoutAnimation(mRecyclerView, R.anim.layout_animation_from_bottom);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView, final int item) {
        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller =
            AnimationUtils.loadLayoutAnimation(context, item);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    protected final void focusOnView(NestedScrollView ns, View v){
        ns.post(new Runnable() {
            @Override
            public void run() {
                ns.scrollTo(0, v.getTop());
            }
        });
    }

    @Override
    public void onUserListItemClick(User user) {
        mListener.onUsersListItemClickListener(user);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUsersListFragmentInteractionListener) {
            mListener = (OnUsersListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnUsersListFragmentInteractionListener");
        }
    }

    public interface OnUsersListFragmentInteractionListener {

        void onUsersListItemClickListener(User user);

    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}