package io.github.Igor5877.nestworld;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class HTTPApiListener extends Thread {
    private final ProxyServer server;

    public HTTPApiListener(ProxyServer server) {
        this.server = server;
    }

    @Override
public void run() {
    try {
        System.out.println("HTTPApiListener: Створення ServerSocket на 8081...");
        ServerSocket serverSocket = new ServerSocket(8081);
        System.out.println("HTTPApiListener: Успішно слухаємо на 8081 порту!");

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("HTTPApiListener: Прийнято підключення від " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream();
                String line = in.readLine();
                if (line != null && line.startsWith("POST /add-server")) {
                    addServer(in, out);
                } else if (line != null && line.startsWith("POST /remove-server")) {
                    removeServer(in, out);
                } else {
                    sendNotFound(out);
                }
            }
        }
    } catch (Exception e) {
        System.err.println("HTTPApiListener помилка: " + e.getMessage());
        e.printStackTrace();
    }
}


    private void addServer(BufferedReader in, OutputStream out) {
    try {
        String requestBody = readBody(in);
        System.out.println("[DEBUG] Прийшло тіло запиту: " + requestBody);

        String[] parts = requestBody.split(",");
        String name = parts[0].split(":")[1].replaceAll("[^a-zA-Z0-9]", "");
        String ip = parts[1].split(":")[1].replaceAll("[^0-9.:]", "");

        ServerInfo info = new ServerInfo(name, InetSocketAddress.createUnresolved(ip, 25565));
        server.registerServer(info);

        System.out.println("[DEBUG] Додано сервер " + name + " з IP " + ip);

        sendOk(out);
    } catch (Exception e) {
        System.err.println("HTTPApiListener помилка в addServer: " + e.getMessage());
        e.printStackTrace();
    }
}


    private void removeServer(BufferedReader in, OutputStream out) {
        try {
            String requestBody = readBody(in);
            String name = requestBody.split(":")[1].replaceAll("[^a-zA-Z0-9]", "");

            server.getAllServers().stream()
                    .filter(registeredServer -> registeredServer.getServerInfo().getName().equalsIgnoreCase(name))
                    .findFirst()
                    .ifPresent(registeredServer -> server.unregisterServer(registeredServer.getServerInfo()));
            sendOk(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readBody(BufferedReader in) throws Exception {
    String line;
    // Чекаємо пустий рядок який розділяє заголовки та тіло
    while ((line = in.readLine()) != null && !line.isEmpty()) {
        // Читаємо заголовки ігноруючи
    }

    // Після пустого рядка іде саме тіло
    StringBuilder body = new StringBuilder();
    while (in.ready() && (line = in.readLine()) != null) {
        body.append(line);
    }
    return body.toString();
}


    private void sendOk(OutputStream out) throws Exception {
        out.write("HTTP/1.1 200 OK\r\nContent-Length: 2\r\n\r\nOK".getBytes());
    }

    private void sendNotFound(OutputStream out) throws Exception {
        out.write("HTTP/1.1 404 Not Found\r\nContent-Length: 9\r\n\r\nNot Found".getBytes());
    }
}
