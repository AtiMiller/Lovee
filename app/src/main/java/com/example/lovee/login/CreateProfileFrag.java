package com.example.lovee.login;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovee.LogInActivity;
import com.example.lovee.MainActivity;
import com.example.lovee.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateProfileFrag extends Fragment implements View.OnClickListener {

    //View Declarations
    private FloatingActionButton addProfileFloating;
    private ImageView addDateOfBirth, addLocation, addDescription, addLookingFor, addInterestedIn;
    private Button btnSubmit;
    private TextView tvDateOfBirth, tvLocation, tvDescription, tvLookingFor, tvInterestedIn;
    private String storagePath = "Users_Profile/";
    private String profilePic;
    private CircleImageView mProfile_image;
    private Spinner genderSpinner;
    private String phoneNumber, userName, userEmail;
    private Bundle bundle;

    //Calendar declarations
    private Calendar cal, calendar;
    private DatePickerDialog dpd;
    private int pickedMonth, pickedYear, pickedDay, age;

    //Location declarations
    private PlacesClient placesClient;
    private int AUTOCOMPLETE_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private Location location;
    private double longitude, latitude;
    private String city, country;
    private static final int REQUEST_LOCATION = 100;

    //Firabase Declaretions
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private Query mQuery;
    private StorageReference mStorageRef;

    //Camera category number declarations.
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 600;
    private static final int IMAGE_PICK_CAMERA_CODE = 800;

    //Arrays of permissions to be requested
    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Context applicationContext;
    //The URI what will be given from the photo
    private Uri image_uri;

    public CreateProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_profile, container, false);

        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        //Firebase Init
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Calendar Init
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2003);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1980);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        //FindViewByIds
        mProfile_image = view.findViewById(R.id.profile_image);
        addProfileFloating = view.findViewById(R.id.addProfileFloating);
        addDateOfBirth = view.findViewById(R.id.addDateOfBirth);
        addLocation = view.findViewById(R.id.addLocation);
        addDescription = view.findViewById(R.id.addDescription);
        addLookingFor = view.findViewById(R.id.addLookingFor);
        addInterestedIn = view.findViewById(R.id.addInterestedIn);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        genderSpinner = view.findViewById(R.id.genderSpinner);
        tvDateOfBirth = view.findViewById(R.id.tvDateOfBirth);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvLookingFor = view.findViewById(R.id.tvLookingFor);
        tvInterestedIn = view.findViewById(R.id.tvInterestedIn);

        //Spinner
        genderSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, GenderData.genders));

        //Camera and Storage permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //SetOnClickListeners
        addProfileFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePic = "profilePicture";
                showImagePicDialog();
            }
        });

        addDateOfBirth.setOnClickListener(this);
        addLocation.setOnClickListener(this);
        addDescription.setOnClickListener(this);
        addLookingFor.setOnClickListener(this);
        addInterestedIn.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        requestPermission();

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.addDateOfBirth:

                tvDateOfBirth.setText("");

                int day = cal.get(Calendar.DAY_OF_MONTH);
                final int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR );

                dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        pickedMonth = month+1;
                        pickedYear = year;
                        pickedDay = day;

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(0);
                        calendar.set(year, month, day, 0, 0, 0);
                        Date chosenDate = calendar.getTime();


                        // Format the date using style medium and UK locale
                        DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                        String df_medium_uk_str = df_medium_uk.format(chosenDate);
                        // Display the formatted date
                        tvDateOfBirth.setText(tvDateOfBirth.getText() + df_medium_uk_str);

                        getAge();
                    }
                }, day, month, year);
                dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                dpd.getDatePicker().setMinDate(cal.getTimeInMillis());

                dpd.show();
                break;

            case R.id.addLocation:

                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cityName = addresses.get(0).getLocality();
                String countryName = addresses.get(0).getCountryName();

                String mLocation = cityName + "," + countryName;

                tvLocation.setText(mLocation);

                break;

            case R.id.addDescription:

                // create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Describe yourself");

                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.layout_description_dialog, null);
                builder.setView(customLayout);

                // add a button
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send data from the AlertDialog to the Activity
                        EditText editText = customLayout.findViewById(R.id.editText);
                        sendDialogDataToActivity(editText.getText().toString());
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.addLookingFor:

                final ArrayList<String> selectedItems = new ArrayList();

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

                final String[] list = getActivity().getResources().getStringArray(R.array.choiceLookingFor);

                mBuilder.setTitle("Title")

                        .setMultiChoiceItems(list, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {

                                        if (isChecked) {
                                            selectedItems.add(list[which]);

                                        } else if (selectedItems.contains(which)) {
                                            selectedItems.remove(list[which]);
                                        }
                                    }
                                })

                        .setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                StringBuilder stringBuilder = new StringBuilder();
                                for(String str : selectedItems){
                                    stringBuilder.append(str + ", ");
                                }

                                String mStr =stringBuilder.substring(0, stringBuilder.length()-2);

                                tvLookingFor.setText(mStr);
                            }
                        })
                        .setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                mBuilder.create().show();
                break;

            case R.id.addInterestedIn:

                final ArrayList<String> mSelectedItems = new ArrayList();

                final AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());

                final String[] mList = getActivity().getResources().getStringArray(R.array.choiceInterestedIn);

                aBuilder.setTitle("Title")

                        .setMultiChoiceItems(mList, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {

                                        if (isChecked) {
                                            mSelectedItems.add(mList[which]);

                                        } else if (mSelectedItems.contains(which)) {
                                            mSelectedItems.remove(mList[which]);
                                        }
                                    }
                                })

                        .setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                StringBuilder stringBuilder = new StringBuilder();
                                for(String str : mSelectedItems){
                                    stringBuilder.append(str + ", ");
                                }

                                String mStr =stringBuilder.substring(0, stringBuilder.length()-2);

                                tvInterestedIn.setText(mStr);
                            }
                        })
                        .setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                aBuilder.create().show();
                break;

            case R.id.btnSubmit:
                if(genderSpinner.getSelectedItem().equals("Select your gender")){
                    Toast.makeText(getActivity(), "Please select your gender", Toast.LENGTH_SHORT).show();
                }else if (tvDateOfBirth.getText().toString().isEmpty() ||
                        tvLocation.getText().toString().isEmpty() ||
                        tvDescription.getText().toString().isEmpty() ||
                        tvLookingFor.getText().toString().isEmpty() ||
                        tvInterestedIn.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {

                    FirebaseUser user = mAuth.getCurrentUser();

                    //Adding data to the database
                    String gender = genderSpinner.getSelectedItem().toString();
                    String birthday = tvDateOfBirth.getText().toString();
                    String location = tvLocation.getText().toString();
                    String description = tvDescription.getText().toString();
                    String lookingFor = tvLookingFor.getText().toString();
                    String interestedIn = tvInterestedIn.getText().toString();
                    String userAge = String.valueOf(age);
                    String lat = String.valueOf(latitude);
                    String lon = String.valueOf(longitude);
                    String mUid = user.getUid();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("UserInfo/gender", gender);
                    hashMap.put("UserInfo/dateOfBirth", birthday);
                    hashMap.put("UserInfo/address", location);
                    hashMap.put("UserInfo/lookingFor", lookingFor);
                    hashMap.put("UserInfo/interestedIn", interestedIn);
                    hashMap.put("UserInfo/description", description);
                    hashMap.put("UserInfo/age", userAge);
                    hashMap.put("UserInfo/lat", lat);
                    hashMap.put("UserInfo/lon", lon);
                    hashMap.put("UserInfo/uid", mUid);

                    mDatabaseRef = mDatabase.getReference("Users");
                    mDatabaseRef.child(mUid).updateChildren(hashMap);

                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        locationManager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            enableGPS();
        }else {
            getLocation();
        }

    }

    private void enableGPS(){

        final AlertDialog.Builder gBuilder = new AlertDialog.Builder(getActivity());
        gBuilder.setMessage("Please enable GPS").setCancelable(false).setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog = gBuilder.create();
        alertDialog.show();
    }

    private void getLocation(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
         != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else {
            Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetwork = locationManager.getLastKnownLocation((LocationManager.NETWORK_PROVIDER));
            Location locationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if(locationGps != null){
                latitude = locationGps.getLatitude();
                longitude = locationGps.getLongitude();
            } else if(locationNetwork != null){
                latitude = locationNetwork.getLatitude();
                longitude = locationNetwork.getLongitude();
            }else if(locationPassive != null){
                latitude = locationPassive.getLatitude();
                longitude = locationPassive.getLongitude();
            }else {
                Toast.makeText(getActivity(), "Can't get your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {

        tvDescription.setText(data);
    }


    private void showImagePicDialog() {

        //show dialog containing options Camera and Gallery to pick the image
        String[] options = {"Camera", "Gallery"};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Pick Image From");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog items
                if (which == 0) {
                    //camera clicked
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {

//                    gallery clicked
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void getAge() {

        age = 0;

        Calendar now = Calendar.getInstance();

        int year1 = now.get(Calendar.YEAR);
        int year2 = pickedYear;
        age = year1 - year2;
        int month1 = now.get(Calendar.MONTH);
        int month2 = pickedMonth;

        if (month2 > month1) {
            age--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = pickedDay;
            if (day2 > day1) {
                age--;
                }
            }

    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void pickFromCamera() {

        applicationContext = LogInActivity.getContextOfApplication();

        //Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        //put image uri
        image_uri = applicationContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //Intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

        //Adding the image Uri to the ImageView

    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Please enable camera and storage permission.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please enable camera and storage permission.", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean readStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (readStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Please enable storage permission.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please enable storage permission.", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == IMAGE_PICK_CAMERA_CODE){
            if(resultCode == RESULT_OK){
                uploadProfileCoverPhoto(image_uri);
            } else if (resultCode == RESULT_CANCELED) {

            }else {
                Toast.makeText(getActivity(), "Error occurred.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == IMAGE_PICK_GALLERY_CODE){
            if(resultCode == RESULT_OK){
                //image is picked from camera, get uri of image
                image_uri = data.getData();

                //Adding the image Uri to the ImageView
                mProfile_image.setImageURI(image_uri);

                uploadProfileCoverPhoto(image_uri);
            }else if (resultCode == RESULT_CANCELED) {

            }else {
                Toast.makeText(getActivity(), "Error occurred.", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Error occurred.", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {

        //path and name of image to be stored in Firebase Storage
        String filePathAndName = storagePath + "" + profilePic + " " + mUser.getUid();

        StorageReference storageReference = mStorageRef.child(filePathAndName);
        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        if(uriTask.isSuccessful()){

                            HashMap<String, Object> result = new HashMap<>();
                            result.put(profilePic, downloadUri.toString());
                            mDatabaseRef.child(mUser.getUid()).child("UserInfo").updateChildren(result)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Error updating the image!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }else {
                            Toast.makeText(getActivity(), "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
