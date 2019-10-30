package com.example.lovee.models;

public class ModelAllUser {

    String address, age, dateOfBirth, description, email, gender, interestedIn, lat, lon,
            lookingFor, phoneNumber, profilePicture, registrationTime, userName, uid;

    public ModelAllUser(){
    }

    public ModelAllUser(String address, String age, String dateOfBirth, String description, String email, String gender, String interestedIn, String lat, String lon, String lookingFor, String phoneNumber, String profilePicture, String registrationTime, String userName, String uid) {
        this.address = address;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.description = description;
        this.email = email;
        this.gender = gender;
        this.interestedIn = interestedIn;
        this.lat = lat;
        this.lon = lon;
        this.lookingFor = lookingFor;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.registrationTime = registrationTime;
        this.userName = userName;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(String interestedIn) {
        this.interestedIn = interestedIn;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
