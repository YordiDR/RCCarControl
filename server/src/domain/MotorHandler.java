package domain;

import static domain.MOTOR_TYPE.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class MotorHandler {

    private double power;

    MotorHandler() {
        setupGPIO();
    }

    void handle(String data) {
        power = Double.parseDouble(data.substring(1));

        if (data.charAt(0) == 'E') {
            switch ((int) (power * 100)) { // * 100 due to only int's being possible to switch (0.25 * 100 = 25)
                case -100:
                    System.out.println("Full brake!");
                    controlMotor(ENGINE, 1, 480);
                    break;

                case -50:
                    controlMotor(ENGINE, 1, 300);
                    System.out.println("Half brake!");
                    break;

                case -25:
                    controlMotor(ENGINE, 1, 150);
                    System.out.println("Quarter brake!");
                    break;

                case 0:
                    controlMotor(ENGINE, 0, 0);
                    System.out.println("Coasting!");
                    break;

                case 25:
                    controlMotor(ENGINE, 0, 150);
                    System.out.println("Quarter throttle!");
                    break;

                case 50:
                    controlMotor(ENGINE, 0, 300);
                    System.out.println("Half throttle!");
                    break;

                case 100:
                    controlMotor(ENGINE, 0, 480);
                    System.out.println("Full throttle!");
            }

        } else {
            switch ((int) (power * 10)) { // * 10 due to only int's being possible to switch (0.5 * 10 = 5)
                case -10:
                    System.out.println("Full left!");
                    controlMotor(STEERING, 1, 480);
                    break;

                case -5:
                    controlMotor(STEERING, 1, 200);
                    System.out.println("Half left!");
                    break;

                case 0:
                    controlMotor(STEERING, 0, 0);
                    System.out.println("Straight!");
                    break;

                case 5:
                    controlMotor(STEERING, 0, 200);
                    System.out.println("Half right!");
                    break;

                case 10:
                    controlMotor(STEERING, 0, 480);
                    System.out.println("Full right!");
            }
        }
    }

    private void setupGPIO() {
        try {
            Runtime.getRuntime().exec("setup_RCCarGPIO.sh");
        } catch (IOException ex) {
            Logger.getLogger(MotorHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("GPIO setup!");
    }

    private void controlMotor(MOTOR_TYPE type, int direction, int power) {
        if (type == ENGINE) {
            try {
                Runtime.getRuntime().exec("gpio -g write 5 " + direction);
            } catch (IOException ex) {
                Logger.getLogger(MotorHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Runtime.getRuntime().exec("gpio -g pwm 12 " + power);
            } catch (IOException ex) {
                Logger.getLogger(MotorHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                Runtime.getRuntime().exec("gpio -g write 6 " + direction);
            } catch (IOException ex) {
                Logger.getLogger(MotorHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Runtime.getRuntime().exec("gpio -g pwm 13 " + power);
            } catch (IOException ex) {
                Logger.getLogger(MotorHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
