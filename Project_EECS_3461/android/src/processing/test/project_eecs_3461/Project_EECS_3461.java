package processing.test.project_eecs_3461;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import android.view.MotionEvent; 
import java.util.Iterator; 
import ketai.sensors.*; 
import apwidgets.*; 
import java.lang.reflect.Method; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Project_EECS_3461 extends PApplet {

//Denise Enriquez, cse13152
//Dallis King, cse13148
//Samantha Puder






KetaiSensor sensor;
APMediaPlayer player; //Plays audio

/////////////////////VISUALIZER VARIABLES//////////////////////
int multiplier = 100;
PImage fade;
boolean hit = false;

int lastx = 0;
int lasty = 0;
int last = 0;

int max;
int min;
int trial;

int red;
int green;
int blue;

int sizeCircle;

String mode = "none";
boolean isVisualizerOn;

////////////////HERE IS THE VARIABLES FOR THE DATA///////////////
PVector magneticField, accelerometer;
float light, proximity;
float rotationX, rotationY, rotationZ;

int counter = 0;
int res = displayWidth + displayHeight; 
float xmag, ymag = 0;
float newXmag, newYmag = 0; 
float size = 60;
Processor process;

String screenText;

/////////////////GESTURE OBJECT//////////////////////////
Gestures g; 
int backgroundColor;  

public void setup() {
 

///////////////////MEDIA PLAYER SETUP/////////////////////////////////
  player = new APMediaPlayer(this); 
  player.setMediaFile("yirumaend.mp3"); //set the file (files are in data folder              
  player.start(); //start play back
  player.setLooping(true); //restart playback end reached
  player.setVolume(0.5f, 0.5f); //Set left and right volumes. Range is from 0.0 to 1.0
////////////////////MEDIA PLAYER SETUP END///////////////////////////////

  //println("W:" + displayWidth + "H:" + displayHeight);
  //noStroke();
  process = new Processor();
  orientation(PORTRAIT);

  stroke(255);
  strokeWeight(1);
  background(0);
  fade = get(0, 0, width, height);

  max = 110;
  min = 30;

  screenText = "Hello." + "\n" + "How are you doing today?"
    + "\n \n \n \n \n \n" + "Swipe to the LEFT for instructions." + "\n"
    + "Swipe to the RIGHT for visualizer";

  //////////////////////SENSORS/////////////////////////////////////////
  sensor = new KetaiSensor(this);
  sensor.start();
  sensor.list();
  sensor.disableOrientation();
  accelerometer = new PVector();
  magneticField = new PVector();

  textAlign(CENTER, CENTER);
  textSize(28);
  //println("INITIALIZING");

  isVisualizerOn = false;
  backgroundColor = color(0);
  g = new Gestures(80, 80, this);    // iniate the gesture object first value is minimum swipe length in pixel and second is the diagonal offset allowed

  ////////////////////PROCESSING'S VERSION OF JAVA LISTENERS/////////////////////////////////////
  g.setSwipeUp("swipeUp");    // attach the function called swipeUp to the gesture of swiping upwards
  g.setSwipeDown("swipeDown");    // attach the function called swipeDown to the gesture of swiping downwards
  g.setSwipeLeft("swipeLeft");  // attach the function called swipeLeft to the gesture of swiping left
  g.setSwipeRight("swipeRight");  // attach the function called swipeRight to the gesture of swiping right
}

public void draw() {
  // set defaults
  noStroke(); 
  process.calculate();

  ///////////////////////VISUALIZER CODE//////////////////////////
  //println(isVisualizerOn);
  if (isVisualizerOn) {
    //a person taps or swipes to change
    //the maximum size of the circles
    trial = (int) random(1, 15); //this is the likelyhood of a person tapping
    if (trial == 1) { //the person taps
      max = (int) random(2, 50);  //max changes
      //this number could be based on where the
      //person taps, or for how long/fast they swipe?
    }

    background(0);
    tint(255, 255, 255, 250);
    image(fade, 0, 0);
    noTint();

    int circleFreq = (int) map(light, 0.f, 400.f, 2, 7);

    trial = (int) random(1, circleFreq);
    if (trial == 1)
      hit = true;

    red = (int) map(rotationX, -1.0f, 1.0f, 0, 255);
    green = (int) map(rotationY, -1.0f, 1.0f, 0, 255);
    blue = (int) map(rotationZ, -1.0f, 1.0f, 0, 255);
    println(red + " " + green + " " + blue);

    if (hit == true) {
      int x = (int) random(30, width-30);
      int y = (int) random(30, height-30);
      sizeCircle = (int) random(min, max);
      stroke(red, green, blue);
      fill(red, green, blue);
      ellipse(x, y, sizeCircle, sizeCircle);

      hit = false; 
      if (lastx == 0 && lasty == 0) {
        lastx=x;
        lasty=y;
        last=millis();
      }
      else if (millis() > last+1000) {
        lastx=x;
        lasty=y;
        last = millis();
      }
      else {
        noFill();
        int curve1 = (int) random(1, 10);
        int curve2 = (int) random(1, 10);
        bezier(x, y, x+curve1, x+curve2, y-curve1, y-curve2, lastx, lasty);
        lastx=x;
        lasty=y;
        last = millis();
      }
    }
    fade = get(0, 0, width, height);
    ////////////////////////END VISUALIZER CODE///////////////////
  } 
  else {
    /////////////////////HOME SCREEN////////////////////////////
    //nfp(accelerometer.x, #ofdigits, #ofdecimal places
    background(backgroundColor);
    //  text("Accelerometer :" + "\n" 
    //    + "x: " + nfp(accelerometer.x, 1, 5) + "\n" 
    //    + "y: " + nfp(accelerometer.y, 1, 2) + "\n" 
    //    + "z: " + nfp(accelerometer.z, 1, 2) + "\n"
    //    + "Gyroscope: \n" + 
    //    "x: " + nfp(rotationX, 1, 3) + "\n" +
    //    "y: " + nfp(rotationY, 1, 3) + "\n" +
    //    "z: " + nfp(rotationZ, 1, 3) + "\n"
    //    + "Light Sensor : " + light + "\n" 
    //    + "Proximity Sensor : " + proximity + "\n"
    //    , 20, 0, width, height);

    text(screenText, 10, 0, width, height);
  }
  ///////////END HOME SCREEN//////////////////////////////////////
}

// Android Touch Event (MotionEvent)
public boolean surfaceTouchEvent(MotionEvent event) {

  // Get the Action performed
  int code = event.getActionMasked();

  // Get the Pointer Index (Unique ID) about what pointer has gone UP or DOWN
  int index = event.getActionIndex();

  // Get information about the Touch event (x, y, id, etc)
  float x = event.getX(index);
  float y = event.getY(index);
  int id = event.getPointerId(index);

  // Touch Down
  // ACTION_DOWN is for first touch, ACTION_POINTER_DOWN is for all the rest
  if (code == MotionEvent.ACTION_DOWN || code == MotionEvent.ACTION_POINTER_DOWN) {
    // Call up touchDown
    process.touchDown(x, y, id);
  }

  // Touch Up
  // ACTION_sUP for first touch, ACTION_POINTER_DOWN is for all the rest
  else if (code == MotionEvent.ACTION_UP || code == MotionEvent.ACTION_POINTER_UP) {
    process.touchUp(id);
  }

  // Touch Moved
  else if (code == MotionEvent.ACTION_MOVE) {
    // number of pointers (to touches)
    int numPointers = event.getPointerCount();
    for (int i = 0; i < numPointers; i++) {
      id = event.getPointerId(i);
      x = event.getX(i);
      y = event.getY(i);
      process.touchMoved(x, y, id);
    }
  }


  // check what that was  triggered  
  switch(event.getAction()) {
  case MotionEvent.ACTION_DOWN:    // ACTION_DOWN means we put our finger down on the screen 
    g.setStartPos(new PVector(event.getX(), event.getY()));    // set our start position
    break;
  case MotionEvent.ACTION_UP:    // ACTION_UP means we pulled our finger away from the screen  
    g.setEndPos(new PVector(event.getX(), event.getY()));    // set our end position of the gesture and calculate if it was a valid one
    break;
  }
  return super.surfaceTouchEvent(event);
}

///////////////////////////////////SENSORS///////////////////////////////////
public void onAccelerometerEvent(float x, float y, float z, long time, int accuracy)
{
  accelerometer.set(x, y, z);
}

public void onMagneticFieldEvent(float x, float y, float z, long time, int accuracy)
{
  magneticField.set(x, y, z);
}

public void onLightEvent(float v)
{
  light = v;
}

public void onProximityEvent(float v)
{
  proximity = v;
}

public void onGyroscopeEvent(float x, float y, float z)
{
  rotationX = x;
  rotationY = y;
  rotationZ = z;
}
///////////////////////SENSORS END/////////////////////////////////////////
/////////////////////MEDIA PLAYER STOP/////////////////////////////////////
public void onDestroy() {

  super.onDestroy(); //call onDestroy on super class
  if(player!=null) { //must be checked because or else crash when return from landscape mode
    player.release(); //release the player

  }
}

public void stop(){
 onDestroy();
 super.stop(); 
}
//////////////////MEDIA PLAYER STOP END//////////////////////////

///////////WHAT HAPPENS WHEN SWIPE UP/DOWN/LEFT/RIGHT/TAP////////////////
//////////PROCESSING'S VERSION OF JAVA LISTENERS/////////////////////////

public void onTap(TapEvent e) {
  //  if (sensor.isStarted()) {
  //    sensor.stop();
  //  }
  //  else {
  //    sensor.start();
  //  }
  //  println("KetaiSensor isStarted: " + sensor.isStarted());
}

public void swipeUp() {
  println("a swipe up");    
  backgroundColor = color(100, 30, 56);
  screenText = "You're swiping up.";
  println("UP");


  min+=6;
  max+=10;

  min = constrain(min, 3, 60);
  max = constrain(max, 70, 300);

  println(max + " " + min);
}
public void swipeDown() {
  println("a swipe down");
  backgroundColor = color(150, 56, 34);
  screenText = "You're swiping down.";
  println("DOWN");

  max-=10;
  min-=6;

  min = constrain(min, 3, 60);
  max = constrain(max, 70, 300);

  println(max + " " + min);
}
public void swipeLeft() {
  println("a swipe left");
  backgroundColor = color(200, 24, 34);
  fill(255);
  screenText = "INSTRUCTIONS:" + "\n \n \n" +
  "SWIPE UP increases circle size." + "\n" +
  "SWIPE DOWN decreases circle size." + "\n" +
  "Tilting the device affects the colours." + "\n" +
  "More circles appear if it's dark," + "\n" +
  "less circles appear if it's bright." + "\n \n \n" +
  "SWIPE to the RIGHT to begin the visualizer.";
  println("LEFT");

  if (isVisualizerOn == true) {
    isVisualizerOn = false;
  }
}
public void swipeRight() {
  println("a swipe right");
  backgroundColor = color(45, 78, 3);
  screenText = "You're swiping right.";
  println("RIGHT");

  if (isVisualizerOn == false) {
    isVisualizerOn = true;
  }
}


class Gestures {
  int  maxOffset, minLength;
  String functionName;
  PVector startPos, endPos;
  PApplet pApp;
  Method[] m;
  Gestures(int minimum,int offSet,PApplet theApplet) {
    m=new Method[4];
    pApp = theApplet;
    maxOffset=offSet;    //number pixels you are allowed to travel off the axis and still being counted as a swipe
    minLength=minimum;    // number of pixels you need to move your finger to count as a swipe
  }
  // where did our motion start
  public void setStartPos(PVector pos) {
    startPos=pos;
  }
  // where did it end and also call to check if it was a valid swipe
  public void setEndPos(PVector pos) {
    endPos=pos;
    checkSwipe();
    endPos=new PVector();
    startPos=new PVector();
  }
  // check if it is a valid swipe that has been performed and if so perform the attached function
  public void checkSwipe() {
    if (abs(startPos.x-endPos.x)>minLength&&abs(startPos.y-endPos.y)<maxOffset) {
      if (startPos.x<endPos.x) {
        performAction(2);    // a swipe right
      }
      else {
        performAction(0);    // a swipe left
      }
    }
    else {
      if (abs(startPos.y-endPos.y)>minLength&&abs(startPos.x-endPos.x)<maxOffset) {
        if (startPos.y<endPos.y) {
          performAction(3);  // a swipe downwards
        }
        else {
          performAction(1);  // a swipe upwards
        }
      }
    }
  }
  // call the function that we have defined with setAction
  public void performAction(int direction) {
    if (m[direction] == null)
      return;
    try {
      m[direction].invoke(pApp);
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  // define a function that should get called when the different swipes is done
  public void setAction(int direction, String method) {
    if (method != null && !method.equals("")) {
      try {
        m[direction] = pApp.getClass().getMethod(method);
      } 
      catch (SecurityException e) {
        e.printStackTrace();
      } 
      catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }
  // attach a function to a left swipe
  public void setSwipeLeft(String _funcName) {
    setAction(0, _funcName);
  }
  public void setSwipeUp(String _funcName) {
    setAction(1, _funcName);
  }
  public void setSwipeRight(String _funcName) {
    setAction(2, _funcName);
  }
  public void setSwipeDown(String _funcName) {
    setAction(3, _funcName);
  }
}

class Processor {

  // Store Touch objects
  ArrayList touches;

  // Store TouchEvent objects
  ArrayList events;

  // PVector centroid
  PVector centroid;

  float time;
  float time2;
  PVector firstPress;
  PVector contPress;
  static final float maxDist = 30; // in pixels
  static final float maxTime = 500; // in ms

  Processor() {
    touches = new ArrayList();
    events = new ArrayList();

    centroid = new PVector();
    
    time = millis();
   
  }

  public synchronized void calculate() {
    //println("CALCULATING");
    updateCentroid();
    findDrag();
    //findSwipe();
    //findFlick();
    findPinch();
    sendEvents();
  }

  public synchronized void sendEvents() {

    // Traverse events ArrayList
    for (int i = 0; i < events.size(); i++) {
      TouchEvent e = (TouchEvent) events.get(i);

      // is TouchEvent e an instance of DragEvent ?
      if (e instanceof DragEvent) {
        // Call a function in our main Processing class (MT_Processor)
        // onDrag( (DragEvent) e);
        //println("DRAGGING");
      }

      // is TouchEvent e an instance of PinchEvent ?
//      else if (e instanceof PinchEvent) {
//        //onPinch( (PinchEvent) e);
//        //println("PINCHING");
//      }
//
//      else if (e instanceof SwipeEvent) {
//        onSwipe( (SwipeEvent) e); 
//        println("SWIPING");
//      }
//
//      else if (e instanceof FlickEvent) {
//        onFlick( (FlickEvent) e);
//        println("FLICKING");
//      }
      else if(e instanceof TapEvent){
       onTap( (TapEvent) e); 
       println("TAPPING");
      }
      else {
        println(e);
      }
    }

    // Clear events ArrayList
    events.clear();
  }

  public void updateCentroid() {

    // Reset centroid to zero
    centroid.set(0, 0, 0);

    // Traverse all Touch objects, add all getLocations together
    for (int i = 0; i < touches.size(); i++) {
      Touch t = (Touch) touches.get(i);

      // Add Touch getLocation to Centroid
      centroid.x += t.getLoc().x;
      centroid.y += t.getLoc().y;
    }

    // Average getLoction (divide by number of touches)
    centroid.x /= touches.size();
    centroid.y /= touches.size();
  }

  public synchronized void findPinch() {

    // Are there currently at least 2 touches (2 touches makes a pinch)
    if (touches.size() < 2) {
      return;
    }

    // 2 or More touches, find Pinch information

      float pinch = 0.0f;

    // Find distnace between each Touch object and Centroid
    for (int i = 0; i < touches.size(); i++) {
      Touch t = (Touch) touches.get(i);

      // Distance between Touch t and Centroid
      float distance = dist(t.getLoc().x, t.getLoc().y, centroid.x, centroid.y);

      // set Touch t variable pinch
      t.setPinch(distance);

      // Find change in pinch distance
      float pinchDelta = t.pinch - t.pinchprev;

      // Add pinchDelta to getLocal variable pinch
      pinch += pinchDelta;
    }

    // Average out pinch distances
    pinch /= touches.size();

    // Test if getLocal variable pinch is not zero
    if (pinch != 0) {
      // Create a PinchEvent
      PinchEvent pe = new PinchEvent(centroid.x, centroid.y, pinch);
      events.add(pe);
    }
  }

  // Determine if a DragEvent has occured
  public synchronized void findDrag() {

    // 1 Touch object in existence?
    if (touches.size() == 1) {

      // Get the first Touch object stored in touches ArrayList
      Touch t = (Touch) touches.get(0);

      // Create a DragEvent object
      // DragEvent(float x, float y, float dx, float dy)
      DragEvent de = new DragEvent(t.getLoc().x, t.getLoc().y, t.getDelta().x, t.getDelta().y);

      // Add DragEvent object 'de' to events ArrayList
      events.add(de);
    }
  }

  public synchronized ArrayList getTouches() {
    return (ArrayList) touches.clone();
  }

  public synchronized Touch getTouch(int pid) {
    Iterator it = touches.iterator();
    while (it.hasNext () ) {
      Touch t = (Touch) it.next();
      if (t.id == pid) {
        return t;
      }
    }
    return null;
  }

  // TouchDown Function (synchronized)
  public synchronized void touchDown(float x, float y, int id) {
    Touch touch = new Touch(x, y, id);
    touches.add(touch);

    // println("TOUCHES");

    updateCentroid();

    // Only needed for Pinch, so only if touches.size() > 2
    if (touches.size() >= 2) {
      touch.initTouch(centroid);
    }
    
    firstPress = new PVector(x, y);
    time = millis();
    
  }

  // TouchUp Fuction (syncronized)
  public synchronized void touchUp(int id) {
    Touch touch = getTouch(id);

    contPress = new PVector(touch.getLoc().x, touch.getLoc().y);
    time2 = millis();
    
    if(time2 - time < maxTime && dist(firstPress.x, firstPress.y, contPress.x, contPress.y) < maxDist){
       events.add(new TapEvent( contPress.x, contPress.y)); 
    }
    touches.remove(touch);
    
  } 

  // TouchMoved Fuction (syncronized)
  public synchronized void touchMoved(float x, float y, int id) {
    Touch touch = getTouch(id);
    touch.update(x, y);
    time2 = millis();
  
  }
}

class Touch {

  PVector loc;
  PVector prev;
  PVector init;
  int id;
  float dim;
  
  float pinch;
  float pinchprev;

  Touch(float x, float y, int pid) {
    loc = new PVector(x, y);
    prev = new PVector(x, y);
    init = new PVector(x, y);
    id = pid;
    dim = 200;
  }

  public void update(float x, float y) {
    prev.set(loc.x, loc.y, 0);
    loc.set(x, y, 0);
  }

  public PVector getLoc() {
    return loc;
  }

  public void display() {
    stroke(255, 255, 255);
    noFill();
    ellipse(loc.x, loc.y, dim, dim);
    text( id + ": " + (int) loc.x + ", " + (int) loc.y, loc.x - dim, loc.y - dim/2);
  }
  
  public PVector getDelta(){
    return PVector.sub(loc, prev);
  }
  
  public void setPinch(float dist){
    pinchprev= pinch;
    pinch = dist;
  }
  
  public void initTouch(PVector p){
    pinch = pinchprev = dist(loc.x, loc.y, p.x, p.y);
  }
  
  static final float thresh = 20f;
  static final float flickThresh = 5f; 

}
// Define our TouchEvent Classes

// Base Class 
class TouchEvent {
  // empty but could hold fields and methods
}

// Drag Event
class DragEvent extends TouchEvent {
  
  PVector loc;
  PVector delta;
  
  DragEvent(float x, float y, float dx, float dy) {
    loc = new PVector(x, y);
    delta = new PVector(dx, dy);
  }
}

// Pinch Event
class PinchEvent extends TouchEvent {
  
  PVector center;
  float amount;
  
  PinchEvent(float x, float y, float amt) {
    center = new PVector(x, y);
    amount = amt;
  }
  
}

class TapEvent extends TouchEvent{
  float dx;
  float dy;
  
  TapEvent(float x, float y){
   dx = x;
   dy = y;
  }
}



  public int sketchWidth() { return displayWidth; }
  public int sketchHeight() { return displayHeight; }
  public String sketchRenderer() { return P2D; }
}
