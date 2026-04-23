//author : sanuka nethsara

package smart_campus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.io.IOException;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws IOException {
        URI baseUri = URI.create("http://0.0.0.0:8080/");
        ResourceConfig config = ResourceConfig
            .forApplicationClass(SmartCampusApplication.class);
        HttpServer server = GrizzlyHttpServerFactory
            .createHttpServer(baseUri, config);
        System.out.println("=================================");
        System.out.println("Server started!");
        System.out.println("URL: http://localhost:8080/api/v1");
        System.out.println("Press ENTER to stop...");
        System.out.println("=================================");
        System.in.read();
        server.stop();
    }
}
