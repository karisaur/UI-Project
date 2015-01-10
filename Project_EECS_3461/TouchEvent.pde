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


