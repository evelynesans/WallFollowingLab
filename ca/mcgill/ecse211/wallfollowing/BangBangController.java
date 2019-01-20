package ca.mcgill.ecse211.wallfollowing;

import lejos.hardware.motor.*;

import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.xy.IntervalXYDataset;

public class BangBangController implements UltrasonicController {

  private final int bandCenter;
  private final int bandwidth;
  private final int motorLow;
  private final int motorHigh;
  private int distance;
  private static final int OUT = 40;
  private int distanceError;
  private int sleepInt = 50;
  private int tooClose = 180;
  private int filterControl;

  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;
    this.bandwidth = bandwidth;
    this.motorLow = motorLow;
    this.motorHigh = motorHigh;
    this.filterControl = 0;
    WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    WallFollowingLab.rightMotor.setSpeed(motorHigh);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {
    this.distance = distance;
    
    if(distance >= 100 && filterControl < OUT)
    {
    	filterControl++;
    }
    else if(distance >= 100)
    {
    	this.distance = distance;
    }else
    {
    	filterControl = 0;
    	this.distance = distance;
    }
    
    distanceError = bandCenter - distance;
    
    if (Math.abs((distanceError - bandCenter) <= bandwidth))
    {
    	WallFollowingLab.leftMotor.setSpeed(motorHigh);
    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
    	WallFollowingLab.leftMotor.forward();
    	WallFollowingLab.rightMotor.forward();
    }
    else if (distance<=14)
    {
    	WallFollowingLab.leftMotor.setSpeed(tooClose);
    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
    	WallFollowingLab.leftMotor.backward();
    	WallFollowingLab.rightMotor.backward();
    }
    else if (distanceError >0)
    {
    	WallFollowingLab.leftMotor.setSpeed(motorHigh);
    	WallFollowingLab.rightMotor.setSpeed(motorLow);
    	WallFollowingLab.leftMotor.forward();
    	WallFollowingLab.rightMotor.backward();
    }
    else if (distanceError <0)
    {
    	WallFollowingLab.leftMotor.setSpeed(motorLow);
    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
    	WallFollowingLab.leftMotor.forward();
    	WallFollowingLab.rightMotor.forward();
    }
    try {
    	Thread.sleep(sleepInt);
    }catch (InterruptedException e)
    {
    	e.printStackTrace();
    }
    // TODO: process a movement based on the us distance passed in (BANG-BANG style)
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
