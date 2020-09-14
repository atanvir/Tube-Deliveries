package com.TUBEDELIVERIES.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterOptionModel {
    @SerializedName("options")
    @Expose
    private Options options;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Options {

        @SerializedName("food")
        @Expose
        private Food food;
        @SerializedName("grocery")
        @Expose
        private Grocery grocery;

        public Food getFood() {
            return food;
        }

        public void setFood(Food food) {
            this.food = food;
        }

        public Grocery getGrocery() {
            return grocery;
        }

        public void setGrocery(Grocery grocery) {
            this.grocery = grocery;
        }

    }

    public class Grocery {

        @SerializedName("category")
        @Expose
        private List<Category_> category = null;

        public List<Category_> getCategory() {
            return category;
        }

        public void setCategory(List<Category_> category) {
            this.category = category;
        }

    }


    public class Food {

        @SerializedName("category")
        @Expose
        private List<Category> category = null;
        @SerializedName("cuisine")
        @Expose
        private List<Cuisine> cuisine = null;

        public List<Category> getCategory() {
            return category;
        }

        public void setCategory(List<Category> category) {
            this.category = category;
        }

        public List<Cuisine> getCuisine() {
            return cuisine;
        }

        public void setCuisine(List<Cuisine> cuisine) {
            this.cuisine = cuisine;
        }

    }
    public class Cuisine {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class Category {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public class Category_ {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
