package com.example;

import java.awt.EventQueue;
import com.example.GUI.GUIForm;

public class App 
{
    public static void main( String[] args )
    {

        			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						
						GUIForm.login.frame.setVisible(true);
						// GUIForm.login.frame.setVisible(true);
					} catch (Exception e) {
						System.out.println("fghjk");
					}
				}
			});
            System.out.println("kkk");
    }
}
