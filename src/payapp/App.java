package payapp;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class App {

    public Robot robot = null;
    public Boolean error = false;
    public Boolean ispay = false;
    public BoardListener clip = null;

    public void run() {
//        ispay = false;
//        while (ispay == false) {
//            error = false;
        myExecute();
//        }
    }

    public void myExecute() {
        while (error == false) {
            error = false;
            try {
                robot = new Robot();
            } catch (Exception e) {
                System.err.println("00_ERROR AL CARGAR ROBOT");
                error = true;
                break;
            }

            //abro nueva pestaña
            robot.mouseMove(270, 18);
            try {
                myClic();
            } catch (InterruptedException ex) {
                System.err.println("01_ERROR AL ABRIR NUEVA PESTAÑA");
                error = true;
                break;
            }

            //posiciono para pegar la url
            robot.mouseMove(500, 50);
            clip = new BoardListener();
            clip.setClipboardContents("https://onlineservices.immigration.govt.nz/WorkingHoliday/default.aspx");

            //pego la url con click derecho PEGAR Y BUSCAR
            myClicDerecho();
            robot.mouseMove(540, 170);
            try {
                myClic();
            } catch (InterruptedException ex) {
                System.err.println("02_ERROR AL PRESIONAR PEGAR Y BUSCAR");
                error = true;
                break;
            }

            //espero y bajo la ventana
            try {
                moveWindow(18);
            } catch (InterruptedException ex) {
                System.err.println("03_ERROR AL BAJAR VENTANA 1");
                error = true;
                break;
            }

            //posiciono en pay y espero un segundo y hago click
            robot.mouseMove(962, 295);
            try {
                myClic();
            } catch (InterruptedException ex) {
                System.err.println("04_ERROR AL CLICKAR EL PRIMER PAY");
                error = true;
                break;
            }

            //espero y bajo la ventana
            try {
                moveWindow(6);
            } catch (InterruptedException ex) {
                System.err.println("05_ERROR AL BAJAR VENTANA 2");
                error = true;
                break;
            }

            //posiciono en next step y espero un segundo y hago click
            robot.mouseMove(962, 620);
            try {
                myClic();
            } catch (InterruptedException ex) {
                System.err.println("06_ERROR AL CLICKAR NEXT STEP");
                error = true;
                break;
            }

            //espero y bajo la ventana
            try {
                moveWindow(6);
            } catch (InterruptedException ex) {
                System.err.println("07_ERROR AL BAJAR VENTANA 3");
                error = true;
                break;
            }

            //copio Payment
            robot.mouseMove(350, 180);
            try {
                myDoubleClic();
                myClicDerecho();
                robot.mouseMove(360, 190);
                myClic();
            } catch (InterruptedException ex) {
                System.err.println("08_ERROR AL INTENTAR COPIAR PAYMENT");
                error = true;
                break;
            }

            System.err.println(clip.getClipboardContents().trim());

            if (!clip.getClipboardContents().trim().equals("Payment")) {
                System.err.println("09_ERROR AL COPIAR PAYMENT");
                error = true;
                break;
            } else {
                //posiciono en OK y espero un segundo y hago click
                try {
                    Thread.sleep(500);
                    robot.mouseMove(480, 662);
                    myClic();
                    Thread.sleep(6000);
                } catch (InterruptedException ex) {
                    System.err.println("10_ERROR posicionar en OK");
                    error = true;
                    break;
                }

                //posiciono y intento copiar el texto tarjeta
                robot.mouseMove(540, 365);
                try {
                    myDoubleClic();
                    Thread.sleep(500);
                    myClicDerecho();
                    robot.mouseMove(550, 400);
                    myClic();
                    Thread.sleep(400);
                } catch (InterruptedException ex) {
                    System.err.println("11_ERROR al copiar inmigracion");
                    error = true;
                    break;
                }

                try {
                    if (intentarPagar()) {
                        break;
                    } else {
                        //no entro al pago por ahora lo detengo para q no se cuelge 
                        break;
                    }
                } catch (Exception e) {
                    System.err.println("12_ERROR AL INTENTAR PAGAR");
                    error = true;
                    break;
                }
            }
        }
    }

    public void moveWindow(int moving) throws InterruptedException {
        robot.mouseMove(1356, 722);
        Thread.sleep(6000);

        for (int i = 0; i < moving; i++) {
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
    }

    public void myDoubleClic() throws InterruptedException {
        Thread.sleep(500);
        // first click
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        // second click
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void myClic() throws InterruptedException {
        Thread.sleep(700);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(50);
    }

    public void myClicDerecho() {
        robot.mousePress(InputEvent.BUTTON3_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
    }

    public boolean intentarPagar() throws InterruptedException {
        Thread.sleep(500);
        if (clip.getClipboardContents().trim() != null && clip.getClipboardContents().trim().equals("4835610420345646")) {
            System.out.print("ENTRO AL PAGO");
            System.out.print(clip.getClipboardContents());
            robot.mouseMove(640, 528);
            myClic();
            ispay = true;
        } else {
            System.out.print("NO ENTRO AL PAGO");
            System.out.print(clip.getClipboardContents());
            Thread.sleep(2000);
        }
        return ispay;
    }
}
