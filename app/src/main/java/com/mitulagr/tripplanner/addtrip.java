package com.mitulagr.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class addtrip extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

  private ImageView[] imv = new ImageView[6];
  private Button cancel, create, dep, ret, hom, des;
  private EditText placename;
  private AdView mAdView;
  private boolean isDep, touchDes=false;
  private int[] depret = new int[6];
  private int selectedImage = 0;
  private Dialog curd;
  Trip trip;
  DBHandler db;
  int id;
  boolean isNew;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_addtrip);

    setupUI(findViewById(R.id.addtripparent));

    mAdView = findViewById(R.id.adView);
    MobileAds.initialize(this, new OnInitializationCompleteListener() {
      @Override
      public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
      }
    });

    placename = (EditText) findViewById(R.id.placename);
    create = findViewById(R.id.create_trip);

    db = new DBHandler(this);

    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    id = sp.getInt("Current Trip", -1);
    if(id>=0){
      trip = db.getTrip(id);
      create.setText("Modify Trip");
      isNew = false;
      placename.setText(trip.place);
    }
    else isNew = true;

    /*
    =============================================================================
    Cancel
    =============================================================================
     */

    cancel = (Button) findViewById(R.id.cancel);

    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(addtrip.this, MainActivity.class));
        finish();
      }
    });


    /*
    =============================================================================
    Image Selector
    =============================================================================
     */

    imv[0] = (ImageView) findViewById(R.id.imageView);
    imv[1] = (ImageView) findViewById(R.id.imageView3);
    imv[2] = (ImageView) findViewById(R.id.imageView4);
    imv[3] = (ImageView) findViewById(R.id.imageView5);
    imv[4] = (ImageView) findViewById(R.id.imageView6);
    imv[5] = (ImageView) findViewById(R.id.imageView7);

    int tI = 0;
    if(!isNew){
      if(trip.imageId == R.drawable.t2) tI = 1;
      if(trip.imageId == R.drawable.t3) tI = 2;
      if(trip.imageId == R.drawable.t4) tI = 3;
      if(trip.imageId == R.drawable.t5) tI = 4;
      if(trip.imageId == R.drawable.t6) tI = 5;
    }

    Drawable[] layers = new Drawable[2];
    layers[0] = imv[tI].getDrawable();
    layers[1] = getDrawable(R.drawable.check2);
    LayerDrawable layerDrawable = new LayerDrawable(layers);
    imv[tI].setImageDrawable(layerDrawable);
    selectedImage = tI;

    for (int i = 0; i < 6; i++) {

      final int I = i;

      imv[i].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (selectedImage == I) return;
          imv[selectedImage].setImageResource(getImg());
          selectedImage = I;
          Drawable[] layers = new Drawable[2];
          layers[0] = imv[I].getDrawable();
          layers[1] = getDrawable(R.drawable.check2);
          LayerDrawable layerDrawable = new LayerDrawable(layers);
          imv[I].setImageDrawable(layerDrawable);
        }
      });

    }


    /*
    =============================================================================
    Date
    =============================================================================
     */

    depret[0] = 0;
    depret[1] = 0;

    dep = (Button) findViewById(R.id.depdate);
    ret = (Button) findViewById(R.id.retdate);

    if(!isNew){
      if(trip.depDate.length()>1){
        dep.setText(trip.depDate);
        depret[0] = Integer.parseInt(trip.depDate.substring(0,2));
        depret[2] = Integer.parseInt(trip.depDate.substring(3,5));
        depret[4] = Integer.parseInt(trip.depDate.substring(6));
      }
      if(trip.retDate.length()>1){
        ret.setText(trip.retDate);
        depret[1] = Integer.parseInt(trip.retDate.substring(0,2));
        depret[3] = Integer.parseInt(trip.retDate.substring(3,5));
        depret[5] = Integer.parseInt(trip.retDate.substring(6));
      }
    }

    dep.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        isDep = true;
        if(depret[0]==0 && depret[1]!=0) showDatePicker(depret[1],depret[3],depret[5]);
        else showDatePicker(depret[0],depret[2],depret[4]);
        dep.setError(null);
      }
    });

    ret.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        isDep = false;
        if(depret[1]==0 && depret[0]!=0) showDatePicker(depret[0],depret[2],depret[4]);
        else showDatePicker(depret[1],depret[3],depret[5]);
        ret.setError(null);
      }
    });


    /*
    =============================================================================
    Currency
    =============================================================================
     */


    hom = (Button) findViewById(R.id.homcur);
    des = (Button) findViewById(R.id.descur);

    if(!isNew){
      hom.setText(trip.Hcur);
      des.setText(trip.Dcur);
    }
    else{
      String temp = sp.getString("Home Currency", "");
      if(temp.length()>1){
        hom.setText(temp);
        des.setText(temp);
      }
    }

    hom.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showCurrency(true);
      }
    });

    des.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        touchDes = true;
        showCurrency(false);
      }
    });


    /*
    =============================================================================
    Create Trip
    =============================================================================
     */

    create.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (isPlaceEmpty()) {
          placename.requestFocus();
          placename.setError("Destination Name Mandatory");
          return;
        }
        if (isReturnWithoutDep()) {
          dep.setFocusableInTouchMode(true);
          dep.requestFocus();
          dep.setError("Enter Departure");
          dep.setFocusableInTouchMode(false);
          return;
        }
        if (!isReturnValid()) {
          ret.setFocusableInTouchMode(true);
          ret.requestFocus();
          ret.setError("Return can't be before Departure");
          ret.setFocusableInTouchMode(false);
          return;
        }

        create.setText("Please Wait..");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Home Currency", hom.getText().toString());
        editor.commit();

        if(isNew){
          trip = new Trip(db.getTripsNewId(),placename.getText().toString(),getImg());
          trip.depDate = dep.getText().toString();
          trip.retDate = ret.getText().toString();
        }
        else {
          trip.depDate = dep.getText().toString();
          trip.retDate = ret.getText().toString();
          trip.place = placename.getText().toString();
          trip.imageId = getImg();
          List<Day> DList = db.getAllDays(trip.srno);
          Day Dy;
          for(int i=0;i<DList.size();i++){
            Dy = DList.get(i);
            Dy.date = getNextDate(trip.depDate,i);
            Dy.day = getCurDay(Dy.date);
            db.updateDay(Dy);
          }
        }
        if(trip.depDate.length()>1 && trip.retDate.length()>1) trip.nights = getNights(trip.depDate,trip.retDate);
        trip.Hcur = hom.getText().toString();
        trip.Dcur = des.getText().toString();
        editor.putInt("Current Trip", trip.srno);
        editor.commit();
        if(!trip.Hcur.equals(trip.Dcur)) {
          trip.isHom = 0;
          trip.rate = savedRate(trip.Dcur.substring(0,3),trip.Hcur.substring(0,3));
          if(isNew) db.addTrip(trip);
          else db.updateTrip(trip);
          startActivity(new Intent(addtrip.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
          Exchange(trip.Dcur.substring(0,3).toLowerCase(),trip.Hcur.substring(0,3).toLowerCase(),view.getContext());
        }
        else {
          if(isNew) db.addTrip(trip);
          else db.updateTrip(trip);
          startActivity(new Intent(addtrip.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
          finish();
        }
      }
    });

  }

  public static String getNextDate(String curDate, int inc) {
    if(curDate.length()<1) return "";
    final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    final Date date;
    try {
      date = format.parse(curDate);
      final Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(Calendar.DAY_OF_YEAR, inc);
      return format.format(calendar.getTime());
    } catch (java.text.ParseException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getCurDay(String curDate) {
    if(curDate.length()<1) return "";
    final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    final Date date;
    try {
      date = format.parse(curDate);
      final Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
      if(dayOfWeek==Calendar.SUNDAY) return "Sunday";
      if(dayOfWeek==Calendar.MONDAY) return "Monday";
      if(dayOfWeek==Calendar.TUESDAY) return "Tuesday";
      if(dayOfWeek==Calendar.WEDNESDAY) return "Wednesday";
      if(dayOfWeek==Calendar.THURSDAY) return "Thursday";
      if(dayOfWeek==Calendar.FRIDAY) return "Friday";
      return "Saturday";
    } catch (java.text.ParseException e) {
      e.printStackTrace();
    }
    return "";
  }

  private void showDatePicker(int day, int month, int year) {
    if(day==0){
      day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
      month = Calendar.getInstance().get(Calendar.MONTH);
      year = Calendar.getInstance().get(Calendar.YEAR);
    }
    else month = month-1;
    DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            this,
            year,
            month,
            day
    );
    datePickerDialog.show();
  }

  @Override
  public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
    String date = String.format("%02d/%02d/%04d", i2,i1+1,i);
    if (isDep) {
      dep.setText(date);
      depret[0] = i2;
      depret[2] = i1 + 1;
      depret[4] = i;
    } else {
      ret.setText(date);
      depret[1] = i2;
      depret[3] = i1 + 1;
      depret[5] = i;
    }
  }

  private int getNights(String d1, String d2){
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try {
      LocalDate date1 = LocalDate.parse(d1, dtf);
      LocalDate date2 = LocalDate.parse(d2, dtf);
      long daysBetween = Duration.between(date1.atStartOfDay(), date2.atStartOfDay()).toDays();
      return (int) daysBetween;
    }
    catch (ParseException e) {}
    return 0;
  }

  private boolean isPlaceEmpty() {
    if (placename.getText().toString().length() == 0) return true;
    return false;
  }

  private boolean isReturnWithoutDep() {
    return (depret[1] != 0 && depret[0] == 0);
  }

  private boolean isReturnValid() {
    if (depret[1] == 0) return true;
    if (depret[5] > depret[4]) return true;
    if (depret[5] < depret[4]) return false;
    if (depret[3] > depret[2]) return true;
    if (depret[3] < depret[2]) return false;
    if (depret[1] >= depret[0]) return true;
    return false;
  }

  private void showErrorDialog(String message) {
    new AlertDialog.Builder(this)
            .setTitle("Oops")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
  }


  public static void hideSoftKeyboard(Activity activity) {
    InputMethodManager inputMethodManager =
            (InputMethodManager) activity.getSystemService(
                    Activity.INPUT_METHOD_SERVICE);
    if (inputMethodManager.isAcceptingText()) {
      inputMethodManager.hideSoftInputFromWindow(
              activity.getCurrentFocus().getWindowToken(),
              0
      );
    }
  }

  public void setupUI(View view) {

    // Set up touch listener for non-text box views to hide keyboard.
    if (!(view instanceof EditText)) {
      view.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
          hideSoftKeyboard(addtrip.this);
          return false;
        }
      });
    }

    //If a layout container, iterate over children and seed recursion.
    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        View innerView = ((ViewGroup) view).getChildAt(i);
        setupUI(innerView);
      }
    }
  }

  float savedRate(String c1, String c2){
    String [] cod = getResources().getStringArray(R.array.code);
    int i1=-1,i2=-1;
    for(int i=0;i<cod.length;i++){
      if(c1.equals(cod[i])) i1=i;
      if(c2.equals(cod[i])) i2=i;
      if(i1>=0 && i2>=0) break;
    }
    String [] cor = getResources().getStringArray(R.array.rate);
    return Float.parseFloat(cor[i2])/Float.parseFloat(cor[i1]);
  }

  void onExchange(float rate){
    if(rate>0.0f) trip.rate = rate;
    db.updateTrip(trip);
    finish();
  }

  void Exchange(String c1, String c2, Context context){

    String url = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/"+c1+"/"+c2+".json";

    StringRequest request = new StringRequest(url, new Response.Listener<String>() {
      @Override
      public void onResponse(String string) {
        //rate[0] = parseJsonData(string,c2);
        parseJsonData(string,c2);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {

        String url2 = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/"+c1+"/"+c2+".min.json";

        StringRequest request2 = new StringRequest(url2, new Response.Listener<String>() {
          @Override
          public void onResponse(String string) {
            //rate[0] = parseJsonData(string,c2);
            parseJsonData(string,c2);
          }
        }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError volleyError) {

          }
        });

        RequestQueue rQueue2 = Volley.newRequestQueue(context);
        rQueue2.add(request2);

      }
    });

    RequestQueue rQueue = Volley.newRequestQueue(context);
    rQueue.add(request);

    //return rate[0];

  }

  void parseJsonData(String jsonString, String c) {
    float rate = 0.0f;
    try {
      JSONObject object = new JSONObject(jsonString);
      rate = Float.parseFloat(object.getString(c));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    onExchange(rate);
  }

  void showCurrency(Boolean isHome){
    curd = new Dialog(addtrip.this);
    curd.setContentView(R.layout.currency);
    curd.getWindow();
    curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    curd.show();

    TextView CurText = curd.findViewById(R.id.CurText);
    EditText CurSearch = curd.findViewById(R.id.CurSearch);
    RecyclerView CurShow = curd.findViewById(R.id.CurShow);

    if(isHome) CurText.setText("Home Currency");
    else CurText.setText("Destination Currency");


    CurShow.setLayoutManager(new LinearLayoutManager(addtrip.this));

    Adapter_Currency adc = new Adapter_Currency(
            getResources().getStringArray(R.array.code),
            getResources().getStringArray(R.array.symbol),
            getResources().getStringArray(R.array.country));

    CurShow.setAdapter(adc);

    CurSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adc.filter(charSequence.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    CurShow.addOnItemTouchListener(new RecyclerTouchListener(addtrip.this, CurShow, new RecyclerTouchListener.ClickListener() {
      @Override
      public void onClick(View view, int position) {
        if(isHome) {
          hom.setText(adc.getItem(position));
        }
        if(!(isHome && touchDes)){
          des.setText(adc.getItem(position));
        }
        curd.dismiss();
      }

      @Override
      public void onLongClick(View view, int position) {

      }
    }));

  }

  int getImg(){
    if (selectedImage == 0) return R.drawable.t1;
    if (selectedImage == 1) return R.drawable.t2;
    if (selectedImage == 2) return R.drawable.t3;
    if (selectedImage == 3) return R.drawable.t4;
    if (selectedImage == 4) return R.drawable.t5;
    return R.drawable.t6;
  }

}
