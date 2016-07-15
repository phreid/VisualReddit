package com.preid.visualreddit;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ImageZoomFragment extends Fragment {

    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_zoom, container, false);

        Bundle bundle = getArguments();
        url = bundle.getString("url");

        if (LinkHandler.isImgurVideo(url)) {
            loadVideo(view);
        } else {
            loadImage(view);
        }

        return view;
    }

    private void loadVideo(View view) {
        final VideoView zoomVideoView = (VideoView) view.findViewById(R.id.video_zoom_view);
        ImageView zoomImageView = (ImageView) view.findViewById(R.id.image_zoom_view);

        zoomImageView.setVisibility(View.INVISIBLE);

        zoomVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        zoomVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                zoomVideoView.start();
            }
        });

        zoomVideoView.setVideoPath(url);
        zoomVideoView.start();
    }

    void loadImage(View view) {
        ImageView zoomImageView = (ImageView) view.findViewById(R.id.image_zoom_view);
        VideoView zoomVideoView = (VideoView) view.findViewById(R.id.video_zoom_view);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        zoomVideoView.setVisibility(View.INVISIBLE);

        zoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.loading)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target, boolean isFirstResource) {

                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(zoomImageView);
    }

}
