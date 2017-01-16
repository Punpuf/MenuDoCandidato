package com.punpuf.uem_menudocandidato.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.punpuf.uem_menudocandidato.R;
import com.punpuf.uem_menudocandidato.data.Contract;
import com.punpuf.uem_menudocandidato.model.Miscellaneous;

import uk.co.senab.photoview.PhotoViewAttacher;

public class EssayPageFragment extends Fragment {

    private int POSITION;

    public EssayPageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_essay_page, container, false);
        final ImageView essayImage = (ImageView) viewGroup.findViewById(R.id.essay_fragment_iv);

        //setting the image
        Miscellaneous miscellaneous = new Miscellaneous(
                getContext().getContentResolver().query(
                        Contract.MiscellaneousEntry.CONTENT_URI,
                        Contract.MiscellaneousEntry.PROJECTION,
                        null,
                        null,
                        null,
                        null
                )
        );
        String url = "http://www.vestibular.uem.br/" + miscellaneous.miscellaneousCdEvent + "/" + POSITION + miscellaneous.miscellaneousCdComposing + ".jpg";

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                essayImage.setImageBitmap(response);
                new PhotoViewAttacher(essayImage);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                essayImage.setImageResource(R.drawable.ic_error_white);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(imageRequest);

        return viewGroup;
    }

    void setPosition(int position){ POSITION = position;}

}
