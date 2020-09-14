package com.TUBEDELIVERIES.RoomDatabase.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class TestListEntity implements Serializable {

/*  "id": "1",
    "description": "This is a concept-building practice test and may not have exact structure as you would expect in the actual exam. ",
    "series_name": "Mathematics SSC CGL",
    "num_of_ques": "5",
    "test_name": "Test 1",
    "time": "1800000",
    "admin_status": "1",
    "created_at": "2019-01-25 06:49:36",
    "updated_at": "0000-00-00 00:00:00"*/


//
//{"status":"SUCCESS","teststart":
//    {"Test_description":" would expect in the actual exam. ",
//            "name":"Test 1","question":"Successive discount of 10% , 20% and 50% will be equivalent to a single discount of-",
//            "option":[{"option":"34%","option_key":"option_a"},
//        {"option":"64%","option_key":"option_b"},
//        {"option":"80%","option_key":"option_c"},
//        {"option":"56%","option_key":"option_d"}],
//        "priorites":1,"finish_flag":"0","start_flag":"1","question_id":"1","ans_record":"","answer":""},
//        "message":"Test Series data retrieved successfully","requestKey":"api\/teststart"}
//





    @SerializedName("id")
    @PrimaryKey
    @NonNull
    private String id;




    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    @SerializedName("record")
    private String record;

    @ColumnInfo(name = "series_name")
    @SerializedName("series_name")
    private String seriesName;

    @ColumnInfo(name = "num_of_ques")
    @SerializedName("num_of_ques")
    private String numOfQues;

    @ColumnInfo(name = "test_name")
    @SerializedName("test_name")
    private String testName;

    @ColumnInfo(name = "time")
    @SerializedName("time")
    private String time;

    @ColumnInfo(name = "admin_status")
    @SerializedName("admin_status")
    private String adminStatus;

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private String createdAt;

    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private String updatedAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getNumOfQues() {
        return numOfQues;
    }

    public void setNumOfQues(String numOfQues) {
        this.numOfQues = numOfQues;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
