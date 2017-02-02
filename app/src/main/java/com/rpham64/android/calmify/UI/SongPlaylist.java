//package com.rpham64.android.calmify.ui;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.rpham64.android.calmify.R;
//import com.rpham64.android.calmify.model.Song;
//
//import java.util.List;
//
///**
// * Displays a list of available songs to choose from.
// * User can click on a list entry to play that song.
// *
// * Created by Rudolf on 5/26/2016.
// */
//public class SongPlaylist extends Fragment{
//
//    private static final String TAG = "SongPlaylist";
//
//    private RecyclerView mRecyclerView;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.list_songs_recycler_view, container, false);
//
//        mRecyclerView = (RecyclerView)view.findViewById(R.id.list_of_songs_recycler_view);
//
//        return view;
//    }
//
//    private class SongAdapter extends RecyclerView.Adapter<SongHolder> {
//
//        private List<Song> mSongs;
//
//        @Override
//        public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//
//            View view = layoutInflater.inflate(R.layout.list_song_info, parent, false);
//
//            return new SongHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(SongHolder songHolder, int position) {
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//    }
//
//    private class SongHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        private TextView mTitle;
//        private TextView mArtist;
//
//        public SongHolder(View itemView) {
//            super(itemView);
//
//            mTitle = (TextView) itemView.findViewById(R.id.list_song_title);
////            mArtist = (TextView) itemView.findViewById(R.id.list_song_artist);
//
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            // Play song in mediaplayer
//        }
//    }
//}
