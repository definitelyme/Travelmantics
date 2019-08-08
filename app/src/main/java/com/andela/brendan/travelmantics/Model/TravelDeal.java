package com.andela.brendan.travelmantics.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class TravelDeal implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String price;
    private String imageStringUri;
    public static final Creator<TravelDeal> CREATOR = new Creator<TravelDeal>() {
        @Override
        public TravelDeal createFromParcel(Parcel in) {
            return new TravelDeal(in);
        }

        @Override
        public TravelDeal[] newArray(int size) {
            return new TravelDeal[size];
        }
    };

    public TravelDeal() {
    }

    private String imageName;

    public TravelDeal(String title, String price, String description, String stringUri, String imageName) {
        this.setId(id);
        this.setTitle(title);
        this.setPrice(price);
        this.setDescription(description);
        this.setImageStringUri(stringUri);
        this.setImageName(imageName);
    }

    // Parcelable Methods
    protected TravelDeal(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        price = in.readString();
        imageStringUri = in.readString();
        imageName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(imageStringUri);
        dest.writeString(imageName);
    }

    // Getter & Setter methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageStringUri() {
        return imageStringUri;
    }

    public void setImageStringUri(String imageStringUri) {
        this.imageStringUri = imageStringUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
