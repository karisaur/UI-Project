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

  void update(float x, float y) {
    prev.set(loc.x, loc.y, 0);
    loc.set(x, y, 0);
  }

  PVector getLoc() {
    return loc;
  }

  void display() {
    stroke(255, 255, 255);
    noFill();
    ellipse(loc.x, loc.y, dim, dim);
    text( id + ": " + (int) loc.x + ", " + (int) loc.y, loc.x - dim, loc.y - dim/2);
  }
  
  PVector getDelta(){
    return PVector.sub(loc, prev);
  }
  
  void setPinch(float dist){
    pinchprev= pinch;
    pinch = dist;
  }
  
  void initTouch(PVector p){
    pinch = pinchprev = dist(loc.x, loc.y, p.x, p.y);
  }
  
  static final float thresh = 20f;
  static final float flickThresh = 5f; 

}
