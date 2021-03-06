/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.osgi.dev.grpc.impl;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServerImpl implements GrpcServer {

    private Server server;

    @Override
    public void shutdown() {
        server.shutdownNow();
    }

    @Override
    public void start(int port) throws Exception {
        server = ServerBuilder.forPort(port).addService(new GreetingServiceImpl()).build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
    }

    @Override
    public Server getServer() {
        return server;
    }
}
