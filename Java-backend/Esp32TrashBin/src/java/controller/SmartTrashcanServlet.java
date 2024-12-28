package controller;

import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/smart-trashcan")
public class SmartTrashcanServlet extends HttpServlet {

    private static TrashcanData latestTrashcanData = null; // Holds the latest data

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Read the parameters from the POST request
        String trashLevel = request.getParameter("trashLevel");
        String buzzerStatus = request.getParameter("buzzerStatus");
        String lidStatus = request.getParameter("lidStatus");

        // Log the received data
        Logger.getLogger(SmartTrashcanServlet.class.getName()).log(Level.INFO, 
            "Received data - Trash Level: " + trashLevel + ", Buzzer Status: " + buzzerStatus + ", Lid Status: " + lidStatus);

        // Check if parameters are missing
        if (trashLevel == null || buzzerStatus == null || lidStatus == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Missing parameters\"}");
            return;
        }

        // Get the current timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Update the latest data object
        latestTrashcanData = new TrashcanData(trashLevel, buzzerStatus, lidStatus, timestamp);
        
        // Response for POST request
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", "success");
        jsonResponse.addProperty("message", "Data received successfully");

        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set response content type to text/event-stream for SSE
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        // Send the latest data only if available
        PrintWriter out = response.getWriter();

        // Check if latest data is available and send SSE event
        if (latestTrashcanData != null) {
            sendSseEvent(out, latestTrashcanData);
        }

        // Optional: Close connection after sending the event if not required to keep it open
        out.close();
    }

    // Helper method to send SSE data
    private void sendSseEvent(PrintWriter out, TrashcanData data) {
        JsonObject json = new JsonObject();
        json.addProperty("trashLevel", data.getTrashLevel());
        json.addProperty("buzzerStatus", data.getBuzzerStatus());
        json.addProperty("lidStatus", data.getLidStatus());
        json.addProperty("timestamp", data.getTimestamp());

        out.write("data: " + json.toString() + "\n\n");
        out.flush(); // Make sure to flush the data so it gets sent immediately
    }

    // Inner class to store the trashcan data
    private static class TrashcanData {
        private String trashLevel;
        private String buzzerStatus;
        private String lidStatus;
        private String timestamp;

        public TrashcanData(String trashLevel, String buzzerStatus, String lidStatus, String timestamp) {
            this.trashLevel = trashLevel;
            this.buzzerStatus = buzzerStatus;
            this.lidStatus = lidStatus;
            this.timestamp = timestamp;
        }

        public String getTrashLevel() {
            return trashLevel;
        }

        public String getBuzzerStatus() {
            return buzzerStatus;
        }

        public String getLidStatus() {
            return lidStatus;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
