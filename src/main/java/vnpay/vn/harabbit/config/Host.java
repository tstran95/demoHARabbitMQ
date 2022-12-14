package vnpay.vn.harabbit.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sontt1
 * Date:10/5/2022
 * Time:2:07 PM
 */
public class Host {
    public static String capture(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder buff = new StringBuilder();
        while ((line = br.readLine()) != null) {
            buff.append(line).append("\n");
        }
        return buff.toString();
    }

    public static Process executeCommand(String command) throws IOException {
        Process pr = executeCommandProcess(command);

        int ev = waitForExitValue(pr);
        if (ev != 0) {
            String stdout = capture(pr.getInputStream());
            String stderr = capture(pr.getErrorStream());
            throw new IOException("unexpected command exit value: " + ev + "\ncommand: " + command + "\n"
                    + "\nstdout:\n" + stdout + "\nstderr:\n" + stderr + "\n");
        }
        return pr;
    }

    private static int waitForExitValue(Process pr) {
        while (true) {
            try {
                pr.waitFor();
                break;
            } catch (InterruptedException ignored) {
            }
        }
        return pr.exitValue();
    }

    private static Process executeCommandProcess(String command) throws IOException {
        String[] finalCommand;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            finalCommand = new String[4];
            finalCommand[0] = "C:\\Windows\\System32\\cmd.exe";
            finalCommand[1] = "/y";
            finalCommand[2] = "/c";
            finalCommand[3] = command;
        } else {
            finalCommand = new String[3];
            finalCommand[0] = "/bin/sh";
            finalCommand[1] = "-c";
            finalCommand[2] = command;
        }
        return Runtime.getRuntime().exec(finalCommand);
    }

    public static Process rabbitmqctl(String command) throws IOException {
        System.out.println(rabbitmqctlCommand());
        return executeCommand(rabbitmqctlCommand() + " " + command);
    }

    public static String rabbitmqctlCommand() {
        return System.getProperty("rabbitmqctl.bin");
    }

    public static void closeConnection(String pid) throws IOException {
        rabbitmqctl("close_connection '" + pid + "' 'Closed via rabbitmqctl'");
    }

//    public static void closeConnection(NetworkConnection c) throws IOException {
//        Host.ConnectionInfo ci = findConnectionInfoFor(Host.listConnections(), c);
//        closeConnection(ci.getPid());
//    }

    public static List<ConnectionInfo> listConnections() throws IOException {
        String output = capture(rabbitmqctl("list_connections -q pid peer_port").getInputStream());
        // output (header line presence depends on broker version):
        // pid   peer_port
        // <rabbit@mercurio.1.11491.0>   58713
        String[] allLines = output.split("\n");

        ArrayList<ConnectionInfo> result = new ArrayList<>();
        for (String line : allLines) {
            // line: <rabbit@mercurio.1.11491.0>   58713
            String[] columns = line.split("\t");
            // can be also header line, so ignoring NumberFormatException
            try {
                result.add(new ConnectionInfo(columns[0], Integer.parseInt(columns[1])));
            } catch (NumberFormatException e) {
                // OK
            }
        }
        return result;
    }

//    private static Host.ConnectionInfo findConnectionInfoFor(List<ConnectionInfo> xs, NetworkConnection c) {
//        Host.ConnectionInfo result = null;
//        for (Host.ConnectionInfo ci : xs) {
//            if (c.getLocalPort() == ci.getPeerPort()) {
//                result = ci;
//                break;
//            }
//        }
//        return result;
//    }

    public static class ConnectionInfo {

        private final String pid;
        private final int peerPort;

        public ConnectionInfo(String pid, int peerPort) {
            this.pid = pid;
            this.peerPort = peerPort;
        }

        public String getPid() {
            return pid;
        }

        public int getPeerPort() {
            return peerPort;
        }
    }
}
