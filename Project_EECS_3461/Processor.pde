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

  synchronized void calculate() {
    //println("CALCULATING");
    updateCentroid();
    findDrag();
    //findSwipe();
    //findFlick();
    findPinch();
    sendEvents();
  }

  synchronized void sendEvents() {

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

  void updateCentroid() {

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

  synchronized void findPinch() {

    // Are there currently at least 2 touches (2 touches makes a pinch)
    if (touches.size() < 2) {
      return;
    }

    // 2 or More touches, find Pinch information

      float pinch = 0.0;

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
  synchronized void findDrag() {

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

  synchronized ArrayList getTouches() {
    return (ArrayList) touches.clone();
  }

  synchronized Touch getTouch(int pid) {
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
  synchronized void touchDown(float x, float y, int id) {
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
  synchronized void touchUp(int id) {
    Touch touch = getTouch(id);

    contPress = new PVector(touch.getLoc().x, touch.getLoc().y);
    time2 = millis();
    
    if(time2 - time < maxTime && dist(firstPress.x, firstPress.y, contPress.x, contPress.y) < maxDist){
       events.add(new TapEvent( contPress.x, contPress.y)); 
    }
    touches.remove(touch);
    
  } 

  // TouchMoved Fuction (syncronized)
  synchronized void touchMoved(float x, float y, int id) {
    Touch touch = getTouch(id);
    touch.update(x, y);
    time2 = millis();
  
  }
}

