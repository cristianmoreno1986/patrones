/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oscarblancarte.ipd.state.states;

import oscarblancarte.ipd.state.Server;

/**
 *
 * @author crist
 */
public class SafeShutdownServerState extends AbstractServerState{
    private Thread monitoringThread;
    
    public SafeShutdownServerState(final Server server) {
        monitoringThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    System.out.println("Turning off");
                    while (true) {
                        Thread.sleep(1000 * 10);
                        if (server.getMessageProcess().countMessage() > 0) {
                                server.getMessageProcess().start();
                        }
                        else {
                            break;
                        }
                    }
                    System.out.println("Safe Shutdown");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        monitoringThread.start();
    }
    @Override
    public void handleMessage(Server server, String message) {
        System.out.println("Cant´t process request, Server is state safe shutdown");
    }
}
