package common.utils;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

public class TextJustification {

  public static void justify(TextView textView,float contentWidth) {
    String text=textView.getText().toString();
    Paint paint=textView.getPaint();

    ArrayList<String> lineList=lineBreak(text,paint,contentWidth);

    textView.setText(TextUtils.join(" ", lineList).replaceFirst("\\s", ""));
  }


  private static ArrayList<String> lineBreak(String text,Paint paint,float contentWidth){
    String [] wordArray=text.split("\\s"); 
    ArrayList<String> lineList = new ArrayList<String>();
    String myText="";

    for(String word:wordArray){
      if(paint.measureText(myText+" "+word)<=contentWidth)
        myText=myText+" "+word;
      else{
        int totalSpacesToInsert=(int)((contentWidth-paint.measureText(myText))/paint.measureText(" "));
        lineList.add(justifyLine(myText,totalSpacesToInsert));
        myText=word;
      }
    }
    lineList.add(myText);
    return lineList;
  }

  private static String justifyLine(String text,int totalSpacesToInsert){
    String[] wordArray=text.split("\\s");
    String toAppend=" ";

    while((totalSpacesToInsert)>=(wordArray.length-1)){
      toAppend=toAppend+" ";
      totalSpacesToInsert=totalSpacesToInsert-(wordArray.length-1);
    }
    int i=0;
    String justifiedText="";
    for(String word:wordArray){
      if(i<totalSpacesToInsert)
        justifiedText=justifiedText+word+" "+toAppend;

      else				
        justifiedText=justifiedText+word+toAppend;

      i++;
    }

    return justifiedText;
  }

}