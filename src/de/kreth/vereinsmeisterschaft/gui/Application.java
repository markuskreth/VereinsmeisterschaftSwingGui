package de.kreth.vereinsmeisterschaft.gui;

import java.awt.EventQueue;

import de.kreth.vereinsmeisterschaft.gui.swing.MainFrame;
import de.kreth.vereinsmeisterschaftprog.FactoryProductive;



public class Application {

   public void run() {
      @SuppressWarnings("unused")
      FactoryProductive factoryProductive = new FactoryProductive();

      EventQueue.invokeLater(new Runnable() {

         @Override
         public void run() {
            try {
               MainFrame frame = new MainFrame();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }
   
   public static void main(String[] args) {
      Application app = new Application();
      app.run();
   }

}
