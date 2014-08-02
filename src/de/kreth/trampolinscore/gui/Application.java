package de.kreth.trampolinscore.gui;

import java.awt.EventQueue;

import de.kreth.trampolinscore.gui.swing.MainFrame;
import de.kreth.trampolinscore.FactoryProductive;

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
