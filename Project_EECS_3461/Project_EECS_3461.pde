/* Our Android application (tested on Android Kitkat 4.4.2) is a prototype
for a sound visualizer. Visualizations can aid in sleep and used as
a visual grounding tool for people with certain degrees of anxiety.
*/

import android.view.MotionEvent;
import java.util.Iterator;
import ketai.sensors.*;
import apwidgets.*;

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
color backgroundColor;  

void setup() {
  size(displayWidth, displayHeight, P2D);

///////////////////MEDIA PLAYER SETUP/////////////////////////////////
  player = new APMediaPlayer(this); 
  player.setMediaFile("yirumaend.mp3"); //set the file (files are in data folder              
  player.start(); //start play back
  player.setLooping(true); //restart playback end reached
  player.setVolume(0.5, 0.5); //Set left and right volumes. Range is from 0.0 to 1.0
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

void draw() {
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

    int circleFreq = (int) map(light, 0., 400., 2, 7);

    trial = (int) random(1, circleFreq);
    if (trial == 1)
      hit = true;

    red = (int) map(rotationX, -1.0, 1.0, 0, 255);
    green = (int) map(rotationY, -1.0, 1.0, 0, 255);
    blue = (int) map(rotationZ, -1.0, 1.0, 0, 255);
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
boolean surfaceTouchEvent(MotionEvent event) {

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
void onAccelerometerEvent(float x, float y, float z, long time, int accuracy)
{
  accelerometer.set(x, y, z);
}

void onMagneticFieldEvent(float x, float y, float z, long time, int accuracy)
{
  magneticField.set(x, y, z);
}

void onLightEvent(float v)
{
  light = v;
}

void onProximityEvent(float v)
{
  proximity = v;
}

void onGyroscopeEvent(float x, float y, float z)
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

void onTap(TapEvent e) {
  //  if (sensor.isStarted()) {
  //    sensor.stop();
  //  }
  //  else {
  //    sensor.start();
  //  }
  //  println("KetaiSensor isStarted: " + sensor.isStarted());
}

void swipeUp() {
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
void swipeDown() {
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
void swipeLeft() {
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
void swipeRight() {
  println("a swipe right");
  backgroundColor = color(45, 78, 3);
  screenText = "You're swiping right.";
  println("RIGHT");

  if (isVisualizerOn == false) {
    isVisualizerOn = true;
  }
}

