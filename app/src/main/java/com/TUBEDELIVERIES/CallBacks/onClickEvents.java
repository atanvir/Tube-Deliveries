package com.TUBEDELIVERIES.CallBacks;

import com.TUBEDELIVERIES.Model.RestaurantResponse;

public interface onClickEvents {

    void onLinkClick(int position,String id,String distance);
    void onFavClick(int position, Object response,int homePosition);

//RestaurantResponse
}
