package com.example.cor;

import com.example.cor.middleware.Middleware;
import com.example.cor.middleware.RoleCheckMiddleware;
import com.example.cor.middleware.ThrottlingMiddleware;
import com.example.cor.middleware.UserExistsMiddleware;
import com.example.cor.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CorApplicatioon {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Server server;

    private static void init() {
        server = new Server();
        server.register("admin@narxoz.kz", "narxoz_admin");
        server.register("user@narxoz.kz", "narxoz_user");
        server.register("nuray.aman@narxoz.kz", "narxoz_student");

        Middleware middleware = Middleware.link(
                new ThrottlingMiddleware(3),
                new UserExistsMiddleware(server),
                new RoleCheckMiddleware()
        );

        server.setMiddleware(middleware);
    }

    public static void main(String[] args) throws IOException {
        init();

        boolean success;
        do {
            System.out.print("Enter email: ");
            String email = reader.readLine();
            System.out.print("Input password: ");
            String password = reader.readLine();
            success = server.logIn(email, password);
        } while (!success);
    }
}