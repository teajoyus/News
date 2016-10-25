package com.example.news;

import java.util.Scanner;

/**
 * Created by Administrator on 2016/10/20.
 */
public class TestUtils {
  public static String pauseUntilPress(){
    Scanner scanner = new Scanner(System.in);
    return scanner.next();
  }
  public static void sleep(int time){
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
