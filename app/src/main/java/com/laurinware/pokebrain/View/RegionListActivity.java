package com.laurinware.pokebrain.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laurinware.pokebrain.Model.PokemonList;
import com.laurinware.pokebrain.Model.RegionItem;
import com.laurinware.pokebrain.Model.RegionList;
import com.laurinware.pokebrain.R;
import com.laurinware.pokebrain.ViewModel.PokemonListViewModel;
import com.laurinware.pokebrain.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of Regions. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RegionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RegionListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private RegionListActivity act = this;

    PokemonListViewModel pokemonListViewModel;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.region_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recyclerView = (RecyclerView) findViewById(R.id.region_list);


        pokemonListViewModel = ViewModelProviders.of(this).get(PokemonListViewModel.class);
        pokemonListViewModel.getAllRegions().observe(this, new Observer<RegionList>() {
            @Override
            public void onChanged(@Nullable RegionList regionNamesList) {
                //Log.d("ONCHANGED",pokemonNamesList.getResults().get(0).getName());
                recyclerView.setAdapter(new RegionListActivity.SimpleItemRecyclerViewAdapter(act, regionNamesList.getResults(), mTwoPane));
            }
        });
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RegionListActivity mParentActivity;
        private final List<RegionItem> mValues;
        private final boolean mTwoPane;
        // TODO if regions list is expanded to detail
        /*private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RegionDetailFragment.ARG_ITEM_ID, item.id);
                    RegionDetailFragment fragment = new RegionDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.region_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RegionDetailActivity.class);
                    intent.putExtra(RegionDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };*/

        SimpleItemRecyclerViewAdapter(RegionListActivity parent,
                                      List<RegionItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.region_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            String[] separated = mValues.get(position).getUrl().split("/");
            holder.mIdView.setText(separated[separated.length-1]);
            String name = mValues.get(position).getName();
            String upName = name.substring(0,1).toUpperCase() + name.substring(1);
            holder.mContentView.setText(upName);

            holder.itemView.setTag(mValues.get(position));
            // TODO if regions list is expanded to detail
            // holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
